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
import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.chimerafront.utils.SizeUtil;
import com.aganchiran.chimera.chimerafront.utils.adapters.CharacterAdapter;
import com.aganchiran.chimera.viewmodels.CharacterSelectionVM;

import java.io.Serializable;
import java.util.List;

public class CharacterSelectionActivity extends ActivityWithUpperBar {

    private CharacterSelectionVM characterSelectionVM;
    private CharacterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_character_selection);
        characterSelectionVM = ViewModelProviders.of(this).get(CharacterSelectionVM.class);

        final RecyclerView recyclerView = findViewById(R.id.character_recycler_view);
        final int campaignId = getIntent().getIntExtra("CAMPAIGN", Integer.MAX_VALUE);
        LiveData<List<CharacterModel>> chars = campaignId != Integer.MAX_VALUE
                ? characterSelectionVM.getCampaignCharacters(campaignId)
                : characterSelectionVM.getAllCharacters();

        setupGrid(chars, recyclerView);
        setupButtons();

        super.onCreate(savedInstanceState);
    }

    private void setupGrid(LiveData<List<CharacterModel>> data, RecyclerView recyclerView) {
        final View characterCard = getLayoutInflater().inflate(R.layout.item_character, null);
        final View characterLayout = characterCard.findViewById(R.id.character_item_layout);
        int characterWidth = SizeUtil.getViewWidth(characterLayout);
        int screenWidth = SizeUtil.getScreenWidth(CharacterSelectionActivity.this);
        int columnNumber = screenWidth / characterWidth;

        recyclerView.setLayoutManager(
                new GridLayoutManager(CharacterSelectionActivity.this, columnNumber));
        recyclerView.setHasFixedSize(true);

        adapter = new CharacterAdapter();
        recyclerView.setAdapter(adapter);

        data.observe(this, new Observer<List<CharacterModel>>() {
            @Override
            public void onChanged(@Nullable List<CharacterModel> characterModels) {
                adapter.setItemModels(characterModels);
            }
        });
        adapter.enableSelectMode();
    }

    private void setupButtons() {

        Button acceptSelection = findViewById(R.id.select_button);
        acceptSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                sendSelectedCharacters();
            }
        });

        Button cancelSelection = findViewById(R.id.cancel_button);
        cancelSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void sendSelectedCharacters() {
        List<CharacterModel> characters = adapter.getCheckedItemModels();
        if (!characters.isEmpty()) {
            Intent intent = new Intent();
            intent.putExtra("CHARACTERS", (Serializable) characters);
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

}
