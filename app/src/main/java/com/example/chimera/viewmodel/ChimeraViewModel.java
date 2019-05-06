package com.example.chimera.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.chimera.chimeracore.CharacterModel;
import com.example.chimera.chimeradata.ChimeraRepository;

import java.util.List;

public class ChimeraViewModel extends AndroidViewModel {

    private ChimeraRepository repository;
    private LiveData<List<CharacterModel>> allCharacters;

    public ChimeraViewModel(@NonNull Application application) {
        super(application);
        repository = new ChimeraRepository(application);
        allCharacters = repository.getAllCharacters();
    }

    public void insert(CharacterModel characterModel){
        repository.insert(characterModel);
    }

    public void update(CharacterModel characterModel){
        repository.update(characterModel);
    }

    public void delete(CharacterModel characterModel){
        repository.delete(characterModel);
    }

    public void deleteAllCharacters(){
        repository.deleteAllCharacters();
    }

    public LiveData<List<CharacterModel>> getAllCharacters() {
        return allCharacters;
    }
}
