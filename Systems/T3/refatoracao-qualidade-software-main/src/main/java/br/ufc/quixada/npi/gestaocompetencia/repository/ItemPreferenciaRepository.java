package br.ufc.quixada.npi.gestaocompetencia.repository;

import br.ufc.quixada.npi.gestaocompetencia.model.ItemPreferencia;
import br.ufc.quixada.npi.gestaocompetencia.model.Preferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPreferenciaRepository extends JpaRepository<ItemPreferencia, Integer> {

    List<ItemPreferencia> findByPreferencia(Preferencia preferencia);
}
