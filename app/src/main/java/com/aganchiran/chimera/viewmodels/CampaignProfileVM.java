package com.aganchiran.chimera.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.aganchiran.chimera.chimeracore.campaign.CampaignModel;
import com.aganchiran.chimera.repositories.CampaignRepo;

import java.util.List;

public class CampaignProfileVM extends AndroidViewModel implements ItemVM<CampaignModel> {

    private CampaignRepo campaignRepo;
    private LiveData<List<CampaignModel>> allCampaigns;

    public CampaignProfileVM(@NonNull Application application) {
        super(application);
        campaignRepo = new CampaignRepo(application);
        allCampaigns = campaignRepo.getAllCampaigns();
    }

    @Override
    public void insert(CampaignModel campaignModel){
        campaignRepo.insert(campaignModel);
    }

    @Override
    public void update(CampaignModel campaignModel){
        campaignRepo.update(campaignModel);
    }

    public void updateCampaigns(List<CampaignModel> campaignModelList){
        campaignRepo.updateCampaigns(campaignModelList);
    }

    @Override
    public void delete(CampaignModel campaignModel){
        campaignRepo.delete(campaignModel);
    }

    public void deleteAllCampaigns(){
        campaignRepo.deleteAllCampaigns();
    }

    public LiveData<CampaignModel> getCampaignById(int id){
        return campaignRepo.getCampaignById(id);
    }

    public LiveData<List<CampaignModel>> getAllCampaigns() {
        return allCampaigns;
    }
}
