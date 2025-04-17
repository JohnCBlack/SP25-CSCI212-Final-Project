import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class weatherAPICall {
    private static final String weatherApiKey = "";

    public static void main(String[] args) {
        /* Format
        String[2] = [lat, lon]
        */

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the your zip code: ");
        String zipCode = sc.next();

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
