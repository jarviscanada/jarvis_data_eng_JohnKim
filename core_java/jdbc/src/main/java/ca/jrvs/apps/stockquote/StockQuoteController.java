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
                logger.warn("Invalid input. Please enter a number.");
                scanner.next(); // Consume the invalid input
                continue;
            }
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                // Get a quote
                logger.info("Enter the ticker symbol:");
                String ticker = scanner.nextLine();
                Optional<Quote> quoteOptional = quoteService.fetchQuoteDataFromAPI(ticker);
                if (quoteOptional.isPresent()) {
                    Quote quote = quoteOptional.get();
                    // logger.info("Quote: {}", quote);
                } else {
                    logger.warn("Invalid Ticker: {}", ticker);
                }
            } else if (choice == 2) {
                // Buy a stock
                logger.info("Enter the ticker symbol:");
                String ticker = scanner.nextLine();
                logger.info("Enter the number of shares:");
                if (!scanner.hasNextInt()) {
                    logger.warn("Invalid input. Please enter a number.");
                    scanner.next();
                    continue;
                }
                int numberOfShares = scanner.nextInt();
                logger.info("Enter the price per share:");
                if (!scanner.hasNextDouble()) {
                    logger.warn("Invalid input. Please enter a valid price.");
                    scanner.next();
                    continue;
                }
                double price = scanner.nextDouble();
                scanner.nextLine();
                try {
                    Position position = positionService.buy(ticker, numberOfShares, price);
                    if (position != null) {
                        logger.info("Bought position: {}", position);
                    } else {
                        logger.warn("Failed to buy position for ticker: {}", ticker);
                    }
                } catch (Exception e) {
                    logger.error("Error: {}", e.getMessage());
                }
            } else if (choice == 3) {
                // Sell a stock
                logger.info("Enter the ticker symbol:");
                String ticker = scanner.nextLine();
                positionService.sell(ticker);
                logger.info("Sold all shares of: {}", ticker);
            } else if (choice == 4) {
                // Exit
                logger.info("Exiting...");
                break;
            } else {
                logger.warn("Invalid choice");
            }
        }
        scanner.close();
    }
}