package com.csci212.finalproject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
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

    public static JSONObject getJSONSettings() {
        JSONParser parser = new JSONParser();
        if (Files.exists(Paths.get("src/main/resources/com/csci212/finalproject/settings.json"))) {
            try {
                return (JSONObject) parser.parse(new FileReader("src/main/resources/com/csci212/finalproject/settings.json"));
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Settings file not found...");
        return null;
    }

    protected void setApiKey(String type) {
        if (APIKey == null) {
            var props = new Properties();
            var envFile = Paths.get("src/main/resources/com/csci212/finalproject/config.env");
            if (Files.exists(envFile)) {
                System.out.printf("Setting %s Key %n", type);
                try {
                    var inputStream = Files.newInputStream(envFile);
                    props.load(inputStream);


                    APIKey = (String) props.get(type);  // load the API key from the file
                    if (APIKey == null || APIKey.isEmpty()) {
                        System.out.printf("%s Key not found in config.env %n", type);
                        throw new RuntimeException("key not found in config.env");
                    }
                } catch (IOException e) {
                    System.out.printf("Error reading config.env in %s %n", type);
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("config.env does not exist...");
            }
        }
    }
}
