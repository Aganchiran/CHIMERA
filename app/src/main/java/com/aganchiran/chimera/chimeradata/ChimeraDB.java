package com.aganchiran.chimera.chimeradata;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.aganchiran.chimera.chimeracore.character.CharacterDAO;
import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.chimeracore.combat.CombatDAO;
import com.aganchiran.chimera.chimeracore.combat.CombatModel;
import com.aganchiran.chimera.chimeracore.combatcharacter.CombatCharacter;
import com.aganchiran.chimera.chimeracore.combatcharacter.CombatCharacterDAO;
import com.aganchiran.chimera.chimeracore.consumable.ConsumableDAO;
import com.aganchiran.chimera.chimeracore.consumable.ConsumableModel;

@Database(
        entities = {CharacterModel.class, ConsumableModel.class, CombatModel.class, CombatCharacter.class},
        version = 12,
        exportSchema = false)
public abstract class ChimeraDB extends RoomDatabase {

    private static ChimeraDB instance;
    public abstract CharacterDAO characterDAO();
    public abstract ConsumableDAO consumableDAO();
    public abstract CombatDAO combatDAO();
    public abstract CombatCharacterDAO combatCharacterDAO();

    public static synchronized ChimeraDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ChimeraDB.class, "chimera_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private CharacterDAO characterDAO;
        private ConsumableDAO consumableDAO;
        private CombatDAO combatDAO;
        private CombatCharacterDAO combatCharacterDAO;

        public PopulateDbAsyncTask(ChimeraDB db) {
            this.characterDAO = db.characterDAO();
            this.consumableDAO = db.consumableDAO();
            this.combatDAO = db.combatDAO();
            this.combatCharacterDAO = db.combatCharacterDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final CharacterModel pepe = new CharacterModel("Pepe", "");
            final CharacterModel juan = new CharacterModel("Juan", "");
            characterDAO.insert(pepe);
            characterDAO.insert(juan);
            characterDAO.insert(new CharacterModel("María", ""));
            characterDAO.insert(new CharacterModel("Gerardo", ""));

            consumableDAO.insert(new ConsumableModel("Life", 150, -40,
                    "#FFFF00", pepe.getId()));
            consumableDAO.insert(new ConsumableModel("Zeon", 1000, 0,
                    "#FFFF00", pepe.getId()));
            consumableDAO.insert(new ConsumableModel("Arrows", 10, 0,
                    "#FFFF00", pepe.getId()));

            final CombatModel epic = new CombatModel("Epic Battle");
            final CombatModel normal = new CombatModel("Normal Battle");
            combatDAO.insert(epic);
            combatDAO.insert(normal);
            combatDAO.insert(new CombatModel("Fun Battle"));

            combatCharacterDAO.insert(new CombatCharacter(epic.getId(), pepe.getId()));
            combatCharacterDAO.insert(new CombatCharacter(epic.getId(), juan.getId()));
            combatCharacterDAO.insert(new CombatCharacter(normal.getId(), pepe.getId()));

            return null;
        }
    }
}
