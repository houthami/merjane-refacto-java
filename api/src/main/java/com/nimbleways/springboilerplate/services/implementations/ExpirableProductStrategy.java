package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.ProductStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ExpirableProductStrategy implements ProductStrategy {

    private final ProductRepository productRepository;
    private final NotificationService notificationService;

    @Override
    public void process(Product product) {
        if (product.getAvailable() > 0 && product.getExpiryDate().isAfter(LocalDate.now())) {
            product.setAvailable(product.getAvailable() - 1);
            productRepository.save(product);
        } else {
            handleExpiredProduct(product);
        }
    }

    public void handleExpiredProduct(Product product) {
        if (product.getAvailable() > 0 && product.getExpiryDate().isAfter(LocalDate.now())) {
            product.setAvailable(product.getAvailable() - 1);
            productRepository.save(product);
        } else {
            notificationService.sendExpirationNotification(product.getName(), product.getExpiryDate());
            product.setAvailable(0);
            productRepository.save(product);
        }
    }
}
