/*
 * Copyright (c) 2020. Mohamed Essam Abdelfattah
 */

package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableScene {

    static Process process;
    static Stage window;
    static Stage tableWindow;

    @FXML
    ListView<String> segmentNumber;
    @FXML
    ListView<Integer> limit;
    @FXML
    ListView<Integer> baseAddress;
    List<String> segmentsName;
    List<Integer> limitList;
    List<Integer> baseAddressList;

    public static void getFromFifth(Process temp) {
        process = temp;
    }

    public static void getActiveStage(Stage temp) {
        window = temp;
    }

    public static void display() throws IOException {
        Parent root = FXMLLoader.load(SecondScene.class.getResource("tableScene.fxml"));
        tableWindow = new Stage();
        tableWindow.initModality(Modality.APPLICATION_MODAL);
        tableWindow.setTitle("Table Scene");
        tableWindow.setScene(new Scene(root, 800, 600));
        tableWindow.showAndWait();
    }

    public void initialize() {
        segmentsName = new ArrayList<>();
        limitList = new ArrayList<>();
        baseAddressList = new ArrayList<>();
        Map<String, Pair<Integer, Integer>> segments = process.getSegments();
        for (String segment :
                segments.keySet()) {
            segmentsName.add(segment);
            limitList.add(segments.get(segment).getKey());
            baseAddressList.add(segments.get(segment).getValue());
        }
        segmentNumber.getItems().addAll(segmentsName);
        limit.getItems().addAll(limitList);
        baseAddress.getItems().addAll(baseAddressList);
    }
}

