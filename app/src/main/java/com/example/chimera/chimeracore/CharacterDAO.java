package com.example.chimera.chimeracore;

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
    void update(CharacterModel note);

    @Delete
    void delete(CharacterModel note);

    @Query("DELETE FROM character_table")
    void deleteAllCharacters();

    @Query("SELECT * FROM character_table ORDER BY displayPosition ASC")
    LiveData<List<CharacterModel>> getAllCharacters();

}
