package com.aganchiran.chimera.chimerafront.utils.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.combat.CombatModel;


public class CombatAdapter extends ItemAdapter<CombatModel, CombatAdapter.CombatHolder> {

    private EditCombat editCombat;

    @NonNull
    @Override
    public CombatHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_combat, parent, false);
        return new CombatHolder(itemView);
    }

    @Override
    public void onBindItemHolder(@NonNull CombatHolder holder, int position) {
        CombatModel currentCombat = getItemAt(position);
        holder.textViewName.setText(currentCombat.getName());
    }

    class CombatHolder extends ItemAdapter.ItemHolder {
        private TextView textViewName;

        CombatHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.combat_name);
        }

        @Override
        protected int getPopupMenu() {
            return R.menu.menu_combat_item;
        }

        @Override
        protected PopupMenu.OnMenuItemClickListener getPopupItemClickListener() {
            return new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.edit_combat:
                            editCombat.perform(CombatAdapter.this.getItemAt(
                                    CombatHolder.this.getAdapterPosition()));
                            return true;
                        default:
                            return false;
                    }
                }
            };
        }
    }

    public void setEditCombat(EditCombat editCombat) {
        this.editCombat = editCombat;
    }

    public interface EditCombat {
        void perform(CombatModel combatModel);
    }
}
