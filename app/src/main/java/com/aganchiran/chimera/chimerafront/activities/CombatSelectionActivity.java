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

package com.aganchiran.chimera.chimerafront.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.combat.CombatModel;
import com.aganchiran.chimera.chimerafront.utils.SizeUtil;
import com.aganchiran.chimera.chimerafront.utils.adapters.CombatAdapter;
import com.aganchiran.chimera.viewmodels.CombatSelectionVM;

import java.io.Serializable;
import java.util.List;

public class CombatSelectionActivity extends ActivityWithUpperBar {

    private CombatSelectionVM combatSelectionVM;
    private CombatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_combat_selection);
        combatSelectionVM = ViewModelProviders.of(this).get(CombatSelectionVM.class);

        final RecyclerView recyclerView = findViewById(R.id.combat_recycler_view);
        final int campaignId = getIntent().getIntExtra("CAMPAIGN", Integer.MAX_VALUE);
        LiveData<List<CombatModel>> chars = campaignId != Integer.MAX_VALUE
                ? combatSelectionVM.getCampaignCombats(campaignId)
                : combatSelectionVM.getAllCombats();

        setupGrid(chars, recyclerView);
        setupButtons();

        super.onCreate(savedInstanceState);
    }

    private void setupGrid(LiveData<List<CombatModel>> data, RecyclerView recyclerView) {
        final View combatCard = getLayoutInflater().inflate(R.layout.item_combat, null);
        final View combatLayout = combatCard.findViewById(R.id.combat_item_layout);
        int combatWidth = SizeUtil.getViewWidth(combatLayout);
        int screenWidth = SizeUtil.getScreenWidth(CombatSelectionActivity.this);
        int columnNumber = screenWidth / combatWidth;

        recyclerView.setLayoutManager(
                new GridLayoutManager(CombatSelectionActivity.this, columnNumber));
        recyclerView.setHasFixedSize(true);

        adapter = new CombatAdapter();
        recyclerView.setAdapter(adapter);

        data.observe(this, new Observer<List<CombatModel>>() {
            @Override
            public void onChanged(@Nullable List<CombatModel> combatModels) {
                adapter.setItemModels(combatModels);
            }
        });
        adapter.enableSelectMode();
    }

    private void setupButtons() {

        Button acceptSelection = findViewById(R.id.select_button);
        acceptSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSelectedCombats();
            }
        });

        Button cancelSelection = findViewById(R.id.cancel_button);
        cancelSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void sendSelectedCombats() {
        Intent intent = new Intent();
        List<CombatModel> combats = adapter.getCheckedItemModels();
        intent.putExtra("COMBATS", (Serializable) combats);
        setResult(RESULT_OK, intent);
        finish();
    }

}
