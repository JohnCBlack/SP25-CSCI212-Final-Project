package com.csci212.finalproject;

import org.json.simple.JSONObject;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class stockAPICall extends APICall{
    // stores the API key to be used in requests
    String stockTicker;
    float percentChange, currentPrice, previousClose, change, high, low, open;

    /*
    constructor to initialize the API key and current date and handle user input
    */
    public stockAPICall() {
        setApiKey("STOCK_API_KEY");  // loads the API key

        // testing through console (for GUI implementation remove this part)
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Enter the stock symbol (e.g., AAPL, MSFT): ");
//        String symbol = sc.next().toUpperCase();  // input for stock symbol



        // --------- GUI implementation --------
        JSONObject settingsStream = getJSONSettings();
        assert settingsStream != null;

        if (settingsStream.containsKey("stockTicker")) {
            this.stockTicker = settingsStream.get("stockTicker").toString();
        } else {
            System.out.println("Error: No stock ticker specified in settings file.");
        }
        // -------------------------------------

        getStockData(stockTicker);  // fetch stock data for the symbol
    }

    /*
    ts method fetches stock data from Finnhub API and calculates percentage change
    */
    public void getStockData(String symbol) {
        // constructing the Finnhub API URL with the stock symbol and API key
        String urlStr = String.format(
            "https://finnhub.io/api/v1/quote?symbol=%s&token=%s",
            symbol, APIKey
        );

        try {
            // creating a connection to the API URL
            URL url = new URI(urlStr).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // checking the response code from the API request
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                JSONObject dataStream = getDataStream(url);

                // parsing data from the response and calculating percentage change
                if (dataStream.containsKey("c") && dataStream.containsKey("pc")) {
                    setCurrentPrice(Float.parseFloat(dataStream.get("c").toString()));

                    // ------- changes for main page -------
                    setPreviousClose(Float.parseFloat(dataStream.get("pc").toString()));
                    setChange(Float.parseFloat(dataStream.get("d").toString()));
                    setHigh(Float.parseFloat(dataStream.get("h").toString()));
                    setLow(Float.parseFloat(dataStream.get("l").toString()));
                    setOpen(Float.parseFloat(dataStream.get("o").toString()));
                    // -------------------------------------

                    setPercentChange(Float.parseFloat(dataStream.get("dp").toString()));
                } else {
                    System.out.println("Error: Invalid stock symbol or no data available.");
                }
            } else {
                System.out.println("Response Code: " + responseCode);
                System.out.println("Error fetching stock data.");
            }

        } catch (Exception e) {
            // handling any exceptions that happen during the API call
            System.out.println("Error at stock API call...");
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    // Getters and Setters
    public void setPercentChange(float percentChange) {
        this.percentChange = percentChange;
    } public float getPercentChange() {
        return this.percentChange;
    }
    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    } public float getCurrentPrice() {
        return this.currentPrice;
    }

    // ------- changes for main page -------
    public void setPreviousClose(float previousClose) {
        this.previousClose = previousClose;
    } public float getPreviousClose() {
        return this.previousClose;
    }
    public void setChange(float change) {
        this.change = change;
    } public float getChange() {
        return this.change;
    }
    public void setHigh(float high) {
        this.high = high;
    } public float getHigh() {
        return this.high;
    }
    public void setLow(float low) {
        this.low = low;
    } public float getLow() {
        return this.low;
    }
    public void setOpen(float open) {
        this.open = open;
    } public float getOpen() {
        return this.open;
    }
    public void setStockTicker(String stockTicker) {
        this.stockTicker = stockTicker;
    } public String getStockTicker() {
        return this.stockTicker;
    }

    // -------------------------------------
}
