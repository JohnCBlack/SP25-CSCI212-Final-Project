package com.csci212.finalproject;/*
link to docs: https://finnhub.io/docs/api/quote
from docs + subscription info:
"60 API calls/minute" w/ free plan
"If your limit is exceeded, you will receive a response with status code 429."
"On top of all plan's limit, there is a 30 API calls/ second limit." (should never be reached)

i parse c (close) and o (open) for the percentage change calculation ^ rest of it is still displayed in console
finnhub’s quote endpoint returns example:
{
  "c": 150.25, Current price (close)
  "h": 152.10, High
  "l": 149.80, Low
  "o": 151.00, Open
  "pc": 149.90, Previous close
  "t": 1697059200 Timestamp
}

example of this codes output:

Setting API Key
Enter the stock symbol (e.g., AAPL, MSFT):
AAPL
Stock Data: {"c":199.74,"pc":193.16,"d":6.58,"t":1745352000,"h":201.59,"dp":3.4065,"l":195.97,"o":196.12}
Previous Close: 193.16
Current Price: 199.74
Percent Change: 3.4065034%
 */

import org.json.simple.JSONObject;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

/*
uses finnhub api to fetch realtime stock data and calculate percentage change
api key handling from config.env file
calculates percentage change using previous close price for better accuracy like google does
 */

public class stockAPICall extends APICall{
    // stores the API key to be used in requests
    float percentChange, currentPrice;

    /*
    constructor to initialize the API key and current date and handle user input
    */
    public stockAPICall() {
        setApiKey("STOCK_API_KEY");  // loads the API key

        // testing through console (for GUI implementation remove this part)
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the stock symbol (e.g., AAPL, MSFT): ");
        String symbol = sc.next().toUpperCase();  // input for stock symbol

        getStockData(symbol);  // fetch stock data for the symbol
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
}
