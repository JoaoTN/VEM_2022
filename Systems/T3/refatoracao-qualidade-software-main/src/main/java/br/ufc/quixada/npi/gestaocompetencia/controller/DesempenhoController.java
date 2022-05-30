package br.ufc.quixada.npi.gestaocompetencia.controller;

import java.util.List;
import java.util.Map;

import br.ufc.quixada.npi.gestaocompetencia.model.Avaliacao;
import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import org.springframework.http.ResponseEntity;

import br.ufc.quixada.npi.gestaocompetencia.model.Diagnostico;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@Api(tags = { "Desempenho" })
public interface DesempenhoController {
	@ApiOperation(value = "Retorna as avaliações que o usuário fez ou recebeu a partir do último diagnóstico ou de outro específico", authorizations = {
			@Authorization(value = "Token Authorization") })
	ResponseEntity<List<Map<String, Object>>> getAvaliacoes(Usuario usuario, Diagnostico diagnosticoId, Avaliacao.TipoAvaliacao tipo,
		Avaliacao.Perspectiva perspectiva, boolean recebidas);

	@ApiOperation(value = "Retorna as avaliações dos servidores do usuário a partir do último diagnóstico ou de outro específico, tanto da própria unidade como de outra específica", authorizations = {
			@Authorization(value = "Token Authorization") })
	ResponseEntity<List<Map<String, Object>>> getAvaliacoesServidores(Usuario usuario, Diagnostico diagnosticoId, Avaliacao.TipoAvaliacao tipo,
		Avaliacao.Perspectiva perspectiva, Unidade unidade);
}
