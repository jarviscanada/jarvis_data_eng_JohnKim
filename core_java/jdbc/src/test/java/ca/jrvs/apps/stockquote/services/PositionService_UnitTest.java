package ca.jrvs.apps.stockquote.services;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PositionService_UnitTest {

    private PositionService positionService;
    private PositionDao mockPositionDao;

    @BeforeEach
    public void setUp() {
        mockPositionDao = mock(PositionDao.class);
        positionService = new PositionService(mockPositionDao);
    }

    @Test
    public void testBuy() {
        // Create a Position object to return from the mock PositionDao
        Position mockPosition = new Position();
        mockPosition.setTicker("AAPL");
        mockPosition.setNumOfShares(10);
        mockPosition.setValuePaid(1000.0);

        // Set up the mock behavior
        when(mockPositionDao.save(any(Position.class))).thenReturn(mockPosition);

        // Call the method to test
        Position result = positionService.buy("AAPL", 10, 100.0);

        // Verify the results
        assertEquals(mockPosition, result);
    }

    @Test
    public void testSell() {
        // Call the method to test
        positionService.sell("AAPL");

        // Verify interactions
        verify(mockPositionDao).deleteById("AAPL");
    }
}