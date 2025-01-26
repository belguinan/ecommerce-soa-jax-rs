package com.example.productservice.contracts;

import com.example.productservice.database.entities.Product;
import com.example.productservice.pagination.Page;
import com.example.productservice.pagination.PaginationParams;

public interface ProductServiceContract {

    /**
     * @param params
     * @return Page<Product>
     */
    public Page<Product> index(PaginationParams params);

    /**
     * @param params
     * @param sellerId
     * @return Page<Product>
     */
    public Page<Product> sellerProducts(PaginationParams params);

    /**
     * @param product
     * @return Product
     */
    public Product store(Product product);

    /**
     * @param id
     * @param product
     * @return Product
     */
    public Product update(Long id, Product product);

    /**
     *
     * @param id
     * @return boolean
     */
    public boolean destroy(Long id);
}
