package com.varied.variedgesture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private VariedGestureController mVariedGestureController;
    private RelativeLayout mGestureMask;
    private CustomDrawView mDrawView;
    private int mAngle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGestureMask = (RelativeLayout) findViewById(R.id.gesture_layout);
        mDrawView = (CustomDrawView) findViewById(R.id.drawView);
        mVariedGestureController = new VariedGestureController(this, mGestureMask);
        mVariedGestureController.setVariedListener(new VariedGestureController.VariedListener() {
            @Override
            public void onScale(float scaleX, float scaleY) {
                mDrawView.setScale(scaleX, scaleY);
            }

            @Override
            public void onAngle(int angle) {
                mDrawView.setAngle(mAngle + angle);
            }

            @Override
            public void onAngleEnd(int angle) {
                mAngle += angle;
            }

            @Override
            public void onShift(float horShift, float verShift) {
                mDrawView.setShift(horShift, verShift);
            }
        });
    }
}
