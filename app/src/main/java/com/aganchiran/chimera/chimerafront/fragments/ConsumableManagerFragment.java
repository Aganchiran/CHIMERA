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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.CharacterModel;
import com.aganchiran.chimera.chimeracore.ConsumableModel;
import com.aganchiran.chimera.chimerafront.dialogs.CreateEditConsumableDialog;
import com.aganchiran.chimera.chimerafront.dialogs.ModifyConsumableDialog;
import com.aganchiran.chimera.chimerafront.utils.ConsumableAdapter;
import com.aganchiran.chimera.chimerafront.utils.ScreenSizeManager;
import com.aganchiran.chimera.viewmodels.ConsumableViewModel;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ConsumableManagerFragment extends Fragment {

    private static final LinearLayout.LayoutParams VISIBLE = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0, 1);
    private static final LinearLayout.LayoutParams INVISIBLE = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0, 0);

    private FloatingActionButton addCharacterButton;
    private ConsumableViewModel consumableViewModel;
    private ConsumableAdapter adapter;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_CHARACTER_MODEL = "character_model";


    public ConsumableManagerFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ConsumableManagerFragment newInstance(CharacterModel characterModel) {
        ConsumableManagerFragment fragment = new ConsumableManagerFragment();
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
                .inflate(R.layout.fragment_consumable_manager, container, false);
        consumableViewModel = ViewModelProviders.of(this).get(ConsumableViewModel.class);

        assert getArguments() != null;
        CharacterModel characterModel = (CharacterModel) getArguments()
                .getSerializable(ARG_CHARACTER_MODEL);

        if (characterModel != null) {
            LiveData<List<ConsumableModel>> consumableListLiveData =
                    consumableViewModel.getCharacterConsumables(characterModel.getId());
            final RecyclerView recyclerView =
                    rootView.findViewById(R.id.consumable_recycler_view);

            setupGrid(consumableListLiveData, recyclerView);
        }

        setupButtons(rootView);

        return rootView;
    }

    private void setupGrid(LiveData<List<ConsumableModel>> data, RecyclerView recyclerView) {

        final View consumableCard = getLayoutInflater().inflate(R.layout.item_consumable, null);
        final View characterLayout = consumableCard.findViewById(R.id.consumable_item_layout);
        int characterWidth = ScreenSizeManager.getViewWidth(characterLayout);
        int screenWidth = ScreenSizeManager.getScreenWidth(Objects.requireNonNull(getContext()));
        int columnNumber = screenWidth / characterWidth;

        recyclerView.setLayoutManager(
                new GridLayoutManager(getContext(), columnNumber));
        recyclerView.setHasFixedSize(true);

        adapter = new ConsumableAdapter();
        adapter.setListener(new ConsumableAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final ConsumableModel consumableModel) {
                ModifyConsumableDialog dialog = new ModifyConsumableDialog();
                dialog.setListener(new ModifyConsumableDialog.CreateConsumableDialogListener() {
                    @Override
                    public void saveValue(long value) {
                        consumableModel.setCurrentValue(value);
                        consumableViewModel.update(consumableModel);
                    }

                    @Override
                    public ConsumableModel getConsumable() {
                        return consumableModel;
                    }
                });
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "modify consumable");
            }

            @Override
            public void onEditClick(final ConsumableModel consumableModel) {
                CreateEditConsumableDialog dialog = new CreateEditConsumableDialog();
                dialog.setListener(new CreateEditConsumableDialog.CreateConsumableDialogListener() {
                    @Override
                    public void saveConsumable(String name, long max, long min) {
                        consumableModel.setName(name);
                        consumableModel.setMaxValue(max);
                        consumableModel.setMinValue(min);

                        final long current = consumableModel.getCurrentValue();
                        if (current > max){
                            consumableModel.setCurrentValue(max);
                        }else if(current < min){
                            consumableModel.setCurrentValue(min);
                        }

                        consumableViewModel.update(consumableModel);
                    }

                    @Override
                    public ConsumableModel getConsumable() {
                        return consumableModel;
                    }
                });
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "edit consumable");
            }
        });

        recyclerView.setAdapter(adapter);

        data.observe(this, new Observer<List<ConsumableModel>>() {
            @Override
            public void onChanged(@Nullable List<ConsumableModel> consumableModels) {
                adapter.setConsumableModels(consumableModels);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP
                        | ItemTouchHelper.DOWN
                        | ItemTouchHelper.RIGHT
                        | ItemTouchHelper.LEFT, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder from,
                                  @NonNull RecyclerView.ViewHolder to) {
                final int fromPosition = from.getAdapterPosition();
                final int toPosition = to.getAdapterPosition();

                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(adapter.getConsumableModels(), i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(adapter.getConsumableModels(), i, i - 1);
                    }
                }

                adapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }

        }).attachToRecyclerView(recyclerView);
    }

    private void setupButtons(final View rootView) {
        addCharacterButton = rootView.findViewById(R.id.add_consumable_button);
        addCharacterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateEditConsumableDialog dialog = new CreateEditConsumableDialog();
                dialog.setListener(new CreateEditConsumableDialog.CreateConsumableDialogListener() {
                    @Override
                    public void saveConsumable(String name, long max, long min) {
                        ConsumableManagerFragment.this.createConsumableImp(name, max, min);
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
                for (ConsumableModel consumableModel : adapter.getCheckedConsumableModels()) {
                    consumableViewModel.delete(consumableModel);
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
        adapter.disableDeleteMode();
        rootView.findViewById(R.id.deletion_interface).setLayoutParams(INVISIBLE);
        addCharacterButton.show();
    }

    private void createConsumableImp(String name, long max, long min) {
        assert getArguments() != null;
        CharacterModel characterModel =
                (CharacterModel) getArguments().getSerializable(ARG_CHARACTER_MODEL);

        ConsumableModel consumableModel = new ConsumableModel(name, max, min, max, min,
                false, "FF0000", -1, characterModel.getId());
        consumableViewModel.insert(consumableModel);
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
                if (!adapter.isDeleteModeEnabled()) {
                    adapter.enableDeleteMode();
                    Objects.requireNonNull(getActivity()).findViewById(R.id.deletion_interface)
                            .setLayoutParams(VISIBLE);
                    addCharacterButton.hide();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        new ConsumableManagerFragment.ReorderConsumableAsyncTask(adapter, consumableViewModel).execute();
        super.onStop();
    }

    private static class ReorderConsumableAsyncTask extends AsyncTask<Void, Void, Void> {
        private ConsumableAdapter adapter;
        private ConsumableViewModel consumableViewModel;

        private ReorderConsumableAsyncTask(ConsumableAdapter adapter, ConsumableViewModel consumableViewModel) {
            this.adapter = adapter;
            this.consumableViewModel = consumableViewModel;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                ConsumableModel consumableModel = adapter.getConsumableModels().get(i);
                consumableModel.setDisplayPosition(i);
                consumableViewModel.update(consumableModel);
            }
            return null;
        }
    }
}
