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
import android.widget.TextView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.combat.CombatModel;


public class CombatAdapter extends ItemAdapter<CombatModel, CombatAdapter.CombatHolder> {

    private MenuActions menuActions;

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
                            menuActions.editCombat(CombatAdapter.this.getItemAt(
                                    CombatHolder.this.getAdapterPosition()));
                            return true;
                        default:
                            return false;
                    }
                }
            };
        }
    }

    public void setMenuActions(MenuActions menuActions) {
        this.menuActions = menuActions;
    }

    public interface MenuActions {
        void editCombat(CombatModel combatModel);
    }
}
