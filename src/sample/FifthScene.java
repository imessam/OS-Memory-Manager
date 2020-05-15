/*
 * Copyright (c) 2020. Mohamed Essam Abdelfattah
 */

package sample;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FifthScene {

    static Manager manager;
    static Stage window;
    @FXML
    ListView<String> processName;
    @FXML
    ListView<String> processAllocated;
    @FXML
    Button addProcessBTN;
    @FXML
    Button showMemoryBTN;
    @FXML
    Button refreshButton;

    List<String> processes;
    List<String> allocation;
    String s;
    Process process;
    int oldSize;

    public static void getFromForth(Manager temp) {
        manager = temp;
    }

    public static void getActiveStage(Stage temp) {
        window = temp;
    }

    public static void display() throws IOException {
        Parent root = FXMLLoader.load(SecondScene.class.getResource("scene5.fxml"));
        window.setTitle("Fifth Scene");
        window.setScene(new Scene(root, 800, 600));
        TableScene.getActiveStage(window);
        MemoryScene.getActiveStage(window);
        AddProcessScene.getActiveStage(window);
        window.show();
    }

    public void initialize() {
        refreshButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("refresh.png"))));
        processes = new ArrayList<>();
        allocation = new ArrayList<>();
        ArrayList<Process> temp = manager.getProcesses();
        oldSize = manager.getProcesses().size();
        for (Process process : temp) {
            processes.add("Process " + process.getProcessNumber());
            if (process.isAllocated()) {
                allocation.add("Allocated");
            } else {
                allocation.add("Not allocated");
            }
        }
        processName.getItems().addAll(processes);
        processAllocated.getItems().addAll(allocation);
        processName.setCellFactory(e -> {
            ListCell<String> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu();


            MenuItem allocateProcess = new MenuItem();
            MenuItem deallocateProcess = new MenuItem();
            MenuItem showTable = new MenuItem();
            allocateProcess.textProperty().bind(Bindings.format("Allocate \"%s\"", cell.itemProperty()));
            allocateProcess.setOnAction(event -> {
                s = cell.itemProperty().get();
                s = s.substring(s.length() - 1);

                if (manager.allocateProcess(Integer.parseInt(s) - 1)) {
                    allocation.set(Integer.parseInt(s) - 1, "Allocated");
                    processAllocated.getItems().clear();
                    processAllocated.getItems().addAll(allocation);
                } else {
                    allocation.set(Integer.parseInt(s) - 1, "Cannot be allocated");
                    processAllocated.getItems().clear();
                    processAllocated.getItems().addAll(allocation);

                }


            });
            deallocateProcess.textProperty().bind(Bindings.format("Deallocate \"%s\"", cell.itemProperty()));
            deallocateProcess.setOnAction(event -> {
                s = cell.itemProperty().get();
                s = s.substring(s.length() - 1);
                manager.deallocateProcess(Integer.parseInt(s) - 1);
                allocation.set(Integer.parseInt(s) - 1, "Not Allocated");
                processAllocated.getItems().clear();
                processAllocated.getItems().addAll(allocation);
            });
            showTable.textProperty().bind(Bindings.format("Show \"%s\" table", cell.itemProperty()));
            showTable.setOnAction(event -> {
                try {
                    s = cell.itemProperty().get();
                    s = s.substring(s.length() - 1);
                    process = manager.getProcesses().get(Integer.parseInt(s) - 1);
                    TableScene.getFromFifth(process);
                    TableScene.display();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            });
            contextMenu.getItems().addAll(allocateProcess, deallocateProcess, showTable);

            cell.textProperty().bind(cell.itemProperty());

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell;
        });

        showMemoryBTN.setOnAction(event -> {
            try {
                MemoryScene.getFromFifth(manager);
                MemoryScene.display();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        addProcessBTN.setOnAction(event -> {

            AddProcessScene.getFromFifth(manager);
            try {
                AddProcessScene.display();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (oldSize != manager.getProcesses().size()) {
                Process tempprocess = manager.getProcesses().get(manager.getProcesses().size() - 1);
                processes.add("Process " + tempprocess.getProcessNumber());
                if (tempprocess.isAllocated()) {
                    allocation.add("Allocated");
                } else {
                    allocation.add("Not allocated");
                }
            }
            processName.getItems().add(processes.get(processes.size() - 1));
            processAllocated.getItems().add(allocation.get(allocation.size() - 1));
        });
        refreshButton.setOnAction(event -> {
            for (int i = 0; i < processAllocated.getItems().size(); i++) {
                if (!manager.getProcesses().get(i).checkAllocated()) {
                    modifyAllocation(i);

                }
            }
        });
    }

    public void modifyAllocation(int processNumber) {
        allocation.set(processNumber, "Not Allocated");
        processAllocated.getItems().clear();
        processAllocated.getItems().addAll(allocation);

    }
}

