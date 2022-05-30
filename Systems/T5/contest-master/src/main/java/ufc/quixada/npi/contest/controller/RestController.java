package ufc.quixada.npi.contest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufc.quixada.npi.contest.model.*;
import ufc.quixada.npi.contest.service.*;

import java.util.Date;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private AtividadeService atividadeService;

    @Autowired
    private ParticipacaoService participacaoService;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private TrabalhoService trabalhoService;

    @RequestMapping("/users/findByNome")
    public List<Pessoa> getAllByName(@RequestParam(value = "search", required = false) String search) {
        return pessoaService.findByNome(search);
    }

    @RequestMapping("/revisores/{evento}")
    public List<Pessoa> getRevisoresByEvento(@PathVariable Evento evento, @RequestParam(value = "search", required = false) String nome) {
        return eventoService.findRevisores(evento, nome);
    }

    @PreAuthorize("#atividade != null && #atividade.evento.isOrganizador(authentication.principal)")
    @RequestMapping("/atividades/status/{atividade}")
    public void atualizarStatusAtividade(@PathVariable Atividade atividade, @RequestParam(value = "ativo", required = false) boolean ativo) {
        atividade.setAtivo(ativo);
        atividadeService.adicionarOuAtualizar(atividade);
    }

    @PreAuthorize("#participacao != null && #participacao.atividade.evento.isOrganizador(authentication.principal)")
    @RequestMapping("/atividades/participacao/{participacao}")
    public void atualizarStatusParticipacao(@PathVariable Participacao participacao, @RequestParam(value = "valido", required = false) boolean valido) {
        participacao.setValida(valido);
        participacaoService.atualizar(participacao);
    }

    @PreAuthorize("#trabalho.evento.isOrganizador(authentication.principal) || #trabalho.sessao.isReponsavel(authentication.principal)")
    @RequestMapping("/apresentacao/{idTrabalho}/{status}")
    public void informarApresentacao(@PathVariable("idTrabalho") Trabalho trabalho, @PathVariable("status") boolean status, 
    		Authentication auth) {
        if (trabalho == null) {
            return;
        }
     
        trabalhoService.atualizar(infoTrabalho(trabalho, (Pessoa) auth.getPrincipal(), status));
    }
    
    private Trabalho infoTrabalho(Trabalho trabalho, Pessoa responsavel, boolean status) {
    	trabalho = resoponsavelApresentacao(trabalho, responsavel);
    	trabalho = dateApresentacao(trabalho);
    	trabalho = statusApresentacao(trabalho, status);
    	
        return trabalho;
    }
    
    private Trabalho resoponsavelApresentacao(Trabalho trabalho, Pessoa responsavel) {
    	trabalho.setResponsavelApresentacao(responsavel);
    	return trabalho;
    }
    
    private Trabalho dateApresentacao(Trabalho trabalho) {
    	trabalho.setDataApresentacao(new Date());
    	return trabalho;
    }
    
    private Trabalho statusApresentacao(Trabalho trabalho, boolean status) {
    	trabalho.setStatusApresentacao(status);
    	return trabalho;
    }

}
