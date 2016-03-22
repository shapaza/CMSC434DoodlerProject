package com.example.brentwang.cmsc434doodler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    public DoodleView doodleView;
    private ImageButton currPaint;
    private Button drawBtn, opacityBtn, eraseBtn, clearBtn;
    private float smallBrush, mediumBrush, largeBrush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doodleView = (DoodleView)findViewById(R.id.drawing);
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        // the brush button, which allows users to change the size of the brush
        drawBtn = (Button)findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        // opacity button
        opacityBtn = (Button)findViewById(R.id.opacity_btn);
        opacityBtn.setOnClickListener(this);

        // erase button
        eraseBtn = (Button)findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        // clear button
        clearBtn = (Button)findViewById(R.id.clear_btn);
        clearBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        //respond to clicks

        // if the brush button is clicked
        if(view.getId()==R.id.draw_btn){
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);

            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    doodleView.setErase(false);
                    doodleView.setBrushSize(smallBrush);
                    doodleView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });

            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    doodleView.setErase(false);
                    doodleView.setBrushSize(mediumBrush);
                    doodleView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    doodleView.setErase(false);
                    doodleView.setBrushSize(largeBrush);
                    doodleView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });

            brushDialog.show();
        }

        // if the opacity button is clicked
        if(view.getId()==R.id.opacity_btn){
            //launch opacity chooser

            final Dialog seekDialog = new Dialog(this);
            seekDialog.setTitle("Opacity level:");
            seekDialog.setContentView(R.layout.opacity_chooser);
            final TextView seekTxt = (TextView)seekDialog.findViewById(R.id.opq_txt);
            final SeekBar seekOpq = (SeekBar)seekDialog.findViewById(R.id.opacity_seek);
            seekOpq.setMax(100);

            int currLevel = doodleView.getOpacity();
            seekTxt.setText(currLevel+"%");
            seekOpq.setProgress(currLevel);

            seekOpq.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekTxt.setText(Integer.toString(progress) + "%");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            Button opqBtn = (Button)seekDialog.findViewById(R.id.opq_ok);

            opqBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    doodleView.setOpacity(seekOpq.getProgress());
                    seekDialog.dismiss();
                }
            });

            seekDialog.show();
        }

        // if the erase button is clicked
        if (view.getId()==R.id.erase_btn){
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);

            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    doodleView.setErase(true);
                    doodleView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    doodleView.setErase(true);
                    doodleView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    doodleView.setErase(true);
                    doodleView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });

            brushDialog.show();
        }

        // if user presses clear
        if(view.getId()==R.id.clear_btn) {
            doodleView.clear();
        }
    }

    public void paintClicked(View view){
        // stop erasing
        doodleView.setErase(false);

        // get the last brush size the user selected
        doodleView.setBrushSize(doodleView.getLastBrushSize());

        if(view!=currPaint){
            //update color
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            doodleView.setColor(color);

            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
    }

}
