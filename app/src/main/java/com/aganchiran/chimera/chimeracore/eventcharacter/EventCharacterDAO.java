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

package com.aganchiran.chimera.chimeracore.eventcharacter;

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
public abstract class EventCharacterDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(EventCharacter eventCharacter);

    @Transaction
    public void insertList(List<EventCharacter> ccList) {
        for (EventCharacter eventCharacter : ccList) {
            insert(eventCharacter);
        }
    }

    @Update
    public abstract void update(EventCharacter eventCharacter);

    @Transaction
    public void updateECs(List<EventCharacter> eventCharacters) {
        for (EventCharacter eventCharacter : eventCharacters) {
            update(eventCharacter);
        }
    }

    @Delete
    public abstract void delete(EventCharacter eventCharacter);

    @Query("SELECT * FROM character_table " +
            "INNER JOIN event_character ON event_character.characterId = character_table.id " +
            "WHERE event_character.eventId = :eventId ORDER BY event_character.displayPosition")
    public abstract LiveData<List<CharacterModel>> getCharactersForEvent(final int eventId);

    @Query("SELECT * FROM event_character WHERE eventId = :eventId AND characterId = :characterId")
    public abstract LiveData<EventCharacter> getEC(final int eventId, final int characterId);

    @Query("SELECT * FROM event_character " +
            "WHERE eventId = :eventId ORDER BY displayPosition")
    public abstract LiveData<List<EventCharacter>> getECsForEvent(final int eventId);
}
