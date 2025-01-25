package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class NormalProductStrategyTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NormalProductStrategy normalProductStrategy;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product(null, 15, 0, "NORMAL", "RJ45 Cable", null, null, null);
    }

    /**
     * Test that the process method should notify the delay when the product is out of stock.
     */
    @Test
    public void process_ShouldNotifyDelayWhenProductIsOutOfStock() {
        // Arrange
        Mockito.when(productRepository.save(product)).thenReturn(product);

        // Act
        normalProductStrategy.process(product);

        // Assert
        assertEquals(15, product.getLeadTime());
        verify(productRepository, Mockito.times(1)).save(product);
        verify(notificationService, Mockito.times(1)).sendDelayNotification(15, "RJ45 Cable");
    }

    /**
     * Test that the process method should decrease the availability when the product is in stock.
     */
    @Test
    public void process_ShouldDecreaseAvailabilityWhenProductIsInStock() {
        // Arrange
        product.setAvailable(10);
        Mockito.when(productRepository.save(product)).thenReturn(product);

        // Act
        normalProductStrategy.process(product);

        // Assert
        assertEquals(9, product.getAvailable());
        verify(productRepository, Mockito.times(1)).save(product);
    }
}