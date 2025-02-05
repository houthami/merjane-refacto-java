package com.nimbleways.springboilerplate.controllers;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @MockBean
    private NotificationService notificationService;

    private Order order;

    @BeforeEach
    public void setUp() {
        // Clear existing data
        orderRepository.deleteAll();
        productRepository.deleteAll();

        // Create test products
        Set<Product> products = new HashSet<>();
        products.add(new Product(null, 15, 30, "NORMAL", "USB Cable", null, null, null));
        products.add(new Product(null, 10, 0, "NORMAL", "USB Dongle", null, null, null));
        products.add(new Product(null, 15, 30, "EXPIRABLE", "Butter", LocalDate.now().plusDays(26), null, null));
        products.add(new Product(null, 90, 6, "EXPIRABLE", "Milk", LocalDate.now().minusDays(2), null, null));
        products.add(new Product(null, 15, 30, "SEASONAL", "Watermelon", null, LocalDate.now().minusDays(2), LocalDate.now().plusDays(58)));
        products.add(new Product(null, 15, 30, "SEASONAL", "Grapes", null, LocalDate.now().plusDays(180), LocalDate.now().plusDays(240)));

        productRepository.saveAll(products);

        order = new Order();
        order.setItems(products);
        order = orderRepository.save(order);
    }

    @Test
    @Transactional
    public void processOrder_ShouldProcessOrderAndReturnOkStatus() throws Exception {
        // Act
        mockMvc.perform(post("/orders/{orderId}/processOrder", order.getId())
                        .contentType("application/json"))
                .andExpect(status().isOk());

        Order processedOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertEquals(order.getId(), processedOrder.getId());
        // Verify product availability updates
        for (Product product : processedOrder.getItems()) {
            if (product.getType().equals("NORMAL") && product.getAvailable() > 0) {
                assertEquals(29, product.getAvailable());
            }
        }
    }
}