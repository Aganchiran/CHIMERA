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
import com.aganchiran.chimera.chimeracore.campaign.CampaignModel;
import com.aganchiran.chimera.chimeracore.combat.CombatModel;
import com.aganchiran.chimera.chimerafront.activities.BattleActivity;
import com.aganchiran.chimera.chimerafront.dialogs.CreateEditCombatDialog;
import com.aganchiran.chimera.chimerafront.utils.adapters.CombatAdapter;
import com.aganchiran.chimera.chimerafront.utils.DragItemListener;
import com.aganchiran.chimera.chimerafront.utils.DropToDeleteListener;
import com.aganchiran.chimera.chimerafront.utils.SizeUtil;
import com.aganchiran.chimera.viewmodels.CampaignCombatsListVM;

import java.util.List;
import java.util.Objects;

public class CamCombatsFragment extends Fragment {

    private static final LinearLayout.LayoutParams VISIBLE = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    private static final LinearLayout.LayoutParams INVISIBLE = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0);

    private FloatingActionButton addCombatButton;
    private CampaignCombatsListVM camChaListVM;
    private CombatAdapter adapter;

    private static final String ARG_CAMPAIGN_MODEL = "campaign_model";


    public CamCombatsFragment() {
    }

    public static CamCombatsFragment newInstance(CampaignModel campaignModel) {
        CamCombatsFragment fragment = new CamCombatsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CAMPAIGN_MODEL, campaignModel);
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
                .inflate(R.layout.fragment_combat_list, container, false);
        camChaListVM = ViewModelProviders.of(this).get(CampaignCombatsListVM.class);

        assert getArguments() != null;
        CampaignModel campaignModel = (CampaignModel) getArguments()
                .getSerializable(ARG_CAMPAIGN_MODEL);

        if (campaignModel != null) {
            LiveData<List<CombatModel>> combatListLiveData =
                    camChaListVM.getCampaignCombats(campaignModel.getId());
            final RecyclerView recyclerView =
                    rootView.findViewById(R.id.combat_recycler_view);

            setupGrid(combatListLiveData, recyclerView);
        }

        setupButtons(rootView);

        final ImageView deleteArea = rootView.findViewById(R.id.delete_area);
        deleteArea.setOnDragListener(new DropToDeleteListener(adapter, camChaListVM));

        return rootView;
    }

    private void setupGrid(LiveData<List<CombatModel>> data, RecyclerView recyclerView) {
        final View combatCard = getLayoutInflater().inflate(R.layout.item_combat, null);
        final View combatLayout = combatCard.findViewById(R.id.combat_item_layout);
        int combatWidth = SizeUtil.getViewWidth(combatLayout);
        int screenWidth = SizeUtil.getScreenWidth(Objects.requireNonNull(getContext()));
        int columnNumber = screenWidth / combatWidth;

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnNumber));
        recyclerView.setHasFixedSize(true);

        adapter = new CombatAdapter();
        adapter.setListener(new CombatAdapter.OnItemClickListener<CombatModel>() {
            @Override
            public void onItemClick(final CombatModel combatModel) {
                Intent intent = new Intent(getActivity(), BattleActivity.class);
                intent.putExtra("COMBAT", combatModel);
                startActivity(intent);
            }
        });
        adapter.setEditCombat(new CombatAdapter.EditCombat() {
            @Override
            public void perform(final CombatModel combat) {
                CreateEditCombatDialog dialog = new CreateEditCombatDialog();
                dialog.setListener(new CreateEditCombatDialog.CreateCombatDialogListener() {

                    @Override
                    public void saveCombat(String name) {
                        combat.setName(name);
                        camChaListVM.update(combat);
                    }

                    @Override
                    public CombatModel getCombat() {
                        return combat;
                    }
                });
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "edit combat");
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setOnDragListener(new DragItemListener(adapter) {
            @Override
            protected void onDrop(View hiddenView) {
                super.onDrop(hiddenView);
                new ReorderCombatAsyncTask(adapter, camChaListVM).execute();
            }
        });

        data.observe(this, new Observer<List<CombatModel>>() {
            @Override
            public void onChanged(@Nullable List<CombatModel> combatModels) {
                adapter.setItemModels(combatModels);
            }
        });
    }

    private void setupButtons(final View rootView) {
        addCombatButton = rootView.findViewById(R.id.add_combat_button);
        addCombatButton.setOnDragListener(new View.OnDragListener() {
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
        addCombatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateEditCombatDialog dialog = new CreateEditCombatDialog();
                dialog.setListener(new CreateEditCombatDialog.CreateCombatDialogListener() {

                    @Override
                    public void saveCombat(String name) {
                        createCombat(name);
                    }

                    @Override
                    public CombatModel getCombat() {
                        return null;
                    }
                });
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "create combat");

            }
        });

        Button acceptDeletion = rootView.findViewById(R.id.accept_button);
        acceptDeletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (CombatModel combatModel : adapter.getCheckedItemModels()) {
                    camChaListVM.delete(combatModel);
                }
                cancelCombatDeletion(rootView);
            }
        });

        Button cancelDeletion = rootView.findViewById(R.id.cancel_button);
        cancelDeletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelCombatDeletion(rootView);
            }
        });
    }

    private void cancelCombatDeletion(View rootView) {
        adapter.disableSelectMode();
        rootView.findViewById(R.id.deletion_interface).setLayoutParams(INVISIBLE);
        addCombatButton.show();
    }

    private void createCombat(String name) {
        assert getArguments() != null;
        CampaignModel campaignModel =
                (CampaignModel) getArguments().getSerializable(ARG_CAMPAIGN_MODEL);

        assert campaignModel != null;
        CombatModel combatModel = new CombatModel(name, campaignModel.getId());
        camChaListVM.insert(combatModel);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_combat_management, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_combats:
                if (!adapter.getSelectModeEnabled()) {
                    adapter.enableSelectMode();
                    Objects.requireNonNull(getActivity()).findViewById(R.id.deletion_interface)
                            .setLayoutParams(VISIBLE);
                    addCombatButton.hide();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class ReorderCombatAsyncTask extends AsyncTask<Void, Void, Void> {
        private CombatAdapter adapter;
        private CampaignCombatsListVM camChaListVM;

        private ReorderCombatAsyncTask(CombatAdapter adapter, CampaignCombatsListVM camChaListVM) {
            this.adapter = adapter;
            this.camChaListVM = camChaListVM;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                CombatModel combatModel = adapter.getItemAt(i);
                combatModel.setDisplayPosition(i);
            }
            camChaListVM.updateCombats(adapter.getItemModels());
            return null;
        }
    }
}
