package br.ufc.quixada.npi.gestaocompetencia.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import br.ufc.quixada.npi.gestaocompetencia.model.Diagnostico;
import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.model.ItemAvaliacao;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Integer> {

    @Query("SELECT d FROM Diagnostico d LEFT JOIN UnidadeMapeada u ON u.mapeamento = d.mapeamento WHERE :unidade member of u.unidades OR u.unidade = :unidade ORDER BY d.fimComportamental DESC")
    List<Diagnostico> findLast(Pageable pageable, Unidade unidade);

    @Query("SELECT d FROM Diagnostico d LEFT JOIN UnidadeMapeada u ON u.mapeamento = d.mapeamento WHERE :unidade member of u.unidades OR u.unidade = :unidade")
    List<Diagnostico> findAllByUnidade(Unidade unidade);
    
    @Query("SELECT DISTINCT d FROM Diagnostico d INNER JOIN Avaliacao a ON d = a.diagnostico WHERE a.avaliador = :usuario or a.avaliado = :usuario ORDER BY d.criadoEm DESC")
    List<Diagnostico> findFirstByUsuario(Usuario usuario, Pageable pageable);   
    
    @Query("SELECT i FROM ItemAvaliacao i LEFT JOIN Avaliacao a ON i.avaliacao = a WHERE a.diagnostico = :diagnostico")
    List<ItemAvaliacao> isIniciado(Diagnostico diagnostico);
}

 