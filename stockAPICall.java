/*
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
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/*
uses finnhub api to fetch realtime stock data and calculate percentage change
api key handling from config.env file
calculates percentage change using previous close price for better accuracy like google does
 */

public class stockAPICall {
    private static String stockApiKey;  // stores the API key to be used in requests
    private static String currentDate;  // stores the current date (static for all instances)
    double openPrice, closePrice, previousClose;  // stores stock data like open, close, and previous close prices

    /*
    constructor to initialize the API key and current date, and handle user input
    */
    public stockAPICall() {
        setApiKey();  // loads the API key
        currentDate = setDate();  // sets the current date as static

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
            symbol, stockApiKey
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
                // reading the response data
                Scanner scanner = new Scanner(url.openStream());
                StringBuilder responseBuilder = new StringBuilder();

                while (scanner.hasNext()) {
                    responseBuilder.append(scanner.nextLine());
                }

                scanner.close();  // closing the scanner after reading all data

                // parsing the JSON response from the api
                JSONParser parser = new JSONParser();
                JSONObject data = (JSONObject) parser.parse(responseBuilder.toString());

                System.out.println("Stock Data: " + data);

                // parsing data from the response and calculating percentage change
                if (data.containsKey("c") && data.containsKey("pc")) {
                    closePrice = Double.parseDouble(data.get("c").toString());  // current price
                    previousClose = Double.parseDouble(data.get("pc").toString());  // previous close price

                    // calculate percent change using previous close price
                    float percentChange = findChangePercent((float) closePrice, (float) previousClose);

                    // print the stock data for now, will use variables for gui i assume
                    System.out.println("Previous Close: " + previousClose);
                    System.out.println("Current Price: " + closePrice);
                    System.out.println("Percent Change: " + percentChange + "%");
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

    /*
    method to calculate the percentage change between current price and previous close price
    formula used: ((current price - previous close) / previous close) * 100
    */
    private static float findChangePercent(float current, float previousClose) {
        return ((current - previousClose) / previousClose) * 100;  // calculates percentage change
    }


    // ts method returns the current date in yyyy-MM-dd format
    private static String setDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(Calendar.getInstance().getTime());
    }


   // ts method loads the API key from the config.env file
    private static void setApiKey() {
        if (stockApiKey == null) {
            System.out.println("Setting API Key");

            var props = new Properties();
            var envFile = Paths.get("config.env");
            try {
                try (var inputStream = Files.newInputStream(envFile)) {
                    props.load(inputStream);
                }

                stockApiKey = (String) props.get("STOCK_API_KEY");  // load the API key from the file
                if (stockApiKey == null || stockApiKey.isEmpty()) {
                    throw new RuntimeException("STOCK_API_KEY not found in config.env");
                }
            } catch (IOException e) {
                System.out.println("Error reading config.env in stockAPICall");
                throw new RuntimeException(e);
            }
        }
    }

    // main method to run the stockAPICall class (testing with console input
    public static void main(String[] args) {
        new stockAPICall();
    }
}
