package com.digitalhouse.money.accountservice.specs;

import com.digitalhouse.money.accountservice.data.dto.TransactionFilterRequestDTO;
import com.digitalhouse.money.accountservice.data.enums.ActivityType;
import com.digitalhouse.money.accountservice.data.model.Transaction;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionFilterSpecification {

    private TransactionFilterSpecification() {
        throw new IllegalStateException("Utility class");
    }

    public static Specification<Transaction> getFilterSpecification(UUID accountId, TransactionFilterRequestDTO filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("transactionDate"), filter.getStartDate()));
            }
            if (filter.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("transactionDate"), filter.getEndDate()));
            }
            if (filter.getMinAmount() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("transactionAmount"), filter.getMinAmount()));
            }
            if (filter.getMaxAmount() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("transactionAmount"), filter.getMaxAmount()));
            }
            if (filter.getTransactionType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("transactionType"), filter.getTransactionType()));
            }
            if (filter.getActivityType() != null) {
                if (filter.getActivityType().equals(ActivityType.RECEITA)) {
                    predicates.add(criteriaBuilder.equal(root.get("recipientAccountNumber"), accountId));
                } else if (filter.getActivityType().equals(ActivityType.DESPESA)) {
                    predicates.add(criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("originAccountNumber"), accountId),
                            criteriaBuilder.notEqual(root.get("recipientAccountNumber"), accountId)
                    ));
                } else {
                    predicates.add(criteriaBuilder.or(
                            criteriaBuilder.equal(root.get("originAccountNumber"), accountId),
                            criteriaBuilder.equal(root.get("recipientAccountNumber"), accountId)
                    ));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

    }
}
