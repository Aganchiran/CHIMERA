package com.aganchiran.chimera.chimerafront.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.CharacterModel;
import com.aganchiran.chimera.viewmodels.CharacterViewModel;

public class AddEditCharacterActivity extends AppCompatActivity {

    private CharacterViewModel characterViewModel;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private CharacterModel characterModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_edit_character);
        characterViewModel = ViewModelProviders.of(this).get(CharacterViewModel.class);

        nameEditText = findViewById(R.id.character_edit_name);
        descriptionEditText = findViewById(R.id.character_edit_description);

        characterModel = (CharacterModel) getIntent().getSerializableExtra("CHARACTER");
        if (characterModel != null){
            nameEditText.setText(characterModel.getName());
            descriptionEditText.setText(characterModel.getDescription());
        }

        super.onCreate(savedInstanceState);
    }

    public void saveCharacter(View view) {
        if (characterModel == null) {
            createCharacter();
        } else {
            updateCharacter(characterModel);
        }
    }

    private void updateCharacter(CharacterModel characterModel) {
        final String name = nameEditText.getText().toString();
        final String description = descriptionEditText.getText().toString();
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Name is mandatory", Toast.LENGTH_SHORT).show();
            return;
        }

        characterModel.setName(name);
        characterModel.setDescription(description);
        characterViewModel.update(characterModel);
        finish();
    }

    private void createCharacter() {
        final String name = nameEditText.getText().toString();
        final String description = descriptionEditText.getText().toString();
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Name is mandatory", Toast.LENGTH_SHORT).show();
            return;
        }

        CharacterModel characterModel = new CharacterModel(name, description);
        characterViewModel.insert(characterModel);
        finish();
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

}
