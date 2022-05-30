package br.ufc.quixada.npi.gestaocompetencia.service;

import br.ufc.quixada.npi.gestaocompetencia.model.Diagnostico;
import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;

import java.util.List;

public interface DiagnosticoService {

    Diagnostico findLast(Unidade unidade);

    List<Diagnostico> findAll();

    List<Diagnostico> findByUsuario(Usuario usuario);

    List<Diagnostico> findbyUnidade(Usuario usuario);

    List<Diagnostico> findAllSecondary(Unidade unidade);

    Boolean verifyAccess(Diagnostico diagnostico, Unidade unidade);

    Boolean verifySecondaryAccess(Diagnostico diagnostico, Unidade unidade);

    Diagnostico create(Diagnostico diagnostico);

    Diagnostico update(Diagnostico diagnostico);

    void delete(Diagnostico diagnostico);

    Boolean validarDatas(Diagnostico diagnostico);

    Boolean validarPrazos(Diagnostico diagnostico);
}
