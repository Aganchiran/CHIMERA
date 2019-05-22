package com.aganchiran.chimera.viewmodels.battle;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.chimeracore.combat.CombatModel;
import com.aganchiran.chimera.chimeracore.combatcharacter.CombatCharacter;
import com.aganchiran.chimera.chimerafront.utils.InitiativeAdapter;
import com.aganchiran.chimera.repositories.CharacterRepo;
import com.aganchiran.chimera.repositories.CombatCharacterRepo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BattleVM extends AndroidViewModel {

    private CharacterRepo characterRepo;
    private CombatCharacterRepo combatCharacterRepo;
    private LiveData<List<CharacterModel>> allCharacters;
    private LiveData<List<CharacterModel>> combatCharacters;
    private List<CharacterModel> iniCharacters;
    private InitiativeAdapter initiativeAdapter;
    private CombatModel combat;


    public BattleVM(@NonNull Application application, CombatModel combat) {
        super(application);
        this.combat = combat;

        characterRepo = new CharacterRepo(application);
        combatCharacterRepo = new CombatCharacterRepo(application);
        allCharacters = characterRepo.getAllCharacters();
        combatCharacters = combatCharacterRepo.getCharactersForCombat(combat.getId());

        initiativeAdapter = new InitiativeAdapter();

        initiativeAdapter.submitList(new ArrayList<CharacterModel>());
    }

    public void unlinkCharacterFromCombat(CharacterModel character) {
        combatCharacterRepo.unlinkCharacterToCombat(combat.getId(), character.getId());
        try {
            iniCharacters.remove(character);
        } catch (NullPointerException e) {
            throw new NullPointerException("iniCharacters is null, must be initialized in the activity");
        }
    }

    public void linkCharactersToCombat(List<CharacterModel> characterModels) {
        ArrayList<Integer> charactersIds = new ArrayList<>();
        for (CharacterModel c : characterModels) {
            charactersIds.add(c.getId());
        }
        combatCharacterRepo.linkCharactersToCombat(combat.getId(), charactersIds);
        try {
            iniCharacters.addAll(characterModels);
        } catch (NullPointerException e) {
            throw new NullPointerException("iniCharacters is null, must be initialized in the activity");
        }
        initiativeAdapter.submitList(iniCharacters);
    }

    public LiveData<List<CharacterModel>> getCharactersForCombat() {
        return combatCharacters;
    }

    public LiveData<List<CharacterModel>> getAllCharacters() {
        return allCharacters;
    }

    public void linkCharacterToCombat(int characterId) {
        combatCharacterRepo.linkCharacterToCombat(combat.getId(), characterId);
    }

    public LiveData<CombatCharacter> getCCByIds(int characterId) {
        return combatCharacterRepo.getCCByIds(combat.getId(), characterId);
    }

    public InitiativeAdapter getInitiativeAdapter() {
        return initiativeAdapter;
    }

    public void recalculateIni() {
        if (iniCharacters == null) {
            return;
        }

        Collections.sort(iniCharacters, new Comparator<CharacterModel>() {
            @Override
            public int compare(CharacterModel o1, CharacterModel o2) {
                return o1.getIniRoll() - o2.getIniRoll();
            }
        });
        initiativeAdapter.submitList(iniCharacters);
    }

    public List<CharacterModel> getIniCharacters() {
        return iniCharacters;
    }

    public void setIniCharacters(List<CharacterModel> iniCharacters) {
        this.iniCharacters = iniCharacters;
    }
}
