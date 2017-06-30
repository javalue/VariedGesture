package com.varied.variedgesture;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * 包含旋转、平移、缩放手势
 * Created by tong.xu on 2017/6/12.
 */

public class VariedGestureController {
    private static final String TAG = VariedGestureController.class.getSimpleName();
    private static final float MAX_SCALE = 5.0f;
    private static final float MIN_SCALE = 0.2f;

    private View mControllerView = null;
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private volatile boolean bPinchZoomMode = false;
    private boolean bInDragMode = false;
    private float downRotation = 0;
    private float originScaleRatio = 1.0f;
    private float deltaX = 0, deltaY = 0;

    private VariedListener mVariedListener;

    public void setOriginParam(float scale, float shiftX, float shiftY) {
        originScaleRatio = scale;
        deltaX = shiftX;
        deltaY = shiftY;
    }

    public interface VariedListener {
        void onScale(float scaleX, float scaleY);

        void onAngle(int angle);

        void onAngleEnd(int angle);

        void onShift(float horShift, float verShift);
    }

    public VariedGestureController(Context context, View view) {
        mControllerView = view;
        mControllerView.setOnTouchListener(onTouchListener);
        mGestureDetector = new GestureDetector(context, new MyOnGestureListener());
        mScaleGestureDetector = new ScaleGestureDetector(context, mScaleGestureListener);
    }

    public void setVariedListener(VariedListener variedListener) {
        this.mVariedListener = variedListener;
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent evt) {
            return processTouchEvent(evt);
        }

    };

    public boolean processTouchEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            mGestureDetector.onTouchEvent(event);
        }
        if (mScaleGestureDetector != null) {
            mScaleGestureDetector.onTouchEvent(event);
        }

        int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                downRotation = rotation(event);
                if (!bInDragMode) {
                    bPinchZoomMode = true;
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                if (mVariedListener != null) {
                    float rotation = rotation(event) - downRotation;
                    Log.i(TAG + "---angleZ-end:", rotation + "");
                    if (mVariedListener != null) {
                        mVariedListener.onAngleEnd((int) rotation);
                    }
                }
                if (bPinchZoomMode) {
                    bPinchZoomMode = false;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (bPinchZoomMode) {
                    float rotation = rotation(event) - downRotation;
                    Log.i(TAG + "---angleZ:", rotation + "");
                    if (mVariedListener != null) {
                        mVariedListener.onAngle((int) rotation);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                bInDragMode = false;
            }
            default:
                break;
        }

        return true;
    }

    // 取旋转角度
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(1) - event.getX(0));
        double delta_y = (event.getY(1) - event.getY(0));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private final ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float scale = scaleGestureDetector.getScaleFactor();
            originScaleRatio *= scale;
            if (Math.abs(originScaleRatio) >= MAX_SCALE) {
                if (originScaleRatio > 0) {
                    originScaleRatio = MAX_SCALE;
                } else {
                    originScaleRatio = -MAX_SCALE;
                }
            }
            if (Math.abs(originScaleRatio) <= MIN_SCALE) {
                if (originScaleRatio > 0) {
                    originScaleRatio = MIN_SCALE;
                } else {
                    originScaleRatio = -MIN_SCALE;
                }
            }
            Log.i(TAG + "---scale:", originScaleRatio + "");
            if (mVariedListener != null) {
                mVariedListener.onScale(originScaleRatio, originScaleRatio);
            }
            return true;
        }
    };

    private class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!bPinchZoomMode) {
                deltaX -= distanceX;
                deltaY -= distanceY;
                Log.i(TAG + "---shift:", deltaX + "===" + deltaY);
                if (mVariedListener != null) {
                    mVariedListener.onShift(deltaX, deltaY);
                }
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            bInDragMode = true;
            super.onLongPress(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }
    }
}
