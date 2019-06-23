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

import com.aganchiran.chimera.chimerafront.utils.adapters.ItemAdapter;
import com.aganchiran.chimera.viewmodels.ItemVM;

public class DropToDeleteRecyclerListener extends AbstractDropToListener {

    private ItemAdapter adapter;
    private ItemVM itemVM;

    public DropToDeleteRecyclerListener(ItemAdapter adapter, ItemVM itemVM) {
        this.adapter = adapter;
        this.itemVM = itemVM;
    }

    @Override
    protected void onDrop() {
        if (adapter.getFlyingItemPos() != -1) {
            Object item = adapter.getItemAt(adapter.getFlyingItemPos());
            try {
                itemVM.delete(item);
            } catch (ClassCastException e) {
                throw new ClassCastException("Adapter and ViewModel doesn't have the same type");
            }
        }
    }

}
