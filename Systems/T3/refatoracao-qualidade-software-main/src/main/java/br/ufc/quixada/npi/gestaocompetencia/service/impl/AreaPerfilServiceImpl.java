package br.ufc.quixada.npi.gestaocompetencia.service.impl;

import br.ufc.quixada.npi.gestaocompetencia.model.AreaPerfil;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.TipoAreaPerfil;
import br.ufc.quixada.npi.gestaocompetencia.repository.AreaPerfilRepository;
import br.ufc.quixada.npi.gestaocompetencia.service.AreaPerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaPerfilServiceImpl implements AreaPerfilService {

    @Autowired
    private AreaPerfilRepository areaPerfilRepository;

    @Override
    public AreaPerfil create(AreaPerfil areaPerfil) {
        return areaPerfilRepository.save(areaPerfil);
    }

    @Override
    public List<AreaPerfil> findAll() {
        return areaPerfilRepository.findAll();
    }

    @Override
    public List<AreaPerfil> findByTipo(TipoAreaPerfil tipoAreaPerfil) {
        return areaPerfilRepository.findAllByTipoEquals(tipoAreaPerfil);
    }

    @Override
    public List<AreaPerfil> findByTipoAndUsuario(TipoAreaPerfil tipoAreaPerfil, Usuario usuario) {
        return areaPerfilRepository.findAllByTipoAndUsuario(tipoAreaPerfil, usuario);
    }

    @Override
    public List<AreaPerfil> findByUsuario(Usuario usuario) {
        return areaPerfilRepository.findAllByUsuario(usuario);
    }

    @Override
    public AreaPerfil update(AreaPerfil areaPerfil) {
        return areaPerfilRepository.save(areaPerfil);
    }

    @Override
    public void delete(AreaPerfil areaPerfil) {
        areaPerfilRepository.delete(areaPerfil);
    }
}
