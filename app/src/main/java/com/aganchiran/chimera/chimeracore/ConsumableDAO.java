package com.aganchiran.chimera.chimeracore;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ConsumableDAO {

    @Insert
    Long insert(ConsumableModel consumableModel);

    @Update
    void update(ConsumableModel consumableModel);

    @Delete
    void delete(ConsumableModel consumableModel);

    @Query("DELETE FROM consumable_table")
    void deleteAllConsumables();

    @Query("SELECT * FROM consumable_table WHERE id = :id")
    LiveData<ConsumableModel> getConsumableById(int id);

    @Query("SELECT * FROM consumable_table ORDER BY displayPosition ASC")
    LiveData<List<ConsumableModel>> getAllConsumables();

    @Query("SELECT * FROM consumable_table WHERE characterId = :characterId " +
            "ORDER BY displayPosition ASC")
    LiveData<List<ConsumableModel>> getCharacterConsumables(int characterId);
}
