import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class weatherAPICall {
    //private static final String baseURL = "https://api.openweathermap.org/data/3.0/onecall?lat=";
    //private static String apiKey = System.getenv("WEATHER_API_KEY");


    public static void main(String[] args) {
        //String[2] = [lat, lon]

        String[] location = getLocation();

        //System.out.println(location[0] + " " + location[1]);
        getWeather(location[0], location[1]);
    }

    public static void getWeather(String lat, String lon) {
        final String baseURL= "https://api.weatherapi.com/v1/current.json?key=";

        //http://api.weatherapi.com/v1/current.json?key=e0687018d39a48d1a25130036251704&q=46032&aqi=no
        String urlStr = String.format("%s%s&q=%s,%s&aqi=no", baseURL, weatherApiKey, lat, lon);

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

    public static String[] getLocation() {
        String[] location = new String[2];

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the your zip code: ");
        String zipCode = sc.next();

        //http://api.openweathermap.org/geo/1.0/zip?zip={zip code},{country code}&appid={API key}
        String urlStr = String.format("https://api.openweathermap.org/geo/1.0/zip?zip=%s,US&appid=%s", zipCode, geoApiKey);

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

                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(informationString.toString());
                location[0] = jsonObject.get("lat").toString();
                location[1] = jsonObject.get("lon").toString();

            } else {
                System.out.println("Response Code: " + responseCode);
                System.out.println("Error in Geolocation API call");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return location;
    }
}
