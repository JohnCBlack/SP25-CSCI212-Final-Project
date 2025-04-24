import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

public class APICall {
    protected String APIKey;

    public JSONObject getDataStream(URL url) {
        try {
            Scanner scanner = new Scanner(url.openStream());

            StringBuilder responseBuilder = new StringBuilder();

            while (scanner.hasNext()) {
                responseBuilder.append(scanner.nextLine());
            }

            scanner.close();  // closing the scanner after reading all data

            JSONParser parser = new JSONParser();

            return (JSONObject) parser.parse(responseBuilder.toString());
        }catch (IOException | ParseException e) {
            System.out.println("Error reading data stream from API");
            throw new RuntimeException(e);
        }
    }

    protected void setApiKey(String type) {
        if (APIKey == null) {
            System.out.printf("Setting %s Key %n", type);

            var props = new Properties();
            var envFile = Paths.get("config.env");
            try {
                try (var inputStream = Files.newInputStream(envFile)) {
                    props.load(inputStream);
                }

                APIKey = (String) props.get(type);  // load the API key from the file
                if (APIKey == null || APIKey.isEmpty()) {
                    System.out.printf("%s Key not found in config.env %n", type);
                    throw new RuntimeException("key not found in config.env");
                }
            } catch (IOException e) {
                System.out.printf("Error reading config.env in %s %n", type);
                throw new RuntimeException(e);
            }
        }
    }
}
