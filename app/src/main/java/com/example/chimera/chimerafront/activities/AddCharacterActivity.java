package com.example.chimera.chimerafront.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chimera.R;

public class AddCharacterActivity extends AppCompatActivity {

    public static final String EXTRA_NAME =
            "com.example.chimera.chimerafront.activities.EXTRA_NAME";
    public static final String EXTRA_DESCRIPTION =
            "com.example.chimera.chimerafront.activities.EXTRA_DERSCRIPTION";

    private EditText nameEditText;
    private EditText descriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_character);

        nameEditText = findViewById(R.id.character_edit_name);
        descriptionEditText = findViewById(R.id.character_edit_description);

        super.onCreate(savedInstanceState);
    }

    public void saveCharacter(View view){
        final String name = nameEditText.getText().toString();
        final String description = descriptionEditText.getText().toString();
        if (name.trim().isEmpty()){
            Toast.makeText(this, "Name is mandatory", Toast.LENGTH_SHORT).show();
            return;
        }

        final Intent intent = new Intent();
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_DESCRIPTION, description);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void cancel(View view){
        setResult(RESULT_CANCELED);
        finish();
    }

}
