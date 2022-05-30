package br.ufc.quixada.npi.gestaocompetencia.service;

import br.ufc.quixada.npi.gestaocompetencia.model.Perfil;
import br.ufc.quixada.npi.gestaocompetencia.model.PerfilPreferencia;
import br.ufc.quixada.npi.gestaocompetencia.model.Preferencia;

import java.util.List;

public interface PerfilPreferenciaService {

    PerfilPreferencia create(PerfilPreferencia perfilPreferencia);

    List<PerfilPreferencia> findAll();

    List<PerfilPreferencia> findByPerfil(Perfil perfil);

    PerfilPreferencia findByPerfilAndPreferencia(Perfil perfil, Preferencia preferencia);

    List<PerfilPreferencia> findByPreferencia(Preferencia preferencia);

    PerfilPreferencia update(PerfilPreferencia perfilPreferencia);

    void delete(PerfilPreferencia perfilPreferencia);
}
