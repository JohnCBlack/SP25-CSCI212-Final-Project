package com.csci212.finalproject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;


public class NewsAPICall extends APICall{
    private String category,country;
    public ArrayList<ArrayList<String>> articlesList;

    //GET https://newsapi.org/v2/everything?q=Apple&from=2025-04-18&sortBy=popularity&apiKey=API_KEY
    // GET https://newsapi.org/v2/top-headlines?country=us&apiKey=API_KEY

    public NewsAPICall() {
        setApiKey("NEWS_API_KEY");

        JSONObject settingsStream = getJSONSettings();
        assert settingsStream != null;

        if (settingsStream.containsKey("newsCategory")) {
            this.category = settingsStream.get("newsCategory").toString();
        } else {
            System.out.println("Error: No category specified in settings file.");
        }

        if (settingsStream.containsKey("newsCountry")) {
            this.country = settingsStream.get("newsCountry").toString();
        } else {
            System.out.println("Error: No country specified in settings file.");
        }
    }

    public void getNewsHeadline() {
        String urlStr = String.format("https://newsapi.org/v2/top-headlines?%scountry=%s&apiKey=%s",
                getCategory() == null ? "" : "category="+getCategory()+"&",
                getCountry(),
                APIKey
        );

        processData(urlStr);
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
                System.out.println("Response Code" + responseCode);
                System.out.println("Error in News API Call");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String getCategory() {
        return category;
    } public String getCountry() {
        return country;
    }
}


