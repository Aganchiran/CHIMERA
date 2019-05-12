package com.aganchiran.chimera.chimerafront.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.CharacterModel;
import com.aganchiran.chimera.chimerafront.utils.CharacterAdapter;
import com.aganchiran.chimera.chimerafront.utils.ScreenSizeManager;
import com.aganchiran.chimera.viewmodels.CharacterViewModel;

import java.util.Collections;
import java.util.List;

public class CharacterListActivity extends ActivityWithUpperBar {

    private static final LinearLayout.LayoutParams VISIBLE = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0, 1);
    private static final LinearLayout.LayoutParams INVISIBLE = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0, 0);

    private FloatingActionButton addCharacterButton;
    private CharacterViewModel characterViewModel;
    private CharacterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_character_list);
        characterViewModel = ViewModelProviders.of(this).get(CharacterViewModel.class);
        setupGrid();

        addCharacterButton = findViewById(R.id.add_character_button);
        addCharacterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CharacterListActivity.this, AddEditCharacterActivity.class);
                startActivity(intent);
            }
        });

        super.onCreate(savedInstanceState);
    }

    private void setupGrid() {
        final View characterCard = getLayoutInflater().inflate(R.layout.item_character, null);
        final View characterLayout = characterCard.findViewById(R.id.character_item_layout);
        int characterWidth = ScreenSizeManager.getViewWidth(characterLayout);
        int screenWidth = ScreenSizeManager.getScreenWidth(CharacterListActivity.this);
        int columnNumber = screenWidth / characterWidth;

        final RecyclerView recyclerView = findViewById(R.id.character_recycler_view);
        recyclerView.setLayoutManager(
                new GridLayoutManager(CharacterListActivity.this, columnNumber));
        recyclerView.setHasFixedSize(true);

        adapter = new CharacterAdapter();
        adapter.setOnItemClickListener(new CharacterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CharacterModel characterModel) {
                Intent intent = new Intent(CharacterListActivity.this,
                        CharacterProfileActivity.class);
                intent.putExtra("CHARACTER", characterModel);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);

        characterViewModel.getAllCharacters().observe(this, new Observer<List<CharacterModel>>() {
            @Override
            public void onChanged(@Nullable List<CharacterModel> characterModels) {
                adapter.setCharacterModels(characterModels);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP
                        | ItemTouchHelper.DOWN
                        | ItemTouchHelper.RIGHT
                        | ItemTouchHelper.LEFT, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder from,
                                  @NonNull RecyclerView.ViewHolder to) {
                final int fromPosition = from.getAdapterPosition();
                final int toPosition = to.getAdapterPosition();

                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(adapter.getCharacterModels(), i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(adapter.getCharacterModels(), i, i - 1);
                    }
                }

                adapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }

        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onStop() {
        new ReorderCharacterAsyncTask(adapter, characterViewModel).execute();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_character_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_characters:
                if (!adapter.isDeleteModeEnabled()) {
                    adapter.enableDeleteMode();
                    findViewById(R.id.character_deletion_interface).setLayoutParams(VISIBLE);
                    addCharacterButton.hide();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void cancelCharacterDeletion(View view) {
        adapter.disableDeleteMode();
        findViewById(R.id.character_deletion_interface).setLayoutParams(INVISIBLE);
        addCharacterButton.show();
    }

    public void deleteSelectedCharacters(View view) {
        for (CharacterModel characterModel : adapter.getCheckedCharacterModels()) {
            characterViewModel.delete(characterModel);
        }
        cancelCharacterDeletion(view);
    }

    private static class ReorderCharacterAsyncTask extends AsyncTask<Void, Void, Void> {
        private CharacterAdapter adapter;
        private CharacterViewModel characterViewModel;

        private ReorderCharacterAsyncTask(CharacterAdapter adapter, CharacterViewModel characterViewModel) {
            this.adapter = adapter;
            this.characterViewModel = characterViewModel;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                CharacterModel characterModel = adapter.getCharacterModels().get(i);
                characterModel.setDisplayPosition(i);
                characterViewModel.update(characterModel);
            }
            return null;
        }
    }

}
