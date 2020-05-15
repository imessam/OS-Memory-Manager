/*
 * Copyright (c) 2020. Mohamed Essam Abdelfattah
 */

package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProcessScene2 {
    static Stage window;
    static Manager manager;
    static Pair<Integer, Integer> process;

    List<Label> labelList = new ArrayList<>();
    List<TextField> nameList = new ArrayList<>();
    List<TextField> sizeList = new ArrayList<>();
    Map<String, Pair<Integer, Integer>> segments;

    @FXML
    VBox labelLayout;
    @FXML
    VBox nameLayout;
    @FXML
    VBox sizeLayout;
    @FXML
    Button okBTN;
    @FXML
    Button nextBTN;
    @FXML
    ColorPicker colorPicker;

    public static void getFromLast(Manager temp, Pair<Integer, Integer> tempPair) {
        manager = temp;
        process = tempPair;
    }

    public static void getActiveStage(Stage temp) {
        window = temp;
    }

    public static void display() throws IOException {
        Parent root = FXMLLoader.load(SecondScene.class.getResource("addProcessScene2.fxml"));
        window.setTitle("Add Process");
        window.setScene(new Scene(root, 800, 600));
        window.show();
    }

    public void initialize() {
        nextBTN.setDisable(true);
        segments = new HashMap<>();
        labelLayout.setSpacing(30);
        nameLayout.setSpacing(20);
        sizeLayout.setSpacing(20);
        addControls();
    }

    private void addControls() {
        for (int j = 0; j < process.getValue(); j++) {
            labelList.add(new Label("Process " + (process.getKey()) + " segment " + (j + 1) + "\nname and size :"));
            nameList.add(new TextField("Name"));
            sizeList.add(new TextField("Size"));
        }
        labelLayout.getChildren().clear();
        labelLayout.getChildren().addAll(labelList);
        nameLayout.getChildren().clear();
        nameLayout.getChildren().addAll(nameList);
        sizeLayout.getChildren().clear();
        sizeLayout.getChildren().addAll(sizeList);
        okBTN.setOnAction(event -> {
            for (int i = 0; i < nameList.size(); i++) {
                segments.put(nameList.get(i).getText(), new Pair<>(Integer.parseInt(sizeList.get(i).getText()), -1));
            }
            manager.addProcess(segments, colorPicker.getValue());
            okBTN.setDisable(true);
            nextBTN.setDisable(false);

        });

        nextBTN.setOnAction(event1 -> window.close());
    }
}
