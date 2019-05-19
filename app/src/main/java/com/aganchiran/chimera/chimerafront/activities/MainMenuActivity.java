package com.aganchiran.chimera.chimerafront.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aganchiran.chimera.R;

public class MainMenuActivity extends ActivityWithUpperBar {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.activity_main_menu);
        super.onCreate(savedInstanceState);
    }

    public void goToCharactersList(final View view) {
        Intent intent = new Intent(this, CharacterListActivity.class);
        startActivity(intent);
    }

    public void goToCombatList(final View view) {
        Intent intent = new Intent(this, CombatListActivity.class);
        startActivity(intent);
    }

    public void goToCommingSoonScreen(final View view) {
        Intent intent = new Intent(this, CommingSoonActivity.class);
        startActivity(intent);
    }

}
