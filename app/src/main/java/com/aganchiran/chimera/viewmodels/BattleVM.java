package com.aganchiran.chimera.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.chimeracore.combat.CombatModel;
import com.aganchiran.chimera.chimeracore.combatcharacter.CombatCharacter;
import com.aganchiran.chimera.repositories.CharacterRepo;
import com.aganchiran.chimera.repositories.CombatCharacterRepo;
import com.aganchiran.chimera.repositories.CombatRepo;

import java.util.List;

public class BattleVM extends AndroidViewModel {

    private CharacterRepo characterRepo;
    private CombatCharacterRepo combatCharacterRepo;
    private CombatRepo combatRepo;
    private LiveData<List<CharacterModel>> allCharacters;
    private LiveData<List<CharacterModel>> combatCharacters;


    public BattleVM(@NonNull Application application) {
        super(application);
        characterRepo = new CharacterRepo(application);
        combatCharacterRepo = new CombatCharacterRepo(application);
        combatRepo = new CombatRepo(application);
        allCharacters = characterRepo.getAllCharacters();
    }

    public void updateCombat(CombatModel combatModel){
        combatRepo.update(combatModel);
    }

    public void linkCharacterToCombat(int combatId, int characterId){
        combatCharacterRepo.linkCharacterToCombat(combatId, characterId);
    }

    public void linkCharactersToCombat(int combatId, List<Integer> charactersIds){
        combatCharacterRepo.linkCharactersToCombat(combatId, charactersIds);
    }

    public void unlinkCharacterFromCombat(int combatId, int characterId){
        combatCharacterRepo.unlinkCharacterToCombat(combatId, characterId);
    }

    public LiveData<List<CharacterModel>> getCharactersForCombat(int combatId){
        if (combatCharacters == null){
            combatCharacters = combatCharacterRepo.getCharactersForCombat(combatId);
        }
        return combatCharacters;
    }

    public LiveData<CombatModel> getCombatById(int combatId){
        return combatRepo.getCombatById(combatId);
    }

    public LiveData<List<CharacterModel>> getAllCharacters() {
        return allCharacters;
    }

    public LiveData<CombatCharacter> getCCByIds(int combatId, int characterId){
        return combatCharacterRepo.getCCByIds(combatId, characterId);
    }

    public void updateCharacters(List<CharacterModel> characterModels){
        characterRepo.updateCharacters(characterModels);
    }
}
