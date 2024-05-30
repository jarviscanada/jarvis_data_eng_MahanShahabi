package ca.jrvs.apps.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QuoteService_UnitTest {
    @Mock
    Quote mockQuote;
    @Mock
    QuoteDao quoteDao;
    @Mock
    QuoteHttpHelper httpHelper;
    @InjectMocks
    QuoteService quoteService;
    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        mockQuote = new Quote();
    }
    @Test
    void fetchQuoteDataFromAPI() {
        Timestamp fixedTimestamp = new Timestamp(System.currentTimeMillis());

        mockQuote.setTicker("MSFT");
        mockQuote.setOpen(429.6300);
        mockQuote.setHigh(430.8200);
        mockQuote.setLow(426.6000);
        mockQuote.setPrice(430.3200);
        mockQuote.setVolume(15718024);
        mockQuote.setLatestTradingDay(LocalDate.of(2024, 5, 28));
        mockQuote.setPreviousClose(430.1600);
        mockQuote.setChange(0.1600);
        mockQuote.setChangePercent("0.0372%");
        mockQuote.setTimestamp(fixedTimestamp);

        when(httpHelper.fetchQuoteInfo("MSFT")).thenReturn(mockQuote);
        when(quoteDao.findById("MSFT")).thenReturn(Optional.of(mockQuote));

        Optional<Quote> serviceActual = quoteService.fetchQuoteDataFromAPI("MSFT");
        assertTrue(serviceActual.isPresent(), "Actual quote should be present");
        serviceActual.get().setTimestamp(fixedTimestamp);

        assertEquals(mockQuote.getTicker(), serviceActual.get().getTicker());
        assertEquals(mockQuote.getOpen(), serviceActual.get().getOpen());
        assertEquals(mockQuote.getHigh(), serviceActual.get().getHigh());
        assertEquals(mockQuote.getLow(), serviceActual.get().getLow());
        assertEquals(mockQuote.getPrice(), serviceActual.get().getPrice());
        assertEquals(mockQuote.getVolume(), serviceActual.get().getVolume());
        assertEquals(mockQuote.getLatestTradingDay(), serviceActual.get().getLatestTradingDay());
        assertEquals(mockQuote.getPreviousClose(), serviceActual.get().getPreviousClose());
        assertEquals(mockQuote.getChange(), serviceActual.get().getChange());
        assertEquals(mockQuote.getChangePercent(), serviceActual.get().getChangePercent());
        assertEquals(mockQuote.getTimestamp(), serviceActual.get().getTimestamp());
    }
}