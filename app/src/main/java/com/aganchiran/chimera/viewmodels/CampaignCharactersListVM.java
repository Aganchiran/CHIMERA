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
import com.aganchiran.chimera.chimeracore.consumable.ConsumableModel;
import com.aganchiran.chimera.repositories.CharacterRepo;
import com.aganchiran.chimera.repositories.ConsumableRepo;

import java.util.List;

public class CampaignCharactersListVM extends AndroidViewModel implements ItemVM<CharacterModel> {

    private CharacterRepo characterRepo;
    private ConsumableRepo consumableRepo;
    private LiveData<List<CharacterModel>> allCharacters;

    public CampaignCharactersListVM(@NonNull final Application application) {
        super(application);
        characterRepo = new CharacterRepo(application);
        consumableRepo = new ConsumableRepo(application);
        allCharacters = characterRepo.getAllCharacters();
    }

    @Override
    public void insert(final CharacterModel characterModel) {
        characterRepo.insert(characterModel);
    }

    public void duplicateCharacter(final CharacterModel character, final List<ConsumableModel> characterConsumables){
        characterRepo.duplicateCharacter(character, characterConsumables);
    }

    @Override
    public void update(final CharacterModel characterModel) {
        characterRepo.update(characterModel);
    }

    public void updateCharacters(final List<CharacterModel> characterModelList) {
        characterRepo.updateCharacters(characterModelList);
    }

    @Override
    public void delete(final CharacterModel characterModel) {
        characterRepo.delete(characterModel);
    }

    public LiveData<CharacterModel> getCharacterById(final int id) {
        return characterRepo.getCharacterById(id);
    }

    public LiveData<List<CharacterModel>> getCampaignCharacters(final int campaignId) {
        return characterRepo.getCampaignCharacters(campaignId);
    }

    public LiveData<List<CharacterModel>> getCharactersWithoutCampaign() {
        return characterRepo.getCharactersWithoutCampaign();
    }

    public LiveData<List<ConsumableModel>> getCharacterConsumables(final int characterId){
        return consumableRepo.getCharacterConsumables(characterId);
    }

    public LiveData<List<CharacterModel>> getAllCharacters() {
        return allCharacters;
    }

}
