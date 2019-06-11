package com.aganchiran.chimera.chimerafront.utils.listeners;

import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimerafront.utils.adapters.ItemAdapter;
import com.aganchiran.chimera.viewmodels.ItemVM;

public abstract class AbstractDropToListener implements View.OnDragListener {

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
                onDrop();
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                ((ImageView) v).setImageResource(R.drawable.ic_delete);
                v.setAlpha(0);
                break;
        }
        return true;
    }

    protected abstract void onDrop();

}
