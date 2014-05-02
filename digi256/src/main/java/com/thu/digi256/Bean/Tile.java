package com.thu.digi256.Bean;

import java.util.Random;

public class Tile extends Cell{
    private int value;
    private Tile[] mergedFrom = null;
    private boolean IsQuestion;
    private boolean IsObstacle;

    public Tile(int x, int y, int value) {
        super(x, y);
        this.value = value;
        IsQuestion = new Random().nextBoolean();
    }

    public Tile(Cell cell, int value) {
        super(cell.getX(), cell.getY());
        this.value = value;
        IsQuestion = new Random().nextBoolean();
    }

    public void updatePosition(Cell cell) {
        this.setX(cell.getX());
        this.setY(cell.getY());
    }

    public int getValue() {
        return this.value;
    }

    public Tile[] getMergedFrom() {
       return mergedFrom;
    }

    public void setMergedFrom(Tile[] tile) {
        mergedFrom = tile;
    }


    public boolean isQuestion() {
        return IsQuestion;
    }

    public void setQuestion(boolean isQusetion) {
        IsQuestion = isQusetion;
    }

    public boolean isObstacle() {
        return IsObstacle;
    }

    public void setObstacle(boolean isObstacle) {
        IsObstacle = isObstacle;
    }
}
