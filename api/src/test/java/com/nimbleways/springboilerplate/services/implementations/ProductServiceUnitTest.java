package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.services.ProductStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class ProductServiceUnitTest {

    @Mock
    private Map<String, ProductStrategy> strategies;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product(null, 15, 0, "NORMAL", "RJ45 Cable", null, null, null);
    }

    /*
     * Test that the processProduct method should delegate to the appropriate ProductStrategy based on the product type.
     */
    @Test
    public void processProduct_ShouldDelegateToNormalProductStrategy() {
        // Arrange
        ProductStrategy normalStrategy = Mockito.mock(ProductStrategy.class);
        Mockito.when(strategies.get("NORMAL")).thenReturn(normalStrategy);

        // Act
        productService.processProduct(product);

        // Assert
        verify(normalStrategy, Mockito.times(1)).process(product);
    }

    /*
     * Test that the processProduct method should delegate to the appropriate ProductStrategy based on the product type.
     */
    @Test
    public void processProduct_ShouldDelegateToSeasonalProductStrategy() {
        // Arrange
        product.setType("SEASONAL");
        ProductStrategy seasonalStrategy = Mockito.mock(ProductStrategy.class);
        Mockito.when(strategies.get("SEASONAL")).thenReturn(seasonalStrategy);

        // Act
        productService.processProduct(product);

        // Assert
        verify(seasonalStrategy, Mockito.times(1)).process(product);
    }

    @Test
    public void processProduct_ShouldDelegateToExpirableProductStrategy() {
        // Arrange
        product.setType("EXPIRABLE");
        ProductStrategy expirableStrategy = Mockito.mock(ProductStrategy.class);
        Mockito.when(strategies.get("EXPIRABLE")).thenReturn(expirableStrategy);

        // Act
        productService.processProduct(product);

        // Assert
        verify(expirableStrategy, Mockito.times(1)).process(product);
    }
}