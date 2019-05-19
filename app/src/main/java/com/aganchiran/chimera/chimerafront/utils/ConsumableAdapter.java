package com.aganchiran.chimera.chimerafront.utils;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.ConsumableModel;
import com.aganchiran.chimera.chimerafront.dialogs.CreateEditConsumableDialog;
import com.aganchiran.chimera.viewmodels.ConsumableViewModel;


public class ConsumableAdapter extends ItemAdapter<ConsumableModel, ConsumableAdapter.ConsumableHolder> {

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
        holder.setCurrentValue(currentConsumable.getCurrentValue());
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
                            CreateEditConsumableDialog dialog = new CreateEditConsumableDialog();
                            dialog.setListener(new CreateEditConsumableDialog.CreateConsumableDialogListener() {
                                @Override
                                public void saveConsumable(String newName, long newMax, long newMin) {
                                    final ConsumableModel consumable = ConsumableAdapter.this
                                            .getItemAt(ConsumableHolder.this.getAdapterPosition());

                                    final long current = consumable.getCurrentValue();
                                    if (current > newMax) {
                                        consumable.setCurrentValue(newMax);
                                    } else if (current < newMin) {
                                        consumable.setCurrentValue(newMin);
                                    }

                                    consumable.setName(newName);
                                    consumable.setMaxValue(newMax);
                                    consumable.setMinValue(newMin);

                                    final ConsumableViewModel consumableViewModel =
                                            ViewModelProviders.of(((AppCompatActivity)itemView
                                                    .getContext())).get(ConsumableViewModel.class);


                                    consumableViewModel.update(consumable);
                                }

                                @Override
                                public ConsumableModel getConsumable() {
                                    return ConsumableAdapter.this
                                            .getItemAt(ConsumableHolder.this.getAdapterPosition());
                                }
                            });
                            dialog.show(((AppCompatActivity) itemView.getContext()).getSupportFragmentManager(), "edit consumable");
                            return true;
                        default:
                            return false;
                    }
                }
            };
        }

        private void setCurrentValue(long numberWithSign) {
            final String formattedNumber;
            final long number = Math.abs(numberWithSign);
            long sign = (number != 0) ? numberWithSign / number : 0;


            if (number >= 1000000) {
                if (number < 10000000) {
                    formattedNumber = (Math.floor((number * sign / 1000000.0) * 10) / 10.0) + "M";
                    textViewCurrentValue.setText(formattedNumber);
                } else {
                    formattedNumber = number * sign / 1000000 + "M";
                    textViewCurrentValue.setText(formattedNumber);
                }
            } else if (number >= 1000) {
                if (number < 10000) {
                    formattedNumber = (Math.floor((number * sign / 1000.0) * 10) / 10.0) + "K";
                    textViewCurrentValue.setText(formattedNumber);
                } else {
                    formattedNumber = number * sign / 1000 + "K";
                    textViewCurrentValue.setText(formattedNumber);
                }
            } else {
                textViewCurrentValue.setText(String.valueOf(number * sign));
            }
        }
    }

}
