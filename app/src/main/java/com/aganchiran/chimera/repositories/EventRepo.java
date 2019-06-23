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

import com.aganchiran.chimera.chimeracore.event.EventDAO;
import com.aganchiran.chimera.chimeracore.event.EventModel;
import com.aganchiran.chimera.chimeradata.ChimeraDB;

import java.util.Arrays;
import java.util.List;

public class EventRepo {

    private EventDAO eventDAO;
    private LiveData<List<EventModel>> allEvents;

    public EventRepo(Application application) {
        ChimeraDB database = ChimeraDB.getInstance(application);
        eventDAO = database.eventDAO();
        allEvents = eventDAO.getAllEvents();
    }

    public void insert(EventModel eventModel) {
        new InsertEventAsyncTask(eventDAO).execute(eventModel);
    }

    public void update(EventModel eventModel) {
        new UpdateEventAsyncTask(eventDAO).execute(eventModel);

    }

    public void updateEvents(List<EventModel> eventModelList) {
        EventModel[] eventArray = new EventModel[eventModelList.size()];
        eventArray = eventModelList.toArray(eventArray);
        new UpdateEventListAsyncTask(eventDAO).execute(eventArray);
    }

    public void delete(EventModel eventModel) {
        new DeleteEventAsyncTask(eventDAO).execute(eventModel);

    }

    public void deleteAllEvents() {
        new DeleteAllEventAsyncTask(eventDAO).execute();

    }

    public LiveData<EventModel> getEventById(int id) {
        return eventDAO.getEventById(id);
    }

    public LiveData<EventModel> getEventByCoordsAndCampaign(float xCoord, float yCoord, int campaignId) {
        return eventDAO.getEventByCoordsAndCampaign(xCoord, yCoord, campaignId);
    }

    public LiveData<List<EventModel>> getAllEvents() {
        return allEvents;
    }

    public LiveData<List<EventModel>> getCampaignEvents(int campaignId) {
        return eventDAO.getCampaignEvents(campaignId);
    }

    /////////////////////////
    //////////TASKS//////////
    /////////////////////////

    private static class InsertEventAsyncTask extends AsyncTask<EventModel, Void, Void> {

        private EventDAO eventDAO;

        private InsertEventAsyncTask(EventDAO eventDAO) {
            this.eventDAO = eventDAO;
        }

        @Override
        protected Void doInBackground(EventModel... eventModels) {
            eventDAO.insert(eventModels[0]);
            return null;
        }
    }

    private static class UpdateEventAsyncTask extends AsyncTask<EventModel, Void, Void> {

        private EventDAO eventDAO;

        private UpdateEventAsyncTask(EventDAO eventDAO) {
            this.eventDAO = eventDAO;
        }

        @Override
        protected Void doInBackground(EventModel... eventModels) {
            eventDAO.update(eventModels[0]);
            return null;
        }
    }

    private static class UpdateEventListAsyncTask extends AsyncTask<EventModel, Void, Void> {

        private EventDAO eventDAO;

        private UpdateEventListAsyncTask(EventDAO eventDAO) {
            this.eventDAO = eventDAO;
        }


        @Override
        protected final Void doInBackground(EventModel... eventModels) {
            eventDAO.updateEvents(Arrays.asList(eventModels));
            return null;
        }
    }

    private static class DeleteEventAsyncTask extends AsyncTask<EventModel, Void, Void> {

        private EventDAO eventDAO;

        private DeleteEventAsyncTask(EventDAO eventDAO) {
            this.eventDAO = eventDAO;
        }

        @Override
        protected Void doInBackground(EventModel... eventModels) {
            eventDAO.delete(eventModels[0]);
            return null;
        }
    }

    private static class DeleteAllEventAsyncTask extends AsyncTask<Void, Void, Void> {

        private EventDAO eventDAO;

        private DeleteAllEventAsyncTask(EventDAO eventDAO) {
            this.eventDAO = eventDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            eventDAO.deleteAllEvents();
            return null;
        }
    }

    private static class GetEventAsyncTask extends AsyncTask<EventModel, Void, LiveData<EventModel>> {

        private EventDAO eventDAO;

        private GetEventAsyncTask(EventDAO eventDAO) {
            this.eventDAO = eventDAO;
        }

        @Override
        protected LiveData<EventModel> doInBackground(EventModel... eventModels) {
            return eventDAO.getEventById(eventModels[0].getId());
        }
    }
}
