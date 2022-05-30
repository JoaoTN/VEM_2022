package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ADMINISTRACAO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_MATRICULAR_ALUNO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_REMOVER_INSCRICAO;
import static ufc.npi.prontuario.util.FragmentsConstants.FRAGMENT_AJUDANTES;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_DETALHES_TURMA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Turma;
import ufc.npi.prontuario.service.AlunoService;
import ufc.npi.prontuario.service.TurmaService;

@Controller
@RequestMapping("/turma")
public class AlunoTurmaController {
	
	@Autowired
	private AlunoService alunoService;
	
	@Autowired
	private TurmaService turmaService;
	
	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@PostMapping("/{idTurma}/inscrever")
	public ModelAndView inscreverAluno(@PathVariable("idTurma") Turma turma,
			@RequestParam("matricula") String matricula, RedirectAttributes attributes) {
		
		try {
			turmaService.inscreverAluno(turma, matricula);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_MATRICULAR_ALUNO);
		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}
		
		return new ModelAndView(REDIRECT_DETALHES_TURMA + mostrarIdTurma(turma));
	}
	
	@GetMapping("/ajudantes/")
	public ModelAndView carregarAjudantes(@RequestParam("idTurma") Integer idTurma, @RequestParam("idResponsavel") Integer idResponsavel) {
		ModelAndView modelAndView = new ModelAndView(FRAGMENT_AJUDANTES);
		modelAndView.addObject("ajudantes", idTurma == null ? null : alunoService.buscarAjudantes(idTurma, alunoService.buscarPorId(idResponsavel)));
		return modelAndView;
	}
	
	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping("/{idTurma}/remover-aluno/{idAluno}")
	public ModelAndView removerInscricaoDeAluno(@PathVariable("idTurma") Turma turma, 
			@PathVariable("idAluno") Aluno aluno, RedirectAttributes attributes) {
		
		try {
			turmaService.removerInscricao(turma, aluno);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_REMOVER_INSCRICAO);
		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}
		
		return new ModelAndView(REDIRECT_DETALHES_TURMA + mostrarIdTurma(turma));
	}

	public Integer mostrarIdTurma(Turma turma) {
		return turma.getId();
	}

}
