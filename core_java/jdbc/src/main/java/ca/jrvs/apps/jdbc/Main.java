package ca.jrvs.apps.jdbc;

import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(QuoteHttpHelper.class);
        Map<String, String> properties = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/properties.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(":");
                properties.put(tokens[0], tokens[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // error log
            logger.error(String.valueOf(new RuntimeException(e)));
        } catch (IOException e) {
            e.printStackTrace();
            // error log
            logger.error(String.valueOf(new IOException(e)));
        }

        try {
            Class.forName(properties.get("db-class"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // error log
            logger.error(String.valueOf(new RuntimeException(e)));
        }
        OkHttpClient client = new OkHttpClient();
        String url = "jdbc:postgresql://"+properties.get("server")+":"+properties.get("port")+"/"+properties.get("database");
        try (Connection c = DriverManager.getConnection(url, properties.get("username"), properties.get("password"))) {
            logger.info("connection established"); // trace log
            QuoteDao qRepo = new QuoteDao(c);
            logger.info("QuoteDao object created"); // trace log
            PositionDao pRepo = new PositionDao(c);
            logger.info("PositionDao object created"); // trace log
            QuoteHttpHelper rcon = new QuoteHttpHelper(properties.get("api-key"), client);
            logger.info("QuoteHttpHelper initialized with api-key: {}", properties.get("api-key")); // trace log
            QuoteService sQuote = new QuoteService(qRepo, rcon);
            logger.info("QuoteService object initialized with previously created QuoteDao and QuoteHttpHelper object"); // trace log
            PositionService sPos = new PositionService(pRepo);
            logger.info("PositionService object initialized with previously created PositionDao object"); // trace log

            StockQuoteController con = new StockQuoteController(sQuote, sPos);
            logger.info("StockQuoteController object initialized with previously created QuoteService and PositionService objects"); // trace log
            logger.info("StockQuoteController's initClient() method called"); // trace log
            con.initClient();
        } catch (SQLException e) {
            e.printStackTrace();
            // error log
            logger.error(String.valueOf(new SQLException(e)));
        }
    }
}
