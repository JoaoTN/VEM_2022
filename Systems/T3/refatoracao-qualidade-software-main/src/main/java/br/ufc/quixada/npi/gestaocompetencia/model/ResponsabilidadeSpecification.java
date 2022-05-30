package br.ufc.quixada.npi.gestaocompetencia.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.SituacaoResponsabilidade;

public class ResponsabilidadeSpecification implements Specification<Responsabilidade>{
	private static final long serialVersionUID = 1L;
	private ResponsabilidadeSearch criteria;
	
	public ResponsabilidadeSpecification(ResponsabilidadeSearch criteria) {
		this.criteria = criteria;
	}
	
	@Override
	public Predicate toPredicate(Root<Responsabilidade> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		final List<Predicate> predicates = new ArrayList<>();

		
		
		if(criteria.getSituacao() != null && ! criteria.getSituacao().equals(SituacaoResponsabilidade.EXCLUIDAS)) {
			predicates.add(criteriaBuilder.isFalse(root.get("excluida")));
			if(criteria.getSituacao().equals(SituacaoResponsabilidade.VALIDADAS)) {
				predicates.add(criteriaBuilder.isTrue(root.get("validada")));
			}else if(criteria.getSituacao().equals(SituacaoResponsabilidade.NAO_VALIDADAS)) {
				predicates.add(criteriaBuilder.isFalse(root.get("validada")));
			}else if(criteria.getSituacao().equals(SituacaoResponsabilidade.EDITADAS)) {
				predicates.add(criteriaBuilder.isTrue(root.get("editada")));
			}else if(criteria.getSituacao().equals(SituacaoResponsabilidade.NAO_EDITADAS)) {
				predicates.add(criteriaBuilder.isFalse(root.get("editada")));
			}else if(criteria.getSituacao().equals(SituacaoResponsabilidade.CONSOLIDADAS)) {
				predicates.add(criteriaBuilder.isTrue(root.get("consolidada")));
			}else if(criteria.getSituacao().equals(SituacaoResponsabilidade.NAO_CONSOLIDADAS)) {
				predicates.add(criteriaBuilder.isFalse(root.get("consolidada")));
			}
		}
		else {
			predicates.add(criteriaBuilder.isTrue(root.get("excluida")));
		}

		
		if(criteria.getUnidades() != null) {
				predicates.add(root.get("unidade").in(criteria.getUnidades()));
		}

		if(criteria.getMapeamento() != null) {
			predicates.add(root.get("mapeamento").in(criteria.getMapeamento()));
		}

		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

}
