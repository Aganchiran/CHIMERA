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
import com.aganchiran.chimera.chimeracore.event.EventModel;
import com.aganchiran.chimera.chimeracore.eventcharacter.EventCharacter;
import com.aganchiran.chimera.repositories.CharacterRepo;
import com.aganchiran.chimera.repositories.ConsumableRepo;
import com.aganchiran.chimera.repositories.EventCharacterRepo;

import java.util.List;

public class EventCharactersListVM extends AndroidViewModel implements ItemVM<CharacterModel> {

    private CharacterRepo characterRepo;
    private EventCharacterRepo eventCharacterRepo;
    private ConsumableRepo consumableRepo;
    private LiveData<List<CharacterModel>> allCharacters;
    private LiveData<List<CharacterModel>> eventCharacters;
    private LiveData<List<EventCharacter>> ecs;
    private EventModel eventModel;

    public EventCharactersListVM(@NonNull final Application application) {
        super(application);
        consumableRepo = new ConsumableRepo(application);
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

    public void updateECs(final List<EventCharacter> eventCharacterList) {
        eventCharacterRepo.updateECs(eventCharacterList);
    }

    @Override
    public void delete(final CharacterModel characterModel) {
        characterRepo.delete(characterModel);
    }

    public LiveData<CharacterModel> getCharacterById(final int id) {
        return characterRepo.getCharacterById(id);
    }

    public LiveData<List<CharacterModel>> getCharactersForEvent(final int eventId) {
        if (eventCharacters == null) {
            eventCharacters = eventCharacterRepo.getCharactersForEvent(eventId);
        }
        return eventCharacters;
    }

    public LiveData<List<EventCharacter>> getECsForEvent(final int eventId) {
        if (ecs == null) {
            ecs = eventCharacterRepo.getECsForEvent(eventId);
        }
        return ecs;
    }

    public LiveData<List<CharacterModel>> getAllCharacters() {
        return allCharacters;
    }

    public void linkCharacter(final int characterId) {
        eventCharacterRepo.linkCharacterToEvent(eventModel.getId(), characterId);
    }

    public void linkCharacters(final List<Integer> charactersIds) {
        eventCharacterRepo.linkCharactersToEvent(eventModel.getId(), charactersIds);
    }

    public void unlinkCharacter(final int characterId) {
        eventCharacterRepo.unlinkCharacterToEvent(eventModel.getId(), characterId);
    }

    public EventModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(final EventModel eventModel) {
        this.eventModel = eventModel;
    }

    public LiveData<List<ConsumableModel>> getCharacterConsumables(final int characterId){
        return consumableRepo.getCharacterConsumables(characterId);
    }

}
