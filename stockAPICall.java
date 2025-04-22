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
* TODO: The math for the percentage gained/lost will need to then be calculated manually
*  currentDate might be better as a static var
* */

public class stockAPICall {
    private static String stockApiKey;

    String currentDate;
    double openPrice, closePrice;

    public stockAPICall() {
        setApiKey();

        this.currentDate = setDate();

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the stock symbol (e.g., AAPL, MSFT): ");
        String symbol = sc.next().toUpperCase();

        getStockData(symbol);
    }

    public void getStockData(String symbol) {
        String urlStr = String.format(
            "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s",
            symbol, stockApiKey
        );

        try {
            URL url = new URI(urlStr).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {

                Scanner scanner = new Scanner(url.openStream());
                StringBuilder responseBuilder = new StringBuilder();

                while (scanner.hasNext()) {
                    responseBuilder.append(scanner.nextLine());
                }

                scanner.close();

                JSONParser parser = new JSONParser();

                JSONObject dataStream = (JSONObject) parser.parse(responseBuilder.toString());

                System.out.println("Stock Data: " + dataStream);

                JSONObject calledData = (JSONObject) (dataStream).get("Time Series (Daily)"); //.get(this.currentDate);

                if (calledData.containsKey(getDate())) {
                    System.out.println("today");
                    calledData = (JSONObject) (calledData.get(getDate()));
                } else {
                    System.out.println("yesterday all my problems seemed so far away");
                    calledData = (JSONObject) dataStream.get(getYesterday());
                }

                System.out.println(calledData);

                // Print the raw JSON
                float percentChange = findChangePercent(
                        Float.parseFloat(calledData.get("4. close").toString()),
                        Float.parseFloat(calledData.get("1. open").toString())
                );

                System.out.println(percentChange);
            } else {
                System.out.println("Response Code: " + responseCode);
                System.out.println("Error fetching stock data.");
            }

        } catch (Exception e) {
            System.out.println("Error at stock API call...");
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    private static float findChangePercent(float close, float open) {
        return ((close-open)/open) * 100;
    }

    private static String setDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(Calendar.getInstance().getTime());
    } private static String getYesterday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return sdf.format(cal.getTime());
    }

    private String getDate() {
        return this.currentDate;
    }

    private static void setApiKey() {
        if (stockApiKey == null) {
            System.out.println("Setting API Key");

            var props = new Properties();
            var envFile = Paths.get("config.env");
            try {
                try (var inputStream = Files.newInputStream(envFile)) {
                    props.load(inputStream);
                }

                stockApiKey = (String) props.get("STOCK_API_KEY");
            } catch (IOException e) {
                System.out.println("Error reading config.env in stockAPICall");
                throw new RuntimeException(e);
            }
        }
    }
}
