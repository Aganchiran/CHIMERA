package com.aganchiran.chimera.chimerafront.utils.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.event.EventModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SizeReadyCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Layout that provides pinch-zooming of content. This view should have exactly one child
 * view containing the content.
 */
public class ZoomLayout extends FrameLayout implements ScaleGestureDetector.OnScaleGestureListener {

    private enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    private static final String TAG = "ZoomLayout";
    private static final float MIN_ZOOM = 1.0f;
    private static final float MAX_ZOOM = 10.0f;

    private Mode mode = Mode.NONE;
    private float scale = 1.0f;
    private float lastScaleFactor = 0f;

    // Where the finger first  touches the screen
    private float startX = 0f;
    private float startY = 0f;

    // How much to translate the canvas
    private float dx = 0f;
    private float dy = 0f;
    private float prevDx = 0f;
    private float prevDy = 0f;

    // Offset of the canvas
    private float offsetX = 0f;
    private float offsetY = 0f;

    private List<EventPoint> events = new ArrayList<>();
    private EventPoint fliyingEvent;
    private OnItemClickListener listener;
    private RelativeLayout eventMap;
    private final int eventSize = (int) getResources().getDimension(R.dimen.event_size);

    private ImageView backgroundImage;
    private Point imageBoundaries;
    private int backgroundOffsetX = 0;
    private int backgroundOffsetY = 0;

    private boolean firstTime = true;

    public ZoomLayout(Context context) {
        super(context);
        init(context);
    }

    public ZoomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(final Context context) {
        setupMap(context);

        setOnDragListener(new DragEventListener());
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener(new View.OnTouchListener() {
            private long startTime;
            private int maxFingers = 0;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        maxFingers = 1;
                        startTime = motionEvent.getEventTime();
                        Log.i(TAG, "DOWN");
                        if (scale > MIN_ZOOM) {
                            mode = Mode.DRAG;
                            startX = motionEvent.getX() - prevDx;
                            startY = motionEvent.getY() - prevDy;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        final boolean fingerReduction =
                                motionEvent.getPointerCount() == 1 && maxFingers > 1;
                        if (mode == Mode.DRAG && !fingerReduction) {
                            dx = motionEvent.getX() - startX;
                            dy = motionEvent.getY() - startY;
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        maxFingers++;
                        mode = Mode.ZOOM;
                        startTime = 0;
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = Mode.DRAG;
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "UP");
                        maxFingers = 0;
                        mode = Mode.NONE;
                        prevDx = dx;
                        prevDy = dy;
                        if (motionEvent.getEventTime() - startTime < 200) {
                            createEvent(motionEvent);
                            return true;
                        }
                        break;
                }

                scaleDetector.onTouchEvent(motionEvent);

                if (((mode == Mode.DRAG && scale >= MIN_ZOOM) || mode == Mode.ZOOM)) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = (child().getWidth() - (child().getWidth() / scale)) / 2 * scale;
                    float maxDy = (child().getHeight() - (child().getHeight() / scale)) / 2 * scale;

                    dx = Math.min(Math.max(dx, -maxDx), maxDx);
                    dy = Math.min(Math.max(dy, -maxDy), maxDy);
//                    Log.i(TAG, "Width: " + child().getWidth() + ", scale " + scale + ", dx " + dx
//                            + ", max " + maxDx);
                    applyScaleAndTranslation();
                }

                return true;
            }
        });
    }

    // ScaleGestureDetector

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleDetector) {
        Log.i(TAG, "onScaleBegin");
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector scaleDetector) {
        float scaleFactor = scaleDetector.getScaleFactor();
        Log.i(TAG, "onScale" + scaleFactor);
        if (lastScaleFactor == 0 || (Math.signum(scaleFactor) == Math.signum(lastScaleFactor))) {
            float prevScale = scale;
            scale *= scaleFactor;
            scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM));
            lastScaleFactor = scaleFactor;

            float adjustedScaleFactor = scale / prevScale;
            float focusX = scaleDetector.getFocusX();
            float focusY = scaleDetector.getFocusY();
            dx += (dx - focusX + this.getWidth() / 2.0) * (adjustedScaleFactor - 1);
            dy += (dy - focusY + this.getHeight() / 2.0) * (adjustedScaleFactor - 1);
        } else {
            lastScaleFactor = 0;
        }
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleDetector) {
        Log.i(TAG, "onScaleEnd");
        mode = Mode.NONE;
    }

    private void applyScaleAndTranslation() {
        child().setScaleX(scale);
        child().setScaleY(scale);
        child().setTranslationX(dx);
        child().setTranslationY(dy);

        final float scaleOffsetX = (child().getWidth() - (child().getWidth() / scale)) / 2;
        final float scaleOffsetY = (child().getHeight() - (child().getHeight() / scale)) / 2;
        offsetX = scaleOffsetX - (dx / scale);
        offsetY = scaleOffsetY - (dy / scale);

        for (EventPoint event : events) {
            event.resize(1 / scale);
        }
    }

    private View child() {
        return getChildAt(0);
    }

    private void setupMap(Context context) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        eventMap = new RelativeLayout(context);
        eventMap.setLayoutParams(params);
        backgroundImage = new ImageView(context);
        backgroundImage.setLayoutParams(params);

        final RelativeLayout child = new RelativeLayout(context);
        child.addView(backgroundImage);
        child.addView(eventMap);
        this.addView(child);
    }

    private void createEvent(MotionEvent e) {
        boolean isRelLay = child() instanceof RelativeLayout;
        boolean oneFinger = e.getPointerCount() == 1;
        if (isRelLay && oneFinger) {

            final EventPoint eventPoint = new EventPoint(getContext(), new EventPoint.OnEventClickListener() {
                @Override
                public void onEventClick(EventPoint eventPoint) {
                    listener.onEventClick(eventPoint);
                }
            });
            eventPoint.setCoord(
                    getMapXCoord(e.getX()) - backgroundOffsetX,
                    getMapYCoord(e.getY()) - backgroundOffsetY,
                    backgroundOffsetX,
                    backgroundOffsetY);
            eventPoint.resize(1 / scale);
            eventPoint.setName("New Event");
            eventMap.addView(eventPoint);
            events.add(eventPoint);

            if (listener != null) {
                listener.onCreateEvent(eventPoint);
            }
        }
    }

    public int getMapXCoord(float xScreenPoint) {
        return (int) (offsetX + (xScreenPoint / scale) - (eventSize / 2));
    }

    public int getMapYCoord(float yScreenPoint) {
        return (int) (offsetY + (yScreenPoint / scale) - (eventSize / 2));
    }

    public class DragEventListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    fliyingEvent = ((EventPoint) event.getLocalState());
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    break;
                case DragEvent.ACTION_DROP:
                    fliyingEvent.setCoord(
                            getMapXCoord(event.getX()) - backgroundOffsetX,
                            getMapYCoord(event.getY()) - backgroundOffsetY,
                            backgroundOffsetX,
                            backgroundOffsetY);
                    listener.onMoveEvent(fliyingEvent);
                    fliyingEvent.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    fliyingEvent = null;
                    break;
            }
            return true;
        }
    }

    public EventPoint getFliyingEvent() {
        return fliyingEvent;
    }

    public void deleteEvent(EventPoint eventPoint) {
        eventMap.removeView(eventPoint.getNameTag());
        eventMap.removeView(eventPoint);
        events.remove(eventPoint);

        if (listener != null) {
            listener.onDeleteEvent(eventPoint);
        }
    }

    public void setEvents(List<EventModel> eventModels) {
        firstTime = false;
        eventMap.removeAllViews();
        events.clear();

        for (final EventModel eventModel : eventModels) {
            final EventPoint eventPoint = new EventPoint(getContext(), new EventPoint.OnEventClickListener() {
                @Override
                public void onEventClick(EventPoint eventPoint) {
                    listener.onEventClick(eventPoint);
                }
            });
            eventPoint.setId(eventModel.getId());

            eventPoint.xPercentage = eventModel.getXCoord();
            eventPoint.yPercentage = eventModel.getYCoord();

            eventPoint.resize(1 / scale);
            eventPoint.setName(eventModel.getName());
            events.add(eventPoint);
            eventMap.addView(eventPoint);
        }

        if (imageBoundaries != null) {
            recalculateOffsets(imageBoundaries.x, imageBoundaries.y);
        }
    }

    public void updateEventName(EventModel eventModel) {
        for (final EventPoint eventPoint : events) {
            if (eventPoint.getId() == eventModel.getId()) {
                eventPoint.setName(eventModel.getName());
                break;
            }
        }
    }

    public int getPXFromPercentageX(float percentageX) {
        return (int) (imageBoundaries.x * percentageX);
    }

    public int getPXFromPercentageY(float percentageY) {
        return (int) (imageBoundaries.y * percentageY);
    }

    public float getPercenageXFromPX(int px) {
        return (float) px / imageBoundaries.x;
    }

    public float getPercenageYFromPX(int px) {
        return (float) px / imageBoundaries.y;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public int size() {
        return events.size();
    }

    public void setBackgroundImage(final Uri newImage) {

        Glide.with(getContext())
                .load(newImage)
                .centerInside()
                .into(backgroundImage)
                .getSize(new SizeReadyCallback() {
                    @Override
                    public void onSizeReady(final int width, final int height) {
                        if (eventMap.getWidth() != 0 && eventMap.getHeight() != 0) {
                            recalculateOffsets(width, height);
                        } else {
                            eventMap.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
                                    eventMap.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                    recalculateOffsets(width, height);
                                }
                            });
                        }
                    }
                });
    }

    public void setDefaultImage() {
        BitmapFactory.Options dimensions = new BitmapFactory.Options();
        dimensions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.img_city_map, dimensions);
        final int height = dimensions.outHeight;
        final int width =  dimensions.outWidth;

        if (eventMap.getWidth() != 0 && eventMap.getHeight() != 0) {
            recalculateOffsets(width, height);
        } else {
            eventMap.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    eventMap.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    recalculateOffsets(width, height);
                }
            });
        }
        backgroundImage.setImageResource(R.drawable.img_city_map);
    }

    private void recalculateOffsets(int imageWidth, int imageHeight) {
        float imageAspect = imageWidth / (float) imageHeight;
        float mapAspect = eventMap.getWidth() / (float) eventMap.getHeight();
        imageBoundaries = new Point(0, 0);

        if (imageAspect > mapAspect) {
            imageBoundaries.x = eventMap.getWidth();
            imageBoundaries.y = (int) (eventMap.getWidth() / imageAspect);

            backgroundOffsetX = 0;
            final float scaleRatio = eventMap.getWidth() / (float) imageBoundaries.x;
            backgroundOffsetY = ((int) (eventMap.getHeight() - (imageBoundaries.y * scaleRatio)) / 2);
        } else {
            imageBoundaries.y = eventMap.getHeight();
            imageBoundaries.x = (int) (eventMap.getHeight() * imageAspect);

            backgroundOffsetY = 0;
            final float scaleRatio = eventMap.getHeight() / (float) imageBoundaries.y;
            backgroundOffsetX = (int) ((eventMap.getWidth() - (imageBoundaries.x * scaleRatio)) / 2);
        }

        for (final EventPoint eventPoint : events) {
            eventPoint.setCoord(
                    getPXFromPercentageX(eventPoint.xPercentage),
                    getPXFromPercentageY(eventPoint.yPercentage),
                    backgroundOffsetX,
                    backgroundOffsetY);
            eventPoint.resize(1 / scale);
        }
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onCreateEvent(EventPoint eventPoint);

        void onDeleteEvent(EventPoint eventPoint);

        void onMoveEvent(EventPoint eventPoint);

        void onEventClick(EventPoint eventPoint);
    }
}
