package com.csci212.finalproject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;


public class NewsAPICall extends APICall{
    private String keyWord,category,country,language;
    //public ArrayList<String> articleToAdd = new ArrayList<>();
    public ArrayList<ArrayList<String>> articlesList = new ArrayList<>();

    //Possibly a dropdown to give a list of sources
    //GET https://newsapi.org/v2/everything?q=Apple&from=2025-04-18&sortBy=popularity&apiKey=API_KEY
    // GET https://newsapi.org/v2/top-headlines?country=us&apiKey=API_KEY
    //public ArrayList<String> getArticles(){return articlesList;}

    public void getNewsHeadline(String category, String country) {
        this.category = category;
        this.country = country;

        setApiKey("NEWS_API_KEY");

        String urlStr = String.format("https://newsapi.org/v2/top-headlines?category=%s&country=%s&apiKey=%s",
                category,country,APIKey
        );

        processData(urlStr);
    }

    /**
    public void getNewsKeyWord(String keyWord, String language) {
        this.keyWord = keyWord;
        this.language = language;

        setApiKey("NEWS_API_KEY");
        String urlStr = "";
        String languageParam = "language=" + language +"&";
        urlStr = String.format("https://newsapi.org/v2/everything?q=%s&%sapiKey=%s", this.keyWord,languageParam,APIKey);

        processData(urlStr);
    }
    **/

    private void processData(String urlStr) {
        try {
            URL url = new URI(urlStr).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                JSONObject inputStream = getDataStream(url);
                JSONArray articles = (JSONArray) inputStream.get("articles");
                System.out.println("Articles:\n");

                for (Object articleObj : articles) {
                    JSONObject article = (JSONObject) articleObj;
                    //Get Details
                    String title = (String) article.get("title");
                    String author = (String) article.get("author");
                    String description = (String) article.get("description");
                    String link = (String) article.get("url");

                    //TODO. Make an array of articles to sent over to the controller YOU CAN NOT EFFECT THE FXML FROM THIS CLASS DIRECTLY!!!!
                    //newsTextArea.appendText(String.format("%s\n%s\n%s\n%s\n",title, author, description, link));
                    ArrayList<String> articleToAdd = new ArrayList<>();
                    articleToAdd.add(title);
                    articleToAdd.add(author);
                    articleToAdd.add(description);
                    articleToAdd.add(link);
                    articlesList.add(articleToAdd);

                    /**Display
                    System.out.println("Title: " + title);
                    System.out.println("Author: " + author);
                    System.out.println("Description: " + description);
                    System.out.println("URL: " + link);
                    System.out.println("---------------------------------------");
                     **/
                }
            } else {
                System.out.println("Response Code" + responseCode);
                System.out.println("Error in News API Call");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


