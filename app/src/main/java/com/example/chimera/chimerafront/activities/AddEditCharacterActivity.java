package com.example.chimera.chimerafront.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chimera.R;
import com.example.chimera.chimeracore.CharacterModel;
import com.example.chimera.viewmodel.ChimeraViewModel;

public class AddEditCharacterActivity extends AppCompatActivity {

    private ChimeraViewModel chimeraViewModel;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private CharacterModel characterModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_edit_character);
        chimeraViewModel = ViewModelProviders.of(this).get(ChimeraViewModel.class);

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
        chimeraViewModel.update(characterModel);
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
        chimeraViewModel.insert(characterModel);
        finish();
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

}
