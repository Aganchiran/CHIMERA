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

package com.aganchiran.chimera.chimeracore.event;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public abstract class EventDAO {

    @Insert
    public abstract Long insert(EventModel eventModel);

    @Update
    public abstract void update(EventModel eventModel);

    @Transaction
    public void updateEvents(List<EventModel> events) {
        for (EventModel event : events) {
            update(event);
        }
    }

    @Delete
    public abstract void delete(EventModel eventModel);

    @Query("DELETE FROM event_table")
    public abstract void deleteAllEvents();

    @Query("SELECT * FROM event_table WHERE id = :id")
    public abstract LiveData<EventModel> getEventById(int id);

    @Query("SELECT * FROM event_table WHERE xCoord = :xCoord AND yCoord = :yCoord AND campaignId = :campaignId")
    public abstract LiveData<EventModel> getEventByCoordsAndCampaign(float xCoord, float yCoord, int campaignId);

    @Query("SELECT * FROM event_table")
    public abstract LiveData<List<EventModel>> getAllEvents();

    @Query("SELECT * FROM event_table WHERE campaignId = :campaignId")
    public abstract LiveData<List<EventModel>> getCampaignEvents(int campaignId);

}
