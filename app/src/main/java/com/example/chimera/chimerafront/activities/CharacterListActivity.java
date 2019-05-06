package com.example.chimera.chimerafront.activities;

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
import android.view.DragEvent;
import android.view.View;

import com.example.chimera.R;
import com.example.chimera.chimeracore.CharacterModel;
import com.example.chimera.chimerafront.utils.CharacterAdapter;
import com.example.chimera.chimerafront.utils.ScreenSizeManager;
import com.example.chimera.viewmodel.ChimeraViewModel;

import java.util.Collections;
import java.util.List;

public class CharacterListActivity extends ActivityWithUpperBar implements View.OnDragListener {

    public static final int ADD_CHARACTER_REQUEST = 1;

    private ChimeraViewModel chimeraViewModel;
    private CharacterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_character_list);
        chimeraViewModel = ViewModelProviders.of(this).get(ChimeraViewModel.class);
        setupGrid();

        FloatingActionButton addCharacterButton = findViewById(R.id.add_character_button);
        addCharacterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CharacterListActivity.this, AddCharacterActivity.class);
                startActivityForResult(intent, ADD_CHARACTER_REQUEST);
            }
        });

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_CHARACTER_REQUEST && resultCode == RESULT_OK) {
            final String name = data.getStringExtra(AddCharacterActivity.EXTRA_NAME);
            final String description = data.getStringExtra(AddCharacterActivity.EXTRA_DESCRIPTION);

            CharacterModel characterModel = new CharacterModel(name, description);
            chimeraViewModel.insert(characterModel);
        }
    }

    private void setupGrid() {
        final View characterCard = getLayoutInflater().inflate(R.layout.item_character, null);
        final View characterLayout = characterCard.findViewById(R.id.character_item_layout);
        int characterWidth = ScreenSizeManager.getViewWidth(characterLayout);
        int screenWidth = ScreenSizeManager.getScreenWidth(CharacterListActivity.this);
        int columnNumber = screenWidth / characterWidth;

        final RecyclerView recyclerView = findViewById(R.id.character_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(CharacterListActivity.this, columnNumber));
        recyclerView.setHasFixedSize(true);

        adapter = new CharacterAdapter();
        adapter.setOnItemClickListener(new CharacterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CharacterModel characterModel) {
                Intent intent = new Intent(CharacterListActivity.this, CharacterDetailsActivity.class);
                intent.putExtra("CHARACTER", characterModel);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        chimeraViewModel.getAllCharacters().observe(this, new Observer<List<CharacterModel>>() {
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
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {}

        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        return false;
    }

    @Override
    protected void onStop() {
        new ReorderCharacterAsyncTask(adapter, chimeraViewModel).execute();
        super.onStop();
    }

    private static class ReorderCharacterAsyncTask extends AsyncTask<Void, Void, Void> {
        private CharacterAdapter adapter;
        private ChimeraViewModel chimeraViewModel;

        private ReorderCharacterAsyncTask(CharacterAdapter adapter, ChimeraViewModel chimeraViewModel) {
            this.adapter = adapter;
            this.chimeraViewModel = chimeraViewModel;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                CharacterModel characterModel = adapter.getCharacterModels().get(i);
                characterModel.setDisplayPosition(i);
                chimeraViewModel.update(characterModel);
            }
            return null;
        }
    }

}
