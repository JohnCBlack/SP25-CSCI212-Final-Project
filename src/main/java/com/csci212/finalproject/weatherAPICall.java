package com.csci212.finalproject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class weatherAPICall extends APICall{
    private static final String baseURL = "https://api.weatherapi.com/v1/forecast.json?key=";

    String zipCode, condition, icon;
    float currentTemp, maxTemp, minTemp, changeOfRain;

    public weatherAPICall() {
        // Set Api key
        setApiKey("WEATHER_API_KEY");

        // Set zipcode for user
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the your zip code: ");
        this.zipCode = sc.next();

        getWeather(zipCode);
    }

    public void getWeather(String zipcode) {
        String urlStr = String.format("%s%s&q=%s&days=1&aqi=no&alerts=no", baseURL, APIKey, zipcode);

        try {
            URL url = new URI(urlStr).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                JSONObject dataStream = getDataStream(url);

                JSONObject currentStream = (JSONObject) dataStream.get("current");
                JSONObject dayForecast = (JSONObject) ((JSONObject) ((JSONArray) ((JSONObject) dataStream.get("forecast")).get("forecastday")).getFirst()).get("day");

                // Current stream
                setCurrentTemp(Float.parseFloat(currentStream.get("temp_f").toString()));
                setIcon(((JSONObject) (currentStream.get("condition"))).get("icon").toString());
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

    public void setIcon(String icon) {
        this.icon = icon;
    } public String getIcon() {
        return this.icon;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    } public String getCondition() {
        return this.condition;
    }

}
