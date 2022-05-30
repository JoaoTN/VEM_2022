package ufc.npi.prontuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.prontuario.model.Odontograma;

public interface OdontogramaRepository extends JpaRepository<Odontograma, Integer>{
	
	public Odontograma findByPaciente_id(Integer id);

}
