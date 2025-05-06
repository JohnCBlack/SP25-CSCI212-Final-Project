package com.csci212.finalproject;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class stockAPICall extends APICall{
    // stores the API key to be used in requests
//    String stockTicker;
//    float percentChange, currentPrice, previousClose, change, high, low, open;

    private List<StockData> stockDataList = new ArrayList<>();
    private String[] stockTickers;

    /*
    constructor to initialize the API key and current date and handle user input
    */
    public stockAPICall() {
        setApiKey("STOCK_API_KEY");  // loads the API key

        // --------- GUI implementation --------
        JSONObject settingsStream = getJSONSettings();
        assert settingsStream != null;

        if (settingsStream.containsKey("stockTicker")) {
            Object tickerObject = settingsStream.get("stockTicker");
            if (tickerObject instanceof JSONArray) {
                JSONArray tickerArray = (JSONArray) tickerObject;
                stockTickers = new String[tickerArray.size()];
                for (int i = 0; i < tickerArray.size(); i++) {
                    stockTickers[i] = (String) tickerArray.get(i);
                }
            }
        } else {
            logger.warning("Error: No stock ticker specified in settings file.");
            stockTickers = null;
        }
        // -------------------------------------


        if (stockTickers != null && stockTickers.length > 0) {
            for (String ticker : stockTickers) {
                StockData data = getStockData(ticker.trim());
                if (data != null) {
                    stockDataList.add(data);
                }
            }
        }
    }

    /*
    ts method fetches stock data from Finnhub API and calculates percentage change
    */
    public StockData getStockData(String symbol) {
        if (getStockTickers() != null) {
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
                        Float currentPrice = Float.parseFloat(dataStream.get("c").toString());

                        // ------- changes for main page -------
                        Float previousClose = Float.parseFloat(dataStream.get("pc").toString());
                        Float change = Float.parseFloat(dataStream.get("d").toString());
                        Float high = Float.parseFloat(dataStream.get("h").toString());
                        Float low = Float.parseFloat(dataStream.get("l").toString());
                        Float open = Float.parseFloat(dataStream.get("o").toString());
                        // -------------------------------------

                        Float percentChange = Float.parseFloat(dataStream.get("dp").toString());

                        return new StockData(symbol, currentPrice, previousClose, change, high, low, open, percentChange);
                    } else {
                        logger.severe("Error: Invalid stock symbol or no data available.");
                    }
                } else {
                    logger.severe("Error getting API Data %nResponse Code: " + responseCode);
                }

            } catch (Exception e) {
                // handling any exceptions that happen during the API call
                logger.severe("Error at stock API call: " + e.getMessage());
            }
        } else {
            logger.warning("Error: No stock ticker specified in settings file, API call aborted.");
        }
        return null;
    }

    public List<StockData> getAllStockData(){
        return stockDataList;
    }

    public void setStockTickers(String[] stockTickers) {
        this.stockTickers = stockTickers;
    }

    public String[] getStockTickers() {
        return stockTickers;
    }
}
