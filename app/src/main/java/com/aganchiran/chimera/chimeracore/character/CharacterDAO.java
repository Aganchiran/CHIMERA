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

package com.aganchiran.chimera.chimeracore.character;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public abstract class CharacterDAO {

    @Insert
    public abstract Long insert(CharacterModel characterModel);

    @Update
    public abstract void update(CharacterModel characterModel);

    @Transaction
    public void updateCharacters(List<CharacterModel> characters) {
        for (CharacterModel character : characters) {
            update(character);
        }
    }

    @Delete
    public abstract void delete(CharacterModel characterModel);

    @Query("DELETE FROM character_table")
    public abstract void deleteAllCharacters();

    @Query("SELECT * FROM character_table WHERE id = :id")
    public abstract LiveData<CharacterModel> getCharacterById(int id);

    @Query("SELECT * FROM character_table ORDER BY displayPosition ASC")
    public abstract LiveData<List<CharacterModel>> getAllCharacters();

    @Query("SELECT * FROM character_table WHERE campaignId = :campaignId " +
            "ORDER BY displayPosition ASC")
    public abstract LiveData<List<CharacterModel>> getCampaignCharacters(int campaignId);

    @Query("SELECT * FROM character_table WHERE campaignId IS NULL " +
            "ORDER BY displayPosition ASC")
    public abstract LiveData<List<CharacterModel>> getCharactersWithoutCampaign();
}
