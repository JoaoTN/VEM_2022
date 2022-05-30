package ufc.npi.prontuario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.Turma;
import ufc.npi.prontuario.model.GetProfessorTurma;
import ufc.npi.prontuario.service.AlunoService;
import ufc.npi.prontuario.service.TurmaService;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ESTUDANTE;
import static ufc.npi.prontuario.util.PagesConstants.FORMULARIO_ADICIONAR_ATENDIMENTO;

import java.util.List;

public class FormularioAtendimentoController {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private TurmaService turmaService;

    @PreAuthorize(PERMISSAO_ESTUDANTE)
    @GetMapping("/{id}/cadastrar")
    public ModelAndView formularioCadastrarAtendimento(@PathVariable("id") Paciente paciente, Authentication auth) {
        ModelAndView modelAndView = new ModelAndView(FORMULARIO_ADICIONAR_ATENDIMENTO);
        Aluno aluno = (Aluno) auth.getPrincipal();
        modelAndView.addObject("atendimento", new Atendimento());
        modelAndView.addObject("turmas", turmaService.buscarAtivasPorAluno(aluno));
        modelAndView.addObject("paciente", paciente);
        modelAndView.addObject("action", "cadastrar");
        return modelAndView;
    }

    public List<Servidor> mostrarTurmaProfessores(Turma turma){
		return GetProfessorTurma.mostrarTurmaProfessores(turma);
	}

    @PreAuthorize(PERMISSAO_ESTUDANTE)
    @GetMapping("/editar/{idAtendimento}")
    public ModelAndView formularioEditarAtendimento(@PathVariable("idAtendimento") Atendimento atendimento,
                                                    Authentication auth) {
        ModelAndView modelAndView = new ModelAndView(FORMULARIO_ADICIONAR_ATENDIMENTO);
        Aluno aluno = (Aluno) auth.getPrincipal();
        modelAndView.addObject("atendimento", atendimento);
        modelAndView.addObject("turmas", turmaService.buscarAtivasPorAluno(aluno));
        modelAndView.addObject("paciente", atendimento.getPaciente());
        modelAndView.addObject("professor", atendimento.getProfessor());
        modelAndView.addObject("professores", mostrarTurmaProfessores(atendimento.getTurma()));
        modelAndView.addObject("auxiliar", atendimento.getAjudante());
        modelAndView.addObject("ajudantes", alunoService.buscarAjudantes(atendimento.getTurma().getId(), aluno));
        modelAndView.addObject("action", "editar");
        return modelAndView;
    }
}
