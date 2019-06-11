package com.aganchiran.chimera.chimerafront.utils.views;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.aganchiran.chimera.R;

public class EventPoint extends AppCompatImageView {

    private OnEventClickListener listener;

    public EventPoint(Context context, OnEventClickListener listener) {
        super(context);
        this.listener = listener;
        setup();
    }

    public EventPoint(Context context, AttributeSet attrs, OnEventClickListener listener) {
        super(context, attrs);
        this.listener = listener;
        setup();
    }

    private void setup() {
        setImageResource(R.drawable.ic_event_point);
        final int eventSize = (int) getResources().getDimension(R.dimen.event_size);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(eventSize, eventSize);
        setLayoutParams(params);

        setOnTouchListener(new OnTouchListener() {
            private long startTime;
            private float initialX = 0;
            private float initialY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getPointerCount() != 1) return false;

                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        startTime = motionEvent.getEventTime();
                        initialX = motionEvent.getX();
                        initialY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        final double xDistance = motionEvent.getX() - initialX;
                        final double yDistance = motionEvent.getY() - initialY;
                        final double distance =
                                Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));

                        if (motionEvent.getEventTime() - startTime > 500 && distance < 50) {

                            final ClipData clipData = ClipData.newPlainText("", "");
                            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                v.startDragAndDrop(clipData, shadowBuilder, v, 0);
                            } else {
                                v.startDrag(clipData, shadowBuilder, v, 0);
                            }
                            v.setVisibility(View.INVISIBLE);

                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (motionEvent.getEventTime() - startTime < 200) {
                            listener.onEventClick(EventPoint.this);
                        }
                        break;
                }
                return true;

            }
        });
    }

    public void setCoord(int xCoord, int yCoord) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        params.setMargins(xCoord, yCoord, 0, 0);
        setLayoutParams(params);
    }

    public int getXCoord() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        return params.leftMargin;
    }

    public int getYCoord() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        return params.topMargin;
    }

    public interface OnEventClickListener {
        void onEventClick(EventPoint eventPoint);
    }

}
