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
    public void insert(EventModel eventModel){
        eventRepo.insert(eventModel);
    }

    @Override
    public void update(EventModel eventModel){
        eventRepo.update(eventModel);
    }

    public void updateCampaign(CampaignModel campaignModel) {
        campaignRepo.update(campaignModel);
    }

    public void updateEvents(List<EventModel> eventModelList){
        eventRepo.updateEvents(eventModelList);
    }

    @Override
    public void delete(EventModel eventModel){
        eventRepo.delete(eventModel);
    }

    public void deleteAllEvents(){
        eventRepo.deleteAllEvents();
    }

    public LiveData<EventModel> getEventById(int id){
        return eventRepo.getEventById(id);
    }

    public LiveData<List<EventModel>> getCampaignEvents(int campaignId){
        return eventRepo.getCampaignEvents(campaignId);
    }

    public LiveData<EventModel> getEventByCoordsAndCampaign(int xCoord, int yCoord, int campaignId) {
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
