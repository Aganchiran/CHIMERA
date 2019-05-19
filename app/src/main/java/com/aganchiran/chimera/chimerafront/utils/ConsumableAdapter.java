package com.aganchiran.chimera.chimerafront.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
        holder.textViewCurrentValue.setText(currentConsumable.getCurrentValueFormated());
    }

    class ConsumableHolder extends ItemAdapter.ItemHolder {
        private TextView textViewName;
        private TextView textViewCurrentValue;

        ConsumableHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.consumable_name);
            textViewCurrentValue = itemView.findViewById(R.id.consumable_value);
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
