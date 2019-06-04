package com.aganchiran.chimera.chimeracore.combat;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.aganchiran.chimera.chimeracore.ItemModel;
import com.aganchiran.chimera.chimeracore.campaign.CampaignModel;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "combat_table", foreignKeys = {@ForeignKey(onDelete = CASCADE, entity = CampaignModel.class, parentColumns = "id", childColumns = "campaignId")}, indices = {@Index("campaignId")})
public class CombatModel extends ItemModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private int displayPosition;

    private int campaignId;

    public CombatModel(String name, int campaignId) {
        this.name = name;
        this.displayPosition = Integer.MAX_VALUE;
        this.campaignId = campaignId;
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

    public int getDisplayPosition() {
        return displayPosition;
    }

    public void setDisplayPosition(int displayPosition) {
        this.displayPosition = displayPosition;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof CombatModel
                && ((CombatModel) obj).getId() == this.getId());
    }

    @Override
    public boolean contentsTheSame(Object obj) {
        return (obj instanceof CombatModel
                && ((CombatModel) obj).getId() == this.getId()
                && ((CombatModel) obj).getName().equals(this.getName())
                && ((CombatModel) obj).getDisplayPosition() == this.getDisplayPosition());
    }
}
