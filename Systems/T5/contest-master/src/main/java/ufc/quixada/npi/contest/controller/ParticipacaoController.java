package ufc.quixada.npi.contest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufc.quixada.npi.contest.model.Participacao;
import ufc.quixada.npi.contest.service.AtividadeService;
import ufc.quixada.npi.contest.service.ParticipacaoService;
import ufc.quixada.npi.contest.util.Constants;
import ufc.quixada.npi.contest.validator.ContestException;

@Controller
@RequestMapping("/participacao")
public class ParticipacaoController {

    @Autowired
    private AtividadeService atividadeService;

    @Autowired
    private ParticipacaoService participacaoService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView registrarParticipacaoPage(@RequestParam(required = false) String codigo, Participacao participacao) {
        ModelAndView modelAndView = new ModelAndView("registrar_participacao");
        if(codigo != null) {
            codigo = codigo.toUpperCase();
            participacao.setAtividade(atividadeService.getAtividadePorCodigo(codigo));
        }
        modelAndView.addObject("participacao", participacao);
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView registrarParticipacao(Participacao participacao, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            participacaoService.registrarParticipacaoComoParticipante(participacao);
            modelAndView.setViewName(Constants.REDIRECIONAR_PARA_LOGIN);
            redirectAttributes.addFlashAttribute("participacaoSuccess", true);
        } catch (ContestException e) {
            modelAndView = registrarParticipacaoPage(participacao.getAtividade().getCodigo(), participacao);
            modelAndView.addObject("error", e.getMessage());
        }

        return modelAndView;
    }

}
