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
