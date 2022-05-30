package br.ufc.quixada.npi.gestaocompetencia.service;

import br.ufc.quixada.npi.gestaocompetencia.model.ItemPreferencia;
import br.ufc.quixada.npi.gestaocompetencia.model.Preferencia;

import java.util.List;

public interface ItemPreferenciaService {

    ItemPreferencia create(ItemPreferencia itemPreferencia);

    List<ItemPreferencia> findAll();

    List<ItemPreferencia> findByPreferencia(Preferencia preferencia);

    ItemPreferencia update(ItemPreferencia itemPreferencia);

    void delete(ItemPreferencia itemPreferencia);
}
