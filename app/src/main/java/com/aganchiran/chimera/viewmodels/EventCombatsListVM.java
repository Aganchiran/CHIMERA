package com.aganchiran.chimera.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.aganchiran.chimera.chimeracore.combat.CombatModel;
import com.aganchiran.chimera.chimeracore.event.EventModel;
import com.aganchiran.chimera.chimeracore.eventcombat.EventCombat;
import com.aganchiran.chimera.repositories.CombatRepo;
import com.aganchiran.chimera.repositories.EventCombatRepo;

import java.util.List;

public class EventCombatsListVM extends AndroidViewModel implements ItemVM<CombatModel> {

    private CombatRepo combatRepo;
    private EventCombatRepo eventCombatRepo;
    private LiveData<List<CombatModel>> allCombats;
    private LiveData<List<CombatModel>> eventCombats;
    private LiveData<List<EventCombat>> ecs;
    private EventModel eventModel;

    public EventCombatsListVM(@NonNull Application application) {
        super(application);
        combatRepo = new CombatRepo(application);
        combatRepo.setListener(new CombatRepo.OnInsertListener() {
            @Override
            public void onInsert(Long combatId) {
                linkCombat(combatId.intValue());
            }
        });
        eventCombatRepo = new EventCombatRepo(application);
        allCombats = combatRepo.getAllCombats();
    }

    @Override
    public void insert(CombatModel combatModel){
        combatRepo.insert(combatModel);
    }

    @Override
    public void update(CombatModel combatModel){
        combatRepo.update(combatModel);
    }

    public void updateCombats(List<CombatModel> combatModelList){
        combatRepo.updateCombats(combatModelList);
    }

    public void updateECs(List<EventCombat> eventCombatList){
        eventCombatRepo.updateECs(eventCombatList);
    }

    @Override
    public void delete(CombatModel combatModel){
        combatRepo.delete(combatModel);
    }

    public void deleteAllCombats(){
        combatRepo.deleteAllCombats();
    }

    public LiveData<CombatModel> getCombatById(int id){
        return combatRepo.getCombatById(id);
    }

    public LiveData<List<CombatModel>> getCombatsForEvent(int eventId){
        if (eventCombats == null){
            eventCombats = eventCombatRepo.getCombatsForEvent(eventId);
        }
        return eventCombats;
    }

    public LiveData<List<EventCombat>> getECsForEvent(int eventId){
        if (ecs == null){
            ecs = eventCombatRepo.getECsForEvent(eventId);
        }
        return ecs;
    }

    public LiveData<List<CombatModel>> getAllCombats() {
        return allCombats;
    }

    public void linkCombat(int combatId){
        eventCombatRepo.linkCombatToEvent(eventModel.getId(), combatId);
    }

    public void linkCombats(List<Integer> combatsIds){
        eventCombatRepo.linkCombatsToEvent(eventModel.getId(), combatsIds);
    }

    public void unlinkCombat(int combatId){
        eventCombatRepo.unlinkCombatToEvent(eventModel.getId(), combatId);
    }

    public EventModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(EventModel eventModel) {
        this.eventModel = eventModel;
    }
}
