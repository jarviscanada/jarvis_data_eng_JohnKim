package ca.jrvs.apps.stockquote.dao;

import java.io.IOException;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.jrvs.apps.stockquote.util.PropertiesLoader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuoteHttpHelper {

    private static final Logger logger = LoggerFactory.getLogger(QuoteHttpHelper.class);

    private String apiKey;
    private OkHttpClient client;

    public QuoteHttpHelper(String apiKey) {
        Properties properties = PropertiesLoader.getProperties();
        this.apiKey = properties.getProperty("api-key");
        this.client = new OkHttpClient();
    }

    /**
     * Fetch latest quote data from Alpha Vantage endpoint
     * 
     * @param symbol
     * @return Quote with latest data
     * @throws IllegalArgumentException - if no data was found for the given symbol
     */
    public Quote getStockQuote(String ticker) {
        Quote quote = null;
        Request request = new Request.Builder()
                .url("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=" + ticker
                        + "&datatype=json")
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            logger.info("API Response: " + responseBody);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);

            if (root == null || root.get("Global Quote") == null) {
                throw new IllegalArgumentException("No data found for the given symbol");
            }
            // Create a Quote object from the JSON response
            String globalQuote = root.get("Global Quote").toString();
            quote = mapper.readValue(globalQuote, Quote.class);
            quote.setTimestamp();

        } catch (JsonMappingException e) {
            logger.error("JsonMappingException: ", e);
        } catch (JsonProcessingException e) {
            logger.error("JsonProcessingException: ", e);
        } catch (IOException e) {
            logger.error("IOException: ", e);
        }
        return quote;
    }

    public static void main(String[] args) {
        String ticker = "NVDA";
        Properties properties = PropertiesLoader.getProperties();
        String apiKey = properties.getProperty("api-key");
        QuoteHttpHelper helper = new QuoteHttpHelper(apiKey);
        try {
            Quote quote = helper.getStockQuote(ticker);
            logger.info("Quote: " + quote);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }
}