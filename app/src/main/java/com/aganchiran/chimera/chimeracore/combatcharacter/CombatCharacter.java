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
