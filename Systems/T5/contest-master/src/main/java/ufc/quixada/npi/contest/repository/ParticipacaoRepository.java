package ufc.quixada.npi.contest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ufc.quixada.npi.contest.model.Atividade;
import ufc.quixada.npi.contest.model.Evento;
import ufc.quixada.npi.contest.model.Participacao;
import ufc.quixada.npi.contest.model.Pessoa;

import java.util.Date;
import java.util.List;

public interface ParticipacaoRepository extends JpaRepository<Participacao, Long> {

    @Query("SELECT p FROM Participacao p JOIN p.atividade a WHERE p.participante = :participante AND (a.inicio >= :inicio OR a.termino > :inicio) AND (a.inicio < :termino OR a.termino <= :termino)")
    List<Participacao> findParticipacoesConcomitantesByParticipante(@Param("participante") Pessoa participante, @Param("inicio") Date inicio, @Param("termino") Date termino);

    Participacao findByAtividadeAndParticipante(Atividade atividade, Pessoa participante);

    List<Participacao> findByParticipante(Pessoa perticipante);

    @Query("SELECT DISTINCT p.participante FROM Participacao p WHERE p.atividade.evento = :evento")
    List<Pessoa> findParticipantes(@Param("evento") Evento evento);

    @Query("SELECT p FROM Participacao p WHERE p.participante = :participante and p.atividade.evento = :evento")
    List<Participacao> findByParticipanteAndEvento(@Param("participante") Pessoa participante, @Param("evento") Evento evento);
}
