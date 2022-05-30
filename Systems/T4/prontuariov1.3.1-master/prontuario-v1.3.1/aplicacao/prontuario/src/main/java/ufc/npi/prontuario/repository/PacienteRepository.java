package ufc.npi.prontuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import ufc.npi.prontuario.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    public Paciente findByCpf(@Param("cpf") String cpf);

    public Paciente findByCns(@Param("cns") String cns);
}

