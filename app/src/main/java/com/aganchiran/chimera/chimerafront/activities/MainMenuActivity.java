package com.aganchiran.chimera.chimerafront.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aganchiran.chimera.R;

public class MainMenuActivity extends ActivityWithUpperBar {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.activity_main_menu);
//        repopulateDatabase();
        super.onCreate(savedInstanceState);
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
