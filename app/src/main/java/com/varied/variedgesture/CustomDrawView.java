package com.varied.variedgesture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2017/6/30.
 */

public class CustomDrawView extends View {

    private Paint paint;
    private Matrix matrix;
    private Bitmap bitmap;

    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float shiftX = 0.0f;
    private float shiftY = 0.0f;
    private int angle = 0;


    public CustomDrawView(Context context) {
        super(context);
        init();
    }

    public CustomDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        matrix = new Matrix();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.khauna);
    }

    public void setScale(float x, float y) {
        scaleX = x;
        scaleY = y;
        invalidate();
    }

    public void setShift(float x, float y) {
        shiftX = x;
        shiftY = y;
        invalidate();
    }

    public void setAngle(int z) {
        angle = z;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        matrix.reset();
        //获得轴心
        Point point = getAxis();

        Log.i("point" , "x:" + point.x + "y:" + point.y);
        //缩放
        matrix.postScale(scaleX, scaleY, point.x, point.y);
        //旋转
        matrix.postRotate(angle, point.x, point.y);
        //平移
        matrix.postTranslate(shiftX, shiftY);
        canvas.drawBitmap(bitmap, matrix, paint);
    }

    private Point getAxis() {
        return new Point(getWidth() / 2, getHeight() / 2);
    }
}
