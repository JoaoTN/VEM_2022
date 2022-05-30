package br.ufc.quixada.npi.gestaocompetencia.repository;


import java.util.List;

import br.ufc.quixada.npi.gestaocompetencia.model.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.ufc.quixada.npi.gestaocompetencia.model.Avaliacao;
import br.ufc.quixada.npi.gestaocompetencia.model.Avaliacao.TipoAvaliacao;
import br.ufc.quixada.npi.gestaocompetencia.model.ItemAvaliacao;
import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Integer>{
	@Query("SELECT a FROM Avaliacao a WHERE a.diagnostico = :diagnostico AND a.tipo = :tipo AND a.avaliador = :avaliador AND a.perspectiva = :perspectiva ORDER BY a.diagnostico.inicioComportamental DESC")
	List<Avaliacao> findAvaliacao(Usuario avaliador, Diagnostico diagnostico, TipoAvaliacao tipo, Avaliacao.Perspectiva perspectiva);

	@Query("SELECT a FROM Avaliacao a WHERE a.diagnostico = :diagnostico AND a.tipo = :tipo AND a.avaliador = :avaliador AND a.perspectiva = :perspectiva AND a.ignorada = false ORDER BY a.diagnostico.inicioComportamental DESC")
	List<Avaliacao> findAvaliacaoByAvaliador(Usuario avaliador, Diagnostico diagnostico, TipoAvaliacao tipo, Avaliacao.Perspectiva perspectiva);

	@Query("SELECT a FROM Avaliacao a WHERE a.diagnostico = :diagnostico AND a.tipo = :tipo AND a.avaliado = :avaliado AND a.perspectiva = :perspectiva AND a.ignorada = false ORDER BY a.diagnostico.inicioComportamental DESC")
	List<Avaliacao> findAvaliacaoByAvaliado (Usuario avaliado, Diagnostico diagnostico, TipoAvaliacao tipo, Avaliacao.Perspectiva perspectiva);

	@Query("SELECT ia FROM ItemAvaliacao ia INNER JOIN ia.avaliacao a WHERE a.diagnostico = :diagnostico AND a.tipo = :tipo AND a.avaliado = :avaliado AND a.perspectiva = :perspectiva AND a.ignorada = false ORDER BY a.diagnostico.inicioComportamental DESC")
	List<ItemAvaliacao> findItemAvaliacaoByAvaliado (Usuario avaliado, Diagnostico diagnostico, TipoAvaliacao tipo, Avaliacao.Perspectiva perspectiva);

	
	List<Avaliacao> findByAvaliadorAndTipoAndIgnoradaFalse(Usuario usuario, TipoAvaliacao subordinado);

	@Query("SELECT a FROM Avaliacao a WHERE a.unidade = :unidade AND a.diagnostico = :diagnostico AND a.tipo = :tipo AND a.perspectiva = :perspectiva ORDER BY a.diagnostico.inicioComportamental DESC")
	List<Avaliacao> findByUnidade(Unidade unidade, Diagnostico diagnostico, TipoAvaliacao tipo, Avaliacao.Perspectiva perspectiva);

	Avaliacao findByAvaliadorAndAvaliadoAndTipoAndIgnoradaFalse(Usuario avaliador, Usuario avaliado, TipoAvaliacao tipo);

	@Query("SELECT av FROM Avaliacao av WHERE av.unidade = :unidade AND av.avaliador >= :avaliador AND av.ignorada = false")
	List<Avaliacao> findByAvaliadorAndUnidade(Usuario avaliador, Unidade unidade);
	
	@Query("SELECT a FROM Avaliacao a WHERE a.avaliador = :avaliador AND a.diagnostico = :diagnostico AND a.tipo = :tipo AND a.perspectiva = :perspectiva AND a.ignorada = true ORDER BY a.diagnostico.inicioComportamental DESC")
	List<Avaliacao> findIgnoradas(Usuario avaliador, Diagnostico diagnostico, TipoAvaliacao tipo, Avaliacao.Perspectiva perspectiva);

	@Query("SELECT a FROM Avaliacao a WHERE a.diagnostico = :diagnostico AND a.unidade = :unidade AND a.tipo = :tipo AND a.perspectiva = :perspectiva AND a.ignorada = true ORDER BY a.diagnostico.inicioComportamental DESC")
	List<Avaliacao> findJustificadasUnidade(Diagnostico diagnostico, Unidade unidade, TipoAvaliacao tipo, Avaliacao.Perspectiva perspectiva);

	@Query("SELECT a FROM Avaliacao a WHERE a.diagnostico = :diagnostico AND a.avaliador = :usuario AND a.tipo = :tipo AND a.perspectiva = :perspectiva AND a.ignorada = true ORDER BY a.diagnostico.inicioComportamental DESC")
	List<Avaliacao> findJustificadasUsuario(Diagnostico diagnostico, Usuario usuario, TipoAvaliacao tipo, Avaliacao.Perspectiva perspectiva);

	@Query("SELECT a FROM Avaliacao a WHERE a.diagnostico = :diagnostico AND a.tipo = :tipo AND a.avaliador = :avaliador AND a.perspectiva = :perspectiva AND a.ignorada = false")
	Avaliacao findAvaliacaoDiagnosticoByAvaliador(Usuario avaliador, Diagnostico diagnostico, TipoAvaliacao tipo, Avaliacao.Perspectiva perspectiva);

	@Query("SELECT a FROM Avaliacao a WHERE a.diagnostico = :diagnostico AND a.tipo = :tipo AND a.avaliado = :avaliado AND a.perspectiva = :perspectiva AND a.ignorada = false")
	Avaliacao findAvaliacaoDiagnosticoByAvaliado(Usuario avaliado, Diagnostico diagnostico, TipoAvaliacao tipo, Avaliacao.Perspectiva perspectiva);
}
