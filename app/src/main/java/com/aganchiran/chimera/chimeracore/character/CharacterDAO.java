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
    public void updateCharacters(List<CharacterModel> characters){
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

}
