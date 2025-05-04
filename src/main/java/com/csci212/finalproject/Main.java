package com.csci212.finalproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        File settingsFile = new File("src/main/resources/com/csci212/finalproject/settings.json");
        if (settingsFile.exists()) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Daily Recap");
            stage.setScene(scene);
            stage.show();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Settings.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Daily Recap");
            stage.setScene(scene);
            stage.show();
        }

    }

    public static void main(String[] args) {
        //This is the NewsAPI Call! To make it go to the display box just change the system.out.print"...." to the box you want.
        //NewsAPICall call = new NewsAPICall();
        //call.getNewsHeadline("", "us");

        launch();

        //FOR TESTING PURPOSES
//        System.out.printf("%f, %f", stock.currentPrice, stock.percentChange);

    }
}