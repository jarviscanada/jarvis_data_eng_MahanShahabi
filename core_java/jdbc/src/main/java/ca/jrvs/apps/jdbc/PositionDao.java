package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.CrudDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PositionDao implements CrudDao<Position, String> {
    private Connection c;
    private static final String INSERT = "INSERT INTO position (symbol, number_of_shares, value_paid) " +
            "VALUES (?, ?, ?)";
    private static final String GET_ONE = "SELECT symbol, number_of_shares, value_paid " +
            "FROM position WHERE symbol = ?";
    private static final String GET_ALL = "SELECT symbol, number_of_shares, value_paid " +
            "FROM position";
    private static final String UPDATE = "UPDATE position SET number_of_shares = ?, " +
            "value_paid = ? WHERE symbol = ?";
    private static final String DELETE = "DELETE FROM position WHERE symbol = ?";
    private static final String DELETE_ALL = "DELETE FROM position";
    private final Logger logger = LoggerFactory.getLogger(QuoteHttpHelper.class);
    public PositionDao(Connection c) {
        this.c = c;
    }

    @Override
    public Position save(Position entity) throws IllegalArgumentException {
        if (entity == null) {
            // error log
            logger.error(String.valueOf(new IllegalArgumentException("ID can not be null")));
            throw new IllegalArgumentException("ID can not be null");
        }

        if (findById(entity.getTicker()).isPresent()) {
            try (PreparedStatement statement = this.c.prepareStatement(UPDATE);) {
                statement.setInt(1, entity.getNumOfShares());
                statement.setDouble(2, entity.getValuePaid());
                statement.setString(3, entity.getTicker());
                statement.execute();
                // trace log
                logger.info("stock updated in position table with {} shares at {} each", entity.getNumOfShares(), entity.getValuePaid());
            } catch (SQLException e) {
                e.printStackTrace();
                // error log
                logger.error(String.valueOf(new RuntimeException(e)));
                throw new RuntimeException(e);
            }
        } else {
            try (PreparedStatement statement = this.c.prepareStatement(INSERT);) {
                statement.setString(1, entity.getTicker());
                statement.setInt(2, entity.getNumOfShares());
                statement.setDouble(3, entity.getValuePaid());
                statement.execute();
                // trace log
                logger.info("stock inserted in position table with {} shares at {}", entity.getNumOfShares(), entity.getValuePaid());
            } catch (SQLException e) {
                e.printStackTrace();
                // error log
                logger.error(String.valueOf(new RuntimeException(e)));
                throw new RuntimeException(e);
            }
        }
        return entity;
    }

    @Override
    public Optional<Position> findById(String s) throws IllegalArgumentException {
        if (s == null) {
            // error log
            logger.error(String.valueOf(new IllegalArgumentException("ID can not be null")));
            throw new IllegalArgumentException("ID can not be null");
        }

        Position entity = new Position();
        try(PreparedStatement statement = this.c.prepareStatement(GET_ONE);) {
            statement.setString(1, s);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                entity.setTicker(rs.getString("symbol"));
                entity.setNumOfShares(rs.getInt("number_of_shares"));
                entity.setValuePaid(rs.getDouble("value_paid"));
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // error log
            logger.error(String.valueOf(new RuntimeException(e)));
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Position> findAll() {
        List<Position> entities = new ArrayList<>();
        try(PreparedStatement statement = this.c.prepareStatement(GET_ALL);) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Position entity = new Position();
                entity.setTicker(rs.getString("symbol"));
                entity.setNumOfShares(rs.getInt("number_of_shares"));
                entity.setValuePaid(rs.getDouble("value_paid"));
                entities.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // error log
            logger.error(String.valueOf(new RuntimeException(e)));
            throw new RuntimeException(e);
        }
        return entities;
    }

    @Override
    public void deleteById(String s) throws IllegalArgumentException {
        if (s == null) {
            // error log
            logger.error(String.valueOf(new IllegalArgumentException("ID can not be null")));
            throw new IllegalArgumentException("ID can not be null");
        }

        try(PreparedStatement statement = this.c.prepareStatement(DELETE);) {
            statement.setString(1, s);
            statement.execute();
            // trace log
            logger.info("stock sold from position table");
        } catch (SQLException e) {
            e.printStackTrace();
            // error log
            logger.error(String.valueOf(new RuntimeException(e)));
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        try(PreparedStatement statement = this.c.prepareStatement(DELETE_ALL);) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            // error log
            logger.error(String.valueOf(new RuntimeException(e)));
            throw new RuntimeException(e);
        }
    }
}
