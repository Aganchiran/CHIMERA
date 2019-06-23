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
    public void insert(ConsumableModel consumableModel) {
        consumableRepo.insert(consumableModel);
    }

    @Override
    public void update(ConsumableModel consumableModel) {
        consumableRepo.update(consumableModel);
    }

    public void updateConsumables(List<ConsumableModel> consumableModelList) {
        consumableRepo.updateConsumables(consumableModelList);
    }

    @Override
    public void delete(ConsumableModel consumableModel) {
        consumableRepo.delete(consumableModel);
    }

    public void deleteAllConsumables() {
        consumableRepo.deleteAllConsumables();
    }

    public LiveData<ConsumableModel> getConsumableById(int id) {
        return consumableRepo.getConsumableById(id);
    }

    public LiveData<List<ConsumableModel>> getCharacterConsumables(int characterId) {
        return consumableRepo.getCharacterConsumables(characterId);
    }

    public LiveData<List<ConsumableModel>> getAllConsumables() {
        return allConsumables;
    }
}
