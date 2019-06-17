package com.aganchiran.chimera.chimerafront.utils.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.campaign.CampaignModel;


public class CampaignAdapter extends ItemAdapter<CampaignModel, CampaignAdapter.CampaignHolder> {

    private MenuActions menuActions;

    @NonNull
    @Override
    public CampaignHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_campaign, parent, false);
        return new CampaignHolder(itemView);
    }

    @Override
    public void onBindItemHolder(@NonNull CampaignHolder holder, int position) {
        CampaignModel currentCampaign = getItemAt(position);
        holder.textViewName.setText(currentCampaign.getName());
    }

    class CampaignHolder extends ItemAdapter.ItemHolder {
        private TextView textViewName;

        CampaignHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.campaign_name);
        }

        @Override
        protected int getPopupMenu() {
            return R.menu.menu_campaign_item;
        }

        @Override
        protected PopupMenu.OnMenuItemClickListener getPopupItemClickListener() {
            return new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.edit_campaign:
                            menuActions.editCampaign(CampaignAdapter.this.getItemAt(
                                    CampaignHolder.this.getAdapterPosition()));
                            return true;
                        case R.id.open_map:
                            menuActions.openMap(CampaignAdapter.this.getItemAt(
                                    CampaignHolder.this.getAdapterPosition()));
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
        void editCampaign(CampaignModel campaignModel);
        void openMap(CampaignModel campaignModel);
    }
}
