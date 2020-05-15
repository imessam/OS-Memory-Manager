/*
 * Copyright (c) 2020. Mohamed Essam Abdelfattah
 */

package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Map;

public class MemoryScene {
    static Stage memoryWindow;
    static Manager manager;
    static Stage window;

    @FXML
    VBox locationLayout;
    @FXML
    VBox allocatedLayout;

    public static void getFromFifth(Manager temp) {
        manager = temp;
    }

    public static void getActiveStage(Stage temp) {
        window = temp;
    }

    public static void display() throws IOException {
        Parent root = FXMLLoader.load(SecondScene.class.getResource("memoryScene.fxml"));
        memoryWindow = new Stage();
        memoryWindow.initModality(Modality.APPLICATION_MODAL);
        memoryWindow.setTitle("Memory Scene");
        memoryWindow.setScene(new Scene(root, 800, 600));
        memoryWindow.showAndWait();
    }

    public void initialize() {
        Label tempLabel2;
        Button tempButton;
        int count = 10;
        double multiplier = 200.0 / (double) manager.getMemorySize();
        Process process;

        tempLabel2 = new Label(String.valueOf(0));
        tempLabel2.setAlignment(Pos.BOTTOM_RIGHT);
        tempLabel2.setPadding(new Insets(0, 0, 0, 80));
        locationLayout.getChildren().add(tempLabel2);
        manager.fillMemory();
        manager.printMemory();
        Map<Integer, Pair<String, Integer>> memory = manager.getMemory();
        for (int address :
                memory.keySet()) {
            tempLabel2 = new Label(String.valueOf(address + memory.get(address).getValue()));
            tempLabel2.setAlignment(Pos.BOTTOM_RIGHT);
            tempLabel2.setPadding(new Insets(memory.get(address).getValue() * multiplier, 0, 0, 80));

            tempButton = new Button(memory.get(address).getKey());
            // tempButton.setStyle("-fx-colo: #f00000");

            if (memory.get(address).getKey().charAt(1) == ':') {
                process = manager.getProcesses().get(Integer.parseInt(memory.get(address).getKey().substring(0, 1)));
                tempButton.setStyle("-fx-background-color: " + process.getColor().substring(2));

            }
            tempButton.setPadding(new Insets((memory.get(address).getValue() + count) * multiplier, 0, 0, 100));
            //tempButton.setDisable(true);
            tempButton.setAlignment(Pos.BOTTOM_CENTER);
            //tempButton.setId(String.valueOf(address));
            Button finalTempButton = tempButton;
            tempButton.setOnMouseClicked(event -> {
                MouseButton mouseButton = event.getButton();
                if (mouseButton == MouseButton.PRIMARY) {
                    if (event.getClickCount() == 2) {
                        System.out.println("MOUSE : " + finalTempButton.getText() + " : " + address);
                        manager.deallocateSegment(new Pair<>(finalTempButton.getText(), address));
                        refreshScene();
                    }

                }
            });

            locationLayout.getChildren().add(tempLabel2);
            allocatedLayout.getChildren().add(tempButton);
            count = 0;
        }


    }

    private void refreshScene() {
        locationLayout.getChildren().clear();
        allocatedLayout.getChildren().clear();
        initialize();
    }
}
