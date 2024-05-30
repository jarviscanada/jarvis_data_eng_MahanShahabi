package ca.jrvs.apps.jdbc;

import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class QuoteService_IntTest {
    QuoteHttpHelper helperTest;
    QuoteService quoteService;
    QuoteDao quoteDao;
    OkHttpClient client;
    DatabaseConnectionManager dcm;
    Connection c;
    @BeforeEach
    void init() throws SQLException {
        dcm = new DatabaseConnectionManager("localhost", "stock_quote",
                "postgres", "password");
        c = dcm.getConnection();
        client = new OkHttpClient();
        quoteDao = new QuoteDao(c);
        helperTest = new QuoteHttpHelper("c477605b46msh6fe0f49c7c9e999p1c250bjsn5ec4019140a5", client);
        quoteService = new QuoteService(quoteDao, helperTest);
    }
    @Test
    void fetchQuoteDataFromAPI() {
        Quote serviceExpected = helperTest.fetchQuoteInfo("MSFT");
        assertNotNull(serviceExpected, "Expected quote should not be null");

        Optional<Quote> serviceActual = quoteService.fetchQuoteDataFromAPI("MSFT");
        assertTrue(serviceActual.isPresent(), "Actual quote should be present");

        assertEquals(Optional.of(serviceExpected).get().getTicker(), quoteDao.findById(serviceActual.get().getTicker()).get().getTicker());
        assertEquals(Optional.of(serviceExpected).get().getOpen(), quoteDao.findById(serviceActual.get().getTicker()).get().getOpen());
        assertEquals(Optional.of(serviceExpected).get().getHigh(), quoteDao.findById(serviceActual.get().getTicker()).get().getHigh());
        assertEquals(Optional.of(serviceExpected).get().getLow(), quoteDao.findById(serviceActual.get().getTicker()).get().getLow());
        assertEquals(Optional.of(serviceExpected).get().getPrice(), quoteDao.findById(serviceActual.get().getTicker()).get().getPrice());
        assertEquals(Optional.of(serviceExpected).get().getVolume(), quoteDao.findById(serviceActual.get().getTicker()).get().getVolume());
        assertEquals(Optional.of(serviceExpected).get().getLatestTradingDay(), quoteDao.findById(serviceActual.get().getTicker()).get().getLatestTradingDay());
        assertEquals(Optional.of(serviceExpected).get().getPreviousClose(), quoteDao.findById(serviceActual.get().getTicker()).get().getPreviousClose());
        assertEquals(Optional.of(serviceExpected).get().getChange(), quoteDao.findById(serviceActual.get().getTicker()).get().getChange());
        assertEquals(Optional.of(serviceExpected).get().getChangePercent(), quoteDao.findById(serviceActual.get().getTicker()).get().getChangePercent());

        Optional<Quote> serviceActual2 = quoteService.fetchQuoteDataFromAPI("gsdgss");

        assertEquals(Optional.empty(), serviceActual2);
    }
}