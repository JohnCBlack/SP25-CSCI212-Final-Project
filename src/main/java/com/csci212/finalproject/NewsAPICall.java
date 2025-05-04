package com.csci212.finalproject;

import javafx.scene.control.TextArea;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;



public class NewsAPICall extends APICall{
    private String keyWord;
    private String category;
    private String country;
    private String language;


    //Hashmap for language to its code
    public static final Map<String, String> languageMap = new HashMap<>();
    static {
        languageMap.put("Arabic","ar");
        languageMap.put("German","de");
        languageMap.put("English","en");
        languageMap.put("Spanish", "es");
        languageMap.put("French", "fr");
        languageMap.put("Hebrew", "he");
        languageMap.put("Italian","it");
        languageMap.put("Dutch","nl");
        languageMap.put("Norwegian","no");
        languageMap.put("Portuguese","pt");
        languageMap.put("Russian","ru");
        languageMap.put("Swedish","sv");
        languageMap.put("Universal Dependencies", "ud");
        languageMap.put("Chinese","zh");
    }

    //Hashmap of all countries that are offered by the API
    public static final Map<String, String> countryMap = new HashMap<>();
    static {
        countryMap.put("Argentina", "ar");
        countryMap.put("Australia", "au");
        countryMap.put("Austria", "at");
        countryMap.put("Belgium", "be");
        countryMap.put("Brazil","br");
        countryMap.put("Canada", "ca");
        countryMap.put("China","cn");
        countryMap.put("Colombia", "co");
        countryMap.put("Cuba","cu");
        countryMap.put("Czech Republic", "cz");
        countryMap.put("Egypt","eg");
        countryMap.put("France","fr");
        countryMap.put("Germany", "de");
        countryMap.put("Greece", "gr");
        countryMap.put("Hong Kong", "hk");
        countryMap.put("Hungary", "hu");
        countryMap.put("India", "in");
        countryMap.put("Indonesia", "id");
        countryMap.put("Ireland", "ir");
        countryMap.put("Israel","il");
        countryMap.put("Italy","it");
        countryMap.put("Japan", "jp");
        countryMap.put("Latvia", "lv");
        countryMap.put("Lithuania", "lt");
        countryMap.put("Malaysia","my");
        countryMap.put("Mexico", "mx");
        countryMap.put("Morocco", "ma");
        countryMap.put("Netherlands","nl");
        countryMap.put("New Zealand", "nz");
        countryMap.put("Nigeria","ng");
        countryMap.put("Norway","no");
        countryMap.put("Philippines","ph");
        countryMap.put("Poland", "pl");
        countryMap.put("Portugal", "pt");
        countryMap.put("Romania", "ro");
        countryMap.put("Russia","ru");
        countryMap.put("Saudi Arabia", "sa");
        countryMap.put("Serbia","rs");
        countryMap.put("Singapore","sg");
        countryMap.put("Slovakia","sl");
        countryMap.put("Slovenia", "si");
        countryMap.put("South Africa","za");
        countryMap.put("South Korea","kr");
        countryMap.put("Sweden", "se");
        countryMap.put("Switzerland", "ch");
        countryMap.put("Taiwan","tw");
        countryMap.put("Thailand","th");
        countryMap.put("Turkey","tr");
        countryMap.put("UAE","ae");
        countryMap.put("Ukraine","ua");
        countryMap.put("United Kingdom","gb");
        countryMap.put("United States","us");
        countryMap.put("Venezuela","ve");
    }


    //Possibly a dropdown to give a list of sources
    //GET https://newsapi.org/v2/everything?q=Apple&from=2025-04-18&sortBy=popularity&apiKey=API_KEY
    // GET https://newsapi.org/v2/top-headlines?country=us&apiKey=API_KEY
    /**
    public NewsAPICall(String source){
        this.source = source;
        setApiKey("NEWS_API_KEY");
    }
    */

    //type 1 for news cite. Type 2 for general country
    public void getNewsHeadline(String category, String country) {
        this.category = category;
        this. country = country;

        setApiKey("NEWS_API_KEY");
        String urlStr = "";

        String categoryParam = (category == null) ? "" : "category=" + category + "&";
        String countryParam = (country == null) ? "" : "country=" + country + "&";
        urlStr = String.format("https://newsapi.org/v2/top-headlines?%s%s%s",categoryParam ,countryParam,"apiKey="+APIKey);

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

                    //Display
                    System.out.println("Title: " + title);
                    System.out.println("Author: " + author);
                    System.out.println("Description: " + description);
                    System.out.println("URL: " + link);
                    System.out.println("---------------------------------------");
                }

            } else {
                System.out.println("Response Code" + responseCode);
                System.out.println("Error in News API Call");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    public void getNewsKeyWord(String keyWord, String language) {
        this.keyWord = keyWord;
        this.language = language;

        setApiKey("NEWS_API_KEY");
        String urlStr = "";
        String languageParam = (language == null) ? "" : "language=" + language +"&";
        urlStr = String.format("https://newsapi.org/v2/everything?q=%s&%sapiKey=%s", this.keyWord,languageParam,APIKey);

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

                    //Display
                    System.out.println("Title: " + title);
                    System.out.println("Author: " + author);
                    System.out.println("Description: " + description);
                    System.out.println("URL: " + link);
                    System.out.println("---------------------------------------");
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


