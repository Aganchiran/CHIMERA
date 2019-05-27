package com.aganchiran.chimera.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.aganchiran.chimera.chimeracore.combat.CombatDAO;
import com.aganchiran.chimera.chimeracore.combat.CombatModel;
import com.aganchiran.chimera.chimeradata.ChimeraDB;

import java.util.Arrays;
import java.util.List;

public class CombatRepo {

    private CombatDAO combatDAO;
    private LiveData<List<CombatModel>> allCombats;

    public CombatRepo(Application application){
        ChimeraDB database = ChimeraDB.getInstance(application);
        combatDAO = database.combatDAO();
        allCombats = combatDAO.getAllCombats();
    }

    public void insert(CombatModel combatModel){
        new InsertCombatAsyncTask(combatDAO).execute(combatModel);
    }

    public void update(CombatModel combatModel){
        new UpdateCombatAsyncTask(combatDAO).execute(combatModel);

    }

    public void updateCombats(List<CombatModel> combatModelList){
        CombatModel[] combatArray = new CombatModel[combatModelList.size()];
        combatArray = combatModelList.toArray(combatArray);
        new UpdateCombatListAsyncTask(combatDAO).execute(combatArray);
    }

    public void delete(CombatModel combatModel){
        new DeleteCombatAsyncTask(combatDAO).execute(combatModel);

    }

    public void deleteAllCombats(){
        new DeleteAllCombatAsyncTask(combatDAO).execute();

    }

    public LiveData<CombatModel> getCombatById(int id){
        return combatDAO.getCombatById(id);
    }

    public LiveData<List<CombatModel>> getAllCombats(){
        return allCombats;
    }

    private static class InsertCombatAsyncTask extends AsyncTask<CombatModel, Void, Void> {

        private CombatDAO combatDAO;

        private InsertCombatAsyncTask(CombatDAO combatDAO){
            this.combatDAO = combatDAO;
        }

        @Override
        protected Void doInBackground(CombatModel... combatModels) {
            combatDAO.insert(combatModels[0]);
            return null;
        }
    }

    private static class UpdateCombatAsyncTask extends AsyncTask<CombatModel, Void, Void> {

        private CombatDAO combatDAO;

        private UpdateCombatAsyncTask(CombatDAO combatDAO){
            this.combatDAO = combatDAO;
        }

        @Override
        protected Void doInBackground(CombatModel... combatModels) {
            combatDAO.update(combatModels[0]);
            return null;
        }
    }

    private static class UpdateCombatListAsyncTask extends AsyncTask<CombatModel, Void, Void> {

        private CombatDAO combatDAO;

        private UpdateCombatListAsyncTask(CombatDAO combatDAO){
            this.combatDAO = combatDAO;
        }


        @Override
        protected final Void doInBackground(CombatModel... combatModels) {
            combatDAO.updateCombats(Arrays.asList(combatModels));
            return null;
        }
    }

    private static class DeleteCombatAsyncTask extends AsyncTask<CombatModel, Void, Void> {

        private CombatDAO combatDAO;

        private DeleteCombatAsyncTask(CombatDAO combatDAO){
            this.combatDAO = combatDAO;
        }

        @Override
        protected Void doInBackground(CombatModel... combatModels) {
            combatDAO.delete(combatModels[0]);
            return null;
        }
    }

    private static class DeleteAllCombatAsyncTask extends AsyncTask<Void, Void, Void> {

        private CombatDAO combatDAO;

        private DeleteAllCombatAsyncTask(CombatDAO combatDAO){
            this.combatDAO = combatDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            combatDAO.deleteAllCombats();
            return null;
        }
    }

    private static class GetCombatAsyncTask extends AsyncTask<CombatModel, Void, LiveData<CombatModel>> {

        private CombatDAO combatDAO;

        private GetCombatAsyncTask(CombatDAO combatDAO){
            this.combatDAO = combatDAO;
        }

        @Override
        protected LiveData<CombatModel> doInBackground(CombatModel... combatModels) {
            return combatDAO.getCombatById(combatModels[0].getId());
        }
    }
}
