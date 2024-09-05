package ca.jrvs.apps.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Scanner;

public class StockQuoteController {
    private QuoteService quoteService;
    private PositionService positionService;
    private final Logger logger = LoggerFactory.getLogger(QuoteHttpHelper.class);

    public StockQuoteController(QuoteService quoteService, PositionService positionService) {
        this.quoteService = quoteService;
        this.positionService = positionService;
    }

    /**
     * User interface for our application
     */
    public void initClient() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Select the number of what you would like to do?");
        System.out.println("1. buy");
        System.out.println("2. sell");
        System.out.println("3. view stock");
        String option = sc.nextLine();

        if (option.equals("1")) {
            logger.info("user inputted 'buy'"); // trace log
            System.out.println("Write the symbol of the stock you want to buy");
            String ticker = sc.nextLine();
            System.out.println("How many shares are you purchasing?");
            int numOfShares = Integer.parseInt(sc.nextLine());
            Optional<Quote> quote = quoteService.fetchQuoteDataFromAPI(ticker);
            double price = quote.get().getPrice() * numOfShares;
            positionService.buy(ticker, numOfShares, price);
        } else if (option.equals("2")) {
            logger.info("user inputted 'sell'"); // trace log
            System.out.println("Write the symbol of the stock you want to buy");
            String ticker = sc.nextLine();
            positionService.sell(ticker);
        } else if (option.equals("3")) {
            logger.info("user inputted 'view stock'"); // trace log
            System.out.println("Write the symbol of the stock you want to view");
            String ticker = sc.nextLine();
            Quote stock = quoteService.fetchQuoteDataFromAPI(ticker).get();
            System.out.println("Symbol: " + stock.getTicker());
            System.out.println("Open: " + stock.getOpen());
            System.out.println("High: " + stock.getHigh());
            System.out.println("Low: " + stock.getLow());
            System.out.println("Price: " + stock.getPrice());
            System.out.println("Volume: " + stock.getVolume());
            System.out.println("Latest Trading Day: " + stock.getLatestTradingDay());
            System.out.println("Previous Close: " + stock.getPreviousClose());
            System.out.println("Change: " + stock.getChange());
            System.out.println("Change Percent: " + stock.getChangePercent());
        } else {
            logger.error("user inputted invalid entry: {}", sc.nextLine()); // error log
            System.out.println("Invalid entry");
        }
    }
}
