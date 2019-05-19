package com.aganchiran.chimera.chimeracore.combat;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.aganchiran.chimera.chimeracore.ItemModel;

@Entity(tableName = "combat_table")
public class CombatModel extends ItemModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private int displayPosition;

    public CombatModel(String name) {
        this.name = name;
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

    public int getDisplayPosition() {
        return displayPosition;
    }

    public void setDisplayPosition(int displayPosition) {
        this.displayPosition = displayPosition;
    }
}
