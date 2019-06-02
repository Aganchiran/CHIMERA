package com.aganchiran.chimera.chimeracore.campaign;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.aganchiran.chimera.chimeracore.ItemModel;

@Entity(tableName = "campaign_table")
public class CampaignModel extends ItemModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String description;

    private int displayPosition;

    public CampaignModel(String name, String description) {
        this.name = name;
        this.description = description;
        this.displayPosition = Integer.MAX_VALUE;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDisplayPosition() {
        return displayPosition;
    }

    public void setDisplayPosition(int displayPosition) {
        this.displayPosition = displayPosition;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof CampaignModel
                && ((CampaignModel) obj).getId() == this.getId());
    }

    @Override
    public boolean contentsTheSame(Object obj) {
        return (obj instanceof CampaignModel
                && ((CampaignModel) obj).getId() == this.getId()
                && ((CampaignModel) obj).getName().equals(this.getName())
                && ((CampaignModel) obj).getDescription().equals(this.getDescription())
                && ((CampaignModel) obj).getDisplayPosition() == this.getDisplayPosition());
    }
}
