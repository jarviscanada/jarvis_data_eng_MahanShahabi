package ca.jrvs.apps.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public class PositionService {
    private PositionDao dao;
    private final Logger logger = LoggerFactory.getLogger(QuoteHttpHelper.class);
    public PositionService(PositionDao dao) {
        this.dao = dao;
    }

    /**
     * Processes a buy order and updates the database accordingly
     * @param ticker
     * @param numberOfShares
     * @param price
     * @return The position in our database after processing the buy
     */
    public Position buy(String ticker, int numberOfShares, double price) {
        Position position = new Position();
        position.setTicker(ticker);
        position.setNumOfShares(numberOfShares);
        position.setValuePaid(price);
        dao.save(position);
        // debug table
        logger.debug(dao.findById(ticker).get().getTicker());
        return dao.findById(ticker).get();
    }

    /**
     * Sells all shares of the given ticker symbol
     * @param ticker
     */
    public void sell(String ticker) {
        dao.deleteById(ticker);
        logger.debug(ticker);
    }
}
