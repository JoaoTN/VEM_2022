package ufc.npi.prontuario.service;

import java.util.Map;

public interface PacienteFormularioService {

	Map<String, Object> formularioCadastro();
	
	Map<String, Object> formularioEdicao();
}
