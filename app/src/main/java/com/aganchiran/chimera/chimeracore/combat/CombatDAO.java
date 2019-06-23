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

    @Query("SELECT * FROM combat_table WHERE campaignId = :campaignId " +
            "ORDER BY displayPosition ASC")
    public abstract LiveData<List<CombatModel>> getCampaignCombats(int campaignId);

}
