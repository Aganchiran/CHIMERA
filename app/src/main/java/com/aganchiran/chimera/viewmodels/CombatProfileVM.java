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
import java.util.Objects;

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
