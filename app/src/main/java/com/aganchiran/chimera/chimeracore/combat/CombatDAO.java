package com.aganchiran.chimera.chimeracore.combat;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public abstract class CombatDAO {

    @Insert
    public abstract Long insert(CombatModel combatModel);

    @Update
    public abstract void update(CombatModel combatModel);

    @Transaction
    public void updateCombats(List<CombatModel> combats) {
        for (CombatModel combat : combats) {
            update(combat);
        }
    }

    @Delete
    public abstract void delete(CombatModel combatModel);

    @Query("DELETE FROM combat_table")
    public abstract void deleteAllCombats();

    @Query("SELECT * FROM combat_table WHERE id = :id")
    public abstract LiveData<CombatModel> getCombatById(int id);

    @Query("SELECT * FROM combat_table ORDER BY displayPosition ASC")
    public abstract LiveData<List<CombatModel>> getAllCombats();

}
