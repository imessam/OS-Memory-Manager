/*
 * Copyright (c) 2020. Mohamed Essam Abdelfattah
 */

package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class FirstScene {
    int noprocesses;
    Manager manager = new Manager();
    @FXML
    Label memorySize;
    @FXML
    Label noOfProcesses;
    @FXML
    Label noOfHoles;
    @FXML
    TextField memorySizeIN;
    @FXML
    TextField noOfProcessesIN;
    @FXML
    TextField noOfHolesIN;
    @FXML
    Button memorySizeBTN;
    @FXML
    Button noOfProcessesBTN;
    @FXML
    Button nextBTN;
    @FXML
    Button noOfHolesBTN;

    @FXML
    public void initialize() {

        memorySizeBTN.setDisable(true);
        noOfProcessesBTN.setDisable(true);
        noOfHolesBTN.setDisable(true);
        memorySizeIN.setOnKeyReleased(e -> {
            memorySizeBTN.setDisable(memorySizeIN.getText().isEmpty());
        });
        noOfProcessesIN.setOnKeyReleased(e -> {
            noOfProcessesBTN.setDisable(noOfProcessesIN.getText().isEmpty());
        });
        noOfHolesIN.setOnKeyReleased(event -> {
            noOfHolesBTN.setDisable(noOfHolesIN.getText().isEmpty());
        });
        memorySizeBTN.setOnAction(event -> {
            memorySizeIN.setDisable(true);
            manager.setMemorySize(Integer.parseInt(memorySizeIN.getText()));
        });
        noOfProcessesBTN.setOnAction(event -> {
            noOfProcessesIN.setDisable(true);
            noprocesses = Integer.parseInt(noOfProcessesIN.getText());
        });
        noOfHolesBTN.setOnAction(event -> {
            noOfHolesIN.setDisable(true);
            ForthScene.setNoOfHoles(Integer.parseInt(noOfHolesIN.getText()));
        });
        System.out.println(memorySize);
        nextBTN.setOnAction(event -> {
            SecondScene.getFromFirst(manager, noprocesses);
            try {
                SecondScene.display();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
