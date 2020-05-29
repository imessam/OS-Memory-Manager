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
    Process process;
    boolean isProcess;

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
        memoryWindow.setTitle("Memory Layout");
        memoryWindow.setScene(new Scene(root, 800, 600));
        memoryWindow.showAndWait();
    }

    public void initialize() {
        Label tempLabel2;
        Button tempButton;
        int count = 10, index;
        double multiplier = 200.0 / (double) manager.getMemorySize();
        tempLabel2 = new Label(String.valueOf(0));
        tempLabel2.setAlignment(Pos.BOTTOM_RIGHT);
        tempLabel2.setPadding(new Insets(0, 0, 0, 80));
        locationLayout.getChildren().add(tempLabel2);
        manager.fillMemory();
        Map<Integer, Pair<String, Integer>> memory = manager.getMemory();

        for (int address :
                memory.keySet()) {
            isProcess = false;
            tempLabel2 = new Label(String.valueOf(address + memory.get(address).getValue()));
            tempLabel2.setAlignment(Pos.BOTTOM_RIGHT);
            tempLabel2.setPadding(new Insets(memory.get(address).getValue() * multiplier, 0, 0, 80));
            tempButton = new Button(memory.get(address).getKey());
            Button finalTempButton = tempButton;
            if (memory.get(address).getKey().contains(":")) {
                index = memory.get(address).getKey().indexOf(':');
                process = manager.getProcesses().get(Integer.parseInt(memory.get(address).getKey().substring(0, index)));
                tempButton.setStyle("-fx-background-color: " + process.getColor().substring(2));
                tempButton.setOnMouseClicked(event -> {
                    MouseButton mouseButton = event.getButton();
                    if (mouseButton == MouseButton.PRIMARY) {
                        if (event.getClickCount() == 2) {
                            System.out.println("MOUSE : " + finalTempButton.getText() + " : " + address);
                            Map<String, Pair<Integer, Integer>> segments = process.getSegments();
                            for (String s :
                                    segments.keySet()) {
                                manager.deallocateSegment(new Pair<>(s, segments.get(s).getValue()), process);
                                refreshScene();
                            }
                        }
                    }
                });
            } else {
                tempButton.setOnAction(event -> {
                    manager.deallocateSegment(new Pair<>(finalTempButton.getText(), address), null);
                    refreshScene();
                });
            }
            tempButton.setPadding(new Insets((memory.get(address).getValue() + count) * multiplier, 0, 0, 100));
            tempButton.setAlignment(Pos.BOTTOM_CENTER);

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
