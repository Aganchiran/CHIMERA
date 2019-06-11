package com.aganchiran.chimera.chimerafront.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.event.EventModel;
import com.aganchiran.chimera.chimerafront.dialogs.CreateEditEventDialog;
import com.aganchiran.chimera.viewmodels.EventDetailsVM;

public class EventDetailsFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_EVENT_MODEL = "event_model";
    private EventDetailsVM eventDetailsVM;


    public EventDetailsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static EventDetailsFragment newInstance(EventModel eventModel) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EVENT_MODEL, eventModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater
                .inflate(R.layout.fragment_event_details, container, false);
        eventDetailsVM = ViewModelProviders.of(this).get(EventDetailsVM.class);

        if (getArguments() != null) {
            final EventModel eventModel = (EventModel) getArguments()
                    .getSerializable(ARG_EVENT_MODEL);

            if (eventModel != null) {
                LiveData<EventModel> eventLiveData =
                        eventDetailsVM.getEventById(eventModel.getId());
                eventLiveData.observe(this, new Observer<EventModel>() {
                    @Override
                    public void onChanged(@Nullable EventModel eventModel) {
                        assert eventModel != null;
                        ((TextView) rootView.findViewById(R.id.event_image))
                                .setText(eventModel.getName());
                        ((TextView) rootView.findViewById(R.id.description_text_view))
                                .setText(eventModel.getDescription());
                    }
                });
            }
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_event_item, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit_event:
                CreateEditEventDialog dialog = new CreateEditEventDialog();
                dialog.setListener(new CreateEditEventDialog.CreateEventDialogListener() {
                    @Override
                    public void saveEvent(String newName, String newDescription) {
                        final EventModel event = (EventModel) getArguments()
                                .getSerializable(ARG_EVENT_MODEL);
                        event.setName(newName);
                        event.setDescription(newDescription);

                        eventDetailsVM.update(event);
                    }

                    @Override
                    public EventModel getEvent() {
                        final EventModel event = (EventModel) getArguments()
                                .getSerializable(ARG_EVENT_MODEL);
                        return event;
                    }
                });
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "edit event");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
