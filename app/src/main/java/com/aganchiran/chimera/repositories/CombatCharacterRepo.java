package com.aganchiran.chimera.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.chimeracore.combatcharacter.CombatCharacter;
import com.aganchiran.chimera.chimeracore.combatcharacter.CombatCharacterDAO;
import com.aganchiran.chimera.chimeradata.ChimeraDB;

import java.util.ArrayList;
import java.util.List;

public class CombatCharacterRepo {

    private CombatCharacterDAO combatCharacterDAO;

    public CombatCharacterRepo(Application application){
        ChimeraDB database = ChimeraDB.getInstance(application);
        combatCharacterDAO = database.combatCharacterDAO();
    }

    public void unlinkCharacterToCombat(int combatId, int characterId){
        final Integer[] ids = {combatId, characterId};
        new DeleteCCAsyncTask(combatCharacterDAO).execute(ids);

    }

    public LiveData<CombatCharacter> getCCByIds(int combatId, int characterId){
        return combatCharacterDAO.getCC(combatId, characterId);
    }

    public LiveData<List<CharacterModel>> getCharactersForCombat(int combatId){
        return combatCharacterDAO.getCharactersForCombat(combatId);
    }

    public void linkCharacterToCombat(int combatId, int characterId){
        final Integer[] ids = {combatId, characterId};
        new InsertCCAsyncTask(combatCharacterDAO).execute(ids);
    }

    public void linkCharactersToCombat(int combatId, List<Integer> characterId){
        final Object[] ids = {combatId, characterId};
        new InsertCCListAsyncTask(combatCharacterDAO).execute(ids);
    }

    /////////////////////////
    //////////TASKS//////////
    /////////////////////////

    private static class DeleteCCAsyncTask extends AsyncTask<Integer, Void, Void> {

        private CombatCharacterDAO combatCharacterDAO;

        private DeleteCCAsyncTask(CombatCharacterDAO combatCharacterDAO){
            this.combatCharacterDAO = combatCharacterDAO;
        }

        @Override
        protected Void doInBackground(Integer... ids) {
            combatCharacterDAO.delete((new CombatCharacter(ids[0],ids[1])));
            return null;
        }
    }


    private static class InsertCCAsyncTask extends AsyncTask<Integer, Void, Void> {

        private CombatCharacterDAO combatCharacterDAO;

        private InsertCCAsyncTask(CombatCharacterDAO combatCharacterDAO){
            this.combatCharacterDAO = combatCharacterDAO;
        }

        @Override
        protected Void doInBackground(Integer... ids) {
            combatCharacterDAO.insert(new CombatCharacter(ids[0],ids[1]));
            return null;
        }
    }

    private static class InsertCCListAsyncTask extends AsyncTask<Object, Void, Void> {

        private CombatCharacterDAO combatCharacterDAO;

        private InsertCCListAsyncTask(CombatCharacterDAO combatCharacterDAO){
            this.combatCharacterDAO = combatCharacterDAO;
        }

        @Override
        protected Void doInBackground(Object... ids) {
            final ArrayList<CombatCharacter> ccList = new ArrayList<>();
            for (Integer id : (List<Integer>) ids[1]) {
                ccList.add(new CombatCharacter((int) ids[0],id));
            }
            combatCharacterDAO.insertList(ccList);
            return null;
        }
    }
}
