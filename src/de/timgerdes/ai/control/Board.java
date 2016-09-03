package de.timgerdes.ai.control;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    private int[][] board;
    private int score;
    private Integer emptyCells = null;

    public Board(int[][] board) {
        this.board = board;
    }

    public int[][] toArray() {
        return board;
    }

    @Override
    public Board clone() {
        Board b = new Board(board);
        b.setScore(getScore());

        return b;
    }

    /**
     * Returns a cell's value
     * @param x coordinate
     * @param y coordinate
     * @return
     */
    public int getCell(int x, int y) {
        return board[x][y];
    }

    /**
     * Set a cell's value
     * @param x coordinate
     * @param y coordinate
     * @param value
     */
    public void setCell(int x, int y, int value) {
        board[x][y] = value;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    /**
     * To get the x and y coordinates, use the following calculation:
     * x = value/4
     * y= value%4
     * @return ArrayList with the coordinates
     */
    public ArrayList<Coordinate> getEmptyCells() {
        ArrayList<Coordinate> cells = new ArrayList<>();
        for(int x = 0; x < 4; x++) {
            for(int y = 0; y < 4; y++) {
                if(board[x][y] == 0)
                    cells.add(new Coordinate(x,y));
            }
        }

        emptyCells = cells.size();

        return cells;
    }

    /**
     * Returns the amount of empty cells
     * @return amount of empty cells
     */
    public int countEmptyCells() {
        return (emptyCells != null) ? emptyCells : getEmptyCells().size();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns a _NEW_ Board
     * @param direction
     * @return
     */
    public Board move(Direction direction) {
        int[][] workBoard = board.clone();
        int times = 0;
        int points = 0;
        boolean left = true;

        switch(direction) {
            case UP:
                times = 0;
                break;
            case LEFT:
                times = 1;
                left = true;
                break;
            case RIGHT:
                times = 1;
                left = false;
                break;
            case DOWN:
                times = 2;
                left = false;
                break;
        }

        if(times != 0)
            for(int i = 0; i < times; i++)
                workBoard = rotateArray(workBoard, left);

        for(int x = 0; x < 4 ; x++) {
            int lastMergePosition = 0;
            for(int y = 1; y < 4; y++) {

                if(workBoard[x][y] == 0)
                    continue;

                int previousPosition = y - 1;
                while(previousPosition > lastMergePosition && workBoard[x][previousPosition] == 0)
                    previousPosition--;

                if(previousPosition == y)
                    continue;

                if(workBoard[x][previousPosition] == 0) {
                    workBoard[x][previousPosition] = workBoard[x][y];
                    workBoard[x][y] = 0;
                } else if(workBoard[x][previousPosition] == workBoard[x][y]){
                    workBoard[x][previousPosition] *= 2;
                    workBoard[x][y] = 0;
                    points += workBoard[x][previousPosition];
                    lastMergePosition = previousPosition + 1;
                } else if(workBoard[x][previousPosition] != workBoard[x][y] && previousPosition +1 != y){
                    workBoard[x][previousPosition + 1] = workBoard[x][y];
                    workBoard[x][y] = 0;
                }
            }
        }

        if(times != 0)
            for(int i = 0; i < times; i++)
                workBoard = rotateArray(workBoard, !left); //rotate back

        Board board = new Board(workBoard);
        board.setScore(getScore() + points);

        return board;
    }

    /**
     * Rotates the array
     * @param array array to rotate
     * @param times how often to rotate
     * @param left otherwise it's right
     * @return
     */
    private int[][] rotateArray(int[][] array, boolean left) {
        int[][] rotated = new int[4][4];

        for(int x = 0; x < 4; x++) {
            for(int y = 0; y < 4; y++) { //loopception ♪ヽ( ⌒o⌒)人(⌒-⌒ )v ♪
                if(left)
                    rotated[3 - y][x] = array[x][y]; // array size - x - 1 = 3 - x
                else
                    rotated[x][y] = array[3 - y][x];
            }
        }

        return rotated;
    }

    /**
     * Compares this board with another board
     * @param compare
     * @return
     */
    public boolean isEqual(Board compare) {
        return Arrays.deepEquals(board, compare.toArray());
    }

}
