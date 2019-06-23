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

import com.aganchiran.chimera.chimeracore.combat.CombatModel;
import com.aganchiran.chimera.repositories.CombatRepo;

import java.util.List;

public class CombatSelectionVM extends AndroidViewModel implements ItemVM<CombatModel> {

    private CombatRepo combatRepo;
    private LiveData<List<CombatModel>> allCombats;

    public CombatSelectionVM(@NonNull Application application) {
        super(application);
        combatRepo = new CombatRepo(application);
        allCombats = combatRepo.getAllCombats();
    }

    @Override
    public void insert(CombatModel combatModel) {
        combatRepo.insert(combatModel);
    }

    @Override
    public void update(CombatModel combatModel) {
        combatRepo.update(combatModel);
    }

    public void updateCombats(List<CombatModel> combatModelList) {
        combatRepo.updateCombats(combatModelList);
    }

    @Override
    public void delete(CombatModel combatModel) {
        combatRepo.delete(combatModel);
    }

    public void deleteAllCombats() {
        combatRepo.deleteAllCombats();
    }

    public LiveData<CombatModel> getCombatById(int id) {
        return combatRepo.getCombatById(id);
    }

    public LiveData<List<CombatModel>> getAllCombats() {
        return allCombats;
    }

    public LiveData<List<CombatModel>> getCampaignCombats(int campaignId) {
        return combatRepo.getCampaignCombats(campaignId);
    }
}
