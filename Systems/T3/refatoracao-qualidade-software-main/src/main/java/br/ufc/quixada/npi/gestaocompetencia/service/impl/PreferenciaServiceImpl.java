package br.ufc.quixada.npi.gestaocompetencia.service.impl;

import br.ufc.quixada.npi.gestaocompetencia.model.Preferencia;
import br.ufc.quixada.npi.gestaocompetencia.repository.PreferenciaRepository;
import br.ufc.quixada.npi.gestaocompetencia.service.PreferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreferenciaServiceImpl implements PreferenciaService {

    @Autowired
    private PreferenciaRepository preferenciaRepository;

    @Override
    public Preferencia create(Preferencia preferencia) {
        return preferenciaRepository.save(preferencia);
    }

    @Override
    public List<Preferencia> findAll() {
        return preferenciaRepository.findAll();
    }

    @Override
    public Preferencia update(Preferencia preferencia) {
        return preferenciaRepository.save(preferencia);
    }

    @Override
    public void delete(Preferencia preferencia) {
        preferenciaRepository.delete(preferencia);
    }
}
