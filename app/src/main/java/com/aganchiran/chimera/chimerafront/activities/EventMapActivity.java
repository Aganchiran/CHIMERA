package com.aganchiran.chimera.chimerafront.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimerafront.utils.SizeUtil;

public class EventMapActivity extends AppCompatActivity {

//    private float mx, my;
//    private ScrollView vScroll;
//    private HorizontalScrollView hScroll;
//    private ImageView eventMap;
//    private ScaleGestureDetector sgd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_map);

//        vScroll = findViewById(R.id.vScroll);
//        hScroll = findViewById(R.id.hScroll);
//        eventMap = findViewById(R.id.event_map);
//        sgd = new ScaleGestureDetector(this, new ScaleListener());
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        float curX, curY;
//
//        switch (event.getAction()) {
//
//            case MotionEvent.ACTION_DOWN:
//                mx = event.getX();
//                my = event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//
//                curX = event.getX();
//                curY = event.getY();
//                if (Math.abs(mx - curX) < 200 && Math.abs(my - curY) < 200) {
//                    vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
//                    hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
//                }
//                mx = curX;
//                my = curY;
//
//                break;
//            case MotionEvent.ACTION_UP:
//                curX = event.getX();
//                curY = event.getY();
//                vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
//                hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
//                break;
//        }
//
//        sgd.onTouchEvent(event);
//
//        return true;
//    }
//
//    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
//
//
//        @Override
//        public boolean onScale(ScaleGestureDetector detector) {
//            ViewGroup.LayoutParams prevParams = eventMap.getLayoutParams();
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (prevParams.width * detector.getScaleFactor()), (int) (prevParams.height * detector.getScaleFactor()));
//            eventMap.setLayoutParams(params);
//
//            float pointX = detector.getFocusX();
//            float pointY = detector.getFocusY();
//            float screenX = SizeUtil.getScreenWidth(getApplicationContext());
//            float screenY = SizeUtil.getScreenHeight(getApplicationContext());
//            int distanceX = (int) (eventMap.getWidth() * (detector.getScaleFactor() - 1) * (pointX / screenX));
//            int distanceY = (int) (eventMap.getHeight() *0.25* (detector.getScaleFactor() - 1) * (pointY / screenY));
//
//            Log.d("Distance X:", distanceX+"");
//            Log.d("Distance Y:", distanceY+"");
//            vScroll.scrollBy(distanceX, distanceY);
//            hScroll.scrollBy(distanceX, distanceY);
//            return true;
//        }
//    }

}
