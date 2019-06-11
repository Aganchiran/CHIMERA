package com.aganchiran.chimera.chimeracore.eventcombat;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

import com.aganchiran.chimera.chimeracore.combat.CombatModel;
import com.aganchiran.chimera.chimeracore.event.EventModel;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "event_combat",
        primaryKeys = {"eventId", "combatId"},
        foreignKeys = {
                @ForeignKey(onDelete = CASCADE, entity = EventModel.class,
                        parentColumns = "id",
                        childColumns = "eventId"),
                @ForeignKey(onDelete = CASCADE, entity = CombatModel.class,
                        parentColumns = "id",
                        childColumns = "combatId")
        },
        indices = {@Index("eventId"), @Index("combatId")})
public class EventCombat {

    public final int eventId;
    public final int combatId;
    public int displayPosition = Integer.MAX_VALUE;

    public EventCombat(int eventId, int combatId) {
        this.eventId = eventId;
        this.combatId = combatId;
    }

    public int getEventId() {
        return eventId;
    }

    public int getCombatId() {
        return combatId;
    }

    public int getDisplayPosition() {
        return displayPosition;
    }

    public void setDisplayPosition(int displayPosition) {
        this.displayPosition = displayPosition;
    }
}
