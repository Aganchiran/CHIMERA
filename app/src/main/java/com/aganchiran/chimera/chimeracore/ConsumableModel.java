package com.aganchiran.chimera.chimeracore;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "consumable_table",foreignKeys ={@ForeignKey(onDelete = CASCADE,entity = CharacterModel.class, parentColumns = "id",childColumns = "characterId")},indices = {@Index("characterId")})
public class ConsumableModel extends ItemModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private long maxValue;

    private long minValue;

    private long currentValue;

    private long increment;

    private boolean showOnIcon;

    private String color;

    private int displayPosition;

    private int characterId;

    public ConsumableModel(String name, long maxValue, long minValue, long currentValue, long increment, boolean showOnIcon, String color, int displayPosition, int characterId) {
        this.name = name;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.currentValue = currentValue;
        this.increment = increment;
        this.showOnIcon = showOnIcon;
        this.color = color;
        this.displayPosition = displayPosition;
        this.characterId = characterId;
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

    public long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
    }

    public long getMinValue() {
        return minValue;
    }

    public void setMinValue(long minValue) {
        this.minValue = minValue;
    }

    public long getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(long currentValue) {
        this.currentValue = currentValue;
    }

    public long getIncrement() {
        return increment;
    }

    public void setIncrement(long increment) {
        this.increment = increment;
    }

    public boolean isShowOnIcon() {
        return showOnIcon;
    }

    public void setShowOnIcon(boolean showOnIcon) {
        this.showOnIcon = showOnIcon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getDisplayPosition() {
        return displayPosition;
    }

    public void setDisplayPosition(int displayPosition) {
        this.displayPosition = displayPosition;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }
}
