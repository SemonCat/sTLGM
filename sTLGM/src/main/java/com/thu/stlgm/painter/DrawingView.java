package com.thu.stlgm.painter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerConfiguration;
import com.amazonaws.services.s3.transfer.TransferProgress;
import com.amazonaws.services.s3.transfer.Upload;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DrawingView extends View {

	//drawing path
	private Path drawPath;
	//drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	//initial color
	private int paintColor = Color.RED;
	//canvas
	private Canvas drawCanvas;
	//canvas bitmap
	private Bitmap canvasBitmap;
	//brush sizes
	private float brushSize, lastBrushSize,maxX,maxY,miniX,miniY;
	//erase flag
	private boolean erase=false;
    private int brushNumber=0;
    private ArrayList<PathObject> paintPath=new ArrayList<PathObject>();
    private String mPath=new String();
    public ArrayList<PathObject> getPaintPath(){
        return paintPath;
    }
    public void setPaintPath(ArrayList<PathObject> newList){
        paintPath=newList;
    }
    public String saveArrayListToJson(){
        String GsonString =  new Gson().toJson(paintPath,ArrayList.class);
        //Log.d("","String:"+GsonString);
        return GsonString;
    }
    public ArrayList<PathObject> getArrayListFromJson(String GsonString){
        return new Gson().fromJson(GsonString,new TypeToken<List<PathObject>>(){}.getType());
    }

	public DrawingView(Context context, AttributeSet attrs){
		super(context, attrs);
		setupDrawing();
	}

    public String getmPath() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    //setup drawing
	private void setupDrawing(){
		//prepare for drawing and setup paint stroke properties
		brushSize = 5;
		lastBrushSize = brushSize;
		drawPath = new Path();
		drawPaint = new Paint();
		drawPaint.setColor(paintColor);
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(brushSize);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		canvasPaint = new Paint(Paint.DITHER_FLAG);

        setDrawingCacheEnabled(true);
	}

	//size assigned to view
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
	}

	//draw the view - will be called after touch event
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);

	}
    public void transBitmapToPng(String s3_key){
        //Bitmap transBitmap =  Bitmap.createBitmap(getDrawingCache(),0 ,0, (int)maxX, (int)maxY);
        Bitmap drawMap = getDrawingCache();
        Canvas mCanvas = new Canvas(drawMap);
        onDraw(mCanvas);
        if (miniX<0) miniX=0;
        if (miniY<0) miniY=0;
        if ((int)miniY+(int)maxY>=canvasBitmap.getHeight()) maxY=canvasBitmap.getHeight()-miniY;
        if ((int)miniX+(int)maxX>=canvasBitmap.getWidth()) maxX=canvasBitmap.getWidth()-miniX;
        Bitmap transBitmap =  Bitmap.createBitmap(canvasBitmap, (int) miniX, (int) miniY, (int) maxX, (int) maxY);
        //Log.d("bW",String.valueOf(canvasBitmap.getWidth()));
        //Log.d("bH",String.valueOf(canvasBitmap.getHeight()));
        try{
            String systemPath = Environment.getExternalStorageDirectory().getPath();
            File mFile = new File(systemPath+"/test.png");
            OutputStream stream = new FileOutputStream(mFile);
            transBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
            stream.close();

            uploadToS3(mFile,s3_key);
        }catch (IOException e){
            e.printStackTrace();
            Log.e("fail","fail");
        }

    }


    String accessKeyID = "AKIAJ6OP4TD5JQRPWOBA";
    String secretKey = "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";
    String bucketName = "tlgm";
    private void uploadToS3(final File mFile,final String s3_key){

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                String gPath="";
                if(mPath!=""){
                    gPath=mPath+"/"+s3_key+"_"+System.currentTimeMillis()+".png";
                }else {
                    gPath= "All/"+s3_key+"_"+System.currentTimeMillis()+".png";
                    Log.d("fail","Get String failed.");
                }


                AWSCredentials credentials;
                AmazonS3 s3Client;
                credentials = new BasicAWSCredentials(accessKeyID, secretKey);
                s3Client = new AmazonS3Client(credentials);
                TransferManager tm = new TransferManager(s3Client);
                TransferManagerConfiguration conf = tm.getConfiguration();

                //Log.d("","DO");

                int threshold = 4 * 1024 * 1024;
                conf.setMultipartUploadThreshold(threshold);
                tm.setConfiguration(conf);

                Upload upload = tm.upload(bucketName, gPath,mFile);
                TransferProgress p = upload.getProgress();

                //Log.d("","DO2");

                while (upload.isDone() == false)
                {
                    int percent =  (int)(p.getPercentTransferred());
                    System.out.print("\r" + gPath + " - " + "[ " + percent + "% ] "
                            + p.getBytesTransferred() + " / " + p.getTotalBytesToTransfer() );
                    // Do work while we wait for our upload to complete...
                    try {
                        Thread.sleep(500);
                    }catch (Exception e){

                    }

                }

                //Log.d("","DO3");
                System.out.print("\r" + gPath + " - " + "[ 100% ] "
                        + p.getBytesTransfered() + " / " + p.getTotalBytesToTransfer() );

// 默认添加public权限
                s3Client.setObjectAcl(bucketName, gPath, CannedAccessControlList.PublicRead);
                String URL  = s3Client.getObject(bucketName, gPath).getRedirectLocation();
                Log.d("DO4:","http://tlgm.s3.amazonaws.com/"+gPath);

                mPath="";
                }catch (Exception e){

                }

            }
        }).start();


    }
	//register user touches as drawing action
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float touchX = event.getX();
		float touchY = event.getY();
        PathObject tempPath;
		//respond to down, move and up events
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
            if(erase){
                cleanBrushNumber(touchX,touchY);
            }else {

                drawPath.moveTo(touchX, touchY);
                tempPath=new PathObject(paintColor,brushSize,touchX,touchY,0,brushNumber);
                //Log.d("BrushSize",String.valueOf(brushSize));
                paintPath.add(tempPath);
            }

			break;
		case MotionEvent.ACTION_MOVE:
            if(erase){
                cleanBrushNumber(touchX,touchY);
            }else {
                drawPath.lineTo(touchX, touchY);
                tempPath=new PathObject(paintColor,brushSize,touchX,touchY,1,brushNumber);
                paintPath.add(tempPath);
            }

			break;
		case MotionEvent.ACTION_UP:
            if(erase){
                cleanBrushNumber(touchX,touchY);
            }else {
                drawPath.lineTo(touchX, touchY);
                drawCanvas.drawPoint(touchX,touchY,drawPaint);
                drawCanvas.drawPath(drawPath, drawPaint);
                tempPath=new PathObject(paintColor,brushSize,touchX,touchY,2,brushNumber);
                paintPath.add(tempPath);
                brushNumber++;
                drawPath.reset();
            }
			break;
		default:
			return false;
		}
		//redraw
		invalidate();
		return true;

	}
    private void cleanBrushNumber(float x ,float y){
        float range=20f;
        int tempNumber=-1;
        for (int i=paintPath.size()-1;i>=0;i--){
            if((paintPath.get(i).getX()<=x+range&&paintPath.get(i).getX()>=x-range)&&(paintPath.get(i).getY()<=y+range&&paintPath.get(i).getY()>=y-range)){
                tempNumber=paintPath.get(i).getNumber();
                break;
            }
        }
        ArrayList<PathObject> tempPath=new ArrayList<PathObject>();
        if(tempNumber!=-1){
            for (int i=0;i<paintPath.size();i++){
                if(paintPath.get(i).getNumber()!=tempNumber){
                    tempPath.add(paintPath.get(i));
                }
            }
            paintPath.clear();
            paintPath=tempPath;
            drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            reDrawPath(paintPath);
        }
    }
	//update color
	public void setColor(String newColor){
		invalidate();
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
	}

	//set brush size
	public void setBrushSize(float newSize){
		float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
				newSize, getResources().getDisplayMetrics());
		brushSize=pixelAmount;
		drawPaint.setStrokeWidth(brushSize);
	}

	//get and set last brush size
	public void setLastBrushSize(float lastSize){
		lastBrushSize=lastSize;
	}
	public float getLastBrushSize(){
		return lastBrushSize;
	}

	//set erase true or false
	public void setErase(boolean isErase){
		erase=isErase;
		if(erase){
		//drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		}
		else drawPaint.setXfermode(null);
	}

    public boolean isErase() {
        return erase;
    }

    //start new drawing
	public void startNew(){
		drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		invalidate();
	}
    public void cleanPath(){
        paintPath.clear();
    }
    public void setBrushNumber(int number){
        brushNumber=number;
    }
    public void moveToOriginPoint(){
        //float miniX,miniY;
        maxX=0;
        maxY=0;
        miniX=0;
        miniY=0;
        int originX=0,originY=0,maxOriginX=0,maxOriginY=0;
        if(paintPath.size()>0){
            for(int i=0;i<paintPath.size();i++){
                if(paintPath.get(originX).getX()>=paintPath.get(i).getX()){
                    originX=i;
                }
                if(paintPath.get(originY).getY()>=paintPath.get(i).getY()){
                    originY=i;
                }
                if(paintPath.get(maxOriginX).getX()<paintPath.get(i).getX()){
                    maxOriginX=i;
                }
                if(paintPath.get(maxOriginY).getY()<paintPath.get(i).getY()){
                    maxOriginY=i;
                }
            }
            miniX=paintPath.get(originX).getX()-paintPath.get(originX).getBrushSize()/2;
            miniY=paintPath.get(originY).getY()-paintPath.get(originY).getBrushSize()/2;
            maxX=paintPath.get(maxOriginX).getX()+paintPath.get(maxOriginX).getBrushSize()/2-miniX;
            maxY=paintPath.get(maxOriginY).getY()+paintPath.get(maxOriginY).getBrushSize()/2-miniY;

            for(int i=0;i<paintPath.size();i++){
                paintPath.get(i).setX(paintPath.get(i).getX() -miniX);
                paintPath.get(i).setY(paintPath.get(i).getY() - miniY);
            }
        }
    }
    public void reDrawPath(ArrayList<PathObject> pathObject){
        if(pathObject.size()>0){
            for (int i=0;i<pathObject.size();i++){
                if(pathObject.get(i).getIsMoveTo()==0){
                    //setErase(pathObject.get(i).getIsErase());
                    setBrushSize(pathObject.get(i).getBrushSize());
                    setColor(String.format("#%06X", (0xFFFFFF & pathObject.get(i).getPaintColor())));
                   // Log.d("BrushSize",String.valueOf(pathObject.get(i).getBrushSize()));
                    drawPath.moveTo(pathObject.get(i).getX(), pathObject.get(i).getY());
                }else if(pathObject.get(i).getIsMoveTo()==1){
                    drawPath.lineTo(pathObject.get(i).getX(),pathObject.get(i).getY());
                }else if(pathObject.get(i).getIsMoveTo()==2){
                    drawPath.lineTo(pathObject.get(i).getX(),pathObject.get(i).getY());
                    drawCanvas.drawPoint(pathObject.get(i).getX(),pathObject.get(i).getY(),drawPaint);
                    drawCanvas.drawPath(drawPath, drawPaint);
                    drawPath.reset();
                    invalidate();
                }
            }
        }
        else {
            Log.d("NO PATH","NO PATH");
        }
    }
}
