package com.aganchiran.chimera.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.aganchiran.chimera.chimeracore.ConsumableModel;
import com.aganchiran.chimera.repositories.ConsumableRepository;

import java.util.List;

public class ConsumableViewModel extends AndroidViewModel implements ItemViewModel<ConsumableModel>{

    private ConsumableRepository consumableRepository;
    private LiveData<List<ConsumableModel>> allConsumables;

    public ConsumableViewModel(@NonNull Application application) {
        super(application);
        consumableRepository = new ConsumableRepository(application);
        allConsumables = consumableRepository.getAllConsumables();
    }

    @Override
    public void insert(ConsumableModel consumableModel){
        consumableRepository.insert(consumableModel);
    }

    @Override
    public void update(ConsumableModel consumableModel){
        consumableRepository.update(consumableModel);
    }

    @Override
    public void delete(ConsumableModel consumableModel){
        consumableRepository.delete(consumableModel);
    }

    public void deleteAllConsumables(){
        consumableRepository.deleteAllConsumables();
    }

    public LiveData<ConsumableModel> getConsumableById(int id){
        return consumableRepository.getConsumableById(id);
    }

    public LiveData<List<ConsumableModel>> getCharacterConsumables(int characterId){
        return consumableRepository.getCharacterConsumables(characterId);
    }

    public LiveData<List<ConsumableModel>> getAllConsumables() {
        return allConsumables;
    }
}
