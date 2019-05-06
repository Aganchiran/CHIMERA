package com.example.chimera.chimeradata;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.chimera.chimeracore.CharacterDAO;
import com.example.chimera.chimeracore.CharacterModel;

import java.util.List;


public class ChimeraRepository {

    private CharacterDAO characterDAO;
    private LiveData<List<CharacterModel>> allCharacters;

    public ChimeraRepository(Application application){
        ChimeraDB database = ChimeraDB.getInstance(application);
        characterDAO = database.characterDAO();
        allCharacters = characterDAO.getAllCharacters();
    }

    public void insert(CharacterModel characterModel){
        new InsertCharacterAsyncTask(characterDAO).execute(characterModel);
    }

    public void update(CharacterModel characterModel){
        new UpdateCharacterAsyncTask(characterDAO).execute(characterModel);

    }

    public void delete(CharacterModel characterModel){
        new DeleteCharacterAsyncTask(characterDAO).execute(characterModel);

    }

    public void deleteAllCharacters(){
        new DeleteAllCharacterAsyncTask(characterDAO).execute();

    }

    public LiveData<List<CharacterModel>> getAllCharacters(){
        return allCharacters;
    }

    private static class InsertCharacterAsyncTask extends AsyncTask<CharacterModel, Void, Void> {

        private CharacterDAO characterDAO;

        private InsertCharacterAsyncTask(CharacterDAO characterDAO){
            this.characterDAO = characterDAO;
        }

        @Override
        protected Void doInBackground(CharacterModel... characterModels) {
            characterDAO.insert(characterModels[0]);
            return null;
        }
    }

    private static class UpdateCharacterAsyncTask extends AsyncTask<CharacterModel, Void, Void> {

        private CharacterDAO characterDAO;

        private UpdateCharacterAsyncTask(CharacterDAO characterDAO){
            this.characterDAO = characterDAO;
        }

        @Override
        protected Void doInBackground(CharacterModel... characterModels) {
            characterDAO.update(characterModels[0]);
            return null;
        }
    }

    private static class DeleteCharacterAsyncTask extends AsyncTask<CharacterModel, Void, Void> {

        private CharacterDAO characterDAO;

        private DeleteCharacterAsyncTask(CharacterDAO characterDAO){
            this.characterDAO = characterDAO;
        }

        @Override
        protected Void doInBackground(CharacterModel... characterModels) {
            characterDAO.delete(characterModels[0]);
            return null;
        }
    }

    private static class DeleteAllCharacterAsyncTask extends AsyncTask<Void, Void, Void> {

        private CharacterDAO characterDAO;

        private DeleteAllCharacterAsyncTask(CharacterDAO characterDAO){
            this.characterDAO = characterDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            characterDAO.deleteAllCharacters();
            return null;
        }
    }

}
