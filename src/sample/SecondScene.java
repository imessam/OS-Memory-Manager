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
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;


public class SecondScene {
    static Manager manager;
    static int noprocesses;
    static Stage window;
    ArrayList<Pair<Integer, Integer>> temp;
    int count = 1;

    @FXML
    Label processInfoOUT;
    @FXML
    TextField processInfoIN;
    @FXML
    Button processInfoBTN;
    @FXML
    Button nextBTN;

    public static void display() throws IOException {
        Parent root = FXMLLoader.load(SecondScene.class.getResource("scene2.fxml"));
        window.setTitle("Second Scene");
        window.setScene(new Scene(root, 600, 300));
        ThirdScene.getActiveStage(window);
        window.show();
    }

    public static void getFromFirst(Manager temp, int noPro) {
        manager = temp;
        noprocesses = noPro;
    }

    public static void getActiveStage(Stage temp) {
        window = temp;
    }

    public void initialize() {
        temp = new ArrayList<>();
        processInfoBTN.setDisable(true);
        processInfoOUT.setText("Process " + count + " no of segments : ");
        processInfoIN.setOnKeyReleased(event -> processInfoBTN.setDisable(processInfoIN.getText().isEmpty()));
        processInfoBTN.setOnAction(event -> processInfoIN.setDisable(true));
        nextBTN.setOnAction(event -> {
            Pair<Integer, Integer> pair = new Pair<>(count, Integer.parseInt(processInfoIN.getText()));
            temp.add(pair);
            count++;
            processInfoIN.setDisable(false);
            if (count > noprocesses) {
//                for (Pair<Integer, Integer> integerIntegerPair : temp) {
//                    System.out.println(integerIntegerPair.getKey() + " : " + integerIntegerPair.getValue());
//                }
                nextBTN.setDisable(true);
                ThirdScene.getFromSecond(manager, temp);
                try {
                    ThirdScene.display();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                processInfoOUT.setText("Process " + count + " no of segments : ");
            }
        });
    }

}
