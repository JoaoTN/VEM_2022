package br.ufc.quixada.npi.gestaocompetencia.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import br.ufc.quixada.npi.gestaocompetencia.model.Comportamento;
import br.ufc.quixada.npi.gestaocompetencia.model.Mapeamento;
import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.CategoriaComportamento;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.Etapa;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.SituacaoComportamento;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@Api(tags = {"Comportamento"})
public interface ComportamentoController {

	/**
	 * Retorna algumas informações referentes a dinamica de gosto/não gosto/ideal seria, como por exemplo se 
	 * está no período de cadastro de servidor ou não, quantos comportamentos cadastrados há no total e 
	 * quantos por categoria.
	 * 
	 * @param mapeamento - Mapeamento desejado
	 * @param usuario - Usuário que está autenticado no sistema
	 * @return Um objeto contendo as seguintes informações: <br/>
	 * <ul>
	 * <li> <strong>ativo: </strong> Um booleano indicando se está no período de cadastros de comportamentos
	 * pelos servidoress ou não.<em>True</em> se estiver ou <em>False</em> se não estiver</li>
	 * <li> <strong>qtd_gosto: </strong> Um inteiro indicando a quantidade de comportamentos
	 * já cadastrados na categoria <em>GOSTO</em> </li>
	 * <li> <strong>qtd_naoGosto: </strong> Um inteiro indicando a quantidade de comportamentos
	 * já cadastrados na categoria <em>NÃO_GOSTO</em> </li>
	 * <li> <strong>qtd_ideal: </strong> Um inteiro indicando a quantidade de comportamentos
	 * já cadastrados na categoria <em>IDEAL</em> </li>
	 * <li> <strong>total: </strong> Um inteiro indicando a quantidade total de comportamentos 
	 * cadastrados pelo usuário nesse mapeamento</li>
	 * </ul>
	 */
	@ApiOperation(value = "Retorna algumas informações referentes a dinamica de gosto/não gosto/ideal seria, como por exemplo "
			+ "se está no período de cadastro de servidor ou não, quantos comportamentos cadastrados há no total "
			+ "e quantos por categoria",
			  authorizations = {@Authorization(value = "Token Authorization")})
	ResponseEntity<Map<String, Object>> getStatus(Mapeamento mapeamento, Usuario usuario);
	
	/**
	 * Salva um novo comportamento
	 * @param mapeamento - Mapeamento em que o comportamento será cadastrado
	 * @param comportamento - Comportamento a ser cadastrado
	 * @param usuario - Usuário(Servidor) que está cadastrando o comportamento
	 * @param request - Objeto contendo as informações da requisição. Injetado automaticamente pelo spring framework
	 * @return Comportamento salvo no sistema
	 */
	@ApiOperation(value = "Salva um novo comportamento", authorizations = {@Authorization(value = "Token Authorization")})
	ResponseEntity<Comportamento> save(Mapeamento mapeamento, Comportamento comportamento, Usuario usuario, HttpServletRequest request);
	
	/**
	 * Retorna os dados de um comportamento especifico
	 * @param comportamento - Comportamento a ser pesquisado. Objeto injetado automaticamente pelo spring a partir do valor do PathVariable
	 * @param usuario - Usuário que está logado no sistema
	 * @param request - Objeto contendo as informações da requisição. Injetado automaticamente pelo spring framework
	 * @return Comportamento com a chave buscada
	 */
	@ApiOperation(value = "Retorna os dados de um comportamento especifico",
			  authorizations = {@Authorization(value = "Token Authorization")})
	ResponseEntity<Comportamento> findById(Comportamento comportamento, Usuario usuario, HttpServletRequest request);
	
	/**
	 * Retorna todos os comportamentos cadastrados pelo usuário em um mapeamento agrupados por categoria
	 * @param mapeamento - {@link Mapeamento mapeamento} no qual se deseja buscar
	 * @param usuario - Usuário logado no sistema e que será o servidor dos quais os comportamentos serão devolvidos
	 * @param request - Objeto contendo as informações da requisição. Injetado automaticamente pelo spring framework
	 * @return Uma Objeto contendo todos os comportamentos do usuário no mapeamento e agrupados por categoria
	 */
	@ApiOperation(value = "Retorna todos os comportamentos cadastrados pelo usuário em um mapeamento agrupados por categoria",
			  authorizations = {@Authorization(value = "Token Authorization")})
	ResponseEntity<Map<CategoriaComportamento, List<Comportamento>>>findByCategoria(Mapeamento mapeamento, 
												Usuario usuario, HttpServletRequest request);
	
	/**
	 * Atualiza os dados de um comportamento a partir de um identificador. 
	 * @param comportamentoOriginal - Comportamento a qual se deseja atualizar(Injetado automaticamente pelo
	 * spring a partir do PathVariable contendo a chave primaria do comportamento
	 * @param comportamento - Comportamento contendo as novas informações a serem persistidas no banco de dados
	 * @param usuario - Usuário logado no sistema, ou seja, servidor que está atualizando esse comportamento 
	 * @param request - Objeto contendo as informações da requisição. Injetado automaticamente pelo spring framework
	 * @return Comportamento atualizado
	 */
	@ApiOperation(value = "Atualiza os dados de um comportamento a partir de um identificador",
			  authorizations = {@Authorization(value = "Token Authorization")})
	ResponseEntity<Comportamento> update(Comportamento comportamentoOriginal, Etapa etapa, Comportamento comportamento,
									Usuario usuario, HttpServletRequest request);
	
	/**
	 * Remove um comportamento a partir de um identificador. Não remove fisicamente do banco de dados,
	 * apenas logicamente através da modificação do atributo excluído para false
	 * @param comportamento - Comportamento a ser removido. Injetado automaticamente pelo spring a partir do
	 * PathVariable contendo a chave primaria do comportamento
	 * @param usuario - Usuário logado no sistema, ou seja, servidor que está tentando remover esse comportamento
	 * @param request - Objeto contendo as informações da requisição. Injetado automaticamente pelo spring framework
	 * @return Retorna o objeto com o estado de excluído
	 */
	@ApiOperation(value = "Remove um comportamento a partir de um identificador",
			  authorizations = {@Authorization(value = "Token Authorization")})
    ResponseEntity<Void> delete(Comportamento comportamento, Usuario usuario, HttpServletRequest request);
	
	/**
	 * Retorna uma lista de comportamentos atrelados a um mapeamento e outros parametros adicionais que
	 * podem ser passados na requisição
	 * @param unidade - Parâmetro opcional que indica uma lista de unidades a serem buscadas
	 * @param situacao - Parâmetro que indica a {@link SituacaoComportamento situacao} a ser buscada 
	 * @param competenciaId - Parâmetro opcional que indica uma competencia a ser buscada
	 * @param subunidades - Parâmetro opcional que indica se deve ser buscadas as subunidades das unidades informadas
	 * @param usuario - Usuário logado no sistema, ou seja, servidor que está tentando buscar esses comportamentos
	 * @return Uma lista de comportamentos respeitando os filtros de busca informados
	 */
	
	@ApiOperation(value = "Retorna uma lista de comportamentos atrelados a um mapeamento e outros "
	 		+ "parametros adicionais que podem ser passados na requisição", 
	 		authorizations = {@Authorization(value = "Token Authorization")})
	ResponseEntity<List<Comportamento>> findByMapeamento(Unidade[] unidade, Mapeamento mapeamento,
			SituacaoComportamento situacao, Integer competenciaId, Boolean subunidades,
			Usuario usuario);
	
	/**
	 * Consolida uma lista de comportamentos
	 * @param mapeamento - {@link Mapeamento mapeamento} no qual se deseja consolidar os comportamentos
	 * @param comportamentos - Lista de comportamentos a serem normalizados
	 * @param usuario - Usuário logado no sistema, ou seja, servidor que está tentando consolidar esses comportamentos
	 * @param request - Objeto contendo as informações da requisição. Injetado automaticamente pelo spring framework
	 * @return Retorna a lista de comportamentos normalizados
	 */
	@ApiOperation(value = "Consolida uma lista de comportamentos",
			  authorizations = {@Authorization(value = "Token Authorization")})
	ResponseEntity<List<Comportamento>> consolidarComportamentos(List<Comportamento> comportamentos, Usuario usuario);
	
	/**
	 * Restaura um comportamento que está excluído
	 * @param comportamento - Comportamento a ser restaurado
	 * @param usuario - Usuário logado no sistema, ou seja, servidor que está tentando restaurar o comportamento
	 * @param request - Objeto contendo as informações da requisição. Injetado automaticamente pelo spring framework
	 * @return Um comportamento resturado(Ou seja, com o atributo excluido = false)
	 */
	@ApiOperation(value = "Restaura um comportamento que está excluído",
			  authorizations = {@Authorization(value = "Token Authorization")})
	ResponseEntity<Comportamento> restaurarComportamento(Comportamento comportamento, Usuario usuario,
							HttpServletRequest request);
	
	/**
	 * Desconsolida um comportamento que está consolidado
	 * @param comportamento - Comportamento a ser desconsolidado
	 * @param usuario - Usuário logado no sistema, ou seja, servidor que está tentando desconsolidar o comportamento
	 * @param request - Objeto contendo as informações da requisição. Injetado automaticamente pelo spring framework
	 * @return Um comportamento desconsolidado(Ou seja, com o atributo consolidado = false)
	 */
	@ApiOperation(value = "Desconsolida um comportamento que está consolidado",
			  authorizations = {@Authorization(value = "Token Authorization")})
	ResponseEntity<Comportamento> desconsolidarComportamento(Comportamento comportamento, Usuario usuario,
							HttpServletRequest request);
	
}
