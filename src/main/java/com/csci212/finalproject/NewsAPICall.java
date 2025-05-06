package com.csci212.finalproject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;


public class NewsAPICall extends APICall{
    private String category,country;
    public ArrayList<ArrayList<String>> articlesList;

    public NewsAPICall() {
        setApiKey("NEWS_API_KEY");

        JSONObject settingsStream = getJSONSettings();
        assert settingsStream != null;

        if (settingsStream.containsKey("newsCategory")) {
            setCategory(settingsStream.get("newsCategory").toString());
        } else {
            logger.warning("Error: No category specified in settings file.");
            setCategory(null);
        }

        if (settingsStream.containsKey("newsCountry")) {
            setCountry(settingsStream.get("newsCountry").toString());
        } else {
            logger.warning("Error: No country specified in settings file.");
            setCategory(null);
        }
    }

    public void getNewsHeadline() {
        if (getCategory() != null && getCountry() != null) {
            String urlStr = String.format("https://newsapi.org/v2/top-headlines?%scountry=%s&apiKey=%s",
                    Objects.equals(getCategory(), "None") ? "" : "category=" + getCategory() + "&",
                    getCountry(),
                    APIKey
            );

            System.out.println(urlStr);

            processData(urlStr);
        } else {
            logger.warning("Error: No category or country specified in settings file. API call aborted.");
        }
    }

    private void processData(String urlStr) {
        articlesList = new ArrayList<>();

        try {
            URL url = new URI(urlStr).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                JSONObject inputStream = getDataStream(url);
                JSONArray articles = (JSONArray) inputStream.get("articles");

                for (Object articleObj : articles) {
                    JSONObject article = (JSONObject) articleObj;
                    ArrayList<String> articleToAdd = new ArrayList<>();

                    //Get Details
                    articleToAdd.add((String) article.get("title"));
                    articleToAdd.add((String) article.get("author"));
                    articleToAdd.add((String) article.get("description"));
                    articleToAdd.add((String) article.get("url"));
                    articlesList.add(articleToAdd);
                }
            } else {
                logger.severe("Error getting API Data %nResponse Code: " + responseCode);
            }
        } catch (Exception e) {
            logger.severe("Error at news API call: " + e.getMessage());
        }
    }

    public Boolean isNull (){
        return getCategory() == null && getCountry() == null;
    }

    public String getCategory() {
        return category;
    }  public void setCategory(String category) {
        this.category = category;
    } public String getCountry() {
        return country;
    } public void setCountry(String country) {
        this.country = country;
    }
}


