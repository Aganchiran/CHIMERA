package com.aganchiran.chimera.chimerafront.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import com.aganchiran.chimera.chimerafront.utils.CompareUtil;
import com.aganchiran.chimera.chimerafront.utils.ConsumableAdapter;
import com.aganchiran.chimera.chimerafront.utils.DragItemListener;
import com.aganchiran.chimera.chimerafront.utils.DropToDeleteListener;
import com.aganchiran.chimera.chimerafront.utils.SizeUtil;
import com.aganchiran.chimera.viewmodels.ConsumableListVM;

import java.util.List;
import java.util.Objects;

public class ConsumableListFragment extends Fragment {

    private static final LinearLayout.LayoutParams VISIBLE = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    private static final LinearLayout.LayoutParams INVISIBLE = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0);

    private FloatingActionButton addCharacterButton;
    private ConsumableListVM consumableListVM;
    private ConsumableAdapter adapter;

    private static final String ARG_CHARACTER_MODEL = "character_model";


    public ConsumableListFragment() {
    }

    public static ConsumableListFragment newInstance(CharacterModel characterModel) {
        ConsumableListFragment fragment = new ConsumableListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CHARACTER_MODEL, characterModel);
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
                .inflate(R.layout.fragment_consumable_list, container, false);
        consumableListVM = ViewModelProviders.of(this).get(ConsumableListVM.class);

        assert getArguments() != null;
        CharacterModel characterModel = (CharacterModel) getArguments()
                .getSerializable(ARG_CHARACTER_MODEL);

        if (characterModel != null) {
            LiveData<List<ConsumableModel>> consumableListLiveData =
                    consumableListVM.getCharacterConsumables(characterModel.getId());
            final RecyclerView recyclerView =
                    rootView.findViewById(R.id.consumable_recycler_view);

            setupGrid(consumableListLiveData, recyclerView);
        }

        setupButtons(rootView);

        final ImageView deleteArea = rootView.findViewById(R.id.delete_area);
        deleteArea.setOnDragListener(new DropToDeleteListener(adapter, consumableListVM));

        return rootView;
    }

    private void setupGrid(LiveData<List<ConsumableModel>> data, RecyclerView recyclerView) {
        final View consumableCard = getLayoutInflater().inflate(R.layout.item_consumable, null);
        final View characterLayout = consumableCard.findViewById(R.id.consumable_item_layout);
        int characterWidth = SizeUtil.getViewWidth(characterLayout);
        int screenWidth = SizeUtil.getScreenWidth(Objects.requireNonNull(getContext()));
        int columnNumber = screenWidth / characterWidth;

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnNumber));
        recyclerView.setHasFixedSize(true);

        adapter = new ConsumableAdapter();
        adapter.setListener(new ConsumableAdapter.OnItemClickListener<ConsumableModel>() {
            @Override
            public void onItemClick(final ConsumableModel consumableModel) {
                ModifyConsumableDialog dialog = new ModifyConsumableDialog();
                dialog.setListener(new ModifyConsumableDialog.CreateConsumableDialogListener() {
                    @Override
                    public void saveValue(long value) {
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
        adapter.setSaveConsumable(new ConsumableAdapter.SaveConsumable() {
            @Override
            public void perform(final ConsumableModel consumable) {
                CreateEditConsumableDialog dialog = new CreateEditConsumableDialog();
                dialog.setListener(new CreateEditConsumableDialog.CreateConsumableDialogListener() {
                    @Override
                    public void saveConsumable(String newName, long newMax, long newMin) {
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
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setOnDragListener(new DragItemListener(adapter) {
            @Override
            protected void onDrop(View hiddenView) {
                super.onDrop(hiddenView);
                new ReorderConsumableAsyncTask(adapter, consumableListVM).execute();
            }
        });

        data.observe(this, new Observer<List<ConsumableModel>>() {
            @Override
            public void onChanged(@Nullable List<ConsumableModel> consumableModels) {
                assert consumableModels != null;
                if (!CompareUtil.areItemsTheSame(adapter.getItemModels(), consumableModels)) {
                    adapter.setItemModels(consumableModels);
                }
            }
        });
    }

    private void setupButtons(final View rootView) {
        addCharacterButton = rootView.findViewById(R.id.add_consumable_button);
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

                CreateEditConsumableDialog dialog = new CreateEditConsumableDialog();
                dialog.setListener(new CreateEditConsumableDialog.CreateConsumableDialogListener() {
                    @Override
                    public void saveConsumable(String name, long max, long min) {
                        ConsumableListFragment.this.createConsumable(name, max, min);
                    }

                    @Override
                    public ConsumableModel getConsumable() {
                        return null;
                    }
                });
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "create consumable");

            }
        });

        Button acceptDeletion = rootView.findViewById(R.id.accept_button);
        acceptDeletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ConsumableModel consumableModel : adapter.getCheckedItemModels()) {
                    consumableListVM.delete(consumableModel);
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

    private void createConsumable(String name, long max, long min) {
        assert getArguments() != null;
        CharacterModel characterModel =
                (CharacterModel) getArguments().getSerializable(ARG_CHARACTER_MODEL);

        assert characterModel != null;
        ConsumableModel consumableModel = new ConsumableModel(name, max, min,
                "FF0000", characterModel.getId());
        consumableListVM.insert(consumableModel);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_consumable_management, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_consumables:
                if (!adapter.getSelectModeEnabled()) {
                    adapter.enableSelectMode();
                    Objects.requireNonNull(getActivity()).findViewById(R.id.deletion_interface)
                            .setLayoutParams(VISIBLE);
                    addCharacterButton.hide();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class ReorderConsumableAsyncTask extends AsyncTask<Void, Void, Void> {
        private ConsumableAdapter adapter;
        private ConsumableListVM consumableListVM;

        private ReorderConsumableAsyncTask(ConsumableAdapter adapter, ConsumableListVM consumableListVM) {
            this.adapter = adapter;
            this.consumableListVM = consumableListVM;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                ConsumableModel consumableModel = adapter.getItemAt(i);
                consumableModel.setDisplayPosition(i);
            }
            consumableListVM.updateConsumables(adapter.getItemModels());
            return null;
        }
    }
}
