package ca.jrvs.apps.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PositionService_IntTest {
    DatabaseConnectionManager dcm;
    Connection connection;
    PositionDao dao;
    PositionService positionService;
    @BeforeEach
    void init() throws SQLException {
        dcm = new DatabaseConnectionManager("localhost", "stock_quote",
                  "postgres", "password");
        connection = dcm.getConnection();
        dao = new PositionDao(connection);
        positionService = new PositionService(dao);
    }

    @Test
    void buy() {
        positionService.buy("AAPL", 100, 200);
        assertEquals("AAPL", dao.findById("AAPL").get().getTicker());
        assertEquals(100, dao.findById("AAPL").get().getNumOfShares());
        assertEquals(200, dao.findById("AAPL").get().getValuePaid());
    }

    @Test
    void sell() {
        positionService.sell("AAPL");
        assertEquals(Optional.empty(), dao.findById("AAPL"));
    }
}