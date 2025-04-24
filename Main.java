public class Main {
    public static void main(String[] args) {
        /*weatherAPICall weather = new weatherAPICall();

        //FOR TESTING PURPOSES
        System.out.println(
                "Weather Report:\n" +
                        "Condition: " + weather.condition + "\n" +
                        "Current Temperature: " + weather.currentTemp + "°F\n" +
                        "Maximum Temperature: " + weather.maxTemp + "°F\n" +
                        "Minimum Temperature: " + weather.minTemp + "°F\n" +
                        "Chance of Rain: " + weather.changeOfRain + "%"
        );

        stockAPICall stock = new stockAPICall();

        //FOR TESTING PURPOSES
        System.out.printf("%f, %f", stock.currentPrice, stock.percentChange);*/

        NewsAPICall news = new NewsAPICall("bbc-news");
        news.getNewsCite(1);
    }
}