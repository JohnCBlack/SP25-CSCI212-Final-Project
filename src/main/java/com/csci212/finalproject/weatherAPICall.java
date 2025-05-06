package com.csci212.finalproject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class weatherAPICall extends APICall{
    private static final String baseURL = "https://api.weatherapi.com/v1/forecast.json?key=";

    String zipCode, condition, icon;
    float currentTemp, maxTemp, minTemp, changeOfRain;

    public weatherAPICall() {
        // Set Api key
        setApiKey("WEATHER_API_KEY");

        JSONObject settingsStream = getJSONSettings();
        assert settingsStream != null;

        if (settingsStream.containsKey("zipCode")) {
            setZipCode(settingsStream.get("zipCode").toString());
        } else {
            logger.warning("Error: No zip code specified in settings file.");
            setZipCode(null);
        }

        getWeather(getZipCode());
    }

    public void getWeather(String zipcode) {
        if (getZipCode() != null) {
            String urlStr = String.format("%s%s&q=%s&days=1&aqi=no&alerts=no", baseURL, APIKey, zipcode);

            try {
                URL url = new URI(urlStr).toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();

                if (responseCode == 200) {
                    JSONObject dataStream = getDataStream(url);

                    // Current stream
                    JSONObject currentStream = (JSONObject) dataStream.get("current");
                    setCurrentTemp(Float.parseFloat(currentStream.get("temp_f").toString()));
                    setIcon(((JSONObject) (currentStream.get("condition"))).get("icon").toString());
                    setCondition(((JSONObject) (currentStream.get("condition"))).get("text").toString());

                    //dayForecast
                    JSONObject dayForecast = (JSONObject) ((JSONObject) ((JSONArray) ((JSONObject) dataStream.get("forecast")).get("forecastday")).getFirst()).get("day");
                    setMaxTemp(Float.parseFloat(dayForecast.get("maxtemp_f").toString()));
                    setMinTemp(Float.parseFloat(dayForecast.get("mintemp_f").toString()));
                    setChangeOfRain(Float.parseFloat(dayForecast.get("daily_chance_of_rain").toString()));
                } else {
                    logger.severe("Error getting API Data %nResponse Code: " + responseCode);
                }
            } catch (Exception e) {
                logger.severe("Error at weather API call: " + e.getMessage());
            }
        } else {
            logger.warning("Error: No zip code specified in settings file, API call aborted.");
        }
    }

    /* Getters and Setters */

    // Current temperature getters and setters
    public void setCurrentTemp(float currentTemp) {
        this.currentTemp = currentTemp;
    }

    public float getCurrentTemp() {
        return this.currentTemp;
    }

    // Maximum temperature getters and setters
    public void setMaxTemp(float maxTemp) {
        this.maxTemp = maxTemp;
    }

    public float getMaxTemp() {
        return this.maxTemp;
    }

    // Minimum temperature getters and setters
    public void setMinTemp(float minTemp) {
        this.minTemp = minTemp;
    }

    public float getMinTemp() {
        return this.minTemp;
    }

    // Zip code getters and setters
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    // Chance of rain getters and setters
    public void setChangeOfRain(float changeOfRain) {
        this.changeOfRain = changeOfRain;
    }

    public float getChangeOfRain() {
        return this.changeOfRain;
    }

    // Weather icon getters and setters
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }

    // Weather condition getters and setters
    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return this.condition;
    }

}
