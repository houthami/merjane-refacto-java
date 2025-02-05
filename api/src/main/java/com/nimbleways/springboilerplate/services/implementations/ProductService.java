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
        log.info("Processing product: {}", product);
        String strategyType = this.getStrategyType(product.getType());
        ProductStrategy strategy = strategies.get(strategyType);
        if (strategy != null) {
            strategy.process(product);
        } else {
            throw new UnsupportedOperationException("Unsupported product type: " + product.getType());
        }
    }

    private String getStrategyType(String type) {
        switch (type) {
            case "NORMAL":
                return "normalProductStrategy";
            case "SEASONAL":
                return "seasonalProductStrategy";
            case "EXPIRABLE":
                return "expirableProductStrategy";

            default:
                throw new UnsupportedOperationException("Unsupported product type: " + type);
        }
    }
}