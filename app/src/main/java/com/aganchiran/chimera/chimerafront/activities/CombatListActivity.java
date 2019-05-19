package com.aganchiran.chimera.chimerafront.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.combat.CombatModel;
import com.aganchiran.chimera.chimerafront.dialogs.CreateEditCombatDialog;
import com.aganchiran.chimera.chimerafront.utils.CombatAdapter;
import com.aganchiran.chimera.chimerafront.utils.CompareUtil;
import com.aganchiran.chimera.chimerafront.utils.DragItemListener;
import com.aganchiran.chimera.chimerafront.utils.DropToDeleteListener;
import com.aganchiran.chimera.chimerafront.utils.SizeUtil;
import com.aganchiran.chimera.viewmodels.CombatListVM;

import java.util.List;

public class CombatListActivity extends ActivityWithUpperBar {

    private static final LinearLayout.LayoutParams VISIBLE = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0, 1);
    private static final LinearLayout.LayoutParams INVISIBLE = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0, 0);

    private FloatingActionButton addCombatButton;
    private CombatListVM combatListVM;
    private CombatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_combat_list);
        combatListVM = ViewModelProviders.of(this).get(CombatListVM.class);

        final RecyclerView recyclerView = findViewById(R.id.combat_recycler_view);
        setupGrid(combatListVM.getAllCombats(), recyclerView);

        setupButtons();

        final ImageView deleteArea = findViewById(R.id.delete_area);
        deleteArea.setOnDragListener(new DropToDeleteListener(adapter, combatListVM));

        super.onCreate(savedInstanceState);
    }

    private void setupGrid(LiveData<List<CombatModel>> data, RecyclerView recyclerView) {
        final View combatCard = getLayoutInflater().inflate(R.layout.item_combat, null);
        final View combatLayout = combatCard.findViewById(R.id.combat_item_layout);
        int combatWidth = SizeUtil.getViewWidth(combatLayout);
        int screenWidth = SizeUtil.getScreenWidth(CombatListActivity.this);
        int columnNumber = screenWidth / combatWidth;

        recyclerView.setLayoutManager(
                new GridLayoutManager(CombatListActivity.this, columnNumber));
        recyclerView.setHasFixedSize(true);

        adapter = new CombatAdapter();
        adapter.setListener(new CombatAdapter.OnItemClickListener<CombatModel>() {
            @Override
            public void onItemClick(CombatModel combatModel) {
//                Intent intent = new Intent(CombatListActivity.this,
//                        CombatProfileActivity.class);
//                intent.putExtra("COMBAT", combatModel);
//                startActivity(intent);
            }
        });
        adapter.setEditCombat(new CombatAdapter.EditCombat() {
            @Override
            public void perform(final CombatModel combat) {
                CreateEditCombatDialog dialog = new CreateEditCombatDialog();
                dialog.setListener(new CreateEditCombatDialog.CreateCombatDialogListener() {
                    @Override
                    public void saveCombat(String newName) {
                        combat.setName(newName);
                        combatListVM.update(combat);
                    }

                    @Override
                    public CombatModel getCombat() {
                        return combat;
                    }
                });
                assert getFragmentManager() != null;
                dialog.show(getSupportFragmentManager(), "edit combat");
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setOnDragListener(new DragItemListener(adapter) {
            @Override
            protected void onDrop(View hiddenView) {
                super.onDrop(hiddenView);
                new ReorderCombatAsyncTask(adapter, combatListVM).execute();
            }
        });

        data.observe(this, new Observer<List<CombatModel>>() {
            @Override
            public void onChanged(@Nullable List<CombatModel> combatModels) {
                assert combatModels != null;
                if (!CompareUtil.areItemsTheSame(adapter.getItemModels(), combatModels)) {
                    adapter.setItemModels(combatModels);
                }
            }
        });
    }

    private void setupButtons(){
        addCombatButton = findViewById(R.id.add_combat_button);
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
                        CombatModel combatModel = new CombatModel(name);
                        combatListVM.insert(combatModel);
                    }

                    @Override
                    public CombatModel getCombat() {
                        return null;
                    }
                });
                assert getFragmentManager() != null;
                dialog.show(getSupportFragmentManager(), "create combat");
            }
        });

    }

    public void cancelCombatDeletion(View view) {
        adapter.disableDeleteMode();
        findViewById(R.id.combat_deletion_interface).setLayoutParams(INVISIBLE);
        addCombatButton.show();
    }

    public void deleteSelectedCombats(View view) {
        for (CombatModel combatModel : adapter.getCheckedItemModels()) {
            combatListVM.delete(combatModel);
        }
        cancelCombatDeletion(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_combat_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_combats:
                if (!adapter.isDeleteModeEnabled()) {
                    adapter.enableDeleteMode();
                    findViewById(R.id.combat_deletion_interface).setLayoutParams(VISIBLE);
                    addCombatButton.hide();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static class ReorderCombatAsyncTask extends AsyncTask<Void, Void, Void> {
        private CombatAdapter adapter;
        private CombatListVM combatListVM;

        private ReorderCombatAsyncTask(CombatAdapter adapter, CombatListVM combatListVM) {
            this.adapter = adapter;
            this.combatListVM = combatListVM;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                CombatModel combatModel = adapter.getItemAt(i);
                combatModel.setDisplayPosition(i);
            }
            combatListVM.updateCombats(adapter.getItemModels());
            return null;
        }
    }

}
