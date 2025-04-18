import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class stockAPICall {
    private static String stockApiKey;

    public stockAPICall() {
        setApiKey();

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the stock symbol (e.g., AAPL, MSFT): ");
        String symbol = sc.next().toUpperCase();

        getStockData(symbol);
    }

    public static void getStockData(String symbol) {
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
                JSONObject jsonObject = (JSONObject) parser.parse(responseBuilder.toString());

                // Print the raw JSON
                System.out.println(jsonObject.toJSONString());
            } else {
                System.out.println("Response Code: " + responseCode);
                System.out.println("Error fetching stock data.");
            }

        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
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
                System.out.println("Error reading config.env in WeatherAPICall");
                throw new RuntimeException(e);
            }
        }
    }
}
