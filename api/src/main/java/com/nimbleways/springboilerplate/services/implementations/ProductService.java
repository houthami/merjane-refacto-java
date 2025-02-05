package com.nimbleways.springboilerplate.services.implementations;

import java.util.HashMap;
import java.util.Map;

import com.nimbleways.springboilerplate.services.IProductService;
import com.nimbleways.springboilerplate.services.ProductStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.nimbleways.springboilerplate.entities.Product;

@Service
//@RequiredArgsConstructor
@Slf4j
public class ProductService implements IProductService {

    private final Map<String, ProductStrategy> strategies;

    public ProductService(ExpirableProductStrategy expirableProductStrategy, NormalProductStrategy normalProductStrategy, SeasonalProductStrategy seasonalProductStrategy) {
        strategies = new HashMap<>();
        strategies.put("EXPIRABLE", expirableProductStrategy);
        strategies.put("NORMAL", normalProductStrategy);
        strategies.put("SEASONAL", seasonalProductStrategy);
    }

    @Override
    public void processProduct(Product product) {
        log.info("Processing product: {}", product);
        ProductStrategy strategy = strategies.get(product.getType());
        if (strategy != null) {
            strategy.process(product);
        } else {
            throw new UnsupportedOperationException("Unsupported product type: " + product.getType());
        }
    }

}