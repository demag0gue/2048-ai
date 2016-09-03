package de.timgerdes.ai.control;

public enum Direction {
    LEFT(3),
    UP(0),
    DOWN(2),
    RIGHT(1);

    private int code; //order of game's direction is: Up, Right, Down, Left (don't ask me why, I'd rather use AWSD)
    Direction(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
