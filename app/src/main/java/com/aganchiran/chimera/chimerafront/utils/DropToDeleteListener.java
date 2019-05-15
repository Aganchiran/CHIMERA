package com.aganchiran.chimera.chimerafront.utils;

import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.viewmodels.ItemViewModel;

public class DropToDeleteListener implements View.OnDragListener {

    private ItemAdapter adapter;
    private ItemViewModel itemViewModel;

    public DropToDeleteListener(ItemAdapter adapter,
                                ItemViewModel itemViewModel) {
        this.adapter = adapter;
        this.itemViewModel = itemViewModel;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                ((ImageView) v).setImageResource(R.drawable.ic_delete);
                v.setAlpha(1f);
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                ((ImageView) v).setImageResource(R.drawable.ic_delete_hover);
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                ((ImageView) v).setImageResource(R.drawable.ic_delete);
                break;
            case DragEvent.ACTION_DROP:
                Object item = adapter.getItemAt(adapter.getFlyingItemPos());
                try {
                    itemViewModel.delete(item);
                }catch (ClassCastException e){
                    throw new ClassCastException("Adapter and ViewModel doesn't have the same type");
                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                ((ImageView) v).setImageResource(R.drawable.ic_delete);
                v.setAlpha(0);
                break;
        }
        return true;
    }

}
