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

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
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
import com.aganchiran.chimera.chimeracore.campaign.CampaignModel;
import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.chimeracore.consumable.ConsumableModel;
import com.aganchiran.chimera.chimerafront.activities.CharacterProfileActivity;
import com.aganchiran.chimera.chimerafront.dialogs.CreateEditCharacterDialog;
import com.aganchiran.chimera.chimerafront.utils.SizeUtil;
import com.aganchiran.chimera.chimerafront.utils.adapters.CharacterAdapter;
import com.aganchiran.chimera.chimerafront.utils.listeners.AbstractDropToListener;
import com.aganchiran.chimera.chimerafront.utils.listeners.DragItemListener;
import com.aganchiran.chimera.viewmodels.CampaignCharactersListVM;

import java.util.List;
import java.util.Objects;

public class CamCharactersFragment extends Fragment {

    private static final LinearLayout.LayoutParams VISIBLE = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    private static final LinearLayout.LayoutParams INVISIBLE = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0);

    private FloatingActionButton addCharacterButton;
    private CampaignCharactersListVM camChaListVM;
    private CharacterAdapter adapter;

    private static final String ARG_CAMPAIGN_MODEL = "campaign_model";
    private View rootView;


    public CamCharactersFragment() {
    }

    public static CamCharactersFragment newInstance(final CampaignModel campaignModel) {
        final CamCharactersFragment fragment = new CamCharactersFragment();
        final Bundle args = new Bundle();
        args.putSerializable(ARG_CAMPAIGN_MODEL, campaignModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_character_list, container, false);
        camChaListVM = ViewModelProviders.of(this).get(CampaignCharactersListVM.class);

        assert getArguments() != null;
        final CampaignModel campaignModel = (CampaignModel) getArguments()
                .getSerializable(ARG_CAMPAIGN_MODEL);

        final LiveData<List<CharacterModel>> characterListLiveData;
        if (campaignModel != null) {
            characterListLiveData = camChaListVM.getCampaignCharacters(campaignModel.getId());

        } else {
            characterListLiveData = camChaListVM.getCharactersWithoutCampaign();
        }

        final RecyclerView recyclerView =
                rootView.findViewById(R.id.character_recycler_view);

        setupGrid(characterListLiveData, recyclerView);
        setupButtons(rootView);

        final ImageView deleteArea = rootView.findViewById(R.id.delete_area);
        deleteArea.setOnDragListener(new AbstractDropToListener() {
            @Override
            protected void onDrop() {
                if (adapter.getFlyingItemPos() != -1) {
                    final CharacterModel characterModel = adapter.getItemAt(adapter.getFlyingItemPos());
                    deleteCharacter(characterModel);
                }
            }
        });

        return rootView;
    }

    private void setupGrid(final LiveData<List<CharacterModel>> data, final RecyclerView recyclerView) {
        final View characterCard = getLayoutInflater().inflate(R.layout.item_character, null);
        final View characterLayout = characterCard.findViewById(R.id.character_item_layout);
        final int characterWidth = SizeUtil.getViewWidth(characterLayout);
        final int screenWidth = SizeUtil.getScreenWidth(Objects.requireNonNull(getContext()));
        final int columnNumber = screenWidth / characterWidth;

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnNumber));
        recyclerView.setHasFixedSize(true);

        adapter = new CharacterAdapter();
        adapter.setListener(new CharacterAdapter.OnItemClickListener<CharacterModel>() {
            @Override
            public void onItemClick(final CharacterModel characterModel) {
                final Intent intent = new Intent(getActivity(), CharacterProfileActivity.class);
                intent.putExtra("CHARACTER", characterModel);
                startActivity(intent);
            }
        });
        adapter.setMenuActions(new CharacterAdapter.MenuActions() {
            @Override
            public void editCharacter(final CharacterModel character) {
                final CreateEditCharacterDialog dialog = new CreateEditCharacterDialog();
                dialog.setListener(new CreateEditCharacterDialog.CreateCharacterDialogListener() {
                    @Override
                    public void saveCharacter(final String newName, final String newDescription, final String image) {
                        character.setName(newName);
                        character.setDescription(newDescription);
                        character.setImage(image);

                        camChaListVM.update(character);
                    }

                    @Override
                    public CharacterModel getCharacter() {
                        return character;
                    }
                });
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "edit character");
            }

            @Override
            public void duplicateCharacter(final CharacterModel characterModel) {
                final LiveData<List<ConsumableModel>> characterConsumableLD =
                        camChaListVM.getCharacterConsumables(characterModel.getId());

                characterConsumableLD.observe(getActivity(), new Observer<List<ConsumableModel>>() {
                    @Override
                    public void onChanged(@Nullable final List<ConsumableModel> consumableModels) {
                        camChaListVM.duplicateCharacter(characterModel, consumableModels);
                        characterConsumableLD.removeObserver(this);
                    }
                });
            }

            @Override
            public void deleteCharacter(final CharacterModel characterModel) {
                CamCharactersFragment.this.deleteCharacter(characterModel);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setOnDragListener(new DragItemListener(adapter) {
            @Override
            protected void onDrop(View hiddenView) {
                super.onDrop(hiddenView);
                new ReorderCharacterAsyncTask(adapter, camChaListVM).execute();
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
            public void onClick(final View v) {
                createCharacterDialog();
            }
        });

        final Button acceptDeletion = rootView.findViewById(R.id.delete_button);
        acceptDeletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                deleteCharacters();
            }
        });

        final Button cancelDeletion = rootView.findViewById(R.id.cancel_button);
        cancelDeletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                cancelCharacterDeletion(rootView);
            }
        });
    }

    private void cancelCharacterDeletion(final View rootView) {
        adapter.disableSelectMode();
        rootView.findViewById(R.id.deletion_interface).setLayoutParams(INVISIBLE);
        addCharacterButton.show();
    }

    private void createCharacter(final String name, final String description, final String image) {
        assert getArguments() != null;
        final CampaignModel campaignModel =
                (CampaignModel) getArguments().getSerializable(ARG_CAMPAIGN_MODEL);

        Integer campaignId = null;
        if (campaignModel != null) {
            campaignId = campaignModel.getId();
        }
        final CharacterModel characterModel = new CharacterModel(name, description, campaignId);
        characterModel.setImage(image);
        camChaListVM.insert(characterModel);
    }

    private void createCharacterDialog() {
        final CreateEditCharacterDialog dialog = new CreateEditCharacterDialog();
        dialog.setListener(new CreateEditCharacterDialog.CreateCharacterDialogListener() {
            @Override
            public void saveCharacter(final String name, final String description, final String image) {
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

    private void deleteCharacter(final CharacterModel characterModel){
        new AlertDialog.Builder(getContext(), R.style.DialogTheme)
                .setTitle(getResources().getString(R.string.delete) + " " + characterModel.getName())
                .setMessage(getResources().getString(R.string.delete_character_confirmation))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        camChaListVM.delete(characterModel);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteCharacters(){
        new AlertDialog.Builder(getContext(), R.style.DialogTheme)
                .setTitle(getResources().getString(R.string.delete_characters))
                .setMessage(getResources().getString(R.string.delete_characters_confirmation))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        for (final CharacterModel characterModel : adapter.getCheckedItemModels()) {
                            camChaListVM.delete(characterModel);
                        }
                        cancelCharacterDeletion(rootView);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.menu_character_management, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.select_characters:
                if (!adapter.getSelectModeEnabled()) {
                    adapter.enableSelectMode();
                    rootView.findViewById(R.id.deletion_interface).setLayoutParams(VISIBLE);
                    addCharacterButton.hide();
                }
                return true;
            case R.id.new_character:
                createCharacterDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private static class ReorderCharacterAsyncTask extends AsyncTask<Void, Void, Void> {
        private CharacterAdapter adapter;
        private CampaignCharactersListVM camChaListVM;

        private ReorderCharacterAsyncTask(final CharacterAdapter adapter, final CampaignCharactersListVM camChaListVM) {
            this.adapter = adapter;
            this.camChaListVM = camChaListVM;
        }

        @Override
        protected Void doInBackground(final Void... voids) {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                final CharacterModel characterModel = adapter.getItemAt(i);
                characterModel.setDisplayPosition(i);
            }
            camChaListVM.updateCharacters(adapter.getItemModels());
            return null;
        }
    }
}
