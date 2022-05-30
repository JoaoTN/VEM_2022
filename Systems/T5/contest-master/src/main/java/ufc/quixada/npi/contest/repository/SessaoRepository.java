package ufc.quixada.npi.contest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ufc.quixada.npi.contest.model.Evento;
import ufc.quixada.npi.contest.model.Pessoa;
import ufc.quixada.npi.contest.model.Sessao;

import javax.transaction.Transactional;
import java.util.List;
@Repository
@Transactional
public interface SessaoRepository extends JpaRepository<Sessao, Long> {
	
	public List<Sessao> findByEventoOrderByNome_Asc(Evento evento);

	List<Sessao> findByEventoAndResponsavelOrderByNome_Asc(Evento evento, Pessoa responsavel);
	
}
