package ca.jrvs.apps.stockquote.dao;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.jrvs.apps.stockquote.util.PropertiesLoader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuoteHttpHelper {

    private String apiKey;
    private OkHttpClient client;

    public QuoteHttpHelper() {
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
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body().string());

            if (root == null || root.get("Global Quote") == null) {
                throw new IllegalArgumentException("No data found for the given symbol");
            }
            // Create a Quote object from the JSON response
            String globalQuote = root.get("Global Quote").toString();
            quote = mapper.readValue(globalQuote, Quote.class);
            quote.setTimestamp();

        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return quote;
    }

    public static void main(String[] args) {
        System.out.println("Please enter a stock ticker symbol: ");
        Scanner ticker = new Scanner(System.in);
        QuoteHttpHelper helper = new QuoteHttpHelper();
        Quote quote = helper.getStockQuote(ticker.nextLine());
        System.out.println(quote);
        ticker.close();
    }
}