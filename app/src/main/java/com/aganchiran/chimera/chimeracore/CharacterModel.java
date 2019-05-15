package com.aganchiran.chimera.chimeracore;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "character_table")
public class CharacterModel extends ItemModel {

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

    @Override
    public int getId() {
        return id;
    }

    @Override
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

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof CharacterModel
                && ((CharacterModel) obj).getId() == this.getId()
                && ((CharacterModel) obj).getName().equals(this.getName())
                && ((CharacterModel) obj).getDescription().equals(this.getDescription())
                && ((CharacterModel) obj).getDisplayPosition() == this.getDisplayPosition());
    }
}
