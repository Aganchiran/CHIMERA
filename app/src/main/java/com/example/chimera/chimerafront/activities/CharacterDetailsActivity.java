package com.example.chimera.chimerafront.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.chimera.R;
import com.example.chimera.chimeracore.CharacterModel;
import com.example.chimera.viewmodel.ChimeraViewModel;

public class CharacterDetailsActivity extends ActivityWithUpperBar {

    private LiveData<CharacterModel> characterModel;
    private ChimeraViewModel chimeraViewModel;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.activity_character_details);
        chimeraViewModel = ViewModelProviders.of(this).get(ChimeraViewModel.class);

        int characterId = ((CharacterModel) getIntent().getSerializableExtra("CHARACTER"))
                .getId();
        characterModel = chimeraViewModel.getCharacterById(characterId);
        characterModel.observe(this, new Observer<CharacterModel>() {
            @Override
            public void onChanged(@Nullable CharacterModel characterModel) {
                ((TextView) findViewById(R.id.character_portrait))
                        .setText(characterModel.getName());
                ((TextView) findViewById(R.id.description_text_view))
                        .setText(characterModel.getDescription());
            }
        });

        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.character_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_character:
                Intent intent = new Intent(this, AddEditCharacterActivity.class);
                intent.putExtra("CHARACTER", characterModel.getValue());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
