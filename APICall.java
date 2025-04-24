import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class APICall {
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
}
