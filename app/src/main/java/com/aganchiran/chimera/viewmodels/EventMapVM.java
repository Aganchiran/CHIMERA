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
import com.aganchiran.chimera.chimeracore.event.EventModel;
import com.aganchiran.chimera.repositories.CampaignRepo;
import com.aganchiran.chimera.repositories.EventRepo;

import java.util.List;

public class EventMapVM extends AndroidViewModel implements ItemVM<EventModel> {

    private EventRepo eventRepo;
    private CampaignRepo campaignRepo;
    private LiveData<List<EventModel>> allEvents;
    private CampaignModel campaignModel;

    public EventMapVM(@NonNull Application application) {
        super(application);
        eventRepo = new EventRepo(application);
        campaignRepo = new CampaignRepo(application);
        allEvents = eventRepo.getAllEvents();
    }

    @Override
    public void insert(EventModel eventModel) {
        eventRepo.insert(eventModel);
    }

    @Override
    public void update(EventModel eventModel) {
        eventRepo.update(eventModel);
    }

    public void updateCampaign(CampaignModel campaignModel) {
        campaignRepo.update(campaignModel);
    }

    public void updateEvents(List<EventModel> eventModelList) {
        eventRepo.updateEvents(eventModelList);
    }

    @Override
    public void delete(EventModel eventModel) {
        eventRepo.delete(eventModel);
    }

    public void deleteAllEvents() {
        eventRepo.deleteAllEvents();
    }

    public LiveData<EventModel> getEventById(int id) {
        return eventRepo.getEventById(id);
    }

    public LiveData<List<EventModel>> getCampaignEvents(int campaignId) {
        return eventRepo.getCampaignEvents(campaignId);
    }

    public LiveData<EventModel> getEventByCoordsAndCampaign(float xCoord, float yCoord, int campaignId) {
        return eventRepo.getEventByCoordsAndCampaign(xCoord, yCoord, campaignId);
    }

    public LiveData<List<EventModel>> getAllEvents() {
        return allEvents;
    }

    public CampaignModel getCampaignModel() {
        return campaignModel;
    }

    public void setCampaignModel(CampaignModel campaignModel) {
        this.campaignModel = campaignModel;
    }
}
