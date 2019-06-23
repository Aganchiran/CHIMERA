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

package com.aganchiran.chimera.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.aganchiran.chimera.chimeracore.campaign.CampaignModel;
import com.aganchiran.chimera.repositories.CampaignRepo;

import java.util.List;

public class CampaignDetailsVM extends AndroidViewModel implements ItemVM<CampaignModel> {

    private CampaignRepo campaignRepo;
    private LiveData<List<CampaignModel>> allCampaigns;

    public CampaignDetailsVM(@NonNull Application application) {
        super(application);
        campaignRepo = new CampaignRepo(application);
        allCampaigns = campaignRepo.getAllCampaigns();
    }

    @Override
    public void insert(CampaignModel campaignModel) {
        campaignRepo.insert(campaignModel);
    }

    @Override
    public void update(CampaignModel campaignModel) {
        campaignRepo.update(campaignModel);
    }

    public void updateCampaigns(List<CampaignModel> campaignModelList) {
        campaignRepo.updateCampaigns(campaignModelList);
    }

    @Override
    public void delete(CampaignModel campaignModel) {
        campaignRepo.delete(campaignModel);
    }

    public void deleteAllCampaigns() {
        campaignRepo.deleteAllCampaigns();
    }

    public LiveData<CampaignModel> getCampaignById(int id) {
        return campaignRepo.getCampaignById(id);
    }

    public LiveData<List<CampaignModel>> getAllCampaigns() {
        return allCampaigns;
    }
}
