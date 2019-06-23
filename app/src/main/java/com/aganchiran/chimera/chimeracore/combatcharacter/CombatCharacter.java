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

package com.aganchiran.chimera.chimeracore.combatcharacter;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.chimeracore.combat.CombatModel;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "combat_character",
        primaryKeys = {"combatId", "characterId"},
        foreignKeys = {
                @ForeignKey(onDelete = CASCADE, entity = CombatModel.class,
                        parentColumns = "id",
                        childColumns = "combatId"),
                @ForeignKey(onDelete = CASCADE, entity = CharacterModel.class,
                        parentColumns = "id",
                        childColumns = "characterId")
        },
        indices = {@Index("combatId"), @Index("characterId")})
public class CombatCharacter {

    public final int combatId;
    public final int characterId;

    public CombatCharacter(int combatId, int characterId) {
        this.combatId = combatId;
        this.characterId = characterId;
    }

}
