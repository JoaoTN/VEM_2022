package ufc.quixada.npi.contest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ufc.quixada.npi.contest.model.Arquivo;

@Repository
public interface ArquivoRepository extends JpaRepository<Arquivo, Integer> {
}
