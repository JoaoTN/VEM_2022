package ufc.quixada.npi.contest.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ufc.quixada.npi.contest.model.Atividade;
import ufc.quixada.npi.contest.model.Evento;
import ufc.quixada.npi.contest.model.Pessoa;

@Repository
@Transactional
public interface AtividadeRepository extends JpaRepository<Atividade, Long> {

	@Query("SELECT a FROM Atividade a WHERE a.evento.id = :idEvento")
	List<Atividade> findAtividadeByEventoId(@Param("idEvento") Long idEvento);

	@Query("SELECT p.atividade FROM Participacao p WHERE p.participante.id = :idPessoa")
	List<Atividade> buscarAtividadePorParticipacao(@Param("idPessoa") Long idPessoa);

	Atividade findByCodigo(String codigo);
}
