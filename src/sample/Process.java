/*
 * Copyright (c) 2020. Mohamed Essam Abdelfattah
 */

package sample;

import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Process {
    private Map<String, Pair<Integer, Integer>> segments;
    private boolean allocated;
    private int processNumber;
    private Color color;


    public Process(int processNumber) {
        this.processNumber = processNumber;
        allocated = false;
    }

    public String getColor() {
        return color.toString();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Map<String, Pair<Integer, Integer>> getSegments() {
        return segments;
    }

    public void setSegments(Map<String, Pair<Integer, Integer>> segments) {
        this.segments = new HashMap<>(segments);
    }

    public boolean isAllocated() {
        return allocated;
    }

    public void setAllocated(boolean allocated) {
        this.allocated = allocated;
    }

    public int getProcessNumber() {
        return processNumber;
    }

    public void addSegment(String name, int size, int address) {
        segments.put(name, new Pair<>(size, address));
    }

    public boolean checkAllocated() {
        for (String segment :
                segments.keySet()) {
            if (segments.get(segment).getValue() != -1) {
                return true;
            }
        }
        return false;
    }
}
