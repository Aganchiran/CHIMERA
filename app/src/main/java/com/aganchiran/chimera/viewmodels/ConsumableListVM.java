package com.aganchiran.chimera.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.aganchiran.chimera.chimeracore.consumable.ConsumableModel;
import com.aganchiran.chimera.repositories.ConsumableRepo;

import java.util.List;

public class ConsumableListVM extends AndroidViewModel implements ItemVM<ConsumableModel> {

    private ConsumableRepo consumableRepo;
    private LiveData<List<ConsumableModel>> allConsumables;

    public ConsumableListVM(@NonNull Application application) {
        super(application);
        consumableRepo = new ConsumableRepo(application);
        allConsumables = consumableRepo.getAllConsumables();
    }

    @Override
    public void insert(ConsumableModel consumableModel){
        consumableRepo.insert(consumableModel);
    }

    @Override
    public void update(ConsumableModel consumableModel){
        consumableRepo.update(consumableModel);
    }

    public void updateConsumables(List<ConsumableModel> consumableModelList){
        consumableRepo.updateConsumables(consumableModelList);
    }

    @Override
    public void delete(ConsumableModel consumableModel){
        consumableRepo.delete(consumableModel);
    }

    public void deleteAllConsumables(){
        consumableRepo.deleteAllConsumables();
    }

    public LiveData<ConsumableModel> getConsumableById(int id){
        return consumableRepo.getConsumableById(id);
    }

    public LiveData<List<ConsumableModel>> getCharacterConsumables(int characterId){
        return consumableRepo.getCharacterConsumables(characterId);
    }

    public LiveData<List<ConsumableModel>> getAllConsumables() {
        return allConsumables;
    }
}
