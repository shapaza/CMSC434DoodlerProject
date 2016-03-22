package com.example.brentwang.cmsc434doodler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;

/**
 * Created by Brent Wang on 3/9/2016.
 */
public class DoodleView extends View {

    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private Paint _paintDoodle = new Paint();
    private Paint canvasPaint;
    private Path _path = new Path();

    private int paintColor;
    private int opacity = 255;
    private float brushSize, lastBrushSize;
    private boolean erase=false;

    public DoodleView(Context context) {
        super(context);
        init(null, 0);
    }

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DoodleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;

        _paintDoodle.setColor(Color.RED);
        _paintDoodle.setAntiAlias(true);
        _paintDoodle.setStyle(Paint.Style.STROKE);
        _paintDoodle.setStrokeWidth(brushSize);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    public int getOpacity(){
        return Math.round((float)opacity/255*100);
    }

    public void setOpacity(int newOpacity){
        opacity=Math.round((float)newOpacity/100*255);
        _paintDoodle.setColor(paintColor);
        _paintDoodle.setAlpha(opacity);
    }

    public void clear(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void setErase(boolean isErase){
        //set erase true or false
        erase=isErase;

        if(erase) {
            _paintDoodle.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        else {
            _paintDoodle.setXfermode(null);
        }
    }

    public void setBrushSize(float newSize){
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        _paintDoodle.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }

    public float getLastBrushSize(){
        return lastBrushSize;
    }

    public void setColor(String newColor){
        //set color
        invalidate();
        paintColor = Color.parseColor(newColor);
        _paintDoodle.setColor(paintColor);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.drawLine(0, 0, getWidth(), getHeight(), _paintDoodle);
        //canvas.drawPath(_path, _paintDoodle);
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(_path, _paintDoodle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float touchX = motionEvent.getX();
        float touchY = motionEvent.getY();

        switch(motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                _path.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                _path.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(_path, _paintDoodle);
                _path.reset();
                break;
        }

        invalidate();
        return true;
    }

}
