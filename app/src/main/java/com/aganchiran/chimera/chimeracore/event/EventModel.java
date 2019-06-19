package com.aganchiran.chimera.chimeracore.event;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.aganchiran.chimera.chimeracore.campaign.CampaignModel;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "event_table", foreignKeys = {@ForeignKey(onDelete = CASCADE, entity = CampaignModel.class, parentColumns = "id", childColumns = "campaignId")}, indices = {@Index("campaignId")})
public class EventModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String description;

    private float xCoord;

    private float yCoord;

    private Integer campaignId;

    public EventModel(String name, String description, float xCoord, float yCoord, int campaignId) {
        this.name = name;
        this.description = description;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.campaignId = campaignId;
    }

    public int getId() {
        return id;
    }

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

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public float getXCoord() {
        return xCoord;
    }

    public void setXCoord(float xCoord) {
        this.xCoord = xCoord;
    }

    public float getYCoord() {
        return yCoord;
    }

    public void setYCoord(float yCoord) {
        this.yCoord = yCoord;
    }
}
