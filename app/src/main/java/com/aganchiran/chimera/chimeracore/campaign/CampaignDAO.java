package com.aganchiran.chimera.chimeracore.campaign;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public abstract class CampaignDAO {

    @Insert
    public abstract Long insert(CampaignModel campaignModel);

    @Update
    public abstract void update(CampaignModel campaignModel);

    @Transaction
    public void updateCampaigns(List<CampaignModel> campaigns){
        for (CampaignModel campaign : campaigns) {
            update(campaign);
        }
    }

    @Delete
    public abstract void delete(CampaignModel campaignModel);

    @Query("DELETE FROM campaign_table")
    public abstract void deleteAllCampaigns();

    @Query("SELECT * FROM campaign_table WHERE id = :id")
    public abstract LiveData<CampaignModel> getCampaignById(int id);

    @Query("SELECT * FROM campaign_table ORDER BY displayPosition ASC")
    public abstract LiveData<List<CampaignModel>> getAllCampaigns();
}
