package br.ufc.quixada.npi.gestaocompetencia.service;

import br.ufc.quixada.npi.gestaocompetencia.model.Preferencia;

import java.util.List;

public interface PreferenciaService {

    Preferencia create(Preferencia preferencia);

    List<Preferencia> findAll();

    Preferencia update(Preferencia preferencia);

    void delete(Preferencia preferencia);
}
