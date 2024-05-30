package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.CrudDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuoteDao implements CrudDao<Quote, String> {

    private Connection c;
    private static final String INSERT = "INSERT INTO quote (symbol, open, high, " +
            "low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_ONE = "SELECT symbol, open, high, low, " +
            "price, volume, latest_trading_day, previous_close, change, change_percent, timestamp " +
            "FROM quote WHERE symbol = ?";
    private static final String GET_ALL = "SELECT symbol, open, high, low, " +
            "price, volume, latest_trading_day, previous_close, change, change_percent, timestamp " +
            "FROM quote";
    private static final String UPDATE = "UPDATE quote SET open = ?, high = ?, low = ?, price = ?, " +
            "volume = ?, latest_trading_day = ?, previous_close = ?, change = ?, change_percent = ?," +
            " timestamp = ? WHERE symbol = ?";
    private static final String DELETE = "DELETE FROM quote WHERE symbol = ?";
    private static final String DELETE_ALL = "DELETE FROM quote";
    public QuoteDao(Connection c) {
        this.c = c;
    }

    //implement all inherited methods
    //you are not limited to methods defined in CrudDao
    @Override
    public Quote save(Quote entity) throws IllegalArgumentException {
        if (entity == null) {
            throw new IllegalArgumentException("ID can not be null");
        }

        if (findById(entity.getTicker()).isPresent()) {
            try (PreparedStatement statement = this.c.prepareStatement(UPDATE);) {
                statement.setDouble(1, entity.getOpen());
                statement.setDouble(2, entity.getHigh());
                statement.setDouble(3, entity.getLow());
                statement.setDouble(4, entity.getPrice());
                statement.setInt(5, entity.getVolume());
                statement.setDate(6, Date.valueOf(entity.getLatestTradingDay()));
                statement.setDouble(7, entity.getPreviousClose());
                statement.setDouble(8, entity.getChange());
                statement.setString(9, entity.getChangePercent());
                statement.setTimestamp(10, entity.getTimestamp());
                statement.setString(11, entity.getTicker());
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else {
            try (PreparedStatement statement = this.c.prepareStatement(INSERT);) {
                statement.setString(1, entity.getTicker());
                statement.setDouble(2, entity.getOpen());
                statement.setDouble(3, entity.getHigh());
                statement.setDouble(4, entity.getLow());
                statement.setDouble(5, entity.getPrice());
                statement.setInt(6, entity.getVolume());
                statement.setDate(7, Date.valueOf(entity.getLatestTradingDay()));
                statement.setDouble(8, entity.getPreviousClose());
                statement.setDouble(9, entity.getChange());
                statement.setString(10, entity.getChangePercent());
                statement.setTimestamp(11, entity.getTimestamp());
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return entity;
    }

    @Override
    public Optional<Quote> findById(String s) throws IllegalArgumentException {
        if (s == null) {
            throw new IllegalArgumentException("ID can not be null");
        }

        Quote entity = new Quote();
        try(PreparedStatement statement = this.c.prepareStatement(GET_ONE);) {
            statement.setString(1, s);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                entity.setTicker(rs.getString("symbol"));
                entity.setOpen(rs.getDouble("open"));
                entity.setHigh(rs.getDouble("high"));
                entity.setLow(rs.getDouble("low"));
                entity.setPrice(rs.getDouble("price"));
                entity.setVolume(rs.getInt("volume"));
                entity.setLatestTradingDay(rs.getDate("latest_trading_day").toLocalDate());
                entity.setPreviousClose(rs.getDouble("previous_close"));
                entity.setChange(rs.getDouble("change"));
                entity.setChangePercent(rs.getString("change_percent"));
                entity.setTimestamp(rs.getTimestamp("timestamp"));
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Quote> findAll() {
        List<Quote> entities = new ArrayList<>();
        try(PreparedStatement statement = this.c.prepareStatement(GET_ALL);) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Quote entity = new Quote();
                entity.setTicker(rs.getString("symbol"));
                entity.setOpen(rs.getDouble("open"));
                entity.setHigh(rs.getDouble("high"));
                entity.setLow(rs.getDouble("low"));
                entity.setPrice(rs.getDouble("price"));
                entity.setVolume(rs.getInt("volume"));
                entity.setLatestTradingDay(rs.getDate("latest_trading_day").toLocalDate());
                entity.setPreviousClose(rs.getDouble("previous_close"));
                entity.setChange(rs.getDouble("change"));
                entity.setChangePercent(rs.getString("change_percent"));
                entity.setTimestamp(rs.getTimestamp("timestamp"));
                entities.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return entities;
    }

    @Override
    public void deleteById(String s) throws IllegalArgumentException {
        if (s == null) {
            throw new IllegalArgumentException("ID can not be null");
        }

        try(PreparedStatement statement = this.c.prepareStatement(DELETE);) {
            statement.setString(1, s);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        try(PreparedStatement statement = this.c.prepareStatement(DELETE_ALL);) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
