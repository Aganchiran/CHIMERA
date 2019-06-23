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

package com.aganchiran.chimera.chimerafront.utils.listeners;

import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;

import com.aganchiran.chimera.R;

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
