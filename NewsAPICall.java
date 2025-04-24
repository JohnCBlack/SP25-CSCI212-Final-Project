import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;



public class NewsAPICall extends APICall{
    private String source;

    //List of specific news cites to pull from
    public static String[] sourceArray = {
            "bbc-news","fox-news","cnn","associated-press"
    };
    //This list can be used for the GUI ComboBox.
    public static String[] countryArray = {
        "Argentina","Australia","Austria","Belgium","Brazil","Canada","Chile","China","Columbia","Cuba","Czech",
            "Egypt","France","Germany","Greece","Hong Kong","Hungary","India","Indonesia","Ireland","Israel","Italy","Japan",
            "Latvia", "Lithuania","Malaysia","Mexico","Morocco","Netherlands","New Zealand","Nigeria","Norway","Philippines","Poland","Portugal","Romania",
            "Russia","Saudi Arabia","Serbia","Singapore","Slovakia","Slovenia","South Africa","South Korea","Sweden","Switzerland","Taiwan","Thailand","Turkey","UAE",
            "Ukraine","United Kingdom","United States","Venezuela"
    };


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

    public NewsAPICall(String source){
        this.source = source;
        setApiKey("NEWS_API_KEY");
    }

    //type 1 for news cite. Type 2 for general country
    public void getNewsCite(int type) {

        String urlStr = "";

        switch (type) {
            case 1 ->
                    urlStr = String.format("https://newsapi.org/v2/top-headlines?sources=%s&apiKey=%s", this.source, APIKey);
            case 2 ->
                    urlStr = String.format("https://newsapi.org/v2/top-headlines?country=%s&apiKey=%s", this.source, APIKey);
            default -> {
                System.out.println("Invalid type specified.");
                return;
            }
        }

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


