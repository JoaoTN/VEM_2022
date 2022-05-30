package br.ufc.quixada.npi.gestaocompetencia.repository;

import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findByEmail(String email);
    
    Integer countByUnidade(Unidade unidade);
    
    @Query("SELECT COUNT(DISTINCT u) "
    		+ "FROM Usuario u INNER JOIN Comportamento c on(c.servidor = u) "
    		+ "WHERE u.unidade = :unidade GROUP BY u.unidade")
    Integer countByUsuariosConcluidosUnidade(@Param(value = "unidade")Unidade unidade);

    @Query("SELECT COUNT(*) > 0 FROM Unidade u WHERE u.chefe = :usuario")
    Boolean isChefia(@Param(value = "usuario")Usuario usuario);

    @Query("SELECT COUNT(*) > 0 FROM Unidade u WHERE u.viceChefe = :usuario")
    Boolean isViceChefia(@Param(value = "usuario")Usuario usuario);

    @Query(
        "SELECT COUNT(*) > 0 FROM Unidade u " +
        "WHERE u.unidadePai.chefe = :usuario OR u.unidadePai.viceChefe = :usuario"
    )
    Boolean hasUnidadesFilhas(@Param(value = "usuario")Usuario usuario);

    List<Usuario> findByUnidade(Unidade unidade);
}
