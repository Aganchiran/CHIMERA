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
import com.aganchiran.chimera.chimeracore.eventcharacter.EventCharacter;

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
    public void updateECs(List<EventCharacter> eventCharacters){
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
