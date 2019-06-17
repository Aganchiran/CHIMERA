package com.aganchiran.chimera.chimerafront.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import com.aganchiran.chimera.chimeracore.combat.CombatModel;
import com.aganchiran.chimera.chimeracore.event.EventModel;
import com.aganchiran.chimera.chimeracore.eventcombat.EventCombat;
import com.aganchiran.chimera.chimerafront.activities.BattleActivity;
import com.aganchiran.chimera.chimerafront.activities.CombatSelectionActivity;
import com.aganchiran.chimera.chimerafront.dialogs.CreateEditCombatDialog;
import com.aganchiran.chimera.chimerafront.utils.SizeUtil;
import com.aganchiran.chimera.chimerafront.utils.adapters.CombatAdapter;
import com.aganchiran.chimera.chimerafront.utils.listeners.DragItemListener;
import com.aganchiran.chimera.chimerafront.utils.listeners.DropToDeleteRecyclerListener;
import com.aganchiran.chimera.viewmodels.EventCombatsListVM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class EventCombatsFragment extends Fragment {

    private static final int ADD_COMBATS = 1;
    private static final LinearLayout.LayoutParams VISIBLE = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    private static final LinearLayout.LayoutParams INVISIBLE = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0);

    private FloatingActionButton addCombatButton;
    private EventCombatsListVM eventCombatsListVM;
    private CombatAdapter adapter;

    private static final String ARG_EVENT_MODEL = "event_model";
    private View rootView;


    public EventCombatsFragment() {
    }

    public static EventCombatsFragment newInstance(EventModel eventModel) {
        EventCombatsFragment fragment = new EventCombatsFragment();
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
        rootView = inflater.inflate(R.layout.fragment_combat_list, container, false);
        eventCombatsListVM = ViewModelProviders.of(this).get(EventCombatsListVM.class);

        assert getArguments() != null;
        final EventModel eventModel = (EventModel) getArguments()
                .getSerializable(ARG_EVENT_MODEL);

        if (eventModel != null) {
            eventCombatsListVM.setEventModel(eventModel);

            LiveData<List<CombatModel>> combatListLiveData =
                    eventCombatsListVM.getCombatsForEvent(eventModel.getId());
            final RecyclerView recyclerView =
                    rootView.findViewById(R.id.combat_recycler_view);

            setupGrid(combatListLiveData, recyclerView);
        }

        setupButtons(rootView);

        final ImageView deleteArea = rootView.findViewById(R.id.delete_area);
        deleteArea.setOnDragListener(new DropToDeleteRecyclerListener(adapter, eventCombatsListVM) {
            @Override
            protected void onDrop() {
                if (adapter.getFlyingItemPos() != -1) {
                    CombatModel combat = adapter.getItemAt(adapter.getFlyingItemPos());
                    eventCombatsListVM.unlinkCombat(combat.getId());
                }
            }
        });

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
        adapter.setMenuActions(new CombatAdapter.MenuActions() {
            @Override
            public void editCombat(final CombatModel combat) {
                CreateEditCombatDialog dialog = new CreateEditCombatDialog();
                dialog.setListener(new CreateEditCombatDialog.CreateCombatDialogListener() {
                    @Override
                    public void saveCombat(String newName) {
                        combat.setName(newName);
                        eventCombatsListVM.update(combat);
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
                final LiveData<List<EventCombat>> ecs = eventCombatsListVM.getECsForEvent(eventCombatsListVM.getEventModel().getId());
                ecs.observe(getActivity(), new Observer<List<EventCombat>>() {
                    @Override
                    public void onChanged(@Nullable List<EventCombat> eventCharacters) {
                        reorderCombats(eventCharacters);
                        ecs.removeObserver(this);
                    }
                });
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
                createCombatDialog();
            }
        });

        Button acceptDeletion = rootView.findViewById(R.id.delete_button);
        acceptDeletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (CombatModel combatModel : adapter.getCheckedItemModels()) {

                    assert getArguments() != null;
                    EventModel eventModel = (EventModel) getArguments()
                            .getSerializable(ARG_EVENT_MODEL);
                    assert eventModel != null;
                    eventCombatsListVM.unlinkCombat(combatModel.getId());
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
        EventModel eventModel =
                (EventModel) getArguments().getSerializable(ARG_EVENT_MODEL);

        assert eventModel != null;
        CombatModel combatModel = new CombatModel(name, eventModel.getCampaignId());
        eventCombatsListVM.insert(combatModel);
    }

    private void createCombatDialog() {
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

    private void reorderCombats(List<EventCombat> eventCharacters) {
        Map<Integer, EventCombat> ecMap = new HashMap<>();
        for (final EventCombat ec : eventCharacters) {
            ecMap.put(ec.getCombatId(), ec);
        }

        for (int i = 0; i < adapter.getItemCount(); i++) {
            final CombatModel characterModel = adapter.getItemAt(i);
            ecMap.get(characterModel.getId()).setDisplayPosition(i);
        }
        eventCombatsListVM.updateECs(new ArrayList<>(ecMap.values()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_event_combat_management, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.select_combats:
                if (!adapter.getSelectModeEnabled()) {
                    adapter.enableSelectMode();
                    rootView.findViewById(R.id.deletion_interface).setLayoutParams(VISIBLE);
                    addCombatButton.hide();
                }
                return true;
            case R.id.link_combats:
                Intent intent = new Intent(getActivity(), CombatSelectionActivity.class);
                intent.putExtra("SELECTION_SCREEN", true);
                intent.putExtra("CAMPAIGN", eventCombatsListVM.getEventModel().getCampaignId());
                startActivityForResult(intent, ADD_COMBATS);
                return true;
            case R.id.new_combat:
                createCombatDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_COMBATS && resultCode == RESULT_OK) {

            final List<CombatModel> combatsToAdd
                    = (List<CombatModel>) data.getSerializableExtra("COMBATS");
            final ArrayList<Integer> combatsIds = new ArrayList<>();
            for (CombatModel c : combatsToAdd) {
                combatsIds.add(c.getId());
            }
            eventCombatsListVM.linkCombats(combatsIds);
        }
    }
}
