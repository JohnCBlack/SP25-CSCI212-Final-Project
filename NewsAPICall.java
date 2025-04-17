import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

public class NewsAPICall {
    private static String NewsAPIKey;



    private static void setNewsAPIKey() {
        if (NewsAPIKey == null) {
            System.out.println("Setting API Key");

            var properties = new Properties();
            var envFile = Paths.get("config.env");

            try{
                try(var inputStream = Files.newInputStream(envFile)){
                    properties.load(inputStream);
                }
                NewsAPIKey = (String) properties.get("NEWS_API_KEY");
            } catch (IOException e) {
                System.out.println("Error reading config.env int NewAPICall");
                throw new RuntimeException(e);
            }
        }
    }
}
