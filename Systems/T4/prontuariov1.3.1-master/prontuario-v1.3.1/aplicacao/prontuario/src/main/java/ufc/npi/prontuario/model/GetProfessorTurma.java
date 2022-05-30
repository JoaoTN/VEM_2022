package ufc.npi.prontuario.model;

import java.util.List;

public class GetProfessorTurma {

	public static List<Servidor> mostrarTurmaProfessores(Turma turma){
		return turma.getProfessores();
	}
	
}
