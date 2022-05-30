package br.ufc.quixada.npi.gestaocompetencia.service;

import br.ufc.quixada.npi.gestaocompetencia.model.AreaPerfil;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.TipoAreaPerfil;

import java.util.List;

public interface AreaPerfilService {

    AreaPerfil create(AreaPerfil areaPerfil);

    List<AreaPerfil> findAll();

    List<AreaPerfil> findByTipo(TipoAreaPerfil tipoAreaPerfil);

    List<AreaPerfil> findByTipoAndUsuario(TipoAreaPerfil tipoAreaPerfil, Usuario usuario);

    List<AreaPerfil> findByUsuario(Usuario usuario);

    AreaPerfil update(AreaPerfil areaPerfil);

    void delete(AreaPerfil areaPerfil);
}
