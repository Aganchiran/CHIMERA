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

package com.aganchiran.chimera.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.chimeracore.eventcharacter.EventCharacter;
import com.aganchiran.chimera.chimeracore.eventcharacter.EventCharacterDAO;
import com.aganchiran.chimera.chimeradata.ChimeraDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventCharacterRepo {

    private EventCharacterDAO eventCharacterDAO;

    public EventCharacterRepo(Application application) {
        ChimeraDB database = ChimeraDB.getInstance(application);
        eventCharacterDAO = database.eventCharacterDAO();
    }

    public void updateECs(List<EventCharacter> eventCharacterList) {
        EventCharacter[] ecArray = new EventCharacter[eventCharacterList.size()];
        ecArray = eventCharacterList.toArray(ecArray);
        new UpdateECListAsyncTask(eventCharacterDAO).execute(ecArray);
    }

    public void unlinkCharacterToEvent(int eventId, int characterId) {
        final Integer[] ids = {eventId, characterId};
        new DeleteCCAsyncTask(eventCharacterDAO).execute(ids);

    }

    public LiveData<EventCharacter> getCCByIds(int eventId, int characterId) {
        return eventCharacterDAO.getEC(eventId, characterId);
    }

    public LiveData<List<CharacterModel>> getCharactersForEvent(int eventId) {
        return eventCharacterDAO.getCharactersForEvent(eventId);
    }

    public LiveData<List<EventCharacter>> getECsForEvent(int eventId) {
        return eventCharacterDAO.getECsForEvent(eventId);
    }

    public void linkCharacterToEvent(int eventId, int characterId) {
        final Integer[] ids = {eventId, characterId};
        new InsertCCAsyncTask(eventCharacterDAO).execute(ids);
    }

    public void linkCharactersToEvent(int eventId, List<Integer> characterId) {
        final Object[] ids = {eventId, characterId};
        new InsertCCListAsyncTask(eventCharacterDAO).execute(ids);
    }

    /////////////////////////
    //////////TASKS//////////
    /////////////////////////

    private static class UpdateECListAsyncTask extends AsyncTask<EventCharacter, Void, Void> {

        private EventCharacterDAO eventCharacterDAO;

        private UpdateECListAsyncTask(EventCharacterDAO eventCharacterDAO) {
            this.eventCharacterDAO = eventCharacterDAO;
        }


        @Override
        protected final Void doInBackground(EventCharacter... eventCharacters) {
            eventCharacterDAO.updateECs(Arrays.asList(eventCharacters));
            return null;
        }
    }

    private static class DeleteCCAsyncTask extends AsyncTask<Integer, Void, Void> {

        private EventCharacterDAO eventCharacterDAO;

        private DeleteCCAsyncTask(EventCharacterDAO eventCharacterDAO) {
            this.eventCharacterDAO = eventCharacterDAO;
        }

        @Override
        protected Void doInBackground(Integer... ids) {
            eventCharacterDAO.delete((new EventCharacter(ids[0], ids[1])));
            return null;
        }
    }


    private static class InsertCCAsyncTask extends AsyncTask<Integer, Void, Void> {

        private EventCharacterDAO eventCharacterDAO;

        private InsertCCAsyncTask(EventCharacterDAO eventCharacterDAO) {
            this.eventCharacterDAO = eventCharacterDAO;
        }

        @Override
        protected Void doInBackground(Integer... ids) {
            eventCharacterDAO.insert(new EventCharacter(ids[0], ids[1]));
            return null;
        }
    }

    private static class InsertCCListAsyncTask extends AsyncTask<Object, Void, Void> {

        private EventCharacterDAO eventCharacterDAO;

        private InsertCCListAsyncTask(EventCharacterDAO eventCharacterDAO) {
            this.eventCharacterDAO = eventCharacterDAO;
        }

        @Override
        protected Void doInBackground(Object... ids) {
            final ArrayList<EventCharacter> ccList = new ArrayList<>();
            for (Integer id : (List<Integer>) ids[1]) {
                ccList.add(new EventCharacter((int) ids[0], id));
            }
            eventCharacterDAO.insertList(ccList);
            return null;
        }
    }
}
