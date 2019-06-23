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
import com.aganchiran.chimera.chimeracore.event.EventModel;
import com.aganchiran.chimera.chimeracore.eventcharacter.EventCharacter;
import com.aganchiran.chimera.repositories.CharacterRepo;
import com.aganchiran.chimera.repositories.EventCharacterRepo;

import java.util.List;

public class EventCharactersListVM extends AndroidViewModel implements ItemVM<CharacterModel> {

    private CharacterRepo characterRepo;
    private EventCharacterRepo eventCharacterRepo;
    private LiveData<List<CharacterModel>> allCharacters;
    private LiveData<List<CharacterModel>> eventCharacters;
    private LiveData<List<EventCharacter>> ecs;
    private EventModel eventModel;

    public EventCharactersListVM(@NonNull Application application) {
        super(application);
        characterRepo = new CharacterRepo(application);
        characterRepo.setListener(new CharacterRepo.OnInsertListener() {
            @Override
            public void onInsert(Long characterId) {
                linkCharacter(characterId.intValue());
            }
        });
        eventCharacterRepo = new EventCharacterRepo(application);
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

    public void updateECs(List<EventCharacter> eventCharacterList) {
        eventCharacterRepo.updateECs(eventCharacterList);
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

    public LiveData<List<CharacterModel>> getCharactersForEvent(int eventId) {
        if (eventCharacters == null) {
            eventCharacters = eventCharacterRepo.getCharactersForEvent(eventId);
        }
        return eventCharacters;
    }

    public LiveData<List<EventCharacter>> getECsForEvent(int eventId) {
        if (ecs == null) {
            ecs = eventCharacterRepo.getECsForEvent(eventId);
        }
        return ecs;
    }

    public LiveData<List<CharacterModel>> getAllCharacters() {
        return allCharacters;
    }

    public void linkCharacter(int characterId) {
        eventCharacterRepo.linkCharacterToEvent(eventModel.getId(), characterId);
    }

    public void linkCharacters(List<Integer> charactersIds) {
        eventCharacterRepo.linkCharactersToEvent(eventModel.getId(), charactersIds);
    }

    public void unlinkCharacter(int characterId) {
        eventCharacterRepo.unlinkCharacterToEvent(eventModel.getId(), characterId);
    }

    public EventModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(EventModel eventModel) {
        this.eventModel = eventModel;
    }
}
