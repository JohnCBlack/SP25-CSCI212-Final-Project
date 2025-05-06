package com.csci212.finalproject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label; 
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DailyTasksController implements Initializable {

    @FXML 
    private TextField taskInputField;
    @FXML 
    private VBox taskListVBox;
    
    private static final String JSON_FILE = "daily_tasks.json";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadTasksFromJson();
    }

    @FXML
    private void handleBackToMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Daily Info");
        stage.show();
    }
    
    @FXML
    private void handleAddItem() {
        String taskText = taskInputField.getText().trim();
        if (!taskText.isEmpty()) {
            addTaskToUI(taskText, false);
            taskInputField.clear();
            saveTasksToJson();
        }
    }
    
    @FXML
    private void handleClearAllCheckmarks() {
        for (Node node : taskListVBox.getChildren()) {
            if (node instanceof HBox taskContainer) {
                CheckBox checkbox = (CheckBox) taskContainer.getChildren().get(0);
                checkbox.setSelected(false);
            }
        }
        saveTasksToJson();
    }
    
    private void addTaskToUI(String taskText, boolean isCompleted) {
        HBox taskContainer = new HBox(10);
        
        CheckBox checkbox = new CheckBox();
        Label taskLabel = new Label(taskText);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button deleteButton = new Button("Remove");
        deleteButton.setOnAction(_ -> {
            taskListVBox.getChildren().remove(taskContainer);
            saveTasksToJson();
        });
        
        checkbox.setSelected(isCompleted);
        taskLabel.setDisable(isCompleted);
        
        checkbox.selectedProperty().addListener((_, _, newVal) -> {
            taskLabel.setDisable(newVal);
            saveTasksToJson();
        });
        
        taskContainer.getChildren().addAll(checkbox, taskLabel, spacer, deleteButton);
        taskListVBox.getChildren().add(taskContainer);
    }
    @SuppressWarnings("unchecked")
    private void saveTasksToJson() {
        try {
            JSONArray tasksArray = new JSONArray();
            
            for (Node node : taskListVBox.getChildren()) {
                if (node instanceof HBox taskContainer) {
                    CheckBox checkbox = (CheckBox) taskContainer.getChildren().get(0);
                    Label label = (Label) taskContainer.getChildren().get(1);
                    
                    JSONObject taskObj = new JSONObject();
                    taskObj.put("text", label.getText());
                    taskObj.put("completed", checkbox.isSelected());
                    tasksArray.add(taskObj);
                }
            }
            
            try (FileWriter fileWriter = new FileWriter(JSON_FILE)) {
                fileWriter.write(tasksArray.toJSONString());
            }
            
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }
    
    private void loadTasksFromJson() {
        File file = new File(JSON_FILE);
        if (!file.exists()) {
            return;
        }
        
        try {
            JSONParser parser = new JSONParser();
            try (FileReader reader = new FileReader(file)) {
                JSONArray tasksArray = (JSONArray) parser.parse(reader);
                
                taskListVBox.getChildren().clear();
                
                for (Object obj : tasksArray) {
                    JSONObject taskObj = (JSONObject) obj;
                    String text = (String) taskObj.get("text"); 
                    boolean completed = (boolean) taskObj.get("completed");
                    
                    addTaskToUI(text, completed);
                }
            }
        } catch (IOException | ParseException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
    }
}