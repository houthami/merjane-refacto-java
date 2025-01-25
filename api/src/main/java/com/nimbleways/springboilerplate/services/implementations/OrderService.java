package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.exception.ResourceNotFoundException;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.services.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Override
    public ProcessOrderResponse processOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        order.getItems().forEach(productService::processProduct);

        return new ProcessOrderResponse(order.getId());
    }
    /*public ProcessOrderResponse processOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).get();
        System.out.println(order);
        List<Long> ids = new ArrayList<>();
        ids.add(orderId);
        Set<Product> products = order.getItems();
        for (Product p : products) {
            if (p.getType().equals("NORMAL")) {
                if (p.getAvailable() > 0) {
                    p.setAvailable(p.getAvailable() - 1);
                    productRepository.save(p);
                } else {
                    int leadTime = p.getLeadTime();
                    if (leadTime > 0) {
                        productService.notifyDelay(leadTime, p);
                    }
                }
            } else if (p.getType().equals("SEASONAL")) {
                // Add new season rules
                if ((LocalDate.now().isAfter(p.getSeasonStartDate()) && LocalDate.now().isBefore(p.getSeasonEndDate())
                        && p.getAvailable() > 0)) {
                    p.setAvailable(p.getAvailable() - 1);
                    productRepository.save(p);
                } else {
                    productService.handleSeasonalProduct(p);
                }
            } else if (p.getType().equals("EXPIRABLE")) {
                if (p.getAvailable() > 0 && p.getExpiryDate().isAfter(LocalDate.now())) {
                    p.setAvailable(p.getAvailable() - 1);
                    productRepository.save(p);
                } else {
                    productService.handleExpiredProduct(p);
                }
            }
        }

        return new ProcessOrderResponse(order.getId());
    }*/
}
