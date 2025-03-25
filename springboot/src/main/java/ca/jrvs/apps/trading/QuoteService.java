package ca.jrvs.apps.trading;

public class QuoteService {

    private final MarketDataDao marketDataDao;

    // Constructor injection of MarketDataDao
    public QuoteService(MarketDataDao marketDataDao) {
        this.marketDataDao = marketDataDao;
    }

    // Fetch quote by ticker symbol
    public Quote getQuoteByTicker(String ticker) throws IllegalArgumentException {
        if (ticker == null || ticker.trim().isEmpty()) {
            throw new IllegalArgumentException("Ticker symbol cannot be null or empty");
        }

        // Fetch quote from MarketDataDao
        return marketDataDao.fetchQuoteFromAPI(ticker.trim().toUpperCase());
    }
}

