package ca.jrvs.apps.jdbc;

import java.util.Optional;
import java.util.Scanner;

public class StockQuoteController {
    private QuoteService quoteService;
    private PositionService positionService;

    public StockQuoteController(QuoteService quoteService, PositionService positionService) {
        this.quoteService = quoteService;
        this.positionService = positionService;
    }

    /**
     * User interface for our application
     */
    public void initClient() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Would you like to buy or sell a stock? (buy/sell)");
        String option = sc.nextLine();

        if (option.equals("buy")) {
            System.out.println("Write the symbol of the stock you want to buy");
            String ticker = sc.nextLine();
            System.out.println("How many shares are you purchasing?");
            int numOfShares = Integer.parseInt(sc.nextLine());
            Optional<Quote> quote = quoteService.fetchQuoteDataFromAPI(ticker);
            double price = quote.get().getPrice() * numOfShares;
            positionService.buy(ticker, numOfShares, price);
        } else if (option.equals("sell")) {
            System.out.println("Write the symbol of the stock you want to buy");
            String ticker = sc.nextLine();
            positionService.sell(ticker);
        } else {
            System.out.println("Invalid entry");
        }
    }
}