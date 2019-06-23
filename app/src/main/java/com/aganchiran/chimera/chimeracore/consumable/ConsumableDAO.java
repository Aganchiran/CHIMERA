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

package com.aganchiran.chimera.chimeracore.consumable;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public abstract class ConsumableDAO {

    @Insert
    public abstract Long insert(ConsumableModel consumableModel);

    @Update
    public abstract void update(ConsumableModel consumableModel);

    @Transaction
    public void updateConsumables(List<ConsumableModel> consumables) {
        for (ConsumableModel consumable : consumables) {
            update(consumable);
        }
    }

    @Delete
    public abstract void delete(ConsumableModel consumableModel);

    @Query("DELETE FROM consumable_table")
    public abstract void deleteAllConsumables();

    @Query("SELECT * FROM consumable_table WHERE id = :id")
    public abstract LiveData<ConsumableModel> getConsumableById(int id);

    @Query("SELECT * FROM consumable_table ORDER BY displayPosition ASC")
    public abstract LiveData<List<ConsumableModel>> getAllConsumables();

    @Query("SELECT * FROM consumable_table WHERE characterId = :characterId " +
            "ORDER BY displayPosition ASC")
    public abstract LiveData<List<ConsumableModel>> getCharacterConsumables(int characterId);
}
