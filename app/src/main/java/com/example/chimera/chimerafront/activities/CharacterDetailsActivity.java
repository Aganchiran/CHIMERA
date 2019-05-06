package com.example.chimera.chimerafront.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.example.chimera.R;
import com.example.chimera.chimeracore.CharacterModel;

public class CharacterDetailsActivity extends ActivityWithUpperBar {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.activity_character_details);

        CharacterModel characterModel = (CharacterModel) getIntent().getSerializableExtra("CHARACTER");
        ((TextView) findViewById(R.id.character_portrait)).setText(characterModel.getName());
        ((TextView) findViewById(R.id.description_text_view)).setText(characterModel.getDescription());

        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.general_menu, menu);
        return true;
    }
}
