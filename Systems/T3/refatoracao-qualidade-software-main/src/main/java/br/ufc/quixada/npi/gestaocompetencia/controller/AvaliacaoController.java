package br.ufc.quixada.npi.gestaocompetencia.controller;

import java.util.Collection;
import java.util.List;

import br.ufc.quixada.npi.gestaocompetencia.model.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;

import br.ufc.quixada.npi.gestaocompetencia.model.Avaliacao.TipoAvaliacao;
import io.swagger.annotations.Api;

@Api(tags = {"Avaliacao"})
public interface AvaliacaoController {
	@ApiOperation(
		value = "Retorna a avaliação a partir do id informado",
		authorizations = {@Authorization(value = "Token Authorization")}
	)
	ResponseEntity<Avaliacao> find(Avaliacao avaliacao);

	@ApiOperation(
		value = "Retorna a avaliação mais recente a partir do diagnóstico e do tipo informado",
		authorizations = {@Authorization(value = "Token Authorization")}
	)
	ResponseEntity<Avaliacao> findLast(Usuario usuario, Diagnostico diagnostico, TipoAvaliacao tipoAvaliacao);

	@ApiOperation(
			value = "Retorna todas as avaliações",
			authorizations = {@Authorization(value = "Token Authorization")}
	)
	ResponseEntity<List<Avaliacao>> findAll();

	/**
	 * Retorna todas avaliações de um avaliador em uma unidade
	 * 
	 * @param avaliador
	 * @param unidade
	 * @return todas as avaliações de um avaliador em certa  unidade 
	 */
	ResponseEntity<Collection<Avaliacao>> getAllAvaliacaoByUserAndUnidade(Usuario avaliador,Unidade unidade);
	
	/**
	 * 
	 * Retorna todas as avaliacoes de um avaliador de um certo tipo
	 * 
	 * @param usuario
	 * @param tipo
	 * @return todas avaliacoes de um avaliador de certo tipo
	 */
	ResponseEntity<Collection<Avaliacao>> getAvaliacaoByTipoAvaliacaoAndUsuario(Usuario usuario,TipoAvaliacao tipo);
	
	/**
	 * 
	 * Cria uma avaliacao comportamental
	 * 
	 * @param usuario
	 * @param tipo
	 * @param avaliado
	 * @param avaliacaoComportamental(nota,fator(id))
	 * 
	 * -> Busca valiação pelo Avaliador, Avaliado e Tipo da Avaliação 
	 * -> Adiciona a Avaliação á Avaliação Comportamental recebida no RequestBody
	 * -> Cria e Salva nova Avaliacao Comportamental;
	 * -> Adiciona nova Avaliação Comportamental na Avaliação
	 * -> Atualiza avaliação
	 * 
	 * @return Avaliação Atualizada
	 */
	ResponseEntity<Avaliacao> addAvaliacaoComportamental( Usuario usuario,
			TipoAvaliacao tipo, Usuario avaliado, ItemAvaliacao avaliacaoComportamental);

	/**
	 * Deleta uma avaliação inteira
	 * 
	 * @param usuario
	 * @param avaliacao
	 */
	void deleteAvaliacao(Usuario usuario, Avaliacao avaliacao);

	
	/**
	 * Atualiza uma avaliacao
	 * 
	 * @param usuario
	 * @param avaliacao
	 * @param atualizada
	 * @return Avaliacao Atualizada
	 */
	ResponseEntity<ItemAvaliacao> putAtualizacaoComportamental(Usuario usuario,
                                                               ItemAvaliacao avaliacao, ItemAvaliacao atualizada);

	
	/**
	 * cria uma avaliacao
	 * 
	 * @param avaliador
	 * @param avaliacao
	 * @return
	 */
	ResponseEntity<Avaliacao> addAvaliacao(Usuario avaliador, Avaliacao avaliacao);


}
