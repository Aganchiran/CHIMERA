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

package com.aganchiran.chimera.chimerafront.utils.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.consumable.ConsumableModel;


public class ConsumableAdapter extends ItemAdapter<ConsumableModel, ConsumableAdapter.ConsumableHolder> {

    private SaveConsumable saveConsumable;

    @NonNull
    @Override
    public ConsumableHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_consumable, parent, false);
        return new ConsumableHolder(itemView);
    }

    @Override
    public void onBindItemHolder(@NonNull ConsumableHolder holder, int position) {
        ConsumableModel currentConsumable = getItemAt(position);
        holder.textViewName.setText(currentConsumable.getName());
        holder.textViewCurrentValue.setText(currentConsumable.getValueFormated());
        int percentage = (int) (((double) currentConsumable.getCurrentValue()
                / (double) currentConsumable.getMaxValue()) * 100);
        holder.progressBar.setProgress(percentage);
    }

    class ConsumableHolder extends ItemAdapter.ItemHolder {
        private final ProgressBar progressBar;
        private TextView textViewName;
        private TextView textViewCurrentValue;

        ConsumableHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.consumable_name);
            textViewCurrentValue = itemView.findViewById(R.id.consumable_value);
            progressBar = itemView.findViewById(R.id.consumable_progress);
        }

        @Override
        protected int getPopupMenu() {
            return R.menu.menu_consumable_item;
        }

        @Override
        protected PopupMenu.OnMenuItemClickListener getPopupItemClickListener() {
            return new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.edit_consumable:
                            final ConsumableModel consumable = ConsumableAdapter.this
                                    .getItemAt(ConsumableHolder.this.getAdapterPosition());
                            saveConsumable.perform(consumable);
                            return true;
                        default:
                            return false;
                    }
                }
            };
        }
    }

    private SaveConsumable getSaveConsumable() {
        return saveConsumable;
    }

    public void setSaveConsumable(SaveConsumable saveConsumable) {
        this.saveConsumable = saveConsumable;
    }

    public interface SaveConsumable {
        void perform(ConsumableModel consumableModel);
    }

}
