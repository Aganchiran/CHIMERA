package com.aganchiran.chimera.chimeracore;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CharacterDAO {

    @Insert
    Long insert(CharacterModel characterModel);

    @Update
    void update(CharacterModel characterModel);

    @Delete
    void delete(CharacterModel characterModel);

    @Query("DELETE FROM character_table")
    void deleteAllCharacters();

    @Query("SELECT * FROM character_table WHERE id = :id")
    LiveData<CharacterModel> getCharacterById(int id);

    @Query("SELECT * FROM character_table ORDER BY displayPosition ASC")
    LiveData<List<CharacterModel>> getAllCharacters();

}
