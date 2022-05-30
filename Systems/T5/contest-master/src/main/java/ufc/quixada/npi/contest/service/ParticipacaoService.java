package ufc.quixada.npi.contest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufc.quixada.npi.contest.model.Atividade;
import ufc.quixada.npi.contest.model.Evento;
import ufc.quixada.npi.contest.model.Participacao;
import ufc.quixada.npi.contest.model.Pessoa;
import ufc.quixada.npi.contest.repository.ParticipacaoRepository;
import ufc.quixada.npi.contest.repository.PessoaRepository;
import ufc.quixada.npi.contest.validator.ContestException;

import java.util.Date;
import java.util.List;

@Service
public class ParticipacaoService {

    @Autowired
    private ParticipacaoRepository participacaoRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AtividadeService atividadeService;

    public void registrarParticipacaoComoParticipante(Participacao participacao) throws ContestException {
        Pessoa participante = userDetailsService.autenticar(participacao.getParticipante().getEmail(), participacao.getParticipante().getPassword());
        if(participante == null) {
            throw new ContestException("O login ou a senha nÃ£o correspondem!");
        }

        Atividade atividade = atividadeService.getAtividadePorCodigo(participacao.getAtividade().getCodigo());
        registrarParticipacao(atividade, participante, participante);
    }

    public void registrarParticipacao(Atividade atividade, Pessoa participante, Pessoa responsavel) throws ContestException {
        if(atividade == null) {
            throw new ContestException("Atividade não encontrada!");
        }
        
        verificaçãoAtividadeParticipante(atividade, participante);

        participacoesConcomitantes(participante, atividade.getInicio(), atividade.getTermino());

        validacaoDataHoraAtividade(getDateInicioAtividade(atividade),
        		getDateTerminoAtividade(atividade),
        		getEventoTerminoAtividade(atividade),
        		isAtivoTerminoAtividade(atividade),
        		responsavel);
        
        Participacao participacao = construcaoParticipante(atividade, participante, responsavel);
        
        participacaoRepository.save(participacao);
    }
    
    private void verificaçãoAtividadeParticipante(Atividade atividade, Pessoa participante) throws ContestException {
    	if (participacaoRepository.findByAtividadeAndParticipante(atividade, participante) != null) {
            throw new ContestException("Já existe inscrição para esta atividade!");
        }
    }
    
    private void participacoesConcomitantes(Pessoa participante, Date inicio, Date termino) throws ContestException {
    	List<Participacao> participacoesConcomitantes = participacaoRepository.findParticipacoesConcomitantesByParticipante(participante, 
    			inicio, termino);
        if(participacoesConcomitantes != null && !participacoesConcomitantes.isEmpty()) {
            throw new ContestException("Foram encontradas participações já registradas em atividades no mesmo horário!");
        }
    }
    
    private void validacaoDataHoraAtividade(Date inicio, Date termino, Evento evento, boolean ativo, Pessoa responsavel) throws ContestException {
    	 Date dataHoraAtual = new Date();
         if (!evento.isOrganizador(responsavel)) {
             if (((inicio != null && dataHoraAtual.before(inicio) || 
             		(termino != null && dataHoraAtual.after(termino))) && !ativo)) {
                 throw new ContestException("A atividade não está no período ativo!");
             }
         }
    }
    
    private Date getDateInicioAtividade(Atividade atividade) {
    	return atividade.getInicio();
    }
    
    private Date getDateTerminoAtividade(Atividade atividade) {
    	return atividade.getTermino();
    }
    
    private Evento getEventoTerminoAtividade(Atividade atividade) {
    	return atividade.getEvento();
    }
    
    private boolean isAtivoTerminoAtividade(Atividade atividade) {
    	return atividade.isAtivo();
    }
    
    private Participacao construcaoParticipante(Atividade atividade, Pessoa participante, Pessoa responsavel) {
    	Participacao participacao = new Participacao();
    	
    	participacao = setAtividadeParticipante(participacao, atividade);
    	participacao = setValidaParticipante(participacao, true);
    	participacao = setDataParticipante(participacao, new Date());
    	participacao = setResponsavelParticipante(participacao, responsavel);
    	participacao = setParticipante(participacao, participante);
    	
    	return participacao;
    }
    
    private Participacao setAtividadeParticipante(Participacao participacao, Atividade atividade) {
    	participacao.setAtividade(atividade);
    	return participacao;
    }
    
    private Participacao setValidaParticipante(Participacao participacao, boolean valida) {
    	participacao.setValida(true);
    	return participacao;
    }
    
    private Participacao setDataParticipante(Participacao participacao, Date date) {
    	participacao.setData(date);
    	return participacao;
    }
    
    private Participacao setResponsavelParticipante(Participacao participacao, Pessoa responsavel) {
    	participacao.setResponsavel(responsavel);
    	return participacao;
    }
    
    private Participacao setParticipante(Participacao participacao, Pessoa participante) {
    	participacao.setParticipante(participante);
    	return participacao;
    }

    public void atualizar(Participacao participacao) {
        participacaoRepository.save(participacao);
    }

    public List<Participacao> findByParticipante(Pessoa participante, Evento evento) {
        return participacaoRepository.findByParticipanteAndEvento(participante, evento);
    }
}
