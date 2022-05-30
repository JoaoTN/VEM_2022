package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ADMINISTRACAO;
import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSOES_ADMINISTRACAO_VERIFICACAO_PROFESSOR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_VINCULAR_PROFESSOR;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_DETALHES_TURMA;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_DETALHES_TURMA;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.npi.prontuario.model.GetProfessorTurma;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.Turma;
import ufc.npi.prontuario.model.Usuario;
import ufc.npi.prontuario.service.TurmaService;

@Controller
@RequestMapping("/turma")
public class TurmaProfessorController {
	
	@Autowired
	private TurmaService turmaService;
	
	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@PostMapping("/{idTurma}/adicionar-professor")
	public ModelAndView adicionarProfessor(@PathVariable("idTurma") Turma turma, 
			@ModelAttribute("novosProfessores") Turma novosProfessores, RedirectAttributes attributes){
	
		turmaService.adicionarProfessorTurma(turma, mostrarTurmaProfessores(novosProfessores));
	
		if(existProfessoresTurma(turma)){
			addMensagemSucesso(attributes, SUCCESS_VINCULAR_PROFESSOR);
		}
		
		return new ModelAndView(REDIRECT_DETALHES_TURMA + mostrarIdTurma( turma));
	}

	public Integer mostrarIdTurma(Turma turma) {
		return turma.getId();
	}

	public List<Servidor> mostrarTurmaProfessores(Turma turma){
		return GetProfessorTurma.mostrarTurmaProfessores(turma);
	}
	
	@PostAuthorize(PERMISSOES_ADMINISTRACAO_VERIFICACAO_PROFESSOR)
	@GetMapping("/{idTurma}")
	public ModelAndView visualizarDetalhes(@PathVariable("idTurma") Turma turma) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_DETALHES_TURMA);
		
		List<Usuario> listaProfessores = turmaService.buscarProfessores(mostrarTurmaProfessores(turma));
		
		modelAndView.addObject("turma", turma);
		modelAndView.addObject("lista_professores", listaProfessores);
		modelAndView.addObject("novosProfessores", new Turma());
		return modelAndView;
	}
		
	private void addMensagemSucesso(RedirectAttributes attributes, String mensagem) {
		attributes.addFlashAttribute(SUCCESS, mensagem);
	}
	
	private boolean existProfessoresTurma(Turma turma) {
		return mostrarTurmaProfessores(turma).isEmpty();
	}
}
