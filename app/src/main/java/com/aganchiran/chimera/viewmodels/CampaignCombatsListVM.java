package com.aganchiran.chimera.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.aganchiran.chimera.chimeracore.combat.CombatModel;
import com.aganchiran.chimera.repositories.CombatRepo;

import java.util.List;

public class CampaignCombatsListVM extends AndroidViewModel implements ItemVM<CombatModel> {

    private CombatRepo combatRepo;
    private LiveData<List<CombatModel>> allCombats;

    public CampaignCombatsListVM(@NonNull Application application) {
        super(application);
        combatRepo = new CombatRepo(application);
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

    public LiveData<List<CombatModel>> getCampaignCombats(int campaignId){
        return combatRepo.getCampaignCombats(campaignId);
    }

    public LiveData<List<CombatModel>> getAllCombats() {
        return allCombats;
    }

}
