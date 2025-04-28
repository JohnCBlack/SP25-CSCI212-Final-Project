package com.csci212.finalproject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    public void initialize(URL arg0, ResourceBundle arg1) {
        weather = new weatherAPICall();

        Image icon = new Image(
                String.format("http:%s", weather.getIcon())
        );

        conditionLabel.setText(weather.getCondition());
        conditionLabel.setAlignment(javafx.geometry.Pos.CENTER);
        weatherIcon.setImage(icon); //set image
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