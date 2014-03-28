package com.thu.stlgm.painter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.thu.stlgm.GameActivity;
import com.thu.stlgm.R;
import com.thu.stlgm.api.SQService;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.UUID;

public class PainterFragment extends Fragment implements View.OnClickListener {
    private DrawingView drawView;
    //buttons
    private ImageButton currPaint, eraseBtn, newBtn, saveBtn,small_Brush,medium_Brush,large_Brush;

    private TextView WhiteBoardTitle;

    private Button enterBtn;
    //sizes
    private float smallBrush, mediumBrush, largeBrush;
    private String pathString;

    private ImageView closeWhiteBoard;

    public PainterFragment(String pathString) {
        this.pathString=pathString;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().setContentView(R.layout.);



        WhiteBoardTitle = (TextView) getActivity().findViewById(R.id.WhiteBoardTitle);
        WhiteBoardTitle.setText(pathString);


        //get drawing view
        drawView = (DrawingView)getActivity().findViewById(R.id.drawing);
        closeWhiteBoard = (ImageView) getActivity().findViewById(R.id.closeWhiteBoard);
        closeWhiteBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(PainterFragment.this).commit();
                ((GameActivity)getActivity()).ShowCamera();
            }
        });

        //get the palette and first color button
        LinearLayout paintLayout = (LinearLayout)getActivity().findViewById(R.id.paint_colors);
        LinearLayout paintLayout2 =(LinearLayout)getActivity().findViewById(R.id.paint_colors2);

        for (int i=0;i<paintLayout.getChildCount();i++){
            View mView = paintLayout.getChildAt(i);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(drawView.isErase()){
                        Toast mToast = Toast.makeText(getActivity(), "筆刷", Toast.LENGTH_SHORT);
                        mToast.setGravity(Gravity.CENTER,0,0);
                        mToast.show();
                    }
                    paintClicked(view);
                }
            });
        }
        for (int i=0;i<paintLayout2.getChildCount();i++){
            View mView = paintLayout2.getChildAt(i);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(drawView.isErase()){
                        Toast mToast = Toast.makeText(getActivity(), "筆刷", Toast.LENGTH_SHORT);
                        mToast.setGravity(Gravity.CENTER,0,0);
                        mToast.show();
                    }
                    paintClicked(view);
                }
            });
        }

        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        //sizes from dimensions
        smallBrush = 10;
        mediumBrush = 20;
        largeBrush =30;

        //draw button
        small_Brush = (ImageButton)getActivity().findViewById(R.id.small_brush);
        small_Brush.setOnClickListener(this);

        medium_Brush = (ImageButton)getActivity().findViewById(R.id.medium_brush);
        medium_Brush.setOnClickListener(this);

        large_Brush = (ImageButton)getActivity().findViewById(R.id.large_brush);
        large_Brush.setOnClickListener(this);

        //set initial size
        drawView.setBrushSize(smallBrush);

        //erase button
        eraseBtn = (ImageButton)getActivity().findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        //new button
        newBtn = (ImageButton)getActivity().findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        //save button
        saveBtn = (ImageButton)getActivity().findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        enterBtn= (Button)getActivity().findViewById(R.id.enterBtn);
        enterBtn.setOnClickListener(this);

    }
    public void paintClicked(View view){
        //use chosen color
        //set erase false
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());
        if(view!=currPaint){
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            //update ui
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
    }
    public void onClick(View view){
        if(view.getId()==R.id.small_brush||view.getId()==R.id.medium_brush||view.getId()==R.id.large_brush){
            small_Brush.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    Toast mToast = Toast.makeText(getActivity(), "小筆刷", Toast.LENGTH_SHORT);
                    mToast.setGravity(Gravity.CENTER,0,0);
                    mToast.show();
                }
            });
            medium_Brush.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    Toast mToast = Toast.makeText(getActivity(), "中筆刷", Toast.LENGTH_SHORT);
                    mToast.setGravity(Gravity.CENTER,0,0);
                    mToast.show();
                }
            });

            large_Brush.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    Toast mToast = Toast.makeText(getActivity(), "大筆刷", Toast.LENGTH_SHORT);
                    mToast.setGravity(Gravity.CENTER,0,0);
                    mToast.show();
                }
            });
            //show and wait for user interaction
        }
        else if(view.getId()==R.id.erase_btn){
            //switch to erase - choose size
            drawView.setErase(true);
            Toast mToast = Toast.makeText(getActivity(), "橡皮擦", Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER,0,0);
            mToast.show();
        }
        else if(view.getId()==R.id.new_btn){
            //new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(getActivity());
            newDialog.setTitle("開新檔案");
            newDialog.setMessage("尚未儲存的檔案將會遺失，按確定繼續");
            newDialog.setPositiveButton("確定", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
                    drawView.cleanPath();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();
        }
        else if(view.getId()==R.id.save_btn){
            //save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(getActivity());
            saveDialog.setTitle("檔案");
            saveDialog.setMessage("存檔/讀檔?");
            saveDialog.setPositiveButton("存檔", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    try {
                        if(drawView.getPaintPath().size()>0){
                            drawView.moveToOriginPoint();
                            writeToFile(drawView.saveArrayListToJson());

                        }else {
                            Toast mToast = Toast.makeText(getActivity(), "您還沒有畫任何東西!", Toast.LENGTH_SHORT);
                            mToast.setGravity(Gravity.CENTER, 0, 0);
                            mToast.show();
                        }
                    }catch (Exception e){
                        Toast mToast = Toast.makeText(getActivity(), "儲存檔案失敗!", Toast.LENGTH_SHORT);
                        mToast.setGravity(Gravity.CENTER,0,0);
                        mToast.show();
                    }
                }
            });
            saveDialog.setNegativeButton("讀檔", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                   // Log.d("read","INREAD");
                    readFileFunction();
                }
            });
            saveDialog.show();
        }
        else if(view.getId()==R.id.enterBtn){
            if (drawView.getPaintPath().size()>0){
                drawView.setmPath(pathString);
                drawView.moveToOriginPoint();
                drawView.transBitmapToPng(((GameActivity)getActivity()).getLeader().getGroupID());
                drawView.startNew();
                drawView.cleanPath();
                //drawView.reDrawPath(drawView.getPaintPath());
                //writeToFile(drawView.saveArrayListToJson());
               // drawView.startNew();
                //drawView.cleanPath();

                SQService.addMoney(((GameActivity)getActivity()).getLeader().getGroupID(),1);

                Toast mToast = Toast.makeText(getActivity(), "送出!", Toast.LENGTH_SHORT);
                mToast.setGravity(Gravity.CENTER,0,0);
                mToast.show();
            }else {
                Toast mToast = Toast.makeText(getActivity(), "您還沒有畫任何東西!", Toast.LENGTH_SHORT);
                mToast.setGravity(Gravity.CENTER,0,0);
                mToast.show();
            }
        }
    }
    public void readFileFunction(){
        try {
            if(drawView.getArrayListFromJson(readFromFile()).size()>0){
                drawView.setPaintPath(drawView.getArrayListFromJson(readFromFile()));
                drawView.setBrushNumber(drawView.getPaintPath().get(drawView.getPaintPath().size()-1).getNumber()+1);
                drawView.startNew();
                drawView.reDrawPath(drawView.getPaintPath());
            }
        }catch (Exception e){
            Toast mToast = Toast.makeText(getActivity(), "讀取檔案失敗!", Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER,0,0);
            mToast.show();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_painter, container, false);
    }
    private void writeToFile(String data) {
        try {
            String systemPath = Environment.getExternalStorageDirectory().getPath();
            File file = new File(systemPath + "/drawingAAJC.txt");
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(data.getBytes());
            fout.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String readFromFile() {
        String systemPath = Environment.getExternalStorageDirectory().getPath();

        File file = new File(systemPath + "/drawingAAJC.txt");
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fin = new FileInputStream(file);
            byte[] data = new byte[fin.available()];
            while (fin.read(data) != -1) {
                sb.append(new String(data));
            }
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Log.d("TAG", "Read from SDCARD: " + json.toString());
        return sb.toString();
    }
}
