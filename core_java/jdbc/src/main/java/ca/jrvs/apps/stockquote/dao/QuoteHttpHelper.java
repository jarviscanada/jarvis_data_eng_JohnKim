package ca.jrvs.apps.stockquote.dao;

import java.io.IOException;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuoteHttpHelper {

    private String apiKey;
    private OkHttpClient client;

    public QuoteHttpHelper() {
        this.apiKey = System.getenv("VANTAGE_API_KEY");
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
                .header("X-RapidAPI-Key", "9f33b7490bmsh607e6ec0a839621p1c6ab9jsn91d5f9c379ad")
                .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            // System.out.println(response.body().string());
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
        helper.getStockQuote(ticker.nextLine());
        ticker.close();
    }
}
