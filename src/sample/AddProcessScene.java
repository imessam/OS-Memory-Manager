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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;

public class AddProcessScene {
    static Stage addProcessWindow;
    static Stage window;
    static Manager manager;

    @FXML
    Label processInfoOUT;
    @FXML
    TextField processInfoIN;
    @FXML
    Button processInfoBTN;
    @FXML
    Button nextBTN;

    public static void getFromFifth(Manager temp) {
        manager = temp;
    }

    public static void getActiveStage(Stage temp) {
        window = temp;
    }

    public static void display() throws IOException {
        Parent root = FXMLLoader.load(SecondScene.class.getResource("addProcessScene.fxml"));
        addProcessWindow = new Stage();
        addProcessWindow.initModality(Modality.APPLICATION_MODAL);
        addProcessWindow.setTitle("Add Process");
        addProcessWindow.setScene(new Scene(root, 800, 600));
        AddProcessScene2.getActiveStage(addProcessWindow);
        addProcessWindow.showAndWait();
    }

    public void initialize() {
        int processNumber = (manager.getProcesses().size()) + 1;
        processInfoOUT.setText("Process " + processNumber + " no of segments : ");
        processInfoIN.setOnKeyReleased(event -> processInfoBTN.setDisable(processInfoIN.getText().isEmpty()));
        processInfoBTN.setOnAction(event -> processInfoIN.setDisable(true));
        nextBTN.setOnAction(event -> {
            Pair<Integer, Integer> pair = new Pair<>(manager.getProcesses().size(), Integer.parseInt(processInfoIN.getText()));
            processInfoIN.setDisable(false);
            AddProcessScene2.getFromLast(manager, pair);
            try {
                AddProcessScene2.display();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
