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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.campaign.CampaignModel;
import com.aganchiran.chimera.chimerafront.dialogs.CreateEditCampaignDialog;
import com.aganchiran.chimera.chimerafront.utils.adapters.CampaignAdapter;
import com.aganchiran.chimera.chimerafront.utils.DragItemListener;
import com.aganchiran.chimera.chimerafront.utils.DropToDeleteListener;
import com.aganchiran.chimera.chimerafront.utils.SizeUtil;
import com.aganchiran.chimera.viewmodels.CampaignListVM;

import java.util.List;

public class CampaignListActivity extends ActivityWithUpperBar {

    private static final LinearLayout.LayoutParams VISIBLE = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    private static final LinearLayout.LayoutParams INVISIBLE = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0);

    private FloatingActionButton addCampaignButton;
    private CampaignListVM campaignListVM;
    private CampaignAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_campaign_list);
        campaignListVM = ViewModelProviders.of(this).get(CampaignListVM.class);

        final RecyclerView recyclerView = findViewById(R.id.campaign_recycler_view);
        setupGrid(campaignListVM.getAllCampaigns(), recyclerView);

        setupButtons();

        final ImageView deleteArea = findViewById(R.id.delete_area);
        deleteArea.setOnDragListener(new DropToDeleteListener(adapter, campaignListVM));

        super.onCreate(savedInstanceState);
    }

    private void setupGrid(LiveData<List<CampaignModel>> data, RecyclerView recyclerView) {
        final View campaignCard = getLayoutInflater().inflate(R.layout.item_campaign, null);
        final View campaignLayout = campaignCard.findViewById(R.id.campaign_item_layout);
        int campaignWidth = SizeUtil.getViewWidth(campaignLayout);
        int screenWidth = SizeUtil.getScreenWidth(CampaignListActivity.this);
        int columnNumber = screenWidth / campaignWidth;

        recyclerView.setLayoutManager(
                new GridLayoutManager(CampaignListActivity.this, columnNumber));
        recyclerView.setHasFixedSize(true);

        adapter = new CampaignAdapter();
        adapter.setListener(new CampaignAdapter.OnItemClickListener<CampaignModel>() {
            @Override
            public void onItemClick(CampaignModel campaignModel) {
                Intent intent = new Intent(CampaignListActivity.this,
                        CampaignProfileActivity.class);
                intent.putExtra("CAMPAIGN", campaignModel);
                startActivity(intent);
            }
        });
        adapter.setEditCampaign(new CampaignAdapter.EditCampaign() {
            @Override
            public void perform(final CampaignModel campaign) {
                CreateEditCampaignDialog dialog = new CreateEditCampaignDialog();
                dialog.setListener(new CreateEditCampaignDialog.CreateCampaignDialogListener() {
                    @Override
                    public void saveCampaign(String name, String description) {
                        campaign.setName(name);
                        campaign.setDescription(description);
                        campaignListVM.update(campaign);
                    }

                    @Override
                    public CampaignModel getCampaign() {
                        return campaign;
                    }
                });
                assert getFragmentManager() != null;
                dialog.show(getSupportFragmentManager(), "edit campaign");
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setOnDragListener(new DragItemListener(adapter) {
            @Override
            protected void onDrop(View hiddenView) {
                super.onDrop(hiddenView);
                new ReorderCampaignAsyncTask(adapter, campaignListVM).execute();
            }
        });

        data.observe(this, new Observer<List<CampaignModel>>() {
            @Override
            public void onChanged(@Nullable List<CampaignModel> campaignModels) {
                adapter.setItemModels(campaignModels);
            }
        });
    }

    private void setupButtons() {
        addCampaignButton = findViewById(R.id.add_campaign_button);
        addCampaignButton.setOnDragListener(new View.OnDragListener() {
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
        addCampaignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateEditCampaignDialog dialog = new CreateEditCampaignDialog();
                dialog.setListener(new CreateEditCampaignDialog.CreateCampaignDialogListener() {
                    @Override
                    public void saveCampaign(String name, String description) {
                        CampaignModel campaignModel = new CampaignModel(name, description);
                        campaignListVM.insert(campaignModel);
                    }

                    @Override
                    public CampaignModel getCampaign() {
                        return null;
                    }
                });
                assert getFragmentManager() != null;
                dialog.show(getSupportFragmentManager(), "create campaign");
            }
        });

    }

    public void cancelCampaignDeletion(View view) {
        adapter.disableSelectMode();
        findViewById(R.id.campaign_deletion_interface).setLayoutParams(INVISIBLE);
        addCampaignButton.show();
    }

    public void deleteSelectedCampaigns(View view) {
        for (CampaignModel campaignModel : adapter.getCheckedItemModels()) {
            campaignListVM.delete(campaignModel);
        }
        cancelCampaignDeletion(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_campaign_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_campaigns:
                if (!adapter.getSelectModeEnabled()) {
                    adapter.enableSelectMode();
                    findViewById(R.id.campaign_deletion_interface).setLayoutParams(VISIBLE);
                    addCampaignButton.hide();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static class ReorderCampaignAsyncTask extends AsyncTask<Void, Void, Void> {
        private CampaignAdapter adapter;
        private CampaignListVM campaignListVM;

        private ReorderCampaignAsyncTask(CampaignAdapter adapter, CampaignListVM campaignListVM) {
            this.adapter = adapter;
            this.campaignListVM = campaignListVM;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                CampaignModel campaignModel = adapter.getItemAt(i);
                campaignModel.setDisplayPosition(i);
            }
            campaignListVM.updateCampaigns(adapter.getItemModels());
            return null;
        }
    }

}
