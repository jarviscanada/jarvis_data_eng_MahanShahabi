package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.GlobalQuote;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Timestamp;
import org.slf4j.Logger;

public class QuoteHttpHelper {

    private String apiKey;
    private OkHttpClient client;
    private final Logger logger = LoggerFactory.getLogger(QuoteHttpHelper.class);

    public QuoteHttpHelper(String apiKey, OkHttpClient client) {
        this.apiKey = apiKey;
        this.client = client;
    }

    /**
     * Fetch latest quote data from Alpha Vantage endpoint
     * @param symbol
     * @return Quote with latest data
     * @throws IllegalArgumentException - if no data was found for the given symbol
     */
    public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException {
        Request request = new Request.Builder()
                .url("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&datatype=json")
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                .build();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            // trace log
            logger.debug(responseBody);

            ObjectMapper m = new ObjectMapper();
            m.registerModule(new JavaTimeModule());
            GlobalQuote globalQuote = m.readValue(responseBody, GlobalQuote.class);
            Quote quote = globalQuote.getGlobalQuote();
            quote.setTimestamp(new Timestamp(System.currentTimeMillis()));
            return quote;
        } catch (JsonMappingException e) {
            e.printStackTrace();
            // error log
            logger.error(String.valueOf(new RuntimeException(e)));
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // error log
            logger.error(String.valueOf(new RuntimeException(e)));
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            // error log
            logger.error(String.valueOf(new RuntimeException(e)));
            throw new RuntimeException(e);
        }
    }
}
