package ca.jrvs.apps.jdbc;

import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class QuoteService {
    private QuoteDao dao;
    private QuoteHttpHelper httpHelper;
    private final Logger logger = LoggerFactory.getLogger(QuoteHttpHelper.class);

    public QuoteService(QuoteDao dao, QuoteHttpHelper httpHelper) {
        this.dao = dao;
        this.httpHelper = httpHelper;
    }

    /**
     * Fetches latest quote data from endpoint
     * @param ticker
     * @return Latest quote information or empty optional if ticker symbol not found
     */
    public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
        Quote quote = httpHelper.fetchQuoteInfo(ticker);

        if (quote.getTicker() == null) {
            return Optional.empty();
        }
        dao.save(quote);
        logger.info("{} added to quote table", ticker); // trace log
        return dao.findById(ticker);
    }
}
