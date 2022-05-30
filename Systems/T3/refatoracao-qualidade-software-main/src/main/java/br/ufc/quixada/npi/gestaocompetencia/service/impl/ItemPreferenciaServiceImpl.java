package br.ufc.quixada.npi.gestaocompetencia.service.impl;

import br.ufc.quixada.npi.gestaocompetencia.model.ItemPreferencia;
import br.ufc.quixada.npi.gestaocompetencia.model.Preferencia;
import br.ufc.quixada.npi.gestaocompetencia.repository.ItemPreferenciaRepository;
import br.ufc.quixada.npi.gestaocompetencia.service.ItemPreferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemPreferenciaServiceImpl implements ItemPreferenciaService {

    @Autowired
    private ItemPreferenciaRepository itemPreferenciaRepository;


    @Override
    public ItemPreferencia create(ItemPreferencia itemPreferencia) {
        return itemPreferenciaRepository.save(itemPreferencia);
    }

    @Override
    public List<ItemPreferencia> findAll() {
        return itemPreferenciaRepository.findAll();
    }

    @Override
    public List<ItemPreferencia> findByPreferencia(Preferencia preferencia) {
        return itemPreferenciaRepository.findByPreferencia(preferencia);
    }

    @Override
    public ItemPreferencia update(ItemPreferencia itemPreferencia) {
        return itemPreferenciaRepository.save(itemPreferencia);
    }

    @Override
    public void delete(ItemPreferencia itemPreferencia) {
        itemPreferenciaRepository.delete(itemPreferencia);
    }
}
