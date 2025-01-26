package com.example.productservice.pagination;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public class PaginationFactory<T> {
    private final JpaSpecificationExecutor<T> repository;

    public PaginationFactory(JpaRepository<T, ?> repository) {
        this.repository = (JpaSpecificationExecutor<T>) repository;
    }

    public Page<T> create(PaginationParams params) {
        return this.create(params, Specification.where(null));
    }

    public Page<T> create(PaginationParams params, Specification<T> specification) {
        PageRequest pageRequest = PageRequest.of(
            params.getPage() - 1,
            params.getPerPage(),
            Sort.by(
                params.getSortOrder().equalsIgnoreCase("DESC") ?
                    Sort.Direction.DESC :
                    Sort.Direction.ASC,
                params.getSortBy()
            )
        );

        org.springframework.data.domain.Page result = this.repository.findAll(
            specification,
            pageRequest
        );

        PaginationMeta meta = new PaginationMeta(
            params.getPage(),
            params.getPerPage(),
            result.getTotalElements()
        );

        return new Page<T>(result.getContent(), meta);
    }
}