package com.aganchiran.chimera.chimeradata;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.aganchiran.chimera.chimeracore.campaign.CampaignDAO;
import com.aganchiran.chimera.chimeracore.campaign.CampaignModel;
import com.aganchiran.chimera.chimeracore.character.CharacterDAO;
import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.chimeracore.combat.CombatDAO;
import com.aganchiran.chimera.chimeracore.combat.CombatModel;
import com.aganchiran.chimera.chimeracore.combatcharacter.CombatCharacter;
import com.aganchiran.chimera.chimeracore.combatcharacter.CombatCharacterDAO;
import com.aganchiran.chimera.chimeracore.consumable.ConsumableDAO;
import com.aganchiran.chimera.chimeracore.consumable.ConsumableModel;

@Database(
        entities = {CharacterModel.class, ConsumableModel.class, CombatModel.class,
                CombatCharacter.class, CampaignModel.class},
        version = 19,
        exportSchema = false)
public abstract class ChimeraDB extends RoomDatabase {

    private static ChimeraDB instance;
    public abstract CharacterDAO characterDAO();
    public abstract ConsumableDAO consumableDAO();
    public abstract CombatDAO combatDAO();
    public abstract CombatCharacterDAO combatCharacterDAO();
    public abstract CampaignDAO campaignDAO();

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
        private CampaignDAO campaignDAO;

        public PopulateDbAsyncTask(ChimeraDB db) {
            this.characterDAO = db.characterDAO();
            this.consumableDAO = db.consumableDAO();
            this.combatDAO = db.combatDAO();
            this.combatCharacterDAO = db.combatCharacterDAO();
            this.campaignDAO = db.campaignDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
