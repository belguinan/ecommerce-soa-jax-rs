package com.example.productservice.contracts;

import com.example.productservice.database.entities.Product;
import com.example.productservice.pagination.Page;
import com.example.productservice.pagination.PaginationParams;
import com.example.productservice.requests.FilterRequest;

public interface ProductServiceContract {

    /**
     * @param params
     * @return Page<Product>
     */
    public Page<Product> index(PaginationParams params, FilterRequest filterRequest);

    /**
     * @param product
     * @return Product
     */
    public Product store(Product product);

    /**
     * @param id
     * @return Product
     */
    public Product show(Long id);

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
