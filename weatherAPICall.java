import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

public class weatherAPICall {
    private static String weatherApiKey;

    String zipCode;
    float currentTemp;

    public weatherAPICall() {
        /* Format

        */

        // Set Api key
        if (weatherApiKey == null) {
            System.out.println("Setting API Key");

            var props = new Properties();
            var envFile = Paths.get("config.env");
            try {
                try (var inputStream = Files.newInputStream(envFile)) {
                    props.load(inputStream);
                }
                weatherApiKey = (String) props.get("WEATHER_API_KEY");
            } catch (IOException e) {
                System.out.println("Error reading config.env in WeatherAPICall");
                throw new RuntimeException(e);
            }
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the your zip code: ");
        this.zipCode = sc.next();

        getWeather(zipCode);
    }

    public static void getWeather(String zipcode) {
        final String baseURL= "https://api.weatherapi.com/v1/current.json?key=";

        //http://api.weatherapi.com/v1/current.json?key=e0687018d39a48d1a25130036251704&q=46032&aqi=no
        String urlStr = String.format("%s%s&q=%s&aqi=no", baseURL, weatherApiKey, zipcode);

        try {
            URL url = new URI(urlStr).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                Scanner scanner = new Scanner(url.openStream());
                StringBuilder informationString = new StringBuilder();

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }

                scanner.close();

                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(informationString.toString());
                System.out.println(jsonObject.toJSONString());
            } else {
                System.out.println("Response Code: " + responseCode);
                System.out.println("Error in Weather API call");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
