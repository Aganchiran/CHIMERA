package com.example.chimera.chimeradata;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.chimera.chimeracore.CharacterDAO;
import com.example.chimera.chimeracore.CharacterModel;

@Database(entities = {CharacterModel.class}, version = 2, exportSchema = false)
public abstract class ChimeraDB extends RoomDatabase {

    private static ChimeraDB instance;

    public abstract CharacterDAO characterDAO();

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

        public PopulateDbAsyncTask(ChimeraDB db) {
            this.characterDAO = db.characterDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            characterDAO.insert(new CharacterModel("Chispitas", "¿¿Eso es un tenedor??\n\n¡MIO!"));
            characterDAO.insert(new CharacterModel("Polvorilla", "¡Estoy que ardo!"));
            characterDAO.insert(new CharacterModel("Fresquín", "Dame un abrazo"));
            characterDAO.insert(new CharacterModel("Caradura", "Zzzzzzzzzz"));
            characterDAO.insert(new CharacterModel("Lucía", "Kira~"));
            characterDAO.insert(new CharacterModel("Carboncillo", "¿Me ves? Ya no me ves"));
            return null;
        }
    }
}
