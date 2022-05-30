package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ufc.npi.prontuario.model.Avaliacao;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Integer> {

	@Query("FROM Avaliacao a WHERE a.ativa = true")
	Avaliacao findAvaliacaoAtiva();

	List<Avaliacao> findAllByOrderByAtivaDesc();

}
