package com.example.orderservice.helpers;

import com.example.orderservice.requests.FilterRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class SpecificationFactory<T> {

    private static <T> Specification<T> createSpecification(String key, String value) {
        return (root, query, builder) ->
            builder.equal(root.get(key), value);
    }

    public static <T> Specification<T> create(FilterRequest filter) {
        Specification<T> spec = Specification.where(null);

        if (filter != null && filter.getFilters() != null) {
            for (Map.Entry<String, String> entry : filter.getFilters().entrySet()) {
                spec = spec.and(createSpecification(entry.getKey(), entry.getValue()));
            }
        }

        return spec;
    }
}