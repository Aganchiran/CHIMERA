package com.aganchiran.chimera.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.repositories.CharacterRepo;

public class CombatProfileVM extends AndroidViewModel{

    private CharacterRepo characterRepo;


    public CombatProfileVM(@NonNull Application application) {
        super(application);
        characterRepo = new CharacterRepo(application);
    }

    public void update(CharacterModel characterModel) {
        characterRepo.update(characterModel);
    }
}
