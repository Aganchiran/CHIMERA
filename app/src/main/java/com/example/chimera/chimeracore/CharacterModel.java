package com.example.chimera.chimeracore;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "character_table")
public class CharacterModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String description;

    private int displayPosition;

    public CharacterModel(String name, String description) {
        this.name = name;
        this.description = description;
        this.displayPosition = Integer.MAX_VALUE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDisplayPosition() {
        return displayPosition;
    }

    public void setDisplayPosition(int displayPosition) {
        this.displayPosition = displayPosition;
    }
}
