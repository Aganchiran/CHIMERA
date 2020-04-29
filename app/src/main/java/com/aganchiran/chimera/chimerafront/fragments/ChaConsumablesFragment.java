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
import com.aganchiran.chimera.chimeracore.consumable.ConsumableModel;
import com.aganchiran.chimera.chimerafront.dialogs.CreateEditConsumableDialog;
import com.aganchiran.chimera.chimerafront.dialogs.ModifyConsumableDialog;
import com.aganchiran.chimera.chimerafront.utils.SizeUtil;
import com.aganchiran.chimera.chimerafront.utils.adapters.ConsumableAdapter;
import com.aganchiran.chimera.chimerafront.utils.listeners.AbstractDropToListener;
import com.aganchiran.chimera.chimerafront.utils.listeners.DragItemListener;
import com.aganchiran.chimera.viewmodels.ConsumableListVM;

import java.util.List;
import java.util.Objects;

public class ChaConsumablesFragment extends Fragment {

    private static final LinearLayout.LayoutParams VISIBLE = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    private static final LinearLayout.LayoutParams INVISIBLE = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0);

    private FloatingActionButton addConsumableButton;
    private ConsumableListVM consumableListVM;
    private ConsumableAdapter adapter;

    private static final String ARG_CHARACTER_MODEL = "character_model";
    private View rootView;


    public ChaConsumablesFragment() {
    }

    public static ChaConsumablesFragment newInstance(final CharacterModel characterModel) {
        final ChaConsumablesFragment fragment = new ChaConsumablesFragment();
        final Bundle args = new Bundle();
        args.putSerializable(ARG_CHARACTER_MODEL, characterModel);
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
        rootView = inflater.inflate(R.layout.fragment_consumable_list, container, false);
        consumableListVM = ViewModelProviders.of(this).get(ConsumableListVM.class);

        assert getArguments() != null;
        final CharacterModel characterModel = (CharacterModel) getArguments()
                .getSerializable(ARG_CHARACTER_MODEL);

        if (characterModel != null) {
            final LiveData<List<ConsumableModel>> consumableListLiveData =
                    consumableListVM.getCharacterConsumables(characterModel.getId());
            final RecyclerView recyclerView =
                    rootView.findViewById(R.id.consumable_recycler_view);

            setupGrid(consumableListLiveData, recyclerView);
        }

        setupButtons();

        final ImageView deleteArea = rootView.findViewById(R.id.delete_area);
        deleteArea.setOnDragListener(new AbstractDropToListener() {
            @Override
            protected void onDrop() {
                if (adapter.getFlyingItemPos() != -1) {
                    final ConsumableModel consumableModel = adapter.getItemAt(adapter.getFlyingItemPos());
                    deleteConsumable(consumableModel);
                }
            }
        });

        return rootView;
    }

    private void setupGrid(LiveData<List<ConsumableModel>> data, RecyclerView recyclerView) {
        final View consumableCard = getLayoutInflater().inflate(R.layout.item_consumable, null);
        final View consumableLayout = consumableCard.findViewById(R.id.consumable_item_layout);
        final int consumableWidth = SizeUtil.getViewWidth(consumableLayout);
        final int screenWidth = SizeUtil.getScreenWidth(Objects.requireNonNull(getContext()));
        final int columnNumber = screenWidth / consumableWidth;

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnNumber));
        recyclerView.setHasFixedSize(true);

        adapter = new ConsumableAdapter();
        adapter.setListener(new ConsumableAdapter.OnItemClickListener<ConsumableModel>() {
            @Override
            public void onItemClick(final ConsumableModel consumableModel) {
                final ModifyConsumableDialog dialog = new ModifyConsumableDialog();
                dialog.setListener(new ModifyConsumableDialog.CreateConsumableDialogListener() {
                    @Override
                    public void saveValue(final long value) {
                        consumableModel.setCurrentValue(value);
                        consumableListVM.update(consumableModel);
                    }

                    @Override
                    public ConsumableModel getConsumable() {
                        return consumableModel;
                    }
                });
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "modify consumable");
            }
        });
        adapter.setMenuActions(new ConsumableAdapter.MenuActions() {
            @Override
            public void editConsumable(final ConsumableModel consumable) {
                final CreateEditConsumableDialog dialog = new CreateEditConsumableDialog();
                dialog.setListener(new CreateEditConsumableDialog.CreateConsumableDialogListener() {
                    @Override
                    public void saveConsumable(final String newName, final long newMax, final long newMin) {
                        consumable.setName(newName);
                        consumable.setMaxValue(newMax);
                        consumable.setMinValue(newMin);

                        consumableListVM.update(consumable);
                    }

                    @Override
                    public ConsumableModel getConsumable() {
                        return consumable;
                    }
                });
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "edit consumable");
            }

            @Override
            public void deleteConsumable(final ConsumableModel consumableModel) {
                ChaConsumablesFragment.this.deleteConsumable(consumableModel);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setOnDragListener(new DragItemListener(adapter) {
            @Override
            protected void onDrop(final View hiddenView) {
                super.onDrop(hiddenView);
                new ReorderConsumableAsyncTask(adapter, consumableListVM).execute();
            }
        });

        data.observe(this, new Observer<List<ConsumableModel>>() {
            @Override
            public void onChanged(@Nullable final List<ConsumableModel> consumableModels) {
                adapter.setItemModels(consumableModels);
            }
        });
    }

    private void setupButtons() {
        addConsumableButton = rootView.findViewById(R.id.add_consumable_button);
        addConsumableButton.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(final View v, final DragEvent event) {
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
        addConsumableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                createConsumableDialog();
            }
        });

        final Button acceptDeletion = rootView.findViewById(R.id.delete_button);
        acceptDeletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                deleteConsumables();
            }
        });

        final Button cancelDeletion = rootView.findViewById(R.id.cancel_button);
        cancelDeletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                cancelConsumableDeletion(rootView);
            }
        });
    }

    private void cancelConsumableDeletion(final View rootView) {
        adapter.disableSelectMode();
        rootView.findViewById(R.id.deletion_interface).setLayoutParams(INVISIBLE);
        addConsumableButton.show();
    }

    private void createConsumable(final String name, final long max, final long min) {
        assert getArguments() != null;
        final CharacterModel characterModel =
                (CharacterModel) getArguments().getSerializable(ARG_CHARACTER_MODEL);

        assert characterModel != null;
        final ConsumableModel consumableModel = new ConsumableModel(name, max, min,
                "FF0000", characterModel.getId());
        consumableListVM.insert(consumableModel);
    }

    public void createConsumableDialog() {
        final CreateEditConsumableDialog dialog = new CreateEditConsumableDialog();
        dialog.setListener(new CreateEditConsumableDialog.CreateConsumableDialogListener() {
            @Override
            public void saveConsumable(final String name, final long max, final long min) {
                ChaConsumablesFragment.this.createConsumable(name, max, min);
            }

            @Override
            public ConsumableModel getConsumable() {
                return null;
            }
        });
        assert getFragmentManager() != null;
        dialog.show(getFragmentManager(), "create consumable");
    }

    private void deleteConsumable(final ConsumableModel consumableModel){
        new AlertDialog.Builder(getContext(), R.style.DialogTheme)
                .setTitle(getResources().getString(R.string.delete) + " " + consumableModel.getName())
                .setMessage(getResources().getString(R.string.delete_consumable_confirmation))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        consumableListVM.delete(consumableModel);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteConsumables(){
        new AlertDialog.Builder(getContext(), R.style.DialogTheme)
                .setTitle(getResources().getString(R.string.delete_consumables))
                .setMessage(getResources().getString(R.string.delete_consumables_confirmation))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        for (ConsumableModel consumableModel : adapter.getCheckedItemModels()) {
                            consumableListVM.delete(consumableModel);
                        }
                        cancelConsumableDeletion(rootView);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.menu_consumable_management, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_consumables:
                if (!adapter.getSelectModeEnabled()) {
                    adapter.enableSelectMode();
                    Objects.requireNonNull(getActivity()).findViewById(R.id.deletion_interface)
                            .setLayoutParams(VISIBLE);
                    addConsumableButton.hide();
                }
                return true;
            case R.id.new_consumable:
                createConsumableDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static class ReorderConsumableAsyncTask extends AsyncTask<Void, Void, Void> {
        private ConsumableAdapter adapter;
        private ConsumableListVM consumableListVM;

        private ReorderConsumableAsyncTask(final ConsumableAdapter adapter,
                                           final ConsumableListVM consumableListVM) {
            this.adapter = adapter;
            this.consumableListVM = consumableListVM;
        }

        @Override
        protected Void doInBackground(final Void... voids) {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                final ConsumableModel consumableModel = adapter.getItemAt(i);
                consumableModel.setDisplayPosition(i);
            }
            consumableListVM.updateConsumables(adapter.getItemModels());
            return null;
        }
    }
}
