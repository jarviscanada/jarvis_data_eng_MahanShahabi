package ca.jrvs.apps.jdbc;

import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;
class QuoteHttpHelperTest {

    @Spy
    Quote mockQuote;
    QuoteHttpHelper helperTest;
    OkHttpClient client;
    @BeforeEach
    void init() {
        mockQuote = new Quote();
        client = new OkHttpClient();
        helperTest = new QuoteHttpHelper("c477605b46msh6fe0f49c7c9e999p1c250bjsn5ec4019140a5", client);
    }
    @org.junit.jupiter.api.Test
    void fetchQuoteInfo() throws Exception {
        Timestamp fixedTimestamp = new Timestamp(System.currentTimeMillis());

        setPrivateField(mockQuote, "ticker", "MSFT");
        setPrivateField(mockQuote, "open", 429.6300);
        setPrivateField(mockQuote, "high", 430.8200);
        setPrivateField(mockQuote, "low", 426.6000);
        setPrivateField(mockQuote, "price", 430.3200);
        setPrivateField(mockQuote, "volume", 15718024);
        setPrivateField(mockQuote, "latestTradingDay", LocalDate.of(2024, 5, 28));
        setPrivateField(mockQuote, "previousClose", 430.1600);
        setPrivateField(mockQuote, "change", 0.1600);
        setPrivateField(mockQuote, "changePercent", "0.0372%");
        setPrivateField(mockQuote, "timestamp", fixedTimestamp);

        Quote quote = helperTest.fetchQuoteInfo("MSFT");
        quote.setTimestamp(fixedTimestamp);

        assertEquals(getPrivateField(mockQuote, "ticker"), quote.getTicker());
        assertEquals(getPrivateField(mockQuote, "open"), quote.getOpen());
        assertEquals(getPrivateField(mockQuote, "high"), quote.getHigh());
        assertEquals(getPrivateField(mockQuote, "low"), quote.getLow());
        assertEquals(getPrivateField(mockQuote, "price"), quote.getPrice());
        assertEquals(getPrivateField(mockQuote, "volume"), quote.getVolume());
        assertEquals(getPrivateField(mockQuote, "latestTradingDay"), quote.getLatestTradingDay());
        assertEquals(getPrivateField(mockQuote, "previousClose"), quote.getPreviousClose());
        assertEquals(getPrivateField(mockQuote, "change"), quote.getChange());
        assertEquals(getPrivateField(mockQuote, "changePercent"), quote.getChangePercent());
        assertEquals(getPrivateField(mockQuote, "timestamp"), quote.getTimestamp());
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
    private Object getPrivateField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }
}