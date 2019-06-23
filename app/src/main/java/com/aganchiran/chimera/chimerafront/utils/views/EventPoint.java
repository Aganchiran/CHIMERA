/*
 This file is part of CHIMERA: Companion for Humans Intending to
 Master Extreme Role Adventures ("CHIMERA").

 CHIMERA is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 CHIMERA is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with CHIMERA.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.aganchiran.chimera.chimerafront.utils.views;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimerafront.utils.Coordinates;

public class EventPoint extends AppCompatImageView {

    private OnEventClickListener listener;

    private final int eventSize = (int) getResources().getDimension(R.dimen.event_size);
    private float scale;
    private TextView nameText;
    private RelativeLayout nameTag;
    private Coordinates coords = new Coordinates(0, 0, 0, 0);
    public float xPercentage = 0;
    public float yPercentage = 0;

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
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(eventSize, eventSize);
        setLayoutParams(params);

        nameTag = new RelativeLayout(getContext());
        RelativeLayout.inflate(getContext(), R.layout.item_name_tag, nameTag);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(eventSize * 3, eventSize);
        nameTag.setLayoutParams(params2);
        nameTag.setElevation(-2);

        nameText = nameTag.findViewById(R.id.nameText);

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
                            listener.onDragStart(EventPoint.this);
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

    public void setCoord(final int xCoord, final int yCoord, final int xOffset, final int yOffset) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        params.setMargins(xCoord + xOffset, yCoord + yOffset, 0, 0);
        setLayoutParams(params);

        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) nameTag.getLayoutParams();
        params2.setMargins((int) (xCoord + xOffset + (eventSize * (scale - 1))), yCoord + yOffset, 0, 0);
        nameTag.setLayoutParams(params2);

        coords.set(xCoord, yCoord);
        coords.setOffsets(xOffset, yOffset);
    }

    public Coordinates getCoords() {
        return coords;
    }

    public void resize(float scale) {
        this.scale = scale;
        setScaleX(scale);
        setScaleY(scale);
        nameTag.setScaleX(scale);
        nameTag.setScaleY(scale);
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) nameTag.getLayoutParams();
        params2.setMargins(
                (int) (getCoords().x + getCoords().xOffset + (eventSize * (scale - 1))),
                getCoords().y + getCoords().yOffset, 0, 0);
        nameTag.setLayoutParams(params2);
    }

    public RelativeLayout getNameTag() {
        return nameTag;
    }

    public void setName(String name) {
        nameText.setText(name);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        ((ViewGroup) getParent()).addView(nameTag);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        nameTag.setVisibility(visibility);
    }

    public interface OnEventClickListener {
        void onEventClick(EventPoint eventPoint);

        void onDragStart(EventPoint eventPoint);
    }

}
