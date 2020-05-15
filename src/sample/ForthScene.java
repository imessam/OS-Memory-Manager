/*
 * Copyright (c) 2020. Mohamed Essam Abdelfattah
 */

package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ForthScene {
    static Stage window;
    static Manager manager;
    static int noOfHoles;
    List<Label> labelList;
    List<TextField> sizeList;
    List<TextField> addressList;

    @FXML
    VBox labelsLayout;
    @FXML
    VBox sizeLayout;
    @FXML
    VBox addressLayout;
    @FXML
    Button okBTN;
    @FXML
    Button nextBTN;

    public static void setNoOfHoles(int noOfHoles) {
        ForthScene.noOfHoles = noOfHoles;
    }

    public static void getFromThird(Manager temp) {
        manager = temp;
    }

    public static void getActiveStage(Stage temp) {
        window = temp;
    }

    public static void display() throws IOException {
        Parent root = FXMLLoader.load(SecondScene.class.getResource("scene4.fxml"));
        window.setTitle("Forth Scene");
        window.setScene(new Scene(root, 800, 600));
        FifthScene.getActiveStage(window);
        window.show();
    }

    public void initialize() {
        labelList = new ArrayList<>();
        sizeList = new ArrayList<>();
        addressList = new ArrayList<>();
        labelsLayout.setSpacing(30);
        sizeLayout.setSpacing(20);
        addressLayout.setSpacing(20);
        nextBTN.setDisable(true);
        for (int i = 0; i < noOfHoles; i++) {
            labelList.add(new Label("Hole " + (i + 1) + " :"));
            sizeList.add(new TextField("Size"));
            addressList.add(new TextField("Base Address"));
        }
        labelsLayout.getChildren().clear();
        sizeLayout.getChildren().clear();
        addressLayout.getChildren().clear();
        labelsLayout.getChildren().addAll(labelList);
        sizeLayout.getChildren().addAll(sizeList);
        addressLayout.getChildren().addAll(addressList);
        okBTN.setOnAction(event -> {
            okBTN.setDisable(true);
            nextBTN.setDisable(false);
            for (int i = 0; i < noOfHoles; i++) {
                manager.addHole(Integer.parseInt(addressList.get(i).getText()), Integer.parseInt(sizeList.get(i).getText()));
            }
        });
        nextBTN.setOnAction(event -> {
//            manager.printHoles();
            manager.fillReserves();
//            manager.printReserves();

            FifthScene.getFromForth(manager);
            try {
                FifthScene.display();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
