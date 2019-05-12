package com.aganchiran.chimera.chimerafront.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

public final class ScreenSizeManager {
    /**
     * Retrieves the screen width in dp.
     *
     * @param context the context of the activity from is called.
     * @return the screen width in dp.
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * Retrieves the screen height in dp.
     *
     * @param context the context of the activity from is called.
     * @return the screen height in dp.
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return ((int)(displayMetrics.heightPixels / displayMetrics.density));
    }

    public static int getViewWidth(View view) {
        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        return view.getMeasuredWidth();
    }
}
