package ufc.npi.prontuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.prontuario.model.Documento;

public interface DocumentoRepository extends JpaRepository<Documento, Integer>{

}
