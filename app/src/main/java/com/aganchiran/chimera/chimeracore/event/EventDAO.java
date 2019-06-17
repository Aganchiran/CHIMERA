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
