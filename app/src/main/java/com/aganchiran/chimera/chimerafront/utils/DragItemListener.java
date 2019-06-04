package com.aganchiran.chimera.chimerafront.utils;

import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import com.aganchiran.chimera.chimerafront.utils.adapters.ItemAdapter;

import java.util.Collections;

public class DragItemListener implements View.OnDragListener {

    private final ItemAdapter adapter;
    private int previousIndex = -1;
    private int previousNewIndex = -1;

    public DragItemListener(ItemAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean onDrag(final View recyclerView, DragEvent event) {
        final View hiddenView = (View) event.getLocalState();
        if (hiddenView == null) return false;

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                if (previousIndex < 0) {
                    previousIndex = ((RecyclerView) recyclerView).getChildAdapterPosition(hiddenView);
                }
                adapter.setFlyingItemPos(previousIndex);
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                final int scrollY = recyclerView.getScrollY();

                final boolean hoverScrollDown = event.getY() - scrollY > recyclerView.getBottom() - 250;
                final boolean hoverScrollUp = event.getY() - scrollY < recyclerView.getTop() + 250;
                final boolean hoverDeleteArea = event.getX() > recyclerView.getRight() - 250;

                if (hoverScrollDown && !hoverDeleteArea && recyclerView.canScrollVertically(1)) {
                    recyclerView.scrollBy(0, 30);
                } else if (hoverScrollUp && recyclerView.canScrollVertically(-1)) {
                    recyclerView.scrollBy(0, -30);
                } else {

                    int newIndex = calculateNewIndex(event.getX(), event.getY(), hiddenView,
                            (RecyclerView) recyclerView);

                    if (previousNewIndex == -1) previousNewIndex = newIndex;

                    if (hiddenView != ((RecyclerView) recyclerView).getChildAt(newIndex) && newIndex != previousNewIndex) {

                        if (previousIndex < newIndex) {
                            for (int i = previousIndex; i < newIndex; i++) {
                                Collections.swap(adapter.getItemModels(), i, i + 1);
                            }
                        } else {
                            for (int i = previousIndex; i > newIndex; i--) {
                                Collections.swap(adapter.getItemModels(), i, i - 1);
                            }
                        }
                        adapter.notifyItemMoved(previousIndex, newIndex);
                        previousIndex = newIndex;
                    }

                    previousNewIndex = newIndex;
                    adapter.setFlyingItemPos(previousNewIndex);
                }
                break;
            case DragEvent.ACTION_DROP:
                onDrop(hiddenView);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                previousIndex = -1;

                ItemAdapter.ItemHolder item = ((ItemAdapter.ItemHolder) ((RecyclerView) recyclerView)
                        .findViewHolderForAdapterPosition(adapter.getFlyingItemPos()));
                if (item != null) {
                    item.setVisibility(View.VISIBLE);

                }
                adapter.setFlyingItemPos(-1);

                hiddenView.setVisibility(View.VISIBLE);
                break;
        }
        return true;
    }

    private int calculateNewIndex(float x, float y, View v, RecyclerView recyclerView) {
        final ViewGroup.MarginLayoutParams layoutParams
                = (ViewGroup.MarginLayoutParams) v.getLayoutParams();

        final float viewWidth = v.getWidth();
        final float cellMarginsH = layoutParams.rightMargin + layoutParams.leftMargin;
        final float cellWidth = cellMarginsH + viewWidth;

        final int recyclerColumns = (int) (recyclerView.getWidth() / cellWidth);
        int column = (int) (x / cellWidth);
        if (column > recyclerColumns - 1) column = recyclerColumns - 1;
        if (column < 0) column = 0;


        final float viewHeight = v.getHeight();
        final float cellMarginsV = layoutParams.topMargin + layoutParams.bottomMargin;
        final float cellHeight = cellMarginsV + viewHeight;

        final int recyclerRows = (int) Math.ceil((double) adapter.getItemCount() / recyclerColumns);

        float scrollOffset = recyclerView.computeVerticalScrollOffset();
        int row = (int) Math.floor((y + scrollOffset) / cellHeight);
        if (!recyclerView.canScrollVertically(1)
                && recyclerView.canScrollVertically(-1)) {
            row = recyclerRows - (int) Math.ceil((recyclerView.getBottom() - y) / cellHeight);
        }
        if (row > recyclerRows - 1) row = recyclerRows - 1;
        if (row < 0) row = 0;


        int index = row * recyclerColumns + column;
        if (index > adapter.getItemCount() - 1) index = adapter.getItemCount() - 1;
        if (index < 0) index = 0;

        return index;
    }

    protected void onDrop(View hiddenView) {
        hiddenView.setVisibility(View.VISIBLE);
    }

}
