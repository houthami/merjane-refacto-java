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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class ExpirableProductStrategyTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ExpirableProductStrategy expirableProductStrategy;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product(null, 15, 30, "EXPIRABLE", "Milk", LocalDate.now().plusDays(10), null, null);
    }

    /*
     * Test that the process method should decrease the availability when the product is not expired.
     */
    @Test
    public void process_ShouldDecreaseAvailabilityWhenProductIsNotExpired() {
        Mockito.when(productRepository.save(product)).thenReturn(product);

        expirableProductStrategy.process(product);

        assertEquals(29, product.getAvailable());
        verify(productRepository, Mockito.times(1)).save(product);
    }

    /*
     * Test that the process method should notify the expiration when the product is expired.
     */
    @Test
    public void process_ShouldNotifyExpirationWhenProductIsExpired() {
        product.setExpiryDate(LocalDate.now().minusDays(1));
        Mockito.when(productRepository.save(product)).thenReturn(product);

        expirableProductStrategy.process(product);

        verify(notificationService, Mockito.times(1)).sendExpirationNotification("Milk", product.getExpiryDate());
        verify(productRepository, Mockito.times(1)).save(product);
    }
}