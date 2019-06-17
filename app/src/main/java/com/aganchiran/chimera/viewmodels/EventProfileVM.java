package com.aganchiran.chimera.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.aganchiran.chimera.chimeracore.event.EventModel;
import com.aganchiran.chimera.repositories.EventRepo;

import java.util.List;

public class EventProfileVM extends AndroidViewModel implements ItemVM<EventModel> {

    private EventRepo eventRepo;
    private LiveData<List<EventModel>> allEvents;

    public EventProfileVM(@NonNull Application application) {
        super(application);
        eventRepo = new EventRepo(application);
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

    public LiveData<List<EventModel>> getAllEvents() {
        return allEvents;
    }
}
