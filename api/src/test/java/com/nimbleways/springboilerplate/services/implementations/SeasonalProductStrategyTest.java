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
public class SeasonalProductStrategyTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private SeasonalProductStrategy seasonalProductStrategy;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product(null, 15, 30, "SEASONAL", "Watermelon", null, LocalDate.now().minusDays(2), LocalDate.now().plusDays(58));
    }

    @Test
    public void process_ShouldDecreaseAvailabilityWhenProductIsInSeasonAndInStock() {
        // Arrange
        Mockito.when(productRepository.save(product)).thenReturn(product);

        // Act
        seasonalProductStrategy.process(product);

        // Assert
        assertEquals(29, product.getAvailable());
        verify(productRepository, Mockito.times(1)).save(product);
    }

    @Test
    public void process_ShouldNotifyOutOfStockWhenProductIsOutOfSeason() {
        // Arrange
        product.setSeasonStartDate(LocalDate.now().plusDays(10)); // Product is out of season
        Mockito.when(productRepository.save(product)).thenReturn(product);

        // Act
        seasonalProductStrategy.process(product);

        // Assert
        verify(notificationService, Mockito.times(1)).sendOutOfStockNotification("Watermelon");
        verify(productRepository, Mockito.times(1)).save(product);
    }
}