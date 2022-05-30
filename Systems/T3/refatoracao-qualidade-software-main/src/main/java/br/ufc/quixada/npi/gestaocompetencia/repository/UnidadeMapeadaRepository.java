package br.ufc.quixada.npi.gestaocompetencia.repository;

import br.ufc.quixada.npi.gestaocompetencia.model.UnidadeMapeada;
import br.ufc.quixada.npi.gestaocompetencia.model.Mapeamento;
import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadeMapeadaRepository extends JpaRepository<UnidadeMapeada, Integer> {

    UnidadeMapeada findFirstMapeamentoByUnidade(Unidade unidade);

	UnidadeMapeada findByUnidade(Unidade u);

	UnidadeMapeada findByMapeamento(Mapeamento mapeamento);

}
