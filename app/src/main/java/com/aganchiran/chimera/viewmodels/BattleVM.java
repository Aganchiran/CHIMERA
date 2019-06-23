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

import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.chimeracore.combat.CombatModel;
import com.aganchiran.chimera.repositories.CharacterRepo;
import com.aganchiran.chimera.repositories.CombatCharacterRepo;
import com.aganchiran.chimera.repositories.CombatRepo;

import org.apache.commons.collections4.list.SetUniqueList;

import java.util.ArrayList;
import java.util.List;

public class BattleVM extends AndroidViewModel {

    private CharacterRepo characterRepo;
    private CombatCharacterRepo combatCharacterRepo;
    private CombatRepo combatRepo;
    private LiveData<List<CharacterModel>> combatCharacters;
    private int combatId;
    private LiveData<CombatModel> combat;
    private List<CharacterModel> defenders = SetUniqueList.setUniqueList(new ArrayList<CharacterModel>());


    public BattleVM(@NonNull Application application) {
        super(application);
        characterRepo = new CharacterRepo(application);
        combatCharacterRepo = new CombatCharacterRepo(application);
        combatRepo = new CombatRepo(application);
    }

    public void updateCombat(CombatModel combatModel) {
        combatRepo.update(combatModel);
    }

    public void linkCharactersToCombat(List<Integer> charactersIds) {
        combatCharacterRepo.linkCharactersToCombat(combatId, charactersIds);
    }

    public void unlinkCharacterFromCombat(int characterId) {
        combatCharacterRepo.unlinkCharacterToCombat(combatId, characterId);
    }

    public LiveData<List<CharacterModel>> getCharactersForCombat() {
        if (combatCharacters == null) {
            combatCharacters = combatCharacterRepo.getCharactersForCombat(combatId);
        }
        return combatCharacters;
    }

    public void setCombat(int combatId) {
        this.combatId = combatId;
        this.combat = combatRepo.getCombatById(combatId);
    }

    public LiveData<CombatModel> getCombat() {
        return combat;
    }

    public void updateCharacters(List<CharacterModel> characterModels) {
        characterRepo.updateCharacters(characterModels);
    }

    public List<CharacterModel> getDefenders() {
        return defenders;
    }
}
