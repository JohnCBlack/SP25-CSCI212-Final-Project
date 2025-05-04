package com.csci212.finalproject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private weatherAPICall weather; // Keep separate for setting page

    @FXML
    private Label conditionLabel;
    @FXML
    private ImageView weatherIcon;
    @FXML
    private Label currentTemp;
    @FXML
    private Label maxTemp;
    @FXML
    private Label minTemp;
    @FXML
    private Label changeOfPercp;

    @FXML
    private Button settingsButton;


    public void initialize(URL arg0, ResourceBundle arg1) {
        weather = new weatherAPICall();

        try {
            Image settingsIcon = new Image(new FileInputStream("src/main/resources/com/csci212/finalproject/settings-icon.png"));
            ImageView settingsIconView = new ImageView(settingsIcon);
            settingsIconView.setFitHeight(25);
            settingsIconView.setPreserveRatio(true);
            settingsButton.setGraphic(settingsIconView);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Image weatherIconLink = new Image(
                String.format("http:%s", weather.getIcon())
        );

        conditionLabel.setText(weather.getCondition());
        weatherIcon.setImage(weatherIconLink); //set image
        currentTemp.setText(
                String.format("%.1f°F", weather.getCurrentTemp())
        );
        maxTemp.setText(
                String.format("%.1f°F", weather.getMaxTemp())
        );
        minTemp.setText(
                String.format("%.1f°F", weather.getMinTemp())
        );
        changeOfPercp.setText(
                String.format("%.1f%%", weather.getChangeOfRain())
        );
    }
}