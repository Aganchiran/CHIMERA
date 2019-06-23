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
