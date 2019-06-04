package com.aganchiran.chimera.chimerafront.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.aganchiran.chimera.R;

public class MainMenuActivity extends ActivityWithUpperBar {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.activity_main_menu);
        int orientation = getResources().getConfiguration().orientation;
        LinearLayout ol = findViewById(R.id.orientation_layout);

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            ol.setOrientation(LinearLayout.VERTICAL);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ol.setOrientation(LinearLayout.HORIZONTAL);
        }
//        repopulateDatabase();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        LinearLayout ol = findViewById(R.id.orientation_layout);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ol.setOrientation(LinearLayout.VERTICAL);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ol.setOrientation(LinearLayout.HORIZONTAL);
        }
    }

    public void goToCharactersList(final View view) {
//        Intent intent = new Intent(this, CharacterListActivity.class);
//        startActivity(intent);
    }

    public void goToCombatList(final View view) {
        Intent intent = new Intent(this, CombatListActivity.class);
        startActivity(intent);
    }

    public void goToCampaignList(final View view) {
        Intent intent = new Intent(this, CampaignListActivity.class);
        startActivity(intent);
    }

//    private void repopulateDatabase(){
//
//        final CharacterDetailsVM characterDetailsVM = ViewModelProviders.of(this).get(CharacterDetailsVM.class);
//        final CombatListVM combatListVM = ViewModelProviders.of(this).get(CombatListVM.class);
//        final BattleVM battleVM= ViewModelProviders.of(this).get(BattleVM.class);
//
//
//        final CharacterModel pepe = new CharacterModel("Pepe", "");
//        final CharacterModel juan = new CharacterModel("Juan", "");
//        characterDetailsVM.insert(pepe);
//        characterDetailsVM.insert(juan);
//        characterDetailsVM.insert(new CharacterModel("María", ""));
//        characterDetailsVM.insert(new CharacterModel("Gerardo", ""));
//
//        final CombatModel epic = new CombatModel("Epic Battle");
//        final CombatModel normal = new CombatModel("Normal Battle");
//        combatListVM.insert(epic);
//        combatListVM.insert(normal);
//        combatListVM.insert(new CombatModel("Fun Battle"));
//
//        battleVM.linkCharacterToCombat(epic.getId(), pepe.getId());
//        battleVM.linkCharacterToCombat(epic.getId(), juan.getId());
//        battleVM.linkCharacterToCombat(normal.getId(), pepe.getId());
//    }

}
