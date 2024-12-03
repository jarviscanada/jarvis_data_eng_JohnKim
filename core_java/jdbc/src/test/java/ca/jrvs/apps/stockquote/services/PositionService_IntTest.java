package ca.jrvs.apps.stockquote.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;

public class PositionService_IntTest {

    private Connection c;
    private PositionDao dao;

    @BeforeEach
    public void setUp() throws SQLException {
        c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/stock_quote", "postgres",
                "password");
        dao = new PositionDao(c);
        dao.deleteAll();
    }

    @Test
    public void testBuy() {
        PositionService service = new PositionService(dao);
        Position position = service.buy("AAPL", 10, 100);
        assertEquals("AAPL", position.getTicker());
        assertEquals(10, position.getNumOfShares());
        assertEquals(1000, position.getValuePaid());
    }

    @Test
    public void testSell() {
        PositionService service = new PositionService(dao);
        Position position = service.buy("AAPL", 10, 100);
        assertEquals("AAPL", position.getTicker());
        assertEquals(10, position.getNumOfShares());
        assertEquals(1000, position.getValuePaid());
        service.sell("AAPL");
        assertTrue(dao.findById("AAPL").isEmpty());
    }

}
