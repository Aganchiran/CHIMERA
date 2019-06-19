package com.aganchiran.chimera.chimerafront.activities;

import android.os.Bundle;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimerafront.fragments.CamCharactersFragment;

public class PlayerCharactersActivity extends ActivityWithUpperBar {

    private CamCharactersFragment charactersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_player_characters);
        super.onCreate(savedInstanceState);
        charactersFragment = CamCharactersFragment.newInstance(null);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.character_list, charactersFragment)
                .commit();

    }
}
