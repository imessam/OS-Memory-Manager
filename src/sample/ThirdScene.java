/*
 * Copyright (c) 2020. Mohamed Essam Abdelfattah
 */

package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThirdScene {
    static Stage window;
    static Manager manager;
    static ArrayList<Pair<Integer, Integer>> processes;
    List<Label> labelList = new ArrayList<>();
    List<TextField> nameList = new ArrayList<>();
    List<TextField> sizeList = new ArrayList<>();
    Map<String, Pair<Integer, Integer>> segments;

    int count = 0;
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
    RadioButton firstFit;
    @FXML
    RadioButton bestFit;
    @FXML
    ToggleGroup toggleGroup1;
    @FXML
    ColorPicker colorPicker;


    public static void getActiveStage(Stage temp) {
        window = temp;
    }


    public void initialize() {
        firstFit.setDisable(true);
        bestFit.setDisable(true);
        nextBTN.setDisable(true);
        segments = new HashMap<>();
        labelLayout.setSpacing(30);
        nameLayout.setSpacing(20);
        sizeLayout.setSpacing(20);
        addControls();
        okBTN.setOnAction(event -> {
            for (int i = 0; i < nameList.size(); i++) {
                segments.put(nameList.get(i).getText(), new Pair<>(Integer.parseInt(sizeList.get(i).getText()), -1));
            }
            manager.addProcess(segments, colorPicker.getValue());
            segments.clear();
            if (count == processes.size()) {
                okBTN.setDisable(true);
                nextBTN.setDisable(false);
                firstFit.setDisable(false);
                bestFit.setDisable(false);
                //manager.printProcesses();
            } else {

                labelList.clear();
                nameList.clear();
                sizeList.clear();
                addControls();
            }
        });
        nextBTN.setOnAction(event -> {
            if (firstFit.isSelected()) {
                System.out.println("First Fit");
                manager.setMethod(true);
            } else if (bestFit.isSelected()) {
                System.out.println("Best Fit");
                manager.setMethod(false);
            }
            ForthScene.getFromThird(manager);
            try {
                ForthScene.display();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void addControls() {
        for (int j = 0; j < processes.get(count).getValue(); j++) {
            labelList.add(new Label("Process " + (count + 1) + " segment " + (j + 1) + "\nname and size :"));
            nameList.add(new TextField("Name"));
            sizeList.add(new TextField("Size"));
        }
        labelLayout.getChildren().clear();
        labelLayout.getChildren().addAll(labelList);
        nameLayout.getChildren().clear();
        nameLayout.getChildren().addAll(nameList);
        sizeLayout.getChildren().clear();
        sizeLayout.getChildren().addAll(sizeList);
        count++;
    }

    public static void getFromSecond(Manager temp, ArrayList<Pair<Integer, Integer>> tempPro) {
        manager = temp;
        processes = tempPro;
    }

    public static void display() throws IOException {
        Parent root = FXMLLoader.load(SecondScene.class.getResource("scene3.fxml"));
        window.setTitle("Third Scene");
        window.setScene(new Scene(root, 800, 600));
        ForthScene.getActiveStage(window);
        window.show();
    }
}
