/*
 * Copyright (c) 2020. Mohamed Essam Abdelfattah
 */

package sample;

import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Manager {

    private int memorySize, count;

    public ArrayList<Process> getProcesses() {
        return processes;
    }

    //private ArrayList<Map<String, Pair<Integer, Integer>>> processes;
    private ArrayList<Process> processes;
    private Map<Integer, Pair<String, Integer>> holes;
    private Map<Integer, Pair<String, Integer>> reserved;
    private Map<Integer, Pair<String, Integer>> memory;
    private boolean method; //true : First Fit , false : Best Fit.


    public Manager() {
        count = 1;
        processes = new ArrayList<>();
        holes = new TreeMap<>();
        reserved = new TreeMap<>();
        memory = new TreeMap<>();
    }

    public boolean getMethod() {
        return method;
    }

    public void setMethod(boolean method) {
        this.method = method;
    }

    public int getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
        System.out.println(memorySize);
    }

    public void addHole(int address, int size) {
        if (size != 0) {
            holes.put(address, new Pair<>("hole", size));
        }
    }

    private void addReserve(int address, int size) {
        if (size != 0) {
            reserved.put(address, new Pair<>("reserve", size));
        }
    }

    public void addProcess(Map<String, Pair<Integer, Integer>> segments, Color value) {
        // processes.add(new HashMap<>(segments));
        Process temp = new Process(count);
        temp.setSegments(segments);
        temp.setColor(value);
        processes.add(temp);
        count++;
    }

    public void fillReserves() {
        int count = 0;
        for (int holeAddress : holes.keySet()
        ) {
            if (holeAddress == 0) {
                count = holeAddress + (holes.get(holeAddress).getValue());
                continue;
            } else {
                //reserved.put(count,(holeAddress-count));
                addReserve(count, (holeAddress - count));
                count = holeAddress + (holes.get(holeAddress).getValue());
            }
            if (count != memorySize) {
                //reserved.put(count,(memorySize-count));
                addReserve(count, (memorySize - count));
            }
        }
    }

    public Map<Integer, Pair<String, Integer>> getMemory() {
        return memory;
    }

    public void fillMemory() {
        memory.clear();
        for (int hole :
                holes.keySet()) {
            memory.put(hole, new Pair<>(holes.get(hole).getKey(), holes.get(hole).getValue()));
        }
        for (int reserve :
                reserved.keySet()) {
            memory.put(reserve, new Pair<>(reserved.get(reserve).getKey(), reserved.get(reserve).getValue()));
        }
    }

    public boolean allocateProcess(int processNumber) {
        Process process = processes.get(processNumber);
        Map<String, Pair<Integer, Integer>> tempSegments = new HashMap<>(process.getSegments());
        Map<Integer, Pair<String, Integer>> tempHoles = new TreeMap<>(holes);
        Map<Integer, Pair<String, Integer>> oldHoles = new TreeMap<>(holes);

        Pair<Integer, Integer> tempBest;
        int diff = -1;
        boolean isAllocated = true;
        for (String segmentName :
                tempSegments.keySet()) {
            tempBest = new Pair<>(memorySize, -1);
            printHoles(tempHoles);
            for (int address :
                    tempHoles.keySet()) {
                diff = tempHoles.get(address).getValue() - tempSegments.get(segmentName).getKey();
                //UNDOOOO
                if ((diff >= 0) && (tempHoles.get(address).getKey().equals("hole"))) {
                    if (method) {
                        tempBest = new Pair<>(diff, address);
                        break;
                    } else {
                        if (diff == 0) {
                            tempBest = new Pair<>(diff, address);
                            break;
                        } else if (diff < tempBest.getKey()) {
                            tempBest = new Pair<>(diff, address);
                        }
                    }
                }


            }
            System.out.println("Putting " + (processNumber + ":" + segmentName) + " at " + tempBest.getValue() + " size is " + tempSegments.get(segmentName).getKey());
            tempHoles.put(tempBest.getValue(), new Pair<>(processNumber + ":" + segmentName, tempSegments.get(segmentName).getKey()));
            process.addSegment(segmentName, tempSegments.get(segmentName).getKey(), tempBest.getValue());
            if (tempBest.getKey() != 0) {
                System.out.println("Putting hole at " + (tempBest.getValue() + tempSegments.get(segmentName).getKey()) + " size is " + tempBest.getKey());
                tempHoles.put(tempBest.getValue() + (tempSegments.get(segmentName).getKey()), new Pair<>("hole", tempBest.getKey()));
            }
        }
        for (String segment :
                process.getSegments().keySet()) {
            if (process.getSegments().get(segment).getValue() == -1) {
                isAllocated = false;
                process.setSegments(tempSegments);
                holes = oldHoles;
                System.out.println("ERRRRORRRR");
                printHoles();

                break;
            } else {
                holes = tempHoles;
            }
        }
        process.setAllocated(isAllocated);
        return isAllocated;
    }

    public void deallocateProcess(int processNumber) {
        String s = String.valueOf(processNumber);
        Process process = processes.get(processNumber);
        //int sum=-1,oldSize=-1;
        int index;
        Map<String, Pair<Integer, Integer>> tempSegments = new HashMap<>(process.getSegments());
        for (int address :
                holes.keySet()) {
            if (holes.get(address).getKey().contains(":")) {
                index = holes.get(address).getKey().indexOf(':');
                if (holes.get(address).getKey().substring(0, index).equals(s)) {
                    System.out.println("Deallocate " + holes.get(address).getKey().substring(index + 1));
                    holes.put(address, new Pair<>("hole", holes.get(address).getValue()));
                    //UNDOOOOOOO

                }
            }
        }
        adjustHoles();
        tempSegments.replaceAll((k, v) -> new Pair<>(tempSegments.get(k).getKey(), -1));
        process.setSegments(tempSegments);
        process.setAllocated(false);
    }

    public void deallocateSegment(Pair<String, Integer> segment) {

        if (segment.getKey().equals("reserve")) {
            holes.put(segment.getValue(), new Pair<>("hole", reserved.get(segment.getValue()).getValue()));
            reserved.remove(segment.getValue());
        } else if (segment.getKey().equals("hole")) {
            System.out.println("Cannot deallocate a hole");
        } else {
            String processSegment;
            int processNumber, i = segment.getKey().indexOf(':');
            processNumber = Integer.parseInt(segment.getKey().substring(0, i));
            processSegment = segment.getKey().substring(i + 1);
            holes.put(segment.getValue(), new Pair<>("hole", holes.get(segment.getValue()).getValue()));
            processes.get(processNumber).getSegments().put(processSegment, new Pair<>(holes.get(segment.getValue()).getValue(), -1));
        }
        adjustHoles();
    }

    private void adjustHoles() {
        int sum = -1, oldSize = -1;
        boolean loop = true;
        while (loop) {
            loop = false;
            for (int address :
                    holes.keySet()) {

                if ((sum == address) && (holes.get(address).getKey().equals("hole"))) {
                    System.out.println("Put hole at " + (sum - oldSize) + " and size is " + (oldSize + holes.get(address).getValue()));
                    holes.put(sum - oldSize, new Pair<>("hole", (oldSize + holes.get(address).getValue())));
                    holes.remove(address);
                    loop = true;
                    break;
                }

                if (holes.get(address).getKey().equals("hole")) {
                    sum = address + holes.get(address).getValue();
                    oldSize = holes.get(address).getValue();
                }


            }
        }
    }

    public void printProcesses() {
        for (Process process : processes) {
            System.out.println("For processes : " + process.getProcessNumber());
            for (String s :
                    process.getSegments().keySet()) {
                System.out.println("Segment " + s + " size is : " + process.getSegments().get(s).getKey() + " and located at : " + process.getSegments().get(s)
                        .getValue());
            }
        }

    }

    public void printHoles() {
        System.out.println("***********************************************************");
        for (int address : holes.keySet()
        ) {
            System.out.println("Hole at address " + address + " size is " + holes.get(address));
        }
        System.out.println("***********************************************************");

    }

    public void printHoles(Map<Integer, Pair<String, Integer>> tempHoles) {
        System.out.println("***********************************************************");
        for (int address : tempHoles.keySet()
        ) {
            System.out.println("Hole at address " + address + " size is " + tempHoles.get(address));
        }
        System.out.println("***********************************************************");
    }

    public void printReserves() {
        for (int address : reserved.keySet()
        ) {
            System.out.println("Reserve at address " + address + " size is " + reserved.get(address));
        }
    }

    public void printMemory() {
        for (int address :
                memory.keySet()) {
            System.out.println(memory.get(address).getKey() + " at address " + address + " size is " + memory.get(address).getValue());
        }
        System.out.println("=========================================================================================" +
                "\n======================================================================");
    }


}
