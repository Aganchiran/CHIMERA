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

package com.aganchiran.chimera.chimeracore.consumable;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.aganchiran.chimera.chimeracore.ItemModel;
import com.aganchiran.chimera.chimeracore.character.CharacterModel;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "consumable_table", foreignKeys = {@ForeignKey(onDelete = CASCADE, entity = CharacterModel.class, parentColumns = "id", childColumns = "characterId")}, indices = {@Index("characterId")})
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

    @Ignore
    public ConsumableModel(final String name, final long maxValue, final long minValue, final String color, final int characterId) {
        this.name = name;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.currentValue = maxValue;
        this.increment = 1;
        this.showOnIcon = false;
        this.color = color;
        this.displayPosition = Integer.MAX_VALUE;
        this.characterId = characterId;
    }

    public ConsumableModel(final String name, final long maxValue, final long minValue, final long currentValue, final long increment, final boolean showOnIcon, final String color, final int displayPosition, final int characterId) {
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

    @Ignore
    public ConsumableModel(final ConsumableModel consumableModel, final int characterId) {
        this.name = consumableModel.name;
        this.maxValue = consumableModel.maxValue;
        this.minValue = consumableModel.minValue;
        this.currentValue = consumableModel.currentValue;
        this.increment = consumableModel.increment;
        this.showOnIcon = consumableModel.showOnIcon;
        this.color = consumableModel.color;
        this.displayPosition = consumableModel.displayPosition;
        this.characterId = characterId;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
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

    public void setMaxValue(final long maxValue) {
        if (getCurrentValue() > maxValue) {
            setCurrentValue(maxValue);
        }
        this.maxValue = maxValue;
    }

    public long getMinValue() {
        return minValue;
    }

    public void setMinValue(final long minValue) {
        if (getCurrentValue() < minValue) {
            setCurrentValue(minValue);
        }
        this.minValue = minValue;
    }

    public long getCurrentValue() {
        return currentValue;
    }

    public String getValueFormated() {
        final long number = Math.abs(currentValue);
        long sign = (number != 0) ? currentValue / number : 0;
        String formattedNumber = String.valueOf(currentValue);

        if (number >= 1000000) {
            if (number < 10000000) {
                formattedNumber = (Math.floor((number * sign / 1000000.0) * 10) / 10.0) + "M";
            } else {
                formattedNumber = number * sign / 1000000 + "M";
            }
        } else if (number >= 1000) {
            if (number < 10000) {
                formattedNumber = (Math.floor((number * sign / 1000.0) * 10) / 10.0) + "K";
            } else {
                formattedNumber = number * sign / 1000 + "K";
            }
        }

        return formattedNumber;
    }

    public void setCurrentValue(final long currentValue) {
        this.currentValue = currentValue;
    }

    public long getIncrement() {
        return increment;
    }

    public void setIncrement(final long increment) {
        this.increment = increment;
    }

    public boolean isShowOnIcon() {
        return showOnIcon;
    }

    public void setShowOnIconfinal (boolean showOnIcon) {
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

    public void setDisplayPosition(final int displayPosition) {
        this.displayPosition = displayPosition;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(final int characterId) {
        this.characterId = characterId;
    }

    @Override
    public boolean equals(final Object obj) {
        return (obj instanceof ConsumableModel
                && ((ConsumableModel) obj).getId() == this.getId());
    }

    @Override
    public boolean contentsTheSame(final Object obj) {
        return (obj instanceof ConsumableModel
                && ((ConsumableModel) obj).getId() == this.getId()
                && ((ConsumableModel) obj).getName().equals(this.getName())
                && ((ConsumableModel) obj).getMaxValue() == this.getMaxValue()
                && ((ConsumableModel) obj).getMinValue() == this.getMinValue()
                && ((ConsumableModel) obj).getIncrement() == this.getIncrement()
                && ((ConsumableModel) obj).isShowOnIcon() == this.isShowOnIcon()
                && ((ConsumableModel) obj).getColor().equals(this.getColor())
                && ((ConsumableModel) obj).getDisplayPosition() == this.getDisplayPosition()
                && ((ConsumableModel) obj).getCharacterId() == this.getCharacterId());
    }
}
