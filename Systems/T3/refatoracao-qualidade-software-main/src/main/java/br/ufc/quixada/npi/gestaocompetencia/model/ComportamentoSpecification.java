package br.ufc.quixada.npi.gestaocompetencia.model;



import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import br.ufc.quixada.npi.gestaocompetencia.model.enums.SituacaoComportamento;

public class ComportamentoSpecification implements Specification<Comportamento>{

	private static final long serialVersionUID = 1L;
	private static final String EXCLUIDO = "excluido";
	private static final String UNIDADE = "unidade";
	private static final String COMPETENCIA = "competencia";
	private ComportamentoSearch criteria;
	
	public ComportamentoSpecification(ComportamentoSearch criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<Comportamento> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {	
		final List<Predicate> predicates = new ArrayList<>();

		Path<Unidade> unidade = root.get(UNIDADE);
		
		if(criteria.getCompetencia() != null) {
			predicates.add(criteriaBuilder.equal(root.get(COMPETENCIA), criteria.getCompetencia()));
		}

		if(criteria.getMapeamento() != null) {
			predicates.add(criteriaBuilder.equal(root.get("mapeamento"), criteria.getMapeamento()));
		}

		// Filtro de Situação
		if(criteria.getSituacao() != null) {
			if(criteria.getSituacao().equals(SituacaoComportamento.NAO_ASSOCIADOS)) {
				predicates.add(criteriaBuilder.and(criteriaBuilder.isNull(root.get(COMPETENCIA)),criteriaBuilder.isFalse(root.get(EXCLUIDO))));
			} else if(criteria.getSituacao().equals(SituacaoComportamento.ASSOCIADOS)) {
				predicates.add(criteriaBuilder.and(criteriaBuilder.isNotNull(root.get(COMPETENCIA)),criteriaBuilder.isFalse(root.get(EXCLUIDO))));
			} else if(criteria.getSituacao().equals(SituacaoComportamento.NAO_CONSOLIDADOS)) {
				predicates.add(criteriaBuilder.and(criteriaBuilder.isFalse(root.get("consolidado")),criteriaBuilder.isFalse(root.get(EXCLUIDO))));
			}else if(criteria.getSituacao().equals(SituacaoComportamento.CONSOLIDADOS)) {
				predicates.add(criteriaBuilder.isTrue(root.get("consolidado")));
			} else if(criteria.getSituacao().equals(SituacaoComportamento.EXCLUIDOS)) {
				predicates.add(criteriaBuilder.isTrue(root.get(EXCLUIDO)));
			} else if(criteria.getSituacao().equals(SituacaoComportamento.TODOS) || criteria.getSituacao().equals(SituacaoComportamento.ORIGINAIS)) {
				predicates.add(criteriaBuilder.isFalse(root.get(EXCLUIDO)));
			} else if(criteria.getSituacao().equals(SituacaoComportamento.EDITADOS)) {
				predicates.add(
					criteriaBuilder.and(
						criteriaBuilder.isNotNull(root.get("descricaoAtualizada")),
						criteriaBuilder.isFalse(root.get(EXCLUIDO)),
						criteriaBuilder.notEqual(root.get("descricao"), root.get("descricaoAtualizada"))
					)
				);
			}
		}

        if(criteria.getUnidades() != null) {
            predicates.add(unidade.in(criteria.getUnidades()));
        }
		
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

}