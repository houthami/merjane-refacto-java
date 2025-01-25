package com.nimbleways.springboilerplate.services.implementations;

import java.time.LocalDate;
import java.util.Map;

import com.nimbleways.springboilerplate.services.IProductService;
import com.nimbleways.springboilerplate.services.ProductStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements IProductService {

    private final Map<String, ProductStrategy> strategies;

    @Override
    public void processProduct(Product product) {
        ProductStrategy strategy = strategies.get(product.getType());
        if (strategy != null) {
            strategy.process(product);
        } else {
            throw new UnsupportedOperationException("Unsupported product type: " + product.getType());
        }
    }
}