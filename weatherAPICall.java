import org.json.simple.JSONArray;
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
    private static final String baseURL = "https://api.weatherapi.com/v1/forecast.json?key=";

    String zipCode, condition;
    float currentTemp, maxTemp, minTemp, changeOfRain;

    public weatherAPICall() {
        // Set Api key
        setApiKey();

        // Set zipcode for user
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the your zip code: ");
        this.zipCode = sc.next();

        getWeather(zipCode);
    }

    public void getWeather(String zipcode) {
        String urlStr = String.format("%s%s&q=%s&days=1&aqi=no&alerts=no", baseURL, weatherApiKey, zipcode);

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
                JSONObject inputStream = (JSONObject) parser.parse(informationString.toString());
                JSONObject currentStream = (JSONObject) inputStream.get("current");
                JSONObject dayForecast = (JSONObject) ((JSONObject) ((JSONArray) ((JSONObject) inputStream.get("forecast")).get("forecastday")).getFirst()).get("day");

                // Current stream
                setCurrentTemp(Float.parseFloat(currentStream.get("temp_f").toString()));
                setCondition(((JSONObject) (currentStream.get("condition"))).get("text").toString());

                //dayForecast
                setMaxTemp(Float.parseFloat(dayForecast.get("maxtemp_f").toString()));
                setMinTemp(Float.parseFloat(dayForecast.get("mintemp_f").toString()));
                setChangeOfRain(Float.parseFloat(dayForecast.get("daily_chance_of_rain").toString()));


            } else {
                System.out.println("Response Code: " + responseCode);
                System.out.println("Error in Weather API call");

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    private static void setApiKey() {
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
    }

    // Getters and Setters
    public void setCurrentTemp(float currentTemp) {
        this.currentTemp = currentTemp;
    } public float getCurrentTemp() {
        return this.currentTemp;
    }
    public void setMaxTemp(float maxTemp) {
        this.maxTemp = maxTemp;
    } public float getMaxTemp() {
        return this.maxTemp;
    }
    public void setMinTemp(float minTemp) {
        this.minTemp = minTemp;
    } public float getMinTemp() {
        return this.minTemp;
    }

    public void setChangeOfRain(float changeOfRain) {
        this.changeOfRain = changeOfRain;
    } public float getChangeOfRain() {
        return this.changeOfRain;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    } public String getCondition() {
        return this.condition;
    }

}
