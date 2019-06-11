package com.aganchiran.chimera.chimeracore.eventcharacter;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.chimeracore.event.EventModel;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "event_character",
        primaryKeys = {"eventId", "characterId"},
        foreignKeys = {
                @ForeignKey(onDelete = CASCADE, entity = EventModel.class,
                        parentColumns = "id",
                        childColumns = "eventId"),
                @ForeignKey(onDelete = CASCADE, entity = CharacterModel.class,
                        parentColumns = "id",
                        childColumns = "characterId")
        },
        indices = {@Index("eventId"), @Index("characterId")})
public class EventCharacter {

    public final int eventId;
    public final int characterId;
    public int displayPosition = Integer.MAX_VALUE;

    public EventCharacter(int eventId, int characterId) {
        this.eventId = eventId;
        this.characterId = characterId;
    }

    public int getEventId() {
        return eventId;
    }

    public int getCharacterId() {
        return characterId;
    }

    public int getDisplayPosition() {
        return displayPosition;
    }

    public void setDisplayPosition(int displayPosition) {
        this.displayPosition = displayPosition;
    }
}
