package lineMatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.linematch.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by JimmyLi on 2014/4/22.
 */
public class lineMatchView extends View {
    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, boxPaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap,tempBitmap,srcBitmap;
    //brush sizes
    private float brushSize;

    private int viewHeight=666;
    private int viewWidth=666;
    private ArrayList<BoxObject> answerList=new ArrayList<BoxObject>();


    private int xSize=5,ySize=5;
    float eachWidth=30;
    float eachHeight=30;

    boolean isStart=false;

    public void setStart(boolean isStart) {
        this.isStart = isStart;
    }
    //    private String questionStringArray[][];
//    private String questionWordArray[][];
//    private int answerBaseColor[][];

    //    private int questionPicArray[][];
//    private int answerArray[][];
    private int RedHexColor = Color.RED;
    private int BlueHexColor = Color.BLUE;
    private int YellowHexColor = Color.YELLOW;
    private int CyanHexColor = Color.CYAN;
    private int GreenHexColor = Color.GREEN;


    GridObject questionArray[][];
    GridObject answerArray[][];

    //boolean isRunPath[][]=new boolean[ySize][xSize];
    private int nowColor;
    private int picResourceArray[]={R.drawable.obj_o_num00,R.drawable.obj_o_num01,
            R.drawable.obj_o_num02,R.drawable.obj_o_num03,R.drawable.obj_o_num04,
            R.drawable.obj_o_num05,R.drawable.obj_o_num06,R.drawable.obj_o_num07,
            R.drawable.obj_o_num08,R.drawable.obj_o_num09};
    //private int colorArray[]={RedHexColor,BlueHexColor,YellowHexColor,CyanHexColor,GreenHexColor};

    private int directionResource[]={R.drawable.arrow_up,R.drawable.arrow_right,R.drawable.arrow_down,R.drawable.arrow_left};
    private int directionTouchResource[]={R.drawable.arrow_up_touch,R.drawable.arrow_right_touch,R.drawable.arrow_down_touch,R.drawable.arrow_left_touch};

    private HashMap<Integer,Bitmap> mBitmapHashMap = new HashMap<Integer, Bitmap>();

    private ArrayList<Integer> colorList = new ArrayList<Integer>(Arrays.asList(new Integer[]{GreenHexColor,BlueHexColor,YellowHexColor,CyanHexColor,RedHexColor}));
    Boolean isTouchdown[][];


    public lineMatchView(Context context, AttributeSet attrs){
        super(context, attrs);
        //setupDrawView();
        Collections.shuffle(colorList);





    }



    public void setupDrawView(GridObject questionArray[][]){
        brushSize = 1;
        drawPath = new Path();
        drawPaint = new Paint();
        boxPaint=new Paint();
        boxPaint.setAlpha(32);
        drawPaint.setColor(Color.BLACK);

        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        //boxPaint = new Paint(Paint.DITHER_FLAG);
        this.questionArray=questionArray;
        this.ySize=questionArray.length;
        this.xSize=questionArray[0].length;
//TODO
//        answerArray=new int[ySize][xSize];
//        answerBaseColor=new int[ySize][xSize];
//        questionPicArray=new int[ySize][xSize];
//        questionWordArray=new String[ySize][xSize];
        answerArray=new GridObject[ySize][xSize];
        isTouchdown=new Boolean[ySize][xSize];
        nowColor=RedHexColor;
        spiltQuestionResource();

        setDrawingCacheEnabled(true);





    }
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        //drawCanvas = new Canvas(canvasBitmap);

        viewHeight=h;
        viewWidth=w;
        eachWidth=(viewWidth-brushSize*2)/xSize;
        eachHeight=(viewHeight-brushSize*2)/ySize;
//        Log.d("onSizeChanged","onSizeChanged");
//        Log.d("view","viewHeight: "+String.valueOf(viewHeight)+" viewWidth: "+String.valueOf(viewWidth));
    }
    //    TODO
    protected void onDraw(Canvas canvas) {
        if (isInEditMode()) return;
        //canvas.drawBitmap(canvasBitmap, 0, 0, drawPaint);
        //canvas.drawPath(drawPath, drawPaint);
        //
        //Log.d("onDraw","onDraw");
        drawGird(canvas);
        drawAnswerBaseArray(canvas);
        drawQuestionArray(canvas);
        drawAnswerArray(canvas);
//        drawBaseLine(canvas);
    }
    private void spiltQuestionResource(){
//        Log.d("","In spilt" +
//                "QuestionResource");
        int count=0;
        String mString[];
        Point p=new Point();
        if(questionArray.length>0&&questionArray[0].length>0){
//            for(int i=0;i<questionStringArray.length;i++){
//                for(int j=0;j<questionStringArray[0].length;j++){
//                    if(questionStringArray[i][j]!=""&&questionStringArray[i][j]!=null){
//                        mString=questionStringArray[i][j].split(",");
//                        if (Integer.valueOf(mString[0])==1){
//                            questionPicArray[i][j]=-1;
//                        }else {
//                            questionPicArray[i][j]=Integer.valueOf(mString[0]);
//                        }
//
//                        answerArray[i][j]=Integer.valueOf(mString[1]);
//                        questionWordArray[i][j]=mString[2];
//                        answerBaseColor[i][j]=colorList.get(count);
//                        count++;
//                        Log.d("",questionWordArray[i][j]+" ");
//                    }else {
//                        questionPicArray[i][j]=-1;
//                        answerArray[i][j]=0;
//                        questionWordArray[i][j]="";
//                        answerBaseColor[i][j]=0;
//                    }
//
//                }
//            }

            HashSet<BoxObject> tempHash=new HashSet<BoxObject>();
            for(int i=0;i<questionArray.length;i++){
                for(int j=0;j<questionArray[0].length;j++){
                    answerArray[i][j]=new GridObject();
                    isTouchdown[i][j]=false;
                    if(!questionArray[i][j].getAnswerString().equals("")&&questionArray[i][j].isQuestion()
                            &&!questionArray[i][j].getAnswerString().equals("X")){
                        BoxObject mBox = new BoxObject(j,i,questionArray[i][j].getAnswerString());
                        tempHash.add(mBox);
//                        Log.d("spilt","("+j+","+i+")"+" , string ="+questionArray[i][j].getAnswerString());
                        answerArray[i][j].setGridColor(colorList.get(count));
                        answerArray[i][j].setDirection(questionArray[i][j].getDirection());
                        count++;
                    }else {
                        answerArray[i][j].setGridColor(-1);
                        answerArray[i][j].setDirection(0);

                    }
                }
            }

            answerList = new ArrayList<BoxObject>(tempHash);
//
//            for (BoxObject mBox:answerList){
//                Log.d("",mBox.toString());
//            }
        }

    }

    private int[] LocationToIndex(float touchX,float touchY){
        //int xIndex,yIndex;
        int mLocation[]=new int[2];
        mLocation[0]=(int)((touchX+1)/eachWidth);
        mLocation[1]=(int)((touchY+1)/eachHeight);
        //Log.d("Index","yIndex:"+String.valueOf(yIndex)+" xIndex:"+String.valueOf(xIndex));
        //Log.d("touch","touchY:"+String.valueOf(touchY)+" touchX:"+String.valueOf(touchX));
        if(mLocation[0]>=xSize){mLocation[0]=xSize-1;}
        if(mLocation[0]<0){mLocation[0]=0;}
        if(mLocation[1]>=ySize){mLocation[1]=ySize-1;}
        if(mLocation[1]<0){mLocation[1]=0;}
        return mLocation;
    }
    private int[] IndexToLocation(int xIndex,int yIndex){
        int mLocation[]=new int[2];
        mLocation[0]=(int)(xIndex*eachWidth+1);
        mLocation[1]=(int)(yIndex*eachHeight+1);
        return mLocation;
    }
    private void drawAnswerBaseArray(Canvas canvas){
        for(int i=0;i<answerArray.length;i++){
            for(int j=0;j<answerArray[0].length;j++){
                if (answerArray[i][j].getGridColor()!=-1){
                    drawBaseColor(canvas,j,i,answerArray[i][j].getGridColor());
//                    drawPng(canvas,R.drawable.grid_touch,j,i);
                }
                else {
//                    drawPng(canvas,R.drawable.grid,j,i);
                }
            }
        }
    }
    private void drawGird(Canvas canvas){
        if(xSize>0&&ySize>0){
            for(int i=0;i<ySize;i++){
                for(int j=0;j<xSize;j++){
//                    drawPng(canvas, R.drawable.grid, j, i);
                    drawPngSet(canvas, R.drawable.grid, j, i,4);
                }
            }
        }
    }
    public void drawBaseLine(Canvas canvas){
        drawPaint.setColor(Color.BLACK);
        drawPaint.setAlpha(128);
        for (int i=0;i<xSize+1;i++){
            drawPath.moveTo(i*eachWidth+brushSize, 0);
            drawPath.lineTo(i*eachWidth+brushSize, ySize*eachHeight);
        }
        for (int i=0;i<ySize+1;i++){
            drawPath.moveTo(0, i*eachHeight+brushSize);
            drawPath.lineTo(xSize*eachWidth, i*eachHeight+brushSize);
        }
        canvas.drawPath(drawPath, drawPaint);
    }
    public void drawQuestionArray(Canvas canvas){

        //Log.d("ArraySize",String.valueOf(questionArray[0].length)+","+String.valueOf(questionArray.length));
        if(questionArray.length>0){
            for(int i=0;i<questionArray.length;i++){
                for(int j=0;j<questionArray[0].length;j++){
                    if(questionArray[i][j].isQuestion()){
                        //drawText(gameArray[i][j],i,j);
//                        Log.d("questionPicArray","questionPicArray:"+questionArray[i][j].getGridColor());
                        //drawBaseColor(canvas,j,i,colorArray[count]);

//                        drawPngInside(canvas,questionArray[i][j].getGridColor(),j,i);
                        drawPngSet(canvas,questionArray[i][j].getGridColor(),j,i,-22);
                    }

                }
            }
        }

    }
    public void drawAnswerArray(Canvas canvas){
        //Log.d("ArraySize",String.valueOf(questionArray[0].length)+","+String.valueOf(questionArray.length));
        if(questionArray.length>0){
            for(int i=0;i<questionArray.length;i++){
                for(int j=0;j<questionArray[0].length;j++){
//                    if(isRunPath[i][j]){
//                        Log.d("isRunPath","y,x="+i+","+j);
//                        drawBaseColor(canvas,j,i,Color.YELLOW);
//                    }
                    if(questionArray[i][j].getGridColor()==-1){
                        //drawText(gameArray[i][j],i,j);
                        //Log.d("gameArray",questionArray[i][j]);
//                        drawPngInside(canvas, directionResource[answerArray[i][j].getDirection()], j, i);
                        if (!isTouchdown[i][j]){
                            drawPngSet(canvas, directionResource[answerArray[i][j].getDirection()], j, i,-30);
                        }else {
                            drawPngSet(canvas, directionTouchResource[answerArray[i][j].getDirection()], j, i,-30);
                        }

                    }
                }
            }
        }

    }

    private void drawText(String indexString,int x,int y){
//        Paint paint = new Paint();
//        paint.setColor(Color.RED);
//        paint.setTextSize(25);
//        paint.setAlpha(128);
//        String stringLocation[]=IndexToLocation(x,y).split(",");
//        String stringData[]=indexString.split(",");
//        Log.d("stringData",stringData[0]);
//        drawCanvas.drawText(stringData[0],Integer.valueOf(stringLocation[0]) ,Integer.valueOf(stringLocation[1]) , paint);
    }
    private void drawBaseColor(Canvas canvas,int x,int y,int color){
        //Log.d("drawBaseColor","IN drawBaseColor");
        int mLocation[]=IndexToLocation(x, y);
        boxPaint.setColor(Color.MAGENTA);
//        boxPaint.setColor(color);
        boxPaint.setAlpha(140);
        canvas.drawRect(new Rect( mLocation[0]+10 , mLocation[1] +10, mLocation[0]+(int) eachWidth-10, mLocation[1]+(int) eachHeight-10), boxPaint);
    }
    private void drawPngSet(Canvas canvas,int picResource,int x,int y,int size){

        //String stringData[]=indexString.split(",");
        int mLocation[]=IndexToLocation(x, y);
        //Paint paint = new Paint();
        //paint.setColor(Color.parseColor(stringData[1]));
        //paint.setAlpha(128);
        //Log.d("","data:"+stringData[0]);
        //Log.d("","ID:"+ResourceId+",obj_o_num09:"+R.drawable.obj_o_num09+",obj_o_num05:"+R.drawable.obj_o_num05);
        Bitmap tempBitmap = mBitmapHashMap.get(picResource);
        if (tempBitmap==null){
            tempBitmap=((BitmapDrawable)getResources().getDrawable(picResource)).getBitmap();
            mBitmapHashMap.put(picResource,tempBitmap);
        }


        //drawCanvas.drawRect(new Rect( Integer.valueOf(stringLocation[0]) , Integer.valueOf(stringLocation[1]) ,
        //Integer.valueOf(stringLocation[0])+(int) eachWidth+1, Integer.valueOf(stringLocation[1])+(int) eachHeight+1), paint);
        tempBitmap= Bitmap.createScaledBitmap( tempBitmap
                , (int) eachWidth+size
                , (int) eachHeight+size
                , true
        );
        canvas.drawBitmap(tempBitmap, Integer.valueOf(mLocation[0])-(int)(size/2), Integer.valueOf(mLocation[1])-(int)(size/2), null);
    }
    private void drawPng(Canvas canvas,int picResource,int x,int y){

        //String stringData[]=indexString.split(",");
        int mLocation[]=IndexToLocation(x, y);
        //Paint paint = new Paint();
        //paint.setColor(Color.parseColor(stringData[1]));
        //paint.setAlpha(128);
        //Log.d("","data:"+stringData[0]);
        int ResourceId = Integer.valueOf(picResource);
        //Log.d("","ID:"+ResourceId+",obj_o_num09:"+R.drawable.obj_o_num09+",obj_o_num05:"+R.drawable.obj_o_num05);
        tempBitmap=((BitmapDrawable)getResources().getDrawable(ResourceId)).getBitmap();
        //drawCanvas.drawRect(new Rect( Integer.valueOf(stringLocation[0]) , Integer.valueOf(stringLocation[1]) ,
        //Integer.valueOf(stringLocation[0])+(int) eachWidth+1, Integer.valueOf(stringLocation[1])+(int) eachHeight+1), paint);
        srcBitmap= Bitmap.createScaledBitmap( tempBitmap
                , (int) eachWidth+1
                , (int) eachHeight+1
                , true
        );
        canvas.drawBitmap(srcBitmap, Integer.valueOf(mLocation[0]), Integer.valueOf(mLocation[1]), null);
    }
    private void drawPngInside(Canvas canvas,int picResource,int x,int y){

        //String stringData[]=indexString.split(",");
        int mLocation[]=IndexToLocation(x, y);
        //Paint paint = new Paint();
        //paint.setColor(Color.parseColor(stringData[1]));
        //paint.setAlpha(128);
        //Log.d("","data:"+stringData[0]);
        int ResourceId = Integer.valueOf(picResource);
        //Log.d("","ID:"+ResourceId+",obj_o_num09:"+R.drawable.obj_o_num09+",obj_o_num05:"+R.drawable.obj_o_num05);
        tempBitmap=((BitmapDrawable)getResources().getDrawable(ResourceId)).getBitmap();
        //drawCanvas.drawRect(new Rect( Integer.valueOf(stringLocation[0]) , Integer.valueOf(stringLocation[1]) ,
        //Integer.valueOf(stringLocation[0])+(int) eachWidth+1, Integer.valueOf(stringLocation[1])+(int) eachHeight+1), paint);
        srcBitmap= Bitmap.createScaledBitmap( tempBitmap
                , (int) eachWidth-22
                , (int) eachHeight-22
                , true
        );
        canvas.drawBitmap(srcBitmap, Integer.valueOf(mLocation[0]+11), Integer.valueOf(mLocation[1]+11), null);
    }
    private void changeAnswerDirection(int x,int y) {
        if(!questionArray[y][x].isQuestion()){
            int direction=answerArray[y][x].getDirection();
//            Log.d("changeAnswerDirection","("+y+","+x+") , direction="+direction);
            if(direction<3){
                answerArray[y][x].setDirection(direction+1);
//                Log.d("direction<3","("+y+","+x+") , direction="+direction);
            }else {
                answerArray[y][x].setDirection(0);
//                Log.d("direction>3","("+y+","+x+") , direction="+direction);
            }
        }

    }
    //    TODO
    public boolean checkTube(){
        boolean gameResult=true;
        boolean isRunPath[][]=new boolean[ySize][xSize];
        for (boolean boolArray[]:isRunPath){
            for (boolean bool:boolArray){

                bool = false;
            }
        }
        for (int i=0;i<questionArray.length;i++){
            for(int j=0;j<questionArray[0].length;j++){
                if (!questionArray[i][j].isQuestion()){
                    answerArray[i][j].setGridColor(-1);
                }
                if (questionArray[i][j].getAnswerString().equals("X")){
                    isRunPath[i][j]=true;
                }
            }
        }
        for (int i=0;i<questionArray.length;i++){
            for(int j=0;j<questionArray[0].length;j++){
                if (!questionArray[i][j].isQuestion()){
                    answerArray[i][j].setGridColor(-1);
                }
            }
        }

        for (BoxObject mBox:answerList){
//            Log.d("mbox",mBox.getAnswer());
        }
        for (BoxObject mBox:answerList){
            Log.d("",mBox.toString());
            //int indexPoint[]=new int[2];
            int nowX=mBox.getXIndex();
            int nowY=mBox.getYIndex();
//            int lastPoint[]=new int[2];
            int lastX=-1,lastY=-1;
            int nowColor=answerArray[nowY][nowX].getGridColor();
//            Log.d("point","(y,x) = ("+nowY+","+nowX+")"+",answer= "+mBox.getAnswer());
            int count=0;
            //int xIndex=mBox.getXIndex();
            //int yIndex=mBox.getYIndex();
            String answer=mBox.getAnswer();
            Boolean isBegin=false;
            Boolean whileTrue=true;
            while (whileTrue){
//                Log.d("InWhile","InWhileBegin");
                if(!questionArray[nowY][nowX].isQuestion()&&answerArray[nowY][nowX].getGridColor()==-1){answerArray[nowY][nowX].setGridColor(nowColor);}
                if (checkDirection(nowX,nowY,answerArray[nowY][nowX].getDirection())
                        &&!isRunPath[nowY][nowX]){
//                    Log.d("in If","checkDirection");

                    if(questionArray[nowY][nowX].getAnswerString().equals(answer)){
//                        Log.d("in If 1","answer&&isBegin ,"+questionArray[nowY][nowX].getAnswerString()+"$");
                        isRunPath[nowY][nowX]=true;
                        if (isBegin) {
//                            Log.d("in If 2","match!");
//                            Log.d("compare","now point=(y,x) = ("+getDirectionIndex(indexPoint).y+","+getDirectionIndex(indexPoint).x+")"+
//                                    "last point=(y,x) = ("+lastPoint.y+","+lastPoint.x+")");
                            int getDirectionArray[]=getDirectionIndex(nowX,nowY);
                            int gdX=getDirectionArray[0];
                            int gdY=getDirectionArray[1];
//                            int lastX1=lastPoint.x;
//                            int lastY1=lastPoint.y;
//                            Log.d("compare","get point=(y,x) = ("+gdY+","+gdX+") "+"now point=(y,x) = ("+nowY+","+nowX+") "+
//                                    "last point=(y,x) = ("+lastY+","+lastX+")");
                            if (gdX==lastX&&gdY==lastY){
                                //if(getDirectionIndex(indexPoint).x==lastPoint.x&&getDirectionIndex(indexPoint).y==lastPoint.y){
//                                Log.d("in If 3","true!");
//                                whileTrue=false;
                            }else {
//                                Log.d("in If 3","false!");
//                                gameResult=false;
//                                whileTrue=false;
//                                return false;
                            }
//                            whileTrue=false;
                        }
                    }else if (!questionArray[nowY][nowX].getAnswerString().equals("")){
//                        Log.d("in If 1","!=空字串 ,"+questionArray[nowY][nowX].getAnswerString()+"$");
                        isRunPath[nowY][nowX]=true;
                        gameResult=false;
                        whileTrue=false;
//                        return false;
                    }else if (questionArray[nowY][nowX].getAnswerString().equals("")){
//                        Log.d("in If 1","==空字串 ,"+"null"+"$");
                        isRunPath[nowY][nowX]=true;
                    }
                }else {
////                    Log.d("in Else","in Else");
//                    Log.d("data","("+nowY+","+nowX+") string="+questionArray[nowY][nowX].getAnswerString()+
//                            ", answer="+answer+", questionDirection="+questionArray[nowY][nowX].getDirection()+
//                            ", answerDirection="+answerArray[nowY][nowX].getDirection());
                    if (questionArray[nowY][nowX].getAnswerString().equals(answer)
                            &&questionArray[nowY][nowX].getDirection()==answerArray[nowY][nowX].getDirection()){
//                        Log.d("in Else","in If");
                        gameResult=true;
                        isRunPath[nowY][nowX]=true;
                    }else {
//                        Log.d("in Else","in Else 2");
                        gameResult=false;

                    }
                    whileTrue=false;
//                    return false;
                }
                lastX=nowX;
                lastY=nowY;
//                lastPoint.set(indexPoint.x,indexPoint.y);
                int getDirectionArray[]=getDirectionIndex(nowX,nowY);
                nowX=getDirectionArray[0];
                nowY=getDirectionArray[1];
//                Log.d("point","now point=(y,x) = ("+nowY+","+nowX+")"+
//                        "last point=(y,x) = ("+lastY+","+lastX+")");

                //Log.d("indexPointInWhile","(y,x) = ("+indexPoint.y+","+indexPoint.x+")");
                isBegin=true;

            }
//            Log.d("endWhile","endWhile");
        }
        for(int i=0;i<isRunPath.length;i++){
            for (int j=0;j<isRunPath[0].length;j++){
                if(!isRunPath[i][j]){
//                    Log.d("isRunPath","false=("+j+","+i+")");
                    gameResult=false;
                }
            }
        }
//        Log.d("endFor","endFor");
        return gameResult;
    }
    private boolean checkList(ArrayList<Point> list,Point p){
        for (Point pp:list){
            if(pp==p){
                return true;
            }
        }
        return false;
    }
    private int[] getDirectionIndex(int x,int y){
        int mArray[]={x,y};
        int direction=answerArray[y][x].getDirection();
//        Log.d("getDirection","direction="+direction);
        if(direction==0){ mArray[1]=y-1;}
        else if(direction==1){ mArray[0]=x+1;}
        else if(direction==2){ mArray[1]=y+1;}
        else if(direction==3){ mArray[0]=x-1;}
//       Point p=new Point();
//       p.set(x,y);
        return mArray;
    }
    private boolean checkDirection(int x,int y,int direction){
        if(direction==0){
            if(y-1<0) {
//                Log.d("checkDirection ("+y+","+x+")","p.y-1<0");
                return false;
            }
        }
        if(direction==1){
            if(x+1>=xSize) {
//                Log.d("checkDirection ("+y+","+x+")","p.x+1>=xSize");
                return false;
            }
        }
        if(direction==2){
            if(y+1>=xSize) {
//                Log.d("checkDirection ("+y+","+x+")","p.y+1>=xSize");
                return false;
            }
        }
        if(direction==3){
            if(x-1<0) {
//                Log.d("checkDirection ("+y+","+x+")","p.x-1<0");
                return false;
            }
        }
        return true;
    }

    //    TODO
    @Override
    public boolean  onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        int location[]=LocationToIndex(touchX,touchY);
        if (!isStart){
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Log.d("Index","location(y,x):"+touchY+","+touchX+"  Index(y,x):"+location[1]+","+location[0]);
                changeAnswerDirection(location[0],location[1]);
                isTouchdown[location[1]][location[0]]=true;
                checkTube();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                //drawBaseColor(xLocation,yLocation,0);
                isTouchdown[location[1]][location[0]]=false;
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }


    public void OnSubmitClick(){
//        Log.d("checkTube","submit");
        if(checkTube()){
            Log.d("checkTube","答對惹");
        }
        else {
            Log.d("checkTube","答錯惹");
        }
        invalidate();

    }

}
