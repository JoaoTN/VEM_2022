package ufc.npi.prontuario.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import ufc.npi.prontuario.model.Estado;
import ufc.npi.prontuario.model.EstadoCivil;
import ufc.npi.prontuario.model.Raca;
import ufc.npi.prontuario.model.Sexo;

@Service
public class PacienteFormularioService implements ufc.npi.prontuario.service.PacienteFormularioService{

	@Override
	public Map<String, Object> formularioCadastro() {
		Map<String, Object> form = new HashMap<>();
		addCampos(form);
		form.put("action", "cadastrar");
		return form;
	}

	@Override
	public Map<String, Object> formularioEdicao() {
		Map<String, Object> form = new HashMap<>();
		addCampos(form);
		form.put("action", "editar");
		return form;
	}
	
	private void addCampos(Map<String, Object> form) {
		form.put("sexo", Sexo.values());
		form.put("estado", Estado.values());
		form.put("estadoCivil", EstadoCivil.values());
		form.put("raca", Raca.values());
	}

}
