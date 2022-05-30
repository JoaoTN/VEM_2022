package ufc.npi.prontuario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.GetAtendimentoId;
import ufc.npi.prontuario.model.Procedimento;
import ufc.npi.prontuario.service.ProcedimentoService;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ESTUDANTE_VERFICACAO_PROFESSOR_VERIFICACAO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EXCLUIR_PROCEDIMENTO;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_DETALHES_ATENDIMENTO;

@Controller
@RequestMapping("/atendimento")
public class AtendimentoProcedimentoController {

    @Autowired
    private ProcedimentoService procedimentoService;

    @PreAuthorize(PERMISSAO_ESTUDANTE_VERFICACAO_PROFESSOR_VERIFICACAO)
    @GetMapping("/{idAtendimento}/excluir-procedimento/{idProcedimento}")
    public ModelAndView removerProcedimento(@PathVariable("idProcedimento") Procedimento procedimento,
                                            @PathVariable("idAtendimento") @Param("atendimento") Atendimento atendimento, Authentication auth,
                                            RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView(REDIRECT_DETALHES_ATENDIMENTO + GetAtendimentoId.getIdAtendimento(atendimento));
        procedimentoService.deletar(procedimento);
        attributes.addFlashAttribute(SUCCESS, SUCCESS_EXCLUIR_PROCEDIMENTO);
        return modelAndView;
    }
}
