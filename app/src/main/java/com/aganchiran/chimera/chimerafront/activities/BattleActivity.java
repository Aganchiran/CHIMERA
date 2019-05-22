package com.aganchiran.chimera.chimerafront.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.chimeracore.combat.CombatModel;
import com.aganchiran.chimera.chimerafront.utils.DefendersAdapter;
import com.aganchiran.chimera.chimerafront.utils.InitiativeAdapter;
import com.aganchiran.chimera.viewmodels.battle.BattleVM;
import com.aganchiran.chimera.viewmodels.battle.BattleVMFactory;

import org.apache.commons.collections4.list.SetUniqueList;

import java.util.ArrayList;
import java.util.List;

public class BattleActivity extends ActivityWithUpperBar {

    private static final int ADD_CHARACTERS = 1;
    private View NO_ATTACKER;

    private BattleVM battleVM;
    private InitiativeAdapter initiativeAdapter;
    private View attacker;
    private List<CharacterModel> defenders = SetUniqueList.setUniqueList(new ArrayList<CharacterModel>());
    private DefendersAdapter defendersAdapter;
    private RecyclerView defenderRecycler;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.activity_battle);
        NO_ATTACKER = findViewById(R.id.attacker);
        attacker = NO_ATTACKER;

        Intent intent = this.getIntent();
        CombatModel combat = (CombatModel) intent.getSerializableExtra("COMBAT");
        battleVM = ViewModelProviders.of(this,
                new BattleVMFactory(this.getApplication(), combat)).get(BattleVM.class);


        setupInitiativeList();
        setupDefendersList();

        super.onCreate(savedInstanceState);
    }

    private void setupInitiativeList() {
        final RecyclerView initiativeRecycler = findViewById(R.id.initiativeRecycler);
        initiativeRecycler.setLayoutManager(new LinearLayoutManager(BattleActivity.this,
                LinearLayoutManager.VERTICAL, false));
        initiativeRecycler.setHasFixedSize(true);

        initiativeAdapter = battleVM.getInitiativeAdapter();
        initiativeAdapter.setListener(new InitiativeAdapter.OnCharacterClickListener() {

            @Override
            public void onCharacterClick(View characterCell) {
                changeAttacker(characterCell);
            }

            @Override
            public void addCharacter() {
                Intent intent = new Intent(BattleActivity.this, CharacterListActivity.class);
                intent.putExtra("SELECTION_SCREEN", true);
                startActivityForResult(intent, ADD_CHARACTERS);
            }
        });

        battleVM.getCharactersForCombat().observe(this, new Observer<List<CharacterModel>>() {
            @Override
            public void onChanged(@Nullable List<CharacterModel> characterModels) {
                if (battleVM.getIniCharacters() == null){
                    battleVM.setIniCharacters(characterModels);
                    initiativeAdapter.submitList(characterModels);
                }
            }
        });
        initiativeRecycler.setAdapter(initiativeAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof InitiativeAdapter.AddButtonHolder) {
                    return 0;
                } else {
                    return super.getSwipeDirs(recyclerView, viewHolder);
                }
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder holder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    ((InitiativeAdapter.InitiativeHolder) holder).disselectAsAttacker();
                    changeAttacker(NO_ATTACKER);

                    CharacterModel characterToDelete =
                            initiativeAdapter.getCharacterAt(holder.getAdapterPosition());
                    defenders.remove(characterToDelete);
                    defendersAdapter.submitList(defenders);
                    battleVM.unlinkCharacterFromCombat(characterToDelete);
                    initiativeAdapter.notifyItemRemoved(holder.getAdapterPosition());
                    initiativeAdapter.submitList(battleVM.getIniCharacters());

                } else {
                    final CharacterModel characterToAdd =
                            initiativeAdapter.getCharacterAt(holder.getAdapterPosition());
                    if (initiativeAdapter.getAttacker() == null) {
                        changeAttacker(NO_ATTACKER);
                    } else {
                        changeAttacker(initiativeAdapter.getAttacker().getCopy());
                    }
                    defenders.add(characterToAdd);
                    defendersAdapter.submitList(defenders);
                    initiativeAdapter.notifyItemChanged(holder.getAdapterPosition());
                }
            }
        }).attachToRecyclerView(initiativeRecycler);
    }

    private void setupDefendersList() {
        defenderRecycler = findViewById(R.id.defenderRecycler);
        defenderRecycler.setLayoutManager(new LinearLayoutManager(BattleActivity.this,
                LinearLayoutManager.VERTICAL, false));
        defenderRecycler.setHasFixedSize(true);

        defendersAdapter = new DefendersAdapter();
        defenderRecycler.setAdapter(defendersAdapter);
        defendersAdapter.submitList(new ArrayList<CharacterModel>());
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder source,
                                  @NonNull RecyclerView.ViewHolder target) {
                CharacterModel onAir = defenders.remove(source.getAdapterPosition());
                if (defenders.size() > target.getAdapterPosition()) {
                    defenders.add(target.getAdapterPosition(), onAir);
                } else {
                    defenders.add(onAir);
                }
                defendersAdapter.submitList(defenders);
                defendersAdapter.notifyItemMoved(source.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder holder, int direction) {
                defenders.remove(holder.getAdapterPosition());
                defendersAdapter.notifyItemRemoved(holder.getAdapterPosition());
                defendersAdapter.submitList(defenders);
            }
        }).attachToRecyclerView(defenderRecycler);
    }

    private void changeAttacker(View characterCell) {
        ViewGroup parent = (ViewGroup) attacker.getParent();
        parent.removeView(attacker);
        parent.addView(characterCell);
        attacker = characterCell;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CHARACTERS && resultCode == RESULT_OK) {

            List<CharacterModel> charactersToAdd
                    = (List<CharacterModel>) data.getSerializableExtra("CHARACTERS");

            battleVM.linkCharactersToCombat(charactersToAdd);
        }
    }

    public void recalculateIni(View view){
        battleVM.recalculateIni();
    }

}
