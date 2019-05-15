package com.aganchiran.chimera.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.aganchiran.chimera.chimeracore.CharacterModel;
import com.aganchiran.chimera.repositories.CharacterRepository;

import java.util.List;

public class CharacterViewModel extends AndroidViewModel implements ItemViewModel<CharacterModel> {

    private CharacterRepository characterRepository;
    private LiveData<List<CharacterModel>> allCharacters;

    public CharacterViewModel(@NonNull Application application) {
        super(application);
        characterRepository = new CharacterRepository(application);
        allCharacters = characterRepository.getAllCharacters();
    }

    @Override
    public void insert(CharacterModel characterModel){
        characterRepository.insert(characterModel);
    }

    @Override
    public void update(CharacterModel characterModel){
        characterRepository.update(characterModel);
    }

    public void updateCharacters(List<CharacterModel> characterModelList){
        characterRepository.updateCharacters(characterModelList);
    }

    @Override
    public void delete(CharacterModel characterModel){
        characterRepository.delete(characterModel);
    }

    public void deleteAllCharacters(){
        characterRepository.deleteAllCharacters();
    }

    public LiveData<CharacterModel> getCharacterById(int id){
        return characterRepository.getCharacterById(id);
    }

    public LiveData<List<CharacterModel>> getAllCharacters() {
        return allCharacters;
    }
}
