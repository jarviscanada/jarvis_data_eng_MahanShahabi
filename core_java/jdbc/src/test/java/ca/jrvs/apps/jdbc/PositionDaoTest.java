package ca.jrvs.apps.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PositionDaoTest {
    DatabaseConnectionManager dcm;
    @BeforeEach
    void init() {
        dcm = new DatabaseConnectionManager("localhost", "stock_quote",
                "postgres", "password");
    }
    @Test
    void save() {
        try {
            Connection connection = dcm.getConnection();
            PositionDao positionDao = new PositionDao(connection);
            Position mockPosition = new Position();
            mockPosition.setTicker("AAPL");
            mockPosition.setNumOfShares(100);
            mockPosition.setValuePaid(200.90);
            positionDao.save(mockPosition);
            Optional<Position> position = positionDao.findById("AAPL");

            assertEquals("AAPL", position.get().getTicker());
            assertEquals(100, position.get().getNumOfShares());
            assertEquals(200.90, position.get().getValuePaid());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void findById() {
        try {
            Connection connection = dcm.getConnection();
            PositionDao positionDao = new PositionDao(connection);
            Optional<Position> position = positionDao.findById("AAPL");

            assertEquals("AAPL", position.get().getTicker());
            assertEquals(100, position.get().getNumOfShares());
            assertEquals(200.90, position.get().getValuePaid());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void findAll() {
        try {
            Connection connection = dcm.getConnection();
            PositionDao positionDao = new PositionDao(connection);
            List<Position> positions = (List<Position>) positionDao.findAll();
            assertEquals(1, positions.size());

            assertEquals("AAPL", positions.get(0).getTicker());
            assertEquals(100, positions.get(0).getNumOfShares());
            assertEquals(200.90, positions.get(0).getValuePaid());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteById() {
        try {
            Connection connection = dcm.getConnection();
            PositionDao positionDao = new PositionDao(connection);
            positionDao.deleteById("AAPL");

            assertEquals(Optional.empty(), positionDao.findById("AAPL"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteAll() {
        try {
            Connection connection = dcm.getConnection();
            PositionDao positionDao = new PositionDao(connection);
            positionDao.deleteAll();
            List<Position> positions = (List<Position>) positionDao.findAll();

            assertEquals(0, positions.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}