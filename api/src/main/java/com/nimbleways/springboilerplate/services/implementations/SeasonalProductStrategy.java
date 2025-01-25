package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.ProductStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class SeasonalProductStrategy implements ProductStrategy {

    private final ProductRepository productRepository;
    private final NotificationService notificationService;

    @Override
    public void process(Product product) {
        if (LocalDate.now().isAfter(product.getSeasonStartDate()) && LocalDate.now().isBefore(product.getSeasonEndDate())
                && product.getAvailable() > 0) {
            product.setAvailable(product.getAvailable() - 1);
            productRepository.save(product);
        } else {
            handleSeasonalProduct(product);
        }
    }

    public void handleSeasonalProduct(Product product) {
        if (LocalDate.now().plusDays(product.getLeadTime()).isAfter(product.getSeasonEndDate())) {
            notificationService.sendOutOfStockNotification(product.getName());
            product.setAvailable(0);
            productRepository.save(product);
        } else if (product.getSeasonStartDate().isAfter(LocalDate.now())) {
            notificationService.sendOutOfStockNotification(product.getName());
            productRepository.save(product);
        } else {
            notifyDelay(product.getLeadTime(), product);
        }
    }
    public void notifyDelay(int leadTime, Product p) {
        p.setLeadTime(leadTime);
        productRepository.save(p);
        notificationService.sendDelayNotification(leadTime, p.getName());
    }

}
