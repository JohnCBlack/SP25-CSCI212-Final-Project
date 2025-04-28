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
        stage.setTitle("Daily Recap");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();

//        //FOR TESTING PURPOSES
//        System.out.printf("%f, %f", stock.currentPrice, stock.percentChange);
//
//        NewsAPICall news = new NewsAPICall("bbc-news");
//        news.getNewsCite(1);
    }
}