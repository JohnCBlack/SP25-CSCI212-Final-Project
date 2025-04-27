package com.csci212.finalproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();

//        weatherAPICall weather = new weatherAPICall();
//
//        //FOR TESTING PURPOSES
//        System.out.println(
//                "Weather Report:\n" +
//                        "Icon: " + weather.icon + "\n" +
//                        "Condition: " + weather.condition + "\n" +
//                        "Current Temperature: " + weather.currentTemp + "°F\n" +
//                        "Maximum Temperature: " + weather.maxTemp + "°F\n" +
//                        "Minimum Temperature: " + weather.minTemp + "°F\n" +
//                        "Chance of Rain: " + weather.changeOfRain + "%"
//        );

//        stockAPICall stock = new stockAPICall();
//
//        //FOR TESTING PURPOSES
//        System.out.printf("%f, %f", stock.currentPrice, stock.percentChange);
//
//        NewsAPICall news = new NewsAPICall("bbc-news");
//        news.getNewsCite(1);
    }
}