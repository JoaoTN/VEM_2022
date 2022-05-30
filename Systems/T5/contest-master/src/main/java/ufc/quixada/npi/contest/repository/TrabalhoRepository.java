package ufc.quixada.npi.contest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ufc.quixada.npi.contest.model.*;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface TrabalhoRepository extends JpaRepository<Trabalho, Long> {

	@Modifying
	@Query("update Trabalho t set t.sessao = :sessao where t in :trabalhos")
	int atualizarSessao(@Param("sessao") Sessao sessao, @Param("trabalhos") List<Trabalho> trabalhos);

	List<Trabalho> getByEventoOrderByTitulo(Evento evento);

	@Query("SELECT r.trabalho FROM Revisao r WHERE r.trabalho.evento = :evento AND r.revisor = :revisor")
	List<Trabalho> getTrabalhosParaRevisar(@Param("revisor") Pessoa revisor, @Param("evento") Evento evento);

	@Query("SELECT r.trabalho FROM Revisao r " + "WHERE r.revisor.id = :idRevisor AND "
			+ "r.trabalho.evento.id = :idEvento")
	List<Trabalho> getTrabalhosRevisados(@Param("idRevisor") Long idRevisor, @Param("idEvento") Long idEvento);

	@Query("select case when count(*) > 0 then true else false end "
			+ "FROM Trabalho as t  WHERE t.trilha.id = :trilhaId")
	boolean existsTrilhaId(@Param("trilhaId") Long trilhaId);

	List<Trabalho> findByTrilha(Trilha trilha);

	/*
	 * @Query("SELECT t FROM Trabalho t WHERE t.evento.id = :eventoId AND t.id in (SELECT pt.trabalho.id FROM ParticipacaoTrabalho"
	 * + " pt where pt.pessoa.id = :autorId AND " +
	 * "(pt.papel = ufc.quixada.npi.contest.model.Papel$Tipo.COAUTOR or pt.papel = ufc.quixada.npi.contest.model.Papel$Tipo.AUTOR))"
	 * ) List<Trabalho> getTrabalhoComoAutorECoautorNoEvento(@Param("autorId") Long
	 * autorId, @Param("eventoId") Long eventoId);
	 */



	@Query("SELECT t FROM Trabalho t WHERE t.evento.id = :eventoId AND t.id in (SELECT rev.trabalho.id FROM Revisao"
			+ " rev where rev.trabalho.id = t.id AND rev.observacoes <> '')")
	List<Trabalho> getTrabalhoRevisadoComentadoEvento(@Param("eventoId") Long eventoId);

	@Query("SELECT t FROM Trabalho t " + "WHERE t.evento.id = :eventoId AND " + "t.sessao.id = null ORDER by t.titulo")
	List<Trabalho> getTrabalhosSemSessaoNoEvento(@Param("eventoId") Long eventoId);

	List<Trabalho> findTrabalhoBySessaoId(Long idSessao);

	List<Trabalho> getTrabalhoBySessao(Sessao sessao);

	@Query("SELECT COUNT(*) FROM Trabalho t WHERE t.sessao = :sessao")
	int countBySessao(@Param("sessao") Sessao sessao);

	@Query("select case when count(*) > 0 then true else false end FROM Trabalho as t INNER JOIN t.revisores as r WHERE t.evento.id = :idEvento and r.id = :idPessoa")
	public boolean existTrablhoAlocado(@Param("idEvento") Long idEvento, @Param("idPessoa") Long idPessoa);

    @Query("SELECT DISTINCT t FROM Trabalho t where t.evento = :evento and (t.autor = :autor or t.orientador = :autor or :autor member of t.coautores or :autor member of t.bolsistas)")
	List<Trabalho> findByEventoAndAutor(@Param("evento") Evento evento, @Param("autor") Pessoa autor);

    @Query("SELECT DISTINCT t FROM Trabalho t WHERE t.evento = :evento AND t.revisoes is empty")
	List<Trabalho> getTrabalhosAguardandoRevisor(@Param("evento") Evento evento);

    @Query("SELECT DISTINCT r.trabalho FROM Revisao r where r.trabalho.evento = :evento AND r.avaliacao in :avaliacoes")
	List<Trabalho> getTrabalhosByAvaliacao(@Param("evento") Evento evento, @Param("avaliacoes") List<Avaliacao> avaliacoes);
}