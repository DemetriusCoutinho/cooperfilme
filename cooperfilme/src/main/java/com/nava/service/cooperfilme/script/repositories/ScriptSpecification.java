package com.nava.service.cooperfilme.script.repositories;

import com.nava.service.cooperfilme.script.entities.Script;
import com.nava.service.cooperfilme.script.requests.ScriptFilter;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ScriptSpecification {

    public static Specification<Script> filters(
            ScriptFilter filter
    ) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.name() != null && !filter.name().isEmpty())
                predicates.add(builder.equal(builder.upper(root.get("nameClient")), filter.name().toUpperCase()));
            if (filter.email() != null && !filter.email().isEmpty())
                predicates.add(builder.equal(builder.upper(root.get("email")), filter.email()));
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
