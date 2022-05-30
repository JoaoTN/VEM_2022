package br.ufc.quixada.npi.gestaocompetencia.service.impl;

import br.ufc.quixada.npi.gestaocompetencia.model.Perfil;
import br.ufc.quixada.npi.gestaocompetencia.model.PerfilPreferencia;
import br.ufc.quixada.npi.gestaocompetencia.model.Preferencia;
import br.ufc.quixada.npi.gestaocompetencia.repository.PerfilPreferenciaRepository;
import br.ufc.quixada.npi.gestaocompetencia.service.PerfilPreferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilPreferenciaServiceImpl implements PerfilPreferenciaService {

    @Autowired
    private PerfilPreferenciaRepository perfilPreferenciaRepository;

    @Override
    public PerfilPreferencia create(PerfilPreferencia perfilPreferencia) {
        return perfilPreferenciaRepository.save(perfilPreferencia);
    }

    @Override
    public List<PerfilPreferencia> findAll() {
        return perfilPreferenciaRepository.findAll();
    }

    @Override
    public List<PerfilPreferencia> findByPerfil(Perfil perfil) {
        return perfilPreferenciaRepository.findByPerfil(perfil);
    }

    @Override
    public PerfilPreferencia findByPerfilAndPreferencia(Perfil perfil, Preferencia preferencia) {
        return perfilPreferenciaRepository.findByPerfilAndPreferencia(perfil, preferencia);
    }

    @Override
    public List<PerfilPreferencia> findByPreferencia(Preferencia preferencia) {
        return perfilPreferenciaRepository.findByPreferencia(preferencia);
    }

    @Override
    public PerfilPreferencia update(PerfilPreferencia perfilPreferencia) {
        return perfilPreferenciaRepository.save(perfilPreferencia);
    }

    @Override
    public void delete(PerfilPreferencia perfilPreferencia) {
        perfilPreferenciaRepository.delete(perfilPreferencia);
    }
}
