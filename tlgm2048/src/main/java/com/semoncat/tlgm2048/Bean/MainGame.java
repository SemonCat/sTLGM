package com.semoncat.tlgm2048.Bean;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.semoncat.tlgm2048.Utils.CircularStack;
import com.semoncat.tlgm2048.View.AnimationGrid;
import com.semoncat.tlgm2048.View.View2048;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainGame {
    public interface GameEventListener{
        void OnGameStart();
        void OnScoreGet(int score);
        void OnScoreChange(long score);
        void OnHighScoreChange(long score);
        void OnGameOver();
    }

    private CircularStack<Tile[][]> mRedoStack;

    public static final int MAX_REDO_COUNTER = 2;

    public int mRedoCounter;

    private long startTime;

    private GameEventListener mListener;

    public Grid grid;
    public AnimationGrid aGrid;
    public final int numSquaresX = 4;
    public final int numSquaresY = 4;
    public final int startTiles = 2;

    public long score = 0;
    public long highScore = 0;
    public boolean won = false;
    public boolean lose = false;

    Context mContext;

    View2048 mView;

    public static final int SPAWN_ANIMATION = -1;
    public static final int MOVE_ANIMATION = 0;
    public static final int MERGE_ANIMATION = 1;

    public static final int FADE_GLOBAL_ANIMATION = 0;

    static final long MOVE_ANIMATION_TIME = View2048.BASE_ANIMATION_TIME;
    static final long SPAWN_ANIMATION_TIME = View2048.BASE_ANIMATION_TIME;
    static final long NOTIFICATION_ANIMATION_TIME = View2048.BASE_ANIMATION_TIME * 5;
    static final long NOTIFICATION_DELAY_TIME = MOVE_ANIMATION_TIME + SPAWN_ANIMATION_TIME;
    static final String HIGH_SCORE = "high score";

    public MainGame(Context context, View2048 view) {
        mContext = context;
        mView = view;
    }

    public void newGame() {
        mRedoStack = new CircularStack<Tile[][]>(MAX_REDO_COUNTER);
        mRedoCounter = MAX_REDO_COUNTER;

        startTime = System.currentTimeMillis();

        grid = new Grid(numSquaresX, numSquaresY);
        aGrid = new AnimationGrid(numSquaresX, numSquaresY);
        highScore = getHighScore();
        if (score >= highScore) {
            highScore = score;
            recordHighScore();
        }
        score = 0;
        won = false;
        lose = false;
        addStartTiles();
        mView.refreshLastTime = true;
        mView.resyncTime();
        mView.postInvalidate();

        if (mListener!=null)
            mListener.OnGameStart();
    }

    public void addStartTiles() {
        for (int xx = 0; xx < startTiles; xx++) {
            this.addRandomTile();
        }
    }

    public void addRandomTile() {
        if (grid.isCellsAvailable()) {
            int value = Math.random() < 0.9 ? 2 : 4;
            Tile tile = new Tile(grid.randomAvailableCell(), value);
            grid.insertTile(tile);
            aGrid.startAnimation(tile.getX(), tile.getY(), SPAWN_ANIMATION,
                    SPAWN_ANIMATION_TIME, MOVE_ANIMATION_TIME, null); //Direction: -1 = EXPANDING
        }
    }

    public void recordHighScore() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(HIGH_SCORE, highScore);
        editor.commit();
    }

    public long getHighScore() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
        return settings.getLong(HIGH_SCORE, -1);
    }

    public void prepareTiles() {
        for (Tile[] array : grid.field) {
            for (Tile tile : array) {
                if (grid.isCellOccupied(tile)) {
                    tile.setMergedFrom(null);
                }
            }
        }
    }

    public void moveTile(Tile tile, Cell cell) {
        grid.field[tile.getX()][tile.getY()] = null;
        grid.field[cell.getX()][cell.getY()] = tile;
        tile.updatePosition(cell);
    }

    /**
     * move cell.
     * @param direction 0: up, 1: right, 2: down, 3: left
     */
    public void move (int direction) {
        aGrid.cancelAnimations();
        // 0: up, 1: right, 2: down, 3: left
        if (lose || won) {
            return;
        }

        //備份
        backupTiles();

        Cell vector = getVector(direction);
        List<Integer> traversalsX = buildTraversalsX(vector);
        List<Integer> traversalsY = buildTraversalsY(vector);
        boolean moved = false;

        prepareTiles();

        for (int xx: traversalsX) {
            for (int yy: traversalsY) {
                Cell cell = new Cell(xx, yy);
                Tile tile = grid.getCellContent(cell);

                if (tile != null) {
                    Cell[] positions = findFarthestPosition(cell, vector);
                    Tile next = grid.getCellContent(positions[1]);

                    if (next != null && next.getValue() == tile.getValue() && next.getMergedFrom() == null) {
                        Tile merged = new Tile(positions[1], tile.getValue() * 2);
                        Tile[] temp = {tile, next};
                        merged.setMergedFrom(temp);

                        grid.insertTile(merged);
                        grid.removeTile(tile);

                        // Converge the two tiles' positions
                        tile.updatePosition(positions[1]);

                        int[] extras = {xx, yy};
                        aGrid.startAnimation(merged.getX(), merged.getY(), MOVE_ANIMATION,
                                MOVE_ANIMATION_TIME, 0, extras); //Direction: 0 = MOVING MERGED
                        aGrid.startAnimation(merged.getX(), merged.getY(), MERGE_ANIMATION,
                                SPAWN_ANIMATION_TIME, MOVE_ANIMATION_TIME, null);

                        // Update the score
                        score = score + merged.getValue();
                        highScore = Math.max(score, highScore);

                        if (mListener!=null) {
                            mListener.OnScoreGet(merged.getValue());
                            mListener.OnScoreChange(score);
                            mListener.OnHighScoreChange(highScore);
                        }

                        // The mighty 8192 tile
                        if (merged.getValue() == 8192) {
                            won = true;
                            endGame();
                        }

                    } else {
                        moveTile(tile, positions[0]);
                        int[] extras = {xx, yy, 0};
                        aGrid.startAnimation(positions[0].getX(), positions[0].getY(), MOVE_ANIMATION, MOVE_ANIMATION_TIME, 0, extras); //Direction: 1 = MOVING NO MERGE
                    }

                    if (!positionsEqual(cell, tile)) {
                        moved = true;
                    }
                }
            }
        }

        if (moved) {
            addRandomTile();

            if (!movesAvailable()) {
                lose = true;
                endGame();
            }

        }
        mView.resyncTime();
        mView.postInvalidate();
    }

    public void endGame() {
        aGrid.startAnimation(-1, -1, FADE_GLOBAL_ANIMATION, NOTIFICATION_ANIMATION_TIME, NOTIFICATION_DELAY_TIME, null);
        if (score >= highScore) {
            highScore = score;
            recordHighScore();
        }

        if (mListener!=null)
            mListener.OnGameOver();
    }

    public Cell getVector(int direction) {
        Cell[] map = {
                new Cell(0, -1), // up
                new Cell(1, 0),  // right
                new Cell(0, 1),  // down
                new Cell(-1, 0)  // left
        };
        return map[direction];
    }

    public List<Integer> buildTraversalsX(Cell vector) {
        List<Integer> traversals = new ArrayList<Integer>();

        for (int xx = 0; xx < numSquaresX; xx++) {
            traversals.add(xx);
        }
        if (vector.getX() == 1) {
            Collections.reverse(traversals);
        }

       return traversals;
    }

    public List<Integer> buildTraversalsY(Cell vector) {
        List<Integer> traversals = new ArrayList<Integer>();

        for (int xx = 0; xx <numSquaresY; xx++) {
            traversals.add(xx);
        }
        if (vector.getY() == 1) {
            Collections.reverse(traversals);
        }

        return traversals;
    }

    public Cell[] findFarthestPosition(Cell cell, Cell vector) {
        Cell previous;
        Cell nextCell = new Cell(cell.getX(), cell.getY());
        do {
            previous = nextCell;
            nextCell = new Cell(previous.getX() + vector.getX(),
                    previous.getY() + vector.getY());
        } while (grid.isCellWithinBounds(nextCell) && grid.isCellAvailable(nextCell));

        Cell[] answer = {previous, nextCell};
        return answer;
    }
    public boolean movesAvailable() {
        return grid.isCellsAvailable() || tileMatchesAvailable();
    }
    public boolean tileMatchesAvailable() {
        Tile tile;

        for (int xx = 0; xx < numSquaresX; xx++) {
            for (int yy = 0; yy < numSquaresY; yy++) {
                tile = grid.getCellContent(new Cell(xx, yy));

                if (tile != null) {
                    for (int direction = 0; direction < 4; direction++) {
                        Cell vector = getVector(direction);
                        Cell cell = new Cell(xx + vector.getX(), yy + vector.getY());

                        Tile other = grid.getCellContent(cell);

                        if (other != null && other.getValue() == tile.getValue()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private void backupTiles(Tile[][] field){
        int width = field.length;
        int height = field[0].length;
        Tile[][] mField = new Tile[width][height];

        for (int xx = 0; xx < width; xx++) {
            for (int yy = 0; yy < height; yy++) {
                Tile mTile = field[xx][yy];
                if (mTile!=null){
                    mField[xx][yy] = new Tile(mTile.getX(),mTile.getY(),mTile.getValue());
                }else {
                    mField[xx][yy] = null;
                }
            }
        }

        mRedoStack.push(mField);
    }

    private void backupTiles(){
        backupTiles(grid.field);
    }

    public void restoreTiles(){

        if (!mRedoStack.empty() && mRedoCounter!=0){
            mRedoCounter--;

            Tile[][] mField = mRedoStack.pop();

            grid.field = mField;


            mView.resyncTime();
            mView.postInvalidate();
        }
    }

    public CircularStack<Tile[][]> getBackupTiles(){

        CircularStack<Tile[][]> mStack = new CircularStack<Tile[][]>(MAX_REDO_COUNTER);
        mStack.addAll(mRedoStack);
        return mStack;
    }

    public void setBackupTiles(CircularStack<Tile[][]> redoStack){
        this.mRedoStack = redoStack;
    }


    public boolean positionsEqual(Cell first, Cell second) {
        return first.getX() == second.getX() && first.getY() == second.getY();
    }

    public long getGameTime(){
        return System.currentTimeMillis()-startTime;
    }

    public String getGameTimeString(){
        return new SimpleDateFormat("mm:ss").format(new Date(getGameTime()));
    }

    public void setTime(long Time){
        this.startTime = System.currentTimeMillis()-Time;
    }

    public void setListener(GameEventListener mListener) {
        this.mListener = mListener;
    }
}
