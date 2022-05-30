package ufc.npi.prontuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.prontuario.model.Pergunta;

public interface PerguntaRepository extends JpaRepository<Pergunta, Integer> {

}
