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

package com.aganchiran.chimera.chimeracore.eventcombat;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.aganchiran.chimera.chimeracore.combat.CombatModel;

import java.util.List;

@Dao
public abstract class EventCombatDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(EventCombat eventCombat);

    @Transaction
    public void insertList(List<EventCombat> ccList) {
        for (EventCombat eventCombat : ccList) {
            insert(eventCombat);
        }
    }

    @Update
    public abstract void update(EventCombat eventCombat);

    @Transaction
    public void updateECs(List<EventCombat> eventCombats) {
        for (EventCombat eventCombat : eventCombats) {
            update(eventCombat);
        }
    }

    @Delete
    public abstract void delete(EventCombat eventCombat);

    @Query("SELECT * FROM combat_table " +
            "INNER JOIN event_combat ON event_combat.combatId = combat_table.id " +
            "WHERE event_combat.eventId = :eventId ORDER BY event_combat.displayPosition")
    public abstract LiveData<List<CombatModel>> getCombatsForEvent(final int eventId);

    @Query("SELECT * FROM event_combat WHERE eventId = :eventId AND combatId = :combatId")
    public abstract LiveData<EventCombat> getCC(final int eventId, final int combatId);

    @Query("SELECT * FROM event_combat " +
            "WHERE eventId = :eventId ORDER BY displayPosition")
    public abstract LiveData<List<EventCombat>> getECsForEvent(final int eventId);
}
