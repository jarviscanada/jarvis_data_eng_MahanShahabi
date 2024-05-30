package ca.jrvs.apps.jdbc;

import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class QuoteDaoTest {
    DatabaseConnectionManager dcm;
    QuoteHttpHelper helperTest;
    OkHttpClient client;
    @BeforeEach
    void init() {
        dcm = new DatabaseConnectionManager("localhost", "stock_quote",
                "postgres", "password");
        client = new OkHttpClient();
        helperTest = new QuoteHttpHelper("c477605b46msh6fe0f49c7c9e999p1c250bjsn5ec4019140a5", client);
    }
    @Test
    void save() {
        try {
            Connection connection = dcm.getConnection();
            QuoteDao quoteDao = new QuoteDao(connection);
            Quote expectedQuote = quoteDao.save(helperTest.fetchQuoteInfo("MSFT"));

            Optional<Quote> quote = quoteDao.findById("MSFT");

            assertEquals(expectedQuote.getTicker(), quote.get().getTicker());
            assertEquals(Math.round(expectedQuote.getOpen() * 100.0) / 100.0, quote.get().getOpen());
            assertEquals(Math.round(expectedQuote.getHigh() * 100.0) / 100.0, quote.get().getHigh());
            assertEquals(Math.round(expectedQuote.getLow() * 100.0) / 100.0, quote.get().getLow());
            assertEquals(Math.round(expectedQuote.getPrice() * 100.0) / 100.0, quote.get().getPrice());
            assertEquals(expectedQuote.getVolume(), quote.get().getVolume());
            assertEquals(expectedQuote.getLatestTradingDay(), quote.get().getLatestTradingDay());
            assertEquals(Math.round(expectedQuote.getPreviousClose() * 100.0) / 100.0, quote.get().getPreviousClose());
            assertEquals(Math.round(expectedQuote.getChange() * 100.0) / 100.0, quote.get().getChange());
            assertEquals(expectedQuote.getChangePercent(), quote.get().getChangePercent());
            assertEquals(expectedQuote.getTimestamp(), quote.get().getTimestamp());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void findById() {
        try {
            Connection connection = dcm.getConnection();
            QuoteDao quoteDao = new QuoteDao(connection);
            Optional<Quote> quote = quoteDao.findById("AAPL");

            Quote expectedQuote = helperTest.fetchQuoteInfo("AAPL");
            expectedQuote.setTimestamp(quote.get().getTimestamp());

            assertEquals(expectedQuote.getTicker(), quote.get().getTicker());
            assertEquals(Math.round(expectedQuote.getOpen() * 100.0) / 100.0, quote.get().getOpen());
            assertEquals(Math.round(expectedQuote.getHigh() * 100.0) / 100.0, quote.get().getHigh());
            assertEquals(Math.round(expectedQuote.getLow() * 100.0) / 100.0, quote.get().getLow());
            assertEquals(Math.round(expectedQuote.getPrice() * 100.0) / 100.0, quote.get().getPrice());
            assertEquals(expectedQuote.getVolume(), quote.get().getVolume());
            assertEquals(expectedQuote.getLatestTradingDay(), quote.get().getLatestTradingDay());
            assertEquals(Math.round(expectedQuote.getPreviousClose() * 100.0) / 100.0, quote.get().getPreviousClose());
            assertEquals(Math.round(expectedQuote.getChange() * 100.0) / 100.0, quote.get().getChange());
            assertEquals(expectedQuote.getChangePercent(), quote.get().getChangePercent());
            assertEquals(expectedQuote.getTimestamp(), quote.get().getTimestamp());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void findAll() {
        try {
            Connection connection = dcm.getConnection();
            QuoteDao quoteDao = new QuoteDao(connection);
            List<Quote> quotes = (List<Quote>) quoteDao.findAll();
            assertEquals(2, quotes.size());

            Quote expectedQuote1 = helperTest.fetchQuoteInfo("MSFT");
            Quote expectedQuote2 = helperTest.fetchQuoteInfo("AAPL");

            assertEquals(expectedQuote1.getTicker(), quotes.get(0).getTicker());
            assertEquals(Math.round(expectedQuote1.getOpen() * 100.0) / 100.0, quotes.get(0).getOpen());
            assertEquals(Math.round(expectedQuote1.getHigh() * 100.0) / 100.0, quotes.get(0).getHigh());
            assertEquals(Math.round(expectedQuote1.getLow() * 100.0) / 100.0, quotes.get(0).getLow());
            assertEquals(Math.round(expectedQuote1.getPrice() * 100.0) / 100.0, quotes.get(0).getPrice());
            assertEquals(expectedQuote1.getVolume(), quotes.get(0).getVolume());
            assertEquals(expectedQuote1.getLatestTradingDay(), quotes.get(0).getLatestTradingDay());
            assertEquals(Math.round(expectedQuote1.getPreviousClose() * 100.0) / 100.0, quotes.get(0).getPreviousClose());
            assertEquals(Math.round(expectedQuote1.getChange() * 100.0) / 100.0, quotes.get(0).getChange());
            assertEquals(expectedQuote1.getChangePercent(), quotes.get(0).getChangePercent());
            assertEquals(expectedQuote1.getTimestamp(), quotes.get(0).getTimestamp());

            assertEquals(Math.round(expectedQuote2.getOpen() * 100.0) / 100.0, quotes.get(1).getOpen());
            assertEquals(Math.round(expectedQuote2.getHigh() * 100.0) / 100.0, quotes.get(1).getHigh());
            assertEquals(Math.round(expectedQuote2.getLow() * 100.0) / 100.0, quotes.get(1).getLow());
            assertEquals(Math.round(expectedQuote2.getPrice() * 100.0) / 100.0, quotes.get(1).getPrice());
            assertEquals(expectedQuote2.getVolume(), quotes.get(1).getVolume());
            assertEquals(expectedQuote2.getLatestTradingDay(), quotes.get(1).getLatestTradingDay());
            assertEquals(Math.round(expectedQuote2.getPreviousClose() * 100.0) / 100.0, quotes.get(1).getPreviousClose());
            assertEquals(Math.round(expectedQuote2.getChange() * 100.0) / 100.0, quotes.get(1).getChange());
            assertEquals(expectedQuote2.getChangePercent(), quotes.get(1).getChangePercent());
            assertEquals(expectedQuote2.getTimestamp(), quotes.get(1).getTimestamp());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteById() {
        try {
            Connection connection = dcm.getConnection();
            QuoteDao quoteDao = new QuoteDao(connection);
            quoteDao.deleteById("AAPL");

            assertEquals(Optional.empty(), quoteDao.findById("AAPL"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteAll() {
        try {
            Connection connection = dcm.getConnection();
            QuoteDao quoteDao = new QuoteDao(connection);
            quoteDao.deleteAll();
            List<Quote> quotes = (List<Quote>) quoteDao.findAll();

            assertEquals(0, quotes.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}