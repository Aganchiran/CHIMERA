package com.aganchiran.chimera.chimeracore.combatcharacter;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.aganchiran.chimera.chimeracore.character.CharacterModel;

import java.util.List;

@Dao
public abstract class CombatCharacterDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(CombatCharacter combatCharacter);

    @Transaction
    public void insertList(List<CombatCharacter> ccList) {
        for (CombatCharacter combatCharacter : ccList) {
            insert(combatCharacter);
        }
    }

    @Update
    public abstract void update(CombatCharacter combatCharacter);

    @Delete
    public abstract void delete(CombatCharacter combatCharacter);

    @Query("SELECT * FROM character_table " +
            "WHERE id IN (SELECT characterId FROM combat_character " +
            "WHERE combatId = :combatId)")
    public abstract LiveData<List<CharacterModel>> getCharactersForCombat(final int combatId);

    @Query("SELECT * FROM combat_character WHERE combatId = :combatId AND characterId = :characterId")
    public abstract LiveData<CombatCharacter> getCC(final int combatId, final int characterId);
}
