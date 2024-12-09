package ca.jrvs.apps.stockquote;

import java.util.Optional;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.services.PositionService;
import ca.jrvs.apps.stockquote.services.QuoteService;

public class StockQuoteController {

    private static Logger logger = LoggerFactory.getLogger(StockQuoteController.class);

    private QuoteService quoteService;
    private PositionService positionService;

    public StockQuoteController(QuoteService quoteService, PositionService positionService) {
        this.quoteService = quoteService;
        this.positionService = positionService;
    }

    /**
     * User interface for the application
     */
    public void initClient() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            logger.info("Press 1 to get a quote, 2 to buy a stock, 3 to sell a stock, and 4 to exit");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Consume the invalid input
                continue;
            }
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                // Get a quote
                System.out.println("Enter the ticker symbol:");
                String ticker = scanner.nextLine();
                Optional<Quote> quoteOptional = quoteService.fetchQuoteDataFromAPI(ticker);
                if (quoteOptional.isPresent()) {
                    Quote quote = quoteOptional.get();
                    System.out.println(quote);
                } else {
                    System.out.println("Invalid Ticker: " + ticker);
                }
            } else if (choice == 2) {
                // Buy a stock
                System.out.println("Enter the ticker symbol:");
                String ticker = scanner.nextLine();
                System.out.println("Enter the number of shares:");
                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next();
                    continue;
                }
                int numberOfShares = scanner.nextInt();
                System.out.println("Enter the price per share:");
                if (!scanner.hasNextDouble()) {
                    System.out.println("Invalid input. Please enter a valid price.");
                    scanner.next();
                    continue;
                }
                double price = scanner.nextDouble();
                scanner.nextLine();
                try {
                    Position position = positionService.buy(ticker, numberOfShares, price);
                    System.out.println("Bought position: " + position);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            } else if (choice == 3) {
                // Sell a stock
                System.out.println("Enter the ticker symbol:");
                String ticker = scanner.nextLine();
                positionService.sell(ticker);
                System.out.println("Sold all shares of: " + ticker);
            } else if (choice == 4) {
                // Exit
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice");
            }
        }
        scanner.close();
    }
}