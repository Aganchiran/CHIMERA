package com.aganchiran.chimera.chimeradata;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.aganchiran.chimera.chimeracore.CharacterDAO;
import com.aganchiran.chimera.chimeracore.CharacterModel;
import com.aganchiran.chimera.chimeracore.ConsumableDAO;
import com.aganchiran.chimera.chimeracore.ConsumableModel;

@Database(entities = {CharacterModel.class, ConsumableModel.class}, version = 5, exportSchema = false)
public abstract class ChimeraDB extends RoomDatabase {

    private static ChimeraDB instance;

    public abstract CharacterDAO characterDAO();
    public abstract ConsumableDAO consumableDAO();

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

        public PopulateDbAsyncTask(ChimeraDB db) {
            this.characterDAO = db.characterDAO();
            this.consumableDAO = db.consumableDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
