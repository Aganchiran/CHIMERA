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

package com.aganchiran.chimera.chimerafront.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.chimeracore.event.EventModel;
import com.aganchiran.chimera.chimeracore.eventcharacter.EventCharacter;
import com.aganchiran.chimera.chimerafront.activities.CharacterProfileActivity;
import com.aganchiran.chimera.chimerafront.activities.CharacterSelectionActivity;
import com.aganchiran.chimera.chimerafront.dialogs.CreateEditCharacterDialog;
import com.aganchiran.chimera.chimerafront.utils.SizeUtil;
import com.aganchiran.chimera.chimerafront.utils.adapters.CharacterAdapter;
import com.aganchiran.chimera.chimerafront.utils.listeners.DragItemListener;
import com.aganchiran.chimera.chimerafront.utils.listeners.DropToDeleteRecyclerListener;
import com.aganchiran.chimera.viewmodels.EventCharactersListVM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class EventCharactersFragment extends Fragment {

    private static final int ADD_CHARACTERS = 1;
    private static final LinearLayout.LayoutParams VISIBLE = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    private static final LinearLayout.LayoutParams INVISIBLE = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0);

    private FloatingActionButton addCharacterButton;
    private EventCharactersListVM eventChaListVM;
    private CharacterAdapter adapter;

    private static final String ARG_EVENT_MODEL = "event_model";
    private View rootView;


    public EventCharactersFragment() {
    }

    public static EventCharactersFragment newInstance(EventModel eventModel) {
        EventCharactersFragment fragment = new EventCharactersFragment();
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
        rootView = inflater.inflate(R.layout.fragment_character_list, container, false);
        eventChaListVM = ViewModelProviders.of(this).get(EventCharactersListVM.class);

        assert getArguments() != null;
        final EventModel eventModel = (EventModel) getArguments()
                .getSerializable(ARG_EVENT_MODEL);

        if (eventModel != null) {
            eventChaListVM.setEventModel(eventModel);

            LiveData<List<CharacterModel>> characterListLiveData =
                    eventChaListVM.getCharactersForEvent(eventModel.getId());
            final RecyclerView recyclerView =
                    rootView.findViewById(R.id.character_recycler_view);

            setupGrid(characterListLiveData, recyclerView);
        }

        setupButtons(rootView);

        final ImageView deleteArea = rootView.findViewById(R.id.delete_area);
        deleteArea.setOnDragListener(new DropToDeleteRecyclerListener(adapter, eventChaListVM) {
            @Override
            protected void onDrop() {
                if (adapter.getFlyingItemPos() != -1) {
                    CharacterModel character = adapter.getItemAt(adapter.getFlyingItemPos());
                    eventChaListVM.unlinkCharacter(character.getId());
                }
            }
        });

        return rootView;
    }

    private void setupGrid(LiveData<List<CharacterModel>> data, RecyclerView recyclerView) {
        final View characterCard = getLayoutInflater().inflate(R.layout.item_character, null);
        final View characterLayout = characterCard.findViewById(R.id.character_item_layout);
        int characterWidth = SizeUtil.getViewWidth(characterLayout);
        int screenWidth = SizeUtil.getScreenWidth(Objects.requireNonNull(getContext()));
        int columnNumber = screenWidth / characterWidth;

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnNumber));
        recyclerView.setHasFixedSize(true);

        adapter = new CharacterAdapter();
        adapter.setListener(new CharacterAdapter.OnItemClickListener<CharacterModel>() {
            @Override
            public void onItemClick(final CharacterModel characterModel) {
                Intent intent = new Intent(getActivity(), CharacterProfileActivity.class);
                intent.putExtra("CHARACTER", characterModel);
                startActivity(intent);
            }
        });
        adapter.setMenuActions(new CharacterAdapter.MenuActions() {
            @Override
            public void editCharacter(final CharacterModel character) {
                CreateEditCharacterDialog dialog = new CreateEditCharacterDialog();
                dialog.setListener(new CreateEditCharacterDialog.CreateCharacterDialogListener() {
                    @Override
                    public void saveCharacter(String newName, String newDescription, String image) {
                        character.setName(newName);
                        character.setDescription(newDescription);
                        character.setImage(image);

                        eventChaListVM.update(character);
                    }

                    @Override
                    public CharacterModel getCharacter() {
                        return character;
                    }
                });
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "edit character");
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setOnDragListener(new DragItemListener(adapter) {
            @Override
            protected void onDrop(View hiddenView) {
                super.onDrop(hiddenView);

                final LiveData<List<EventCharacter>> ecs = eventChaListVM.getECsForEvent(eventChaListVM.getEventModel().getId());
                ecs.observe(getActivity(), new Observer<List<EventCharacter>>() {
                    @Override
                    public void onChanged(@Nullable List<EventCharacter> eventCharacters) {
                        reorderCharacters(eventCharacters);
                        ecs.removeObserver(this);
                    }
                });
            }
        });

        data.observe(this, new Observer<List<CharacterModel>>() {
            @Override
            public void onChanged(@Nullable List<CharacterModel> characterModels) {
                adapter.setItemModels(characterModels);
            }
        });
    }

    private void setupButtons(final View rootView) {
        addCharacterButton = rootView.findViewById(R.id.add_character_button);
        addCharacterButton.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        ((FloatingActionButton) v).hide();
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        ((FloatingActionButton) v).show();
                        break;
                }
                return true;
            }
        });
        addCharacterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCharacterDialog();
            }
        });

        Button acceptDeletion = rootView.findViewById(R.id.delete_button);
        acceptDeletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (CharacterModel characterModel : adapter.getCheckedItemModels()) {

                    assert getArguments() != null;
                    EventModel eventModel = (EventModel) getArguments()
                            .getSerializable(ARG_EVENT_MODEL);
                    assert eventModel != null;
                    eventChaListVM.unlinkCharacter(characterModel.getId());
                }
                cancelCharacterDeletion(rootView);
            }
        });

        Button cancelDeletion = rootView.findViewById(R.id.cancel_button);
        cancelDeletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelCharacterDeletion(rootView);
            }
        });
    }

    private void cancelCharacterDeletion(View rootView) {
        adapter.disableSelectMode();
        rootView.findViewById(R.id.deletion_interface).setLayoutParams(INVISIBLE);
        addCharacterButton.show();
    }

    private void createCharacter(String name, String description, String image) {
        assert getArguments() != null;
        EventModel eventModel =
                (EventModel) getArguments().getSerializable(ARG_EVENT_MODEL);

        assert eventModel != null;
        CharacterModel characterModel = new CharacterModel(name, description, eventModel.getCampaignId());
        characterModel.setImage(image);
        eventChaListVM.insert(characterModel);
    }

    private void createCharacterDialog() {
        CreateEditCharacterDialog dialog = new CreateEditCharacterDialog();
        dialog.setListener(new CreateEditCharacterDialog.CreateCharacterDialogListener() {
            @Override
            public void saveCharacter(String name, String description, String image) {
                createCharacter(name, description, image);
            }

            @Override
            public CharacterModel getCharacter() {
                return null;
            }
        });
        assert getFragmentManager() != null;
        dialog.show(getFragmentManager(), "create character");
    }

    private void reorderCharacters(List<EventCharacter> eventCharacters) {
        Map<Integer, EventCharacter> ecMap = new HashMap<>();
        for (final EventCharacter ec : eventCharacters) {
            ecMap.put(ec.getCharacterId(), ec);
        }

        for (int i = 0; i < adapter.getItemCount(); i++) {
            final CharacterModel characterModel = adapter.getItemAt(i);
            ecMap.get(characterModel.getId()).setDisplayPosition(i);
        }
        eventChaListVM.updateECs(new ArrayList<>(ecMap.values()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_event_character_management, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.select_characters:
                if (!adapter.getSelectModeEnabled()) {
                    adapter.enableSelectMode();
                    rootView.findViewById(R.id.deletion_interface)
                            .setLayoutParams(VISIBLE);
                    addCharacterButton.hide();
                }
                return true;
            case R.id.link_characters:
                Intent intent = new Intent(getActivity(), CharacterSelectionActivity.class);
                intent.putExtra("SELECTION_SCREEN", true);
                intent.putExtra("CAMPAIGN", eventChaListVM.getEventModel().getCampaignId());
                startActivityForResult(intent, ADD_CHARACTERS);
                return true;
            case R.id.new_character:
                createCharacterDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CHARACTERS && resultCode == RESULT_OK) {

            final List<CharacterModel> charactersToAdd
                    = (List<CharacterModel>) data.getSerializableExtra("CHARACTERS");
            final ArrayList<Integer> charactersIds = new ArrayList<>();
            for (CharacterModel c : charactersToAdd) {
                charactersIds.add(c.getId());
            }
            eventChaListVM.linkCharacters(charactersIds);
        }
    }

    private static class ReorderCharacterAsyncTask extends AsyncTask<Void, Void, Void> {
        private CharacterAdapter adapter;
        private EventCharactersListVM eventCharactersListVM;

        private ReorderCharacterAsyncTask(CharacterAdapter adapter, EventCharactersListVM eventCharactersListVM) {
            this.adapter = adapter;
            this.eventCharactersListVM = eventCharactersListVM;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                CharacterModel characterModel = adapter.getItemAt(i);
                characterModel.setDisplayPosition(i);
            }
            eventCharactersListVM.updateCharacters(adapter.getItemModels());
            return null;
        }
    }
}
