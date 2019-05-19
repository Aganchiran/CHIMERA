package com.aganchiran.chimera.chimerafront.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.CharacterModel;
import com.aganchiran.chimera.chimerafront.utils.CharacterAdapter;
import com.aganchiran.chimera.chimerafront.utils.CompareUtil;
import com.aganchiran.chimera.chimerafront.utils.DragItemListener;
import com.aganchiran.chimera.chimerafront.utils.DropToDeleteListener;
import com.aganchiran.chimera.chimerafront.utils.SizeUtil;
import com.aganchiran.chimera.viewmodels.CharacterViewModel;

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
                Intent intent = new Intent(CharacterListActivity.this, CreateEditCharacterActivity.class);
                startActivity(intent);
            }
        });
        addCharacterButton.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        ((FloatingActionButton) v).hide();
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        ((FloatingActionButton) v).show();
                        break;
                }
                return true;
            }
        });

        final ImageView deleteArea = findViewById(R.id.delete_area);
        deleteArea.setOnDragListener(new DropToDeleteListener(adapter, characterViewModel));

        super.onCreate(savedInstanceState);
    }

    private void setupGrid() {
        final View characterCard = getLayoutInflater().inflate(R.layout.item_character, null);
        final View characterLayout = characterCard.findViewById(R.id.character_item_layout);
        int characterWidth = SizeUtil.getViewWidth(characterLayout);
        int screenWidth = SizeUtil.getScreenWidth(CharacterListActivity.this);
        int columnNumber = screenWidth / characterWidth;

        final RecyclerView recyclerView = findViewById(R.id.character_recycler_view);
        recyclerView.setLayoutManager(
                new GridLayoutManager(CharacterListActivity.this, columnNumber));
        recyclerView.setHasFixedSize(true);

        adapter = new CharacterAdapter();
        adapter.setListener(new CharacterAdapter.OnItemClickListener<CharacterModel>() {
            @Override
            public void onItemClick(CharacterModel characterModel) {
                Intent intent = new Intent(CharacterListActivity.this,
                        CharacterProfileActivity.class);
                intent.putExtra("CHARACTER", characterModel);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setOnDragListener(new DragItemListener(adapter) {
            @Override
            protected void onDrop(View hiddenView) {
                super.onDrop(hiddenView);
                new ReorderCharacterAsyncTask(adapter, characterViewModel).execute();
            }
        });

        characterViewModel.getAllCharacters().observe(this, new Observer<List<CharacterModel>>() {
            @Override
            public void onChanged(@Nullable List<CharacterModel> characterModels) {
                assert characterModels != null;
                if (!CompareUtil.areItemsTheSame(adapter.getItemModels(), characterModels)) {
                    adapter.setItemModels(characterModels);
                }
            }
        });
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
        for (CharacterModel characterModel : adapter.getCheckedItemModels()) {
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
                CharacterModel characterModel = adapter.getItemAt(i);
                characterModel.setDisplayPosition(i);
            }
            characterViewModel.updateCharacters(adapter.getItemModels());
            return null;
        }
    }

}
