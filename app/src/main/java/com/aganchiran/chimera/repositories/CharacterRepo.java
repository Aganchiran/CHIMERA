package com.aganchiran.chimera.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.aganchiran.chimera.chimeracore.character.CharacterDAO;
import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.chimeradata.ChimeraDB;

import java.util.Arrays;
import java.util.List;


public class CharacterRepo {

    private CharacterDAO characterDAO;
    private LiveData<List<CharacterModel>> allCharacters;

    public CharacterRepo(Application application){
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

    public void updateCharacters(List<CharacterModel> characterModelList){
        CharacterModel[] characterArray = new CharacterModel[characterModelList.size()];
        characterArray = characterModelList.toArray(characterArray);
        new UpdateCharacterListAsyncTask(characterDAO).execute(characterArray);
    }

    public void delete(CharacterModel characterModel){
        new DeleteCharacterAsyncTask(characterDAO).execute(characterModel);

    }

    public void deleteAllCharacters(){
        new DeleteAllCharacterAsyncTask(characterDAO).execute();

    }

    public LiveData<CharacterModel> getCharacterById(int id){
        return characterDAO.getCharacterById(id);
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

    private static class UpdateCharacterListAsyncTask extends AsyncTask<CharacterModel, Void, Void> {

        private CharacterDAO characterDAO;

        private UpdateCharacterListAsyncTask(CharacterDAO characterDAO){
            this.characterDAO = characterDAO;
        }


        @Override
        protected final Void doInBackground(CharacterModel... characterModels) {
            characterDAO.updateCharacters(Arrays.asList(characterModels));
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

    private static class GetCharacterAsyncTask extends AsyncTask<CharacterModel, Void, LiveData<CharacterModel>> {

        private CharacterDAO characterDAO;

        private GetCharacterAsyncTask(CharacterDAO characterDAO){
            this.characterDAO = characterDAO;
        }

        @Override
        protected LiveData<CharacterModel> doInBackground(CharacterModel... characterModels) {
            return characterDAO.getCharacterById(characterModels[0].getId());
        }
    }

}
