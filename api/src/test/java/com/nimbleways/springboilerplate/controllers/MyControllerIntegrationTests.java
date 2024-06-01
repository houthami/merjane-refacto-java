package com.nimbleways.springboilerplate.controllers;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import com.nimbleways.springboilerplate.utils.Annotations.SetupDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;

// import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Specify the controller class you want to test
// This indicates to spring boot to only load UsersController into the context
// Which allows a better performance and needs to do less mocks
@SetupDatabase
@SpringBootTest
@AutoConfigureMockMvc
public class MyControllerIntegrationTests {
        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private NotificationService notificationService;

        @Autowired
        private OrderRepository orderRepository;

        @Autowired
        private ProductRepository productRepository;

        @Test
        public void processOrderShouldReturn() throws Exception {
                List<Product> allProducts = createProducts();
                Set<Product> orderItems = new HashSet<Product>(allProducts);
                Order order = createOrder(orderItems);
                productRepository.saveAll(allProducts);
                order = orderRepository.save(order);
                mockMvc.perform(post("/orders/{orderId}/processOrder", order.getId())
                                .contentType("application/json"))
                                .andExpect(status().isOk());
                Order resultOrder = orderRepository.findById(order.getId()).get();
                assertEquals(resultOrder.getId(), order.getId());
        }

        @Test
        public void processOrderFlushSallProductShouldNotPass() throws Exception {
                List<Product> allProducts = productCanTBeSelled();
                Set<Product> orderItems = new HashSet<Product>(allProducts);
                Order order = createOrder(orderItems);
                productRepository.saveAll(allProducts);
                order = orderRepository.save(order);
                mockMvc.perform(post("/orders/{orderId}/processOrder", order.getId())
                                .contentType("application/json"))
                                .andExpect(status().isOk());
                Order resultOrder = orderRepository.findById(order.getId()).get();
                for(Product p: resultOrder.getItems()) {
                    Integer tempHadSelled = productCanTBeSelled().stream().filter(or -> or.getId().equals(p.getId())).findFirst().get().getHadSelled();
                    assertEquals(p.getHadSelled(), tempHadSelled);
                }
        }

    @Test
    public void processOrderFlushSallProductShouldPass() throws Exception {
        List<Product> allProducts = productCanBeSelled();
        Set<Product> orderItems = new HashSet<Product>(allProducts);
        Order order = createOrder(orderItems);
        productRepository.saveAll(allProducts);
        order = orderRepository.save(order);
        mockMvc.perform(post("/orders/{orderId}/processOrder", order.getId())
                        .contentType("application/json"))
                .andExpect(status().isOk());
        Order resultOrder = orderRepository.findById(order.getId()).get();
        for(Product p: resultOrder.getItems()) {
            Integer tempHadSelled = productCanBeSelled().stream().filter(or -> or.getId().equals(p.getId())).findFirst().get().getHadSelled() + 1;
            assertEquals(p.getHadSelled(), tempHadSelled);
        }
    }

        private static Order createOrder(Set<Product> products) {
                Order order = new Order();
                order.setItems(products);
                return order;
        }

        private static List<Product> createProducts() {
                List<Product> products = new ArrayList<>();
                products.add(new Product(null, 15, 30, "NORMAL", "USB Cable", null, null, null, null, null, null, null));
                products.add(new Product(null, 10, 0, "NORMAL", "USB Dongle", null, null, null, null, null, null, null));
                products.add(new Product(null, 15, 30, "EXPIRABLE", "Butter", LocalDate.now().plusDays(26), null,
                                null, null, null, null, null));
                products.add(new Product(null, 90, 6, "EXPIRABLE", "Milk", LocalDate.now().minusDays(2), null, null, null, null, null, null));
                products.add(new Product(null, 15, 30, "SEASONAL", "Watermelon", null, LocalDate.now().minusDays(2),
                                LocalDate.now().plusDays(58), null, null, null, null));
                products.add(new Product(null, 15, 30, "SEASONAL", "Grapes", null, LocalDate.now().plusDays(180),
                                LocalDate.now().plusDays(240), null, null, null, null));
            return products;
        }

        private static List<Product> productCanTBeSelled(){
            List<Product> products = new ArrayList<>();

            products.add(new Product(1L, 15, 30, "FLASHSALE", "USB Cable", null, null, null, LocalDate.now().minusDays(2), 3, 6, 6));
            products.add(new Product(2L, 15, 30, "FLASHSALE", "USB Cable", null, null, null, LocalDate.now().minusDays(4), 3, 4, 6));

            return products;
        }

        private static List<Product> productCanBeSelled(){
        List<Product> products = new ArrayList<>();

        products.add(new Product(1L, 15, 30, "FLASHSALE", "USB Cable", null, null, null, LocalDate.now().minusDays(2), 3, 2, 6));
        products.add(new Product(2L, 15, 30, "FLASHSALE", "USB Cable", null, null, null, LocalDate.now().minusDays(1), 3, 4, 6));

        return products;
    }
}
