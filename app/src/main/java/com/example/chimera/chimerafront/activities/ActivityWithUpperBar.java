package com.example.chimera.chimerafront.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.example.chimera.R;

public abstract class ActivityWithUpperBar extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.general_menu, menu);
        return true;
    }

    public void goToMainMenu(View view) {
        if (!(this instanceof MainMenuActivity)) {
            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
