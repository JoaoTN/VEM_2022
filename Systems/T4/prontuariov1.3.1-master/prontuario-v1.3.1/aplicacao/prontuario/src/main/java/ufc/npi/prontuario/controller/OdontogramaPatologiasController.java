package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ESTUDANTE;
import static ufc.npi.prontuario.util.FragmentsConstants.FRAGMENT_MODAL_DETALHES_PATOLOGIA;
import static ufc.npi.prontuario.util.PagesConstants.TABLE_PATOLOGIAS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.GetMatriculaUsuario;
import ufc.npi.prontuario.model.Odontograma;
import ufc.npi.prontuario.model.Patologia;
import ufc.npi.prontuario.model.TipoPatologia;
import ufc.npi.prontuario.model.Tratamento;
import ufc.npi.prontuario.model.Usuario;
import ufc.npi.prontuario.service.AlunoService;
import ufc.npi.prontuario.service.AtendimentoService;
import ufc.npi.prontuario.service.PatologiaService;
import ufc.npi.prontuario.service.TipoPatologiaService;

@Controller
@RequestMapping("/odontograma")
public class OdontogramaPatologiasController {

	@Autowired
	private AlunoService alunoService;

	@Autowired
	private PatologiaService patologiaService;

	@Autowired
	private AtendimentoService atendimentoService;

	@Autowired
	private TipoPatologiaService tipoPatologiaService;

	@GetMapping("/tablePatologias/{id}")
	public ModelAndView tablePatologias(@PathVariable("id") Odontograma odontograma, Authentication auth) {
		ModelAndView mv = new ModelAndView(TABLE_PATOLOGIAS);
		Usuario usuario = (Usuario) auth.getPrincipal();
		mv.addObject("patologias", patologiaService.buscarPatologiasOdontograma(odontograma, usuario));
		if (usuario instanceof Aluno)
			mv.addObject("existeAtendimento", atendimentoService.existeAtendimentoAbertoAlunoPaciente((Aluno) usuario,
					odontograma.getPaciente()));
		return mv;
	}

	@PreAuthorize(PERMISSAO_ESTUDANTE)
	@PostMapping("/adicionarPatologia")
	public @ResponseBody Map<String, Object> adicionarPatologia(@ModelAttribute("faceDente") String faceDente,
			@ModelAttribute("patologias") List<Integer> patologiasId, @ModelAttribute("local") String local,
			@ModelAttribute("idOdontograma") Integer idOdontograma, @ModelAttribute("descricao") String descricao,
			Authentication auth) throws ProntuarioException {

		Usuario usuario = (Usuario) auth.getPrincipal();

		Aluno aluno = alunoService.buscarPorMatricula(GetMatriculaUsuario.getUsuarioMatricula(usuario));

		Map<String, Object> map = new HashMap<String, Object>();
		List<Patologia> patologias = patologiaService.salvar(faceDente, patologiasId, local, idOdontograma, descricao,
				aluno);

		map.put("patologias", patologias);
		return map;
	}

	@GetMapping("/buscar-patologia")
	public @ResponseBody List<TipoPatologia> buscaPatologia(@RequestParam("query") String query) {

		List<TipoPatologia> resultado = tipoPatologiaService.buscarPorNome(query);

		return resultado;
	}

	@GetMapping("/buscarPatologias/{id}")
	public @ResponseBody Map<String, Object> buscarPatologias(@PathVariable("id") Odontograma odontograma,
			Authentication auth) {
		Map<String, Object> map = new HashMap<String, Object>();

		Usuario usuario = (Usuario) auth.getPrincipal();
		map.put("patologiasOdontograma", patologiaService.buscarPatologiasOdontograma(odontograma, usuario));
		return map;
	}

	@GetMapping("/patologias/{idPatologia}")
	public ModelAndView buscarPatologia(@PathVariable("idPatologia") Patologia patologia) {
		ModelAndView modelAndView = new ModelAndView(FRAGMENT_MODAL_DETALHES_PATOLOGIA);
		modelAndView.addObject("patologia", patologia);
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ESTUDANTE)
	@PostMapping("/tratar/{idPatologia}")
	public ResponseEntity<?> tratarPatologia(@PathVariable("idPatologia") Patologia patologia,
			@ModelAttribute Tratamento tratamento, Authentication auth) {
		DefinirResponsavelTratamento(tratamento, auth, patologia);
		patologiaService.tratar(patologia);
		ResponseEntity<List<Patologia>> responseEntity = new ResponseEntity<List<Patologia>>(
				patologiaService.buscarPatologiasTratadas(patologia.getOdontograma()), HttpStatus.OK);
		return responseEntity;
	}

	public void DefinirResponsavelTratamento(Tratamento tratamento, Authentication auth, Patologia patologia) {
		tratamento.setResponsavel((Aluno) auth.getPrincipal());
		patologia.setTratamento(tratamento);
	}

	@GetMapping("/buscar-patologia/{odontograma}")
	public @ResponseBody List<Patologia> buscaPatologiasProcedimento(
			@PathVariable("odontograma") Odontograma odontograma,
			@RequestParam(value = "face", required = false) String faceDente,
			@RequestParam(value = "dente", required = false) String dente, Authentication auth) {

		Aluno aluno = (Aluno) auth.getPrincipal();
		List<Patologia> patologias = patologiaService.buscarPatologiasDentePaciente(odontograma, faceDente, dente,
				aluno);

		return patologias;
	}

}
