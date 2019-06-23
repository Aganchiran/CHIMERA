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

import com.aganchiran.chimera.chimeracore.combat.CombatModel;
import com.aganchiran.chimera.chimeracore.eventcombat.EventCombat;
import com.aganchiran.chimera.chimeracore.eventcombat.EventCombatDAO;
import com.aganchiran.chimera.chimeradata.ChimeraDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventCombatRepo {

    private EventCombatDAO eventCombatDAO;

    public EventCombatRepo(Application application) {
        ChimeraDB database = ChimeraDB.getInstance(application);
        eventCombatDAO = database.eventCombatDAO();
    }

    public void updateECs(List<EventCombat> eventCombatList) {
        EventCombat[] ecArray = new EventCombat[eventCombatList.size()];
        ecArray = eventCombatList.toArray(ecArray);
        new UpdateECListAsyncTask(eventCombatDAO).execute(ecArray);
    }

    public void unlinkCombatToEvent(int eventId, int combatId) {
        final Integer[] ids = {eventId, combatId};
        new DeleteCCAsyncTask(eventCombatDAO).execute(ids);

    }

    public LiveData<EventCombat> getCCByIds(int eventId, int combatId) {
        return eventCombatDAO.getCC(eventId, combatId);
    }

    public LiveData<List<CombatModel>> getCombatsForEvent(int eventId) {
        return eventCombatDAO.getCombatsForEvent(eventId);
    }

    public LiveData<List<EventCombat>> getECsForEvent(int eventId) {
        return eventCombatDAO.getECsForEvent(eventId);
    }

    public void linkCombatToEvent(int eventId, int combatId) {
        final Integer[] ids = {eventId, combatId};
        new InsertCCAsyncTask(eventCombatDAO).execute(ids);
    }

    public void linkCombatsToEvent(int eventId, List<Integer> combatId) {
        final Object[] ids = {eventId, combatId};
        new InsertCCListAsyncTask(eventCombatDAO).execute(ids);
    }

    /////////////////////////
    //////////TASKS//////////
    /////////////////////////

    private static class UpdateECListAsyncTask extends AsyncTask<EventCombat, Void, Void> {

        private EventCombatDAO eventCombatDAO;

        private UpdateECListAsyncTask(EventCombatDAO eventCombatDAO) {
            this.eventCombatDAO = eventCombatDAO;
        }


        @Override
        protected final Void doInBackground(EventCombat... eventCombats) {
            eventCombatDAO.updateECs(Arrays.asList(eventCombats));
            return null;
        }
    }

    private static class DeleteCCAsyncTask extends AsyncTask<Integer, Void, Void> {

        private EventCombatDAO eventCombatDAO;

        private DeleteCCAsyncTask(EventCombatDAO eventCombatDAO) {
            this.eventCombatDAO = eventCombatDAO;
        }

        @Override
        protected Void doInBackground(Integer... ids) {
            eventCombatDAO.delete((new EventCombat(ids[0], ids[1])));
            return null;
        }
    }


    private static class InsertCCAsyncTask extends AsyncTask<Integer, Void, Void> {

        private EventCombatDAO eventCombatDAO;

        private InsertCCAsyncTask(EventCombatDAO eventCombatDAO) {
            this.eventCombatDAO = eventCombatDAO;
        }

        @Override
        protected Void doInBackground(Integer... ids) {
            eventCombatDAO.insert(new EventCombat(ids[0], ids[1]));
            return null;
        }
    }

    private static class InsertCCListAsyncTask extends AsyncTask<Object, Void, Void> {

        private EventCombatDAO eventCombatDAO;

        private InsertCCListAsyncTask(EventCombatDAO eventCombatDAO) {
            this.eventCombatDAO = eventCombatDAO;
        }

        @Override
        protected Void doInBackground(Object... ids) {
            final ArrayList<EventCombat> ccList = new ArrayList<>();
            for (Integer id : (List<Integer>) ids[1]) {
                ccList.add(new EventCombat((int) ids[0], id));
            }
            eventCombatDAO.insertList(ccList);
            return null;
        }
    }
}
