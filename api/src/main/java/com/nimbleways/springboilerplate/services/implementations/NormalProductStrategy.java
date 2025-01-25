package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.ProductStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class NormalProductStrategy implements ProductStrategy {

    private final ProductRepository productRepository;
    private final NotificationService notificationService;

    @Override
    public void process(Product product) {
        if (product.getAvailable() > 0) {
            product.setAvailable(product.getAvailable() - 1);
            productRepository.save(product);
        } else {
            int leadTime = product.getLeadTime();
            if (leadTime > 0) {
                notifyDelay(leadTime, product);
            }
        }
    }

    public void notifyDelay(int leadTime, Product p) {
        p.setLeadTime(leadTime);
        productRepository.save(p);
        notificationService.sendDelayNotification(leadTime, p.getName());
    }
}
