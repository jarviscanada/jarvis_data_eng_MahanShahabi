package ca.jrvs.apps.trading;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.sql.Timestamp;

public class MarketDataDao {

    private static final String RAPID_API_HOST = "alpha-vantage.p.rapidapi.com";
    private static final String RAPID_API_KEY = "df9ff107c1msh09c36f153034ef1p1b885djsnc02a719d3f61";  // Get API key from environment variable

    private static final Logger logger = LoggerFactory.getLogger(MarketDataDao.class);
    private OkHttpClient client = new OkHttpClient();  // Initialize OkHttp client

    public Quote fetchQuoteFromAPI(String ticker) throws IllegalArgumentException {
        // Create the request
        Request request = new Request.Builder()
                .url("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=" + ticker + "&datatype=json")
                .header("X-RapidAPI-Key", RAPID_API_KEY)
                .header("X-RapidAPI-Host", RAPID_API_HOST)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                String responseBody = response.body().string();
                // trace log
                logger.debug(responseBody);

                ObjectMapper m = new ObjectMapper();
                m.registerModule(new JavaTimeModule());
                
                GlobalQuote globalQuote = m.readValue(responseBody, GlobalQuote.class);
                Quote quote = globalQuote.getGlobalQuote();
                quote.setTimestamp(new Timestamp(System.currentTimeMillis()));
                return quote;
            } else {
                throw new IOException("Empty response body for ticker: " + ticker);
            }
        } catch (JsonMappingException e) {
            logger.error("JSON mapping error for ticker: " + ticker, e);
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            logger.error("JSON processing error for ticker: " + ticker, e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("I/O error when fetching quote for ticker: " + ticker, e);
            throw new RuntimeException(e);
        }
    }
}
