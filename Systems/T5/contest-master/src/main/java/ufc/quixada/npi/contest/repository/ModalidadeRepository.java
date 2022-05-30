package ufc.quixada.npi.contest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ufc.quixada.npi.contest.model.Modalidade;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ModalidadeRepository extends CrudRepository<Modalidade, Long>{

	@Query("select case when count(*) > 0 then true else false end "
			+ "FROM Evento as e LEFT JOIN e.modalidadesSubmissao m WHERE e.id = :eventoId and m.nome = :nome")
	public boolean findByNomeAndEventoId(@Param("nome") String nome, @Param("eventoId") Long eventoId);
	
	
}
