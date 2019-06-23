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
import com.aganchiran.chimera.repositories.CharacterRepo;

import java.util.List;

public class CampaignCharactersListVM extends AndroidViewModel implements ItemVM<CharacterModel> {

    private CharacterRepo characterRepo;
    private LiveData<List<CharacterModel>> allCharacters;

    public CampaignCharactersListVM(@NonNull Application application) {
        super(application);
        characterRepo = new CharacterRepo(application);
        allCharacters = characterRepo.getAllCharacters();
    }

    @Override
    public void insert(CharacterModel characterModel) {
        characterRepo.insert(characterModel);
    }

    @Override
    public void update(CharacterModel characterModel) {
        characterRepo.update(characterModel);
    }

    public void updateCharacters(List<CharacterModel> characterModelList) {
        characterRepo.updateCharacters(characterModelList);
    }

    @Override
    public void delete(CharacterModel characterModel) {
        characterRepo.delete(characterModel);
    }

    public void deleteAllCharacters() {
        characterRepo.deleteAllCharacters();
    }

    public LiveData<CharacterModel> getCharacterById(int id) {
        return characterRepo.getCharacterById(id);
    }

    public LiveData<List<CharacterModel>> getCampaignCharacters(int campaignId) {
        return characterRepo.getCampaignCharacters(campaignId);
    }

    public LiveData<List<CharacterModel>> getCharactersWithoutCampaign() {
        return characterRepo.getCharactersWithoutCampaign();
    }

    public LiveData<List<CharacterModel>> getAllCharacters() {
        return allCharacters;
    }

}
