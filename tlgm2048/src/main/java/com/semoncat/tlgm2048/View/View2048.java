package com.semoncat.tlgm2048.View;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.semoncat.tlgm2048.Bean.MainGame;
import com.semoncat.tlgm2048.Bean.Tile;
import com.semoncat.tlgm2048.Input.GestureListener;
import com.semoncat.tlgm2048.Input.GestureListener;
import com.semoncat.tlgm2048.R;

import java.util.ArrayList;

/**
 * Created by SemonCat on 2014/3/31.
 */
public class View2048 extends View {

    Paint paint = new Paint();
    public MainGame game;
    public boolean hasSaveState = false;

    boolean getScreenSize = true;
    int cellSize = 0;
    float textSize = 0;
    int gridWidth = 0;
    int screenMiddleX = 0;
    int screenMiddleY = 0;
    int boardMiddleX = 0;
    int boardMiddleY = 0;
    Drawable backgroundRectangle;
    Drawable[] cellRectangle;
    BitmapDrawable[] bitmapCell;
    String[] valueArray;
    Drawable settingsIcon;
    Drawable lightUpRectangle;
    Drawable fadeRectangle;
    Bitmap background = null;
    int TEXT_BLACK;
    int TEXT_WHITE;
    int TEXT_BROWN;


    int halfNumSquaresX;
    int halfNumSquaresY;

    int startingX;
    int startingY;
    int endingX;
    int endingY;

    int sYAll;
    int titleStartYAll;
    int bodyStartYAll;
    int eYAll;
    int titleWidthHighScore;
    int titleWidthScore;

    public static int sYIcons;
    public static int sXNewGame;
    public static int sXRedo;
    public static int sXTime;
    public static int bodyWidthRedo;

    public static int iconSize;
    long lastFPSTime = System.nanoTime();
    long currentTime = System.nanoTime();

    float titleTextSize;
    float bodyTextSize;
    float headerTextSize;
    float instructionsTextSize;
    float gameOverTextSize;

    public boolean refreshLastTime = true;

    public static final int BASE_ANIMATION_TIME = 100000000;
    public static int textPaddingSize = 0;
    public static int iconPaddingSize = 0;

    public static final float MERGING_ACCELERATION = (float) -0.5;
    public static final float INITIAL_VELOCITY = (1 - MERGING_ACCELERATION) / 4;


    @Override
    public void onDraw(Canvas canvas) {
        //Reset the transparency of the screen

        canvas.drawBitmap(background, 0, 0, paint);

        //drawScoreText(canvas);

        /*
        if ((game.won || game.lose) && !game.aGrid.isAnimationActive()) {
            drawNewGameButton(canvas);
        }
        */

        //drawRedoButton(canvas);
        //drawTime(canvas);

        drawCells(canvas);

        drawEndGameState(canvas);

        //Refresh the screen if there is still an animation running

        if (game.aGrid==null) return;
        if (game.aGrid.isAnimationActive()) {
            invalidate(startingX, startingY, endingX, endingY);
            tick();
            //Refresh one last time on game end.
        } else if ((game.won || game.lose) && refreshLastTime) {
            invalidate();
            refreshLastTime = false;
        }


    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        getLayout(width, height);
        createBackgroundBitmap(width, height);
        createBitmapCells();
    }

    public void drawDrawable(Canvas canvas, Drawable draw, int startingX, int startingY, int endingX, int endingY) {
        draw.setBounds(startingX, startingY, endingX, endingY);
        draw.draw(canvas);
    }

    public void drawCellText(Canvas canvas, int index, int sX, int sY) {
        int textShiftY = centerText();
        if (index >= 2) {
            paint.setColor(TEXT_WHITE);
        } else {
            paint.setColor(TEXT_BLACK);
        }


        canvas.drawText(valueArray[index], sX + cellSize / 2, sY + cellSize / 2 - textShiftY, paint);

    }

    public void drawScoreText(Canvas canvas) {
        //Drawing the score text: Ver 2
        paint.setTextSize(bodyTextSize);
        paint.setTextAlign(Paint.Align.CENTER);

        //int bodyWidthHighScore = (int) (paint.measureText(String.valueOf(game.highScore)));
        //int bodyWidthScore = (int) (paint.measureText(String.valueOf(game.score)));

        int bodyWidthHighScore = (int) (paint.measureText("00000"));
        int bodyWidthScore = (int) (paint.measureText("00000"));

        int textWidthHighScore = Math.max(titleWidthHighScore, bodyWidthHighScore) + textPaddingSize * 2;
        int textWidthScore = Math.max(titleWidthScore, bodyWidthScore) + textPaddingSize * 2;

        int textMiddleHighScore = textWidthHighScore / 2;
        int textMiddleScore = textWidthScore / 2;

        int eXHighScore = endingX;
        int sXHighScore = eXHighScore - textWidthHighScore;

        int eXScore = sXHighScore - textPaddingSize;
        int sXScore = eXScore - textWidthScore;

        //Outputting high-scores box
        backgroundRectangle.setBounds(sXHighScore, sYAll, eXHighScore, eYAll);
        backgroundRectangle.draw(canvas);
        paint.setTextSize(titleTextSize);
        paint.setColor(TEXT_BROWN);
        canvas.drawText(getResources().getString(R.string.high_score), sXHighScore + textMiddleHighScore, titleStartYAll, paint);
        paint.setTextSize(bodyTextSize);
        paint.setColor(TEXT_WHITE);
        canvas.drawText(String.valueOf(game.highScore), sXHighScore + textMiddleHighScore, bodyStartYAll, paint);


        //Outputting scores box
        backgroundRectangle.setBounds(sXScore, sYAll, eXScore, eYAll);
        backgroundRectangle.draw(canvas);
        paint.setTextSize(titleTextSize);
        paint.setColor(TEXT_BROWN);
        canvas.drawText(getResources().getString(R.string.score), sXScore + textMiddleScore, titleStartYAll, paint);
        paint.setTextSize(bodyTextSize);
        paint.setColor(TEXT_WHITE);
        canvas.drawText(String.valueOf(game.score), sXScore + textMiddleScore, bodyStartYAll, paint);
    }

    public void drawNewGameButton(Canvas canvas) {
        if ((game.won || game.lose)) {
            drawDrawable(canvas, lightUpRectangle, sXNewGame, sYIcons, sXNewGame + iconSize, sYIcons + iconSize);
        } else {
            drawDrawable(canvas, backgroundRectangle, sXNewGame, sYIcons, sXNewGame + iconSize, sYIcons + iconSize);
        }
        drawDrawable(canvas, settingsIcon, sXNewGame + iconPaddingSize, sYIcons + iconPaddingSize,
                sXNewGame + iconSize - iconPaddingSize, sYIcons + iconSize - iconPaddingSize);
    }

    public void drawRedoButton(Canvas canvas) {
        String RedoString = String.format(getResources().getString(R.string.redo), game.mRedoCounter);

        paint.setTextSize(bodyTextSize);
        paint.setTextAlign(Paint.Align.CENTER);

        paint.setColor(TEXT_WHITE);
        bodyWidthRedo = (int) (paint.measureText(RedoString)) + textPaddingSize * 2;
        drawDrawable(canvas, backgroundRectangle, sXRedo, sYIcons, sXRedo + bodyWidthRedo, sYIcons + iconSize);

        canvas.drawText(RedoString, (sXRedo + sXRedo + bodyWidthRedo) / 2, (sYIcons + sYIcons + iconSize + bodyTextSize / 2) / 2, paint);
    }

    public void drawTime(Canvas canvas) {

        paint.setTextSize(bodyTextSize);
        paint.setTextAlign(Paint.Align.CENTER);

        paint.setColor(TEXT_BLACK);

        canvas.drawText(game.getGameTimeString(), sXTime, (sYIcons + sYIcons + iconSize + bodyTextSize / 2) / 2, paint);
    }

    public void drawHeader(Canvas canvas) {
        //Drawing the header
        paint.setTextSize(headerTextSize);
        paint.setColor(TEXT_BLACK);
        paint.setTextAlign(Paint.Align.LEFT);
        int textShiftY = centerText() * 2;
        int headerStartY = sYAll - textShiftY;
        canvas.drawText(getResources().getString(R.string.titleHeader), startingX, headerStartY, paint);
    }

    public void drawInstructions(Canvas canvas) {
        //Drawing the instructions
        paint.setTextSize(instructionsTextSize);
        paint.setTextAlign(Paint.Align.LEFT);
        int textShiftY = centerText() * 2;

        String Instructions = String.format(getResources().getString(R.string.instructions), valueArray[0], valueArray[1]);

        canvas.drawText(Instructions,
                startingX, endingY - textShiftY + textPaddingSize, paint);
    }

    public void drawBackground(Canvas canvas) {
        drawDrawable(canvas, backgroundRectangle, startingX, startingY, endingX, endingY);
    }

    public void drawBackgroundGrid(Canvas canvas) {
        // Outputting the game grid
        for (int xx = 0; xx < game.numSquaresX; xx++) {
            for (int yy = 0; yy < game.numSquaresY; yy++) {
                int sX = startingX + gridWidth + (cellSize + gridWidth) * xx;
                int eX = sX + cellSize;
                int sY = startingY + gridWidth + (cellSize + gridWidth) * yy;
                int eY = sY + cellSize;

                drawDrawable(canvas, cellRectangle[0], sX, sY, eX, eY);
            }
        }
    }

    public void drawCells(Canvas canvas) {
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        // Outputting the individual cells
        for (int xx = 0; xx < game.numSquaresX; xx++) {
            for (int yy = 0; yy < game.numSquaresY; yy++) {
                int sX = startingX + gridWidth + (cellSize + gridWidth) * xx;
                int eX = sX + cellSize;
                int sY = startingY + gridWidth + (cellSize + gridWidth) * yy;
                int eY = sY + cellSize;

                if (game.grid==null) return;
                Tile currentTile = game.grid.getCellContent(xx, yy);
                if (currentTile != null) {
                    //Get and represent the value of the tile
                    int value = currentTile.getValue();
                    int index = log2(value);

                    //Check for any active animations
                    ArrayList<AnimationCell> aArray = game.aGrid.getAnimationCell(xx, yy);
                    boolean animated = false;
                    for (int i = aArray.size() - 1; i >= 0; i--) {
                        AnimationCell aCell = aArray.get(i);
                        //If this animation is not active, skip it
                        if (aCell.getAnimationType() == MainGame.SPAWN_ANIMATION) {
                            animated = true;
                        }
                        if (!aCell.isActive()) {
                            continue;
                        }

                        if (aCell.getAnimationType() == MainGame.SPAWN_ANIMATION) { // Spawning animation
                            double percentDone = aCell.getPercentageDone();
                            float textScaleSize = (float) (percentDone);
                            paint.setTextSize(textSize * textScaleSize);

                            float cellScaleSize = cellSize / 2 * (1 - textScaleSize);
                            bitmapCell[index].setBounds((int) (sX + cellScaleSize), (int) (sY + cellScaleSize), (int) (eX - cellScaleSize), (int) (eY - cellScaleSize));
                            bitmapCell[index].draw(canvas);
                        } else if (aCell.getAnimationType() == MainGame.MERGE_ANIMATION) { // Merging Animation
                            double percentDone = aCell.getPercentageDone();
                            float textScaleSize = (float) (1 + INITIAL_VELOCITY * percentDone
                                    + MERGING_ACCELERATION * percentDone * percentDone / 2);
                            paint.setTextSize(textSize * textScaleSize);

                            float cellScaleSize = cellSize / 2 * (1 - textScaleSize);
                            bitmapCell[index].setBounds((int) (sX + cellScaleSize), (int) (sY + cellScaleSize), (int) (eX - cellScaleSize), (int) (eY - cellScaleSize));
                            bitmapCell[index].draw(canvas);
                        } else if (aCell.getAnimationType() == MainGame.MOVE_ANIMATION) {  // Moving animation
                            double percentDone = aCell.getPercentageDone();
                            int tempIndex = index;
                            if (aArray.size() >= 2) {
                                tempIndex = tempIndex - 1;
                            }
                            int previousX = aCell.extras[0];
                            int previousY = aCell.extras[1];
                            int currentX = currentTile.getX();
                            int currentY = currentTile.getY();
                            int dX = (int) ((currentX - previousX) * (cellSize + gridWidth) * (percentDone - 1) * 1.0);
                            int dY = (int) ((currentY - previousY) * (cellSize + gridWidth) * (percentDone - 1) * 1.0);
                            bitmapCell[tempIndex].setBounds(sX + dX, sY + dY, eX + dX, eY + dY);
                            if (tempIndex < bitmapCell.length) {
                                bitmapCell[tempIndex].draw(canvas);
                            } else {
                                bitmapCell[bitmapCell.length - 1].draw(canvas);
                            }
                        }
                        animated = true;
                    }

                    //No active animations? Just draw the cell
                    if (!animated) {
                        bitmapCell[index].setBounds(sX, sY, eX, eY);
                        if (index < bitmapCell.length) {
                            bitmapCell[index].draw(canvas);
                        } else {
                            bitmapCell[bitmapCell.length - 1].draw(canvas);
                        }
                    }
                }
            }
        }
    }

    public void drawEndGameState(Canvas canvas) {
        double alphaChange = 1;
        //Animation: Dynamically change the alpha
        /*
        for (AnimationCell animation : game.aGrid.globalAnimation) {
            if (animation.getAnimationType() == MainGame.FADE_GLOBAL_ANIMATION) {
                alphaChange = animation.getPercentageDone();
                Log.d("TAG",String.valueOf(alphaChange));
            }

        }
        */
        // Displaying game over
        if (game.won) {
            lightUpRectangle.setAlpha((int) (127 * alphaChange));
            drawDrawable(canvas, lightUpRectangle, startingX, startingY, endingX, endingY);
            lightUpRectangle.setAlpha(255);
            paint.setColor(TEXT_WHITE);
            paint.setAlpha((int) (255 * alphaChange));
            paint.setTextSize(gameOverTextSize);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(getResources().getString(R.string.game_win), boardMiddleX, boardMiddleY - centerText(), paint);
            paint.setAlpha(255);
        } else if (game.lose) {
            fadeRectangle.setAlpha((int) (127 * alphaChange));
            drawDrawable(canvas, fadeRectangle, startingX, startingY, endingX, endingY);
            fadeRectangle.setAlpha(255);
            paint.setColor(TEXT_BLACK);
            paint.setAlpha((int) (255 * alphaChange));
            paint.setTextSize(gameOverTextSize);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(getResources().getString(R.string.game_over), boardMiddleX, boardMiddleY - centerText(), paint);
            paint.setAlpha(255);
        }
    }

    public void createBackgroundBitmap(int width, int height) {
        background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(background);
        //drawHeader(canvas);
        //drawNewGameButton(canvas);
        drawBackground(canvas);
        drawBackgroundGrid(canvas);
        //drawInstructions(canvas);

    }

    public void createBitmapCells() {
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        Resources resources = getResources();
        for (int xx = 0; xx < bitmapCell.length; xx++) {
            Bitmap bitmap = Bitmap.createBitmap(cellSize, cellSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawDrawable(canvas, cellRectangle[xx], 0, 0, cellSize, cellSize);
            //drawCellText(canvas, (int) Math.pow(2, xx), 0, 0);
            if (xx != 0) {
                drawCellText(canvas, xx - 1, 0, 0);
            }
            bitmapCell[xx] = new BitmapDrawable(resources, bitmap);
        }
    }

    public void tick() {
        currentTime = System.nanoTime();
        game.aGrid.tickAll(currentTime - lastFPSTime);
        lastFPSTime = currentTime;
    }

    public void resyncTime() {
        lastFPSTime = System.nanoTime();
    }

    public static int log2(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    /**
     * @see android.view.View#measure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int minSize = widthSpecSize;

        if (heightMeasureSpec<widthMeasureSpec){
            minSize = heightSpecSize;
        }

        setMeasuredDimension(measureHeight(minSize),
                measureHeight(minSize));
    }

    /**
     * Determines the height of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            getLayout(specSize,specSize);
            result = endingY;
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


    public void getLayout(int width, int height) {
        cellSize = Math.min(width / (game.numSquaresX + 1), height / (game.numSquaresY + 3));
        cellSize = cellSize*150/100;
        gridWidth = cellSize / 7;
        screenMiddleX = width / 2;
        screenMiddleY = height / 2;
        boardMiddleX = screenMiddleX;
        boardMiddleY = screenMiddleY + cellSize / 2;
        iconSize = cellSize / 2;

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(cellSize);
        textSize = cellSize * cellSize / Math.max(cellSize, paint.measureText("0000"));
        titleTextSize = textSize / 3;
        bodyTextSize = (int) (textSize / 1.5);
        instructionsTextSize = (int) (textSize / 1.5);
        headerTextSize = textSize * 2;
        gameOverTextSize = textSize * 2;
        textPaddingSize = (int) (textSize / 3);
        iconPaddingSize = (int) (textSize / 5);

        //Grid Dimensions
        halfNumSquaresX = game.numSquaresX / 2;
        halfNumSquaresY = game.numSquaresY / 2;

        //startingX = boardMiddleX - (cellSize + gridWidth) * halfNumSquaresX - gridWidth / 2;
        startingX = 0;
        //endingX = boardMiddleX + (cellSize + gridWidth) * halfNumSquaresX + gridWidth / 2;
        endingX = ((cellSize + gridWidth) * halfNumSquaresX + gridWidth / 2)*2;
        //startingY = boardMiddleY - (cellSize + gridWidth) * halfNumSquaresY - gridWidth / 2;
        startingY = 0;
        //endingY = boardMiddleY + (cellSize + gridWidth) * halfNumSquaresY + gridWidth / 2;
        endingY = ((cellSize + gridWidth) * halfNumSquaresY + gridWidth / 2)*2;

        paint.setTextSize(titleTextSize);

        int textShiftYAll = centerText();
        //static variables
        sYAll = (int) (startingY - cellSize * 1.5);
        titleStartYAll = (int) (sYAll + textPaddingSize + titleTextSize / 2 - textShiftYAll);
        bodyStartYAll = (int) (titleStartYAll + textPaddingSize + titleTextSize / 2 + bodyTextSize / 2);

        titleWidthHighScore = (int) (paint.measureText("HIGH SCORE"));
        titleWidthScore = (int) (paint.measureText("SCORE"));
        paint.setTextSize(bodyTextSize);
        textShiftYAll = centerText();
        eYAll = (int) (bodyStartYAll + textShiftYAll + bodyTextSize / 2 + textPaddingSize);

        sYIcons = (startingY + eYAll) / 2 - iconSize / 2;
        sXNewGame = (endingX - iconSize);
        sXRedo = startingX;
        sXTime = (startingX + endingX) / 2;
        resyncTime();
        getScreenSize = false;
    }

    public int centerText() {
        return (int) ((paint.descent() + paint.ascent()) / 2);
    }

    public View2048(Context context) {
        super(context);

        init(context);
    }

    public View2048(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public View2048(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        Resources resources = context.getResources();
        //Loading resources

        game = new MainGame(context, this);
        try {

            valueArray = new String[]{
                    "2", "4", "8", "16", "32", "64", "128", "256",
                    "512", "1024", "2048", "4096", "8192"};

            valueArray = new String[]{
                    "無", "不", "在", "社", "所", "群", "溫", "庭",
                    "立", "李", "宗", "熙", "周"};

            backgroundRectangle = resources.getDrawable(R.drawable.background_rectangle);
            cellRectangle = new Drawable[]{
                    resources.getDrawable(R.drawable.cell_rectangle),
                    resources.getDrawable(R.drawable.cell_rectangle_2),
                    resources.getDrawable(R.drawable.cell_rectangle_4),
                    resources.getDrawable(R.drawable.cell_rectangle_8),
                    resources.getDrawable(R.drawable.cell_rectangle_16),
                    resources.getDrawable(R.drawable.cell_rectangle_32),
                    resources.getDrawable(R.drawable.cell_rectangle_64),
                    resources.getDrawable(R.drawable.cell_rectangle_128),
                    resources.getDrawable(R.drawable.cell_rectangle_256),
                    resources.getDrawable(R.drawable.cell_rectangle_512),
                    resources.getDrawable(R.drawable.cell_rectangle_1024),
                    resources.getDrawable(R.drawable.cell_rectangle_2048),
                    resources.getDrawable(R.drawable.cell_rectangle_4096),
                    resources.getDrawable(R.drawable.cell_rectangle_8192)};
            bitmapCell = new BitmapDrawable[cellRectangle.length];
            settingsIcon = resources.getDrawable(R.drawable.ic_action_refresh);
            lightUpRectangle = resources.getDrawable(R.drawable.light_up_rectangle);
            fadeRectangle = resources.getDrawable(R.drawable.fade_rectangle);
            TEXT_WHITE = resources.getColor(R.color.text_white);
            TEXT_BLACK = resources.getColor(R.color.text_black);
            TEXT_BROWN = resources.getColor(R.color.text_brown);
            this.setBackgroundColor(resources.getColor(R.color.background));
            Typeface font = Typeface.createFromAsset(resources.getAssets(), "ClearSans-Bold.ttf");
            paint.setTypeface(font);
        } catch (AssertionError e) {


        }
        setOnTouchListener(new GestureListener(this));
        //game.newGame();

        setBackgroundColor(Color.TRANSPARENT);
    }


    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            invalidate();
            postDelayed(this, 500);

        }
    };

    public void pauseUpdate(){
        removeCallbacks(mRunnable);
    }

    public void resumeUpdate(){
        if (!(game.won || game.lose)){
            post(mRunnable);
        }
    }
}
