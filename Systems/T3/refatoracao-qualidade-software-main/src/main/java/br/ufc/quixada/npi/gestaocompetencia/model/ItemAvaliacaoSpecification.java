package br.ufc.quixada.npi.gestaocompetencia.model;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class ItemAvaliacaoSpecification implements Specification<ItemAvaliacao> {
    private static final long serialVersionUID = 1L;
    private static final String AVALIACAO = "avaliacao";
    private ItemAvaliacaoSearch criteria;

    public ItemAvaliacaoSpecification(ItemAvaliacaoSearch criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<ItemAvaliacao> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicates = new ArrayList<>();

        predicates.add(
                criteriaBuilder.or(
                        root.get(AVALIACAO).get("avaliador").in(criteria.getAvaliador()),
                        root.get(AVALIACAO).get("avaliador").in(criteria.getVisualizador())
                )
        );

        if(criteria.getAvaliacoes() != null) {
            predicates.add(root.get(AVALIACAO).in(criteria.getAvaliacoes()));
        }

        if(criteria.getResponsabilidades() != null) {
            predicates.add(root.get("responsabilidade").in(criteria.getResponsabilidades()));
        }

        if(criteria.getComportamentos() != null) {
            predicates.add(root.get("fator").in(criteria.getComportamentos()));
        }

        if(criteria.getNotas() != null) {
            predicates.add(root.get("nota").in(criteria.getNotas()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
