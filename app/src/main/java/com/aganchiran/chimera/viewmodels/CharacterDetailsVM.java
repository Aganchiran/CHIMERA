package com.aganchiran.chimera.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.repositories.CharacterRepo;

import java.util.List;

public class CharacterDetailsVM extends AndroidViewModel implements ItemVM<CharacterModel> {

    private CharacterRepo characterRepo;
    private LiveData<List<CharacterModel>> allCharacters;

    public CharacterDetailsVM(@NonNull Application application) {
        super(application);
        characterRepo = new CharacterRepo(application);
        allCharacters = characterRepo.getAllCharacters();
    }

    @Override
    public void insert(CharacterModel characterModel){
        characterRepo.insert(characterModel);
    }

    @Override
    public void update(CharacterModel characterModel){
        characterRepo.update(characterModel);
    }

    public void updateCharacters(List<CharacterModel> characterModelList){
        characterRepo.updateCharacters(characterModelList);
    }

    @Override
    public void delete(CharacterModel characterModel){
        characterRepo.delete(characterModel);
    }

    public void deleteAllCharacters(){
        characterRepo.deleteAllCharacters();
    }

    public LiveData<CharacterModel> getCharacterById(int id){
        return characterRepo.getCharacterById(id);
    }

    public LiveData<List<CharacterModel>> getAllCharacters() {
        return allCharacters;
    }
}
