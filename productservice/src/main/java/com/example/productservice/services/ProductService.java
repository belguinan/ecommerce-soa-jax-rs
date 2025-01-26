package com.example.productservice.services;

import com.example.productservice.auth.AuthContext;
import com.example.productservice.auth.JwtService;
import com.example.productservice.contracts.ProductServiceContract;
import com.example.productservice.database.entities.Product;
import com.example.productservice.database.repositories.ProductRepository;
import com.example.productservice.pagination.Page;
import com.example.productservice.pagination.PaginationFactory;
import com.example.productservice.pagination.PaginationParams;
import jakarta.ws.rs.NotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements ProductServiceContract {

    private final ProductRepository productRepository;
    private final JwtService jwtService;
    private final AuthContext authContext;

    /**
     * @param productRepository
     * @param jwtService
     * @param authContext
     */
    public ProductService(ProductRepository productRepository, JwtService jwtService, AuthContext authContext) {
        this.jwtService = jwtService;
        this.productRepository = productRepository;
        this.authContext = authContext;
    }

    @Override
    public Page<Product> index(PaginationParams params) {
        PaginationFactory<Product> paginationFactory = new PaginationFactory<>(this.productRepository);
        return paginationFactory.create(params);
    }

    @Override
    public Page<Product> sellerProducts(PaginationParams params) {
        PaginationFactory<Product> paginationFactory = new PaginationFactory<>(this.productRepository);
        Specification<Product> specification = (root, query, builder) -> builder.equal(root.get("sellerId"), this.authContext.getCurrentUserId());

        return paginationFactory.create(params, specification);
    }

    @Override
    public Product store(Product product) {
        product.setSellerId(this.authContext.getCurrentUserId());
        return this.productRepository.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        Product dbProduct = this.productRepository
                .findBySellerIdAndId(id, this.authContext.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException());

        product.setId(dbProduct.getId());
        product.setSellerId(dbProduct.getSellerId());
        return this.productRepository.save(product);
    }

    @Override
    public boolean destroy(Long id) {
        Product dbProduct = this.productRepository
                .findBySellerIdAndId(id, this.authContext.getCurrentUserId())
                .orElseThrow();

        this.productRepository.delete(dbProduct);
        return true;
    }
}
