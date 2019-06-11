package com.aganchiran.chimera.chimerafront.activities;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.chimeracore.combat.CombatModel;
import com.aganchiran.chimera.chimerafront.utils.adapters.DefendersAdapter;
import com.aganchiran.chimera.chimerafront.utils.adapters.InitiativeAdapter;
import com.aganchiran.chimera.chimerafront.utils.Quicksort;
import com.aganchiran.chimera.viewmodels.BattleVM;

import org.apache.commons.collections4.list.SetUniqueList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class BattleActivity extends ActivityWithUpperBar {

    private static final int ADD_CHARACTERS = 1;
    private static final int SEE_CHARACTER = 2;
    private static final int NONE = 0;
    private static final int ATTACK = 1;
    private static final int DAMAGE = 2;
    private View NO_ATTACKER;

    private BattleVM battleVM;
    private CombatModel combat;
    private InitiativeAdapter initiativeAdapter;
    private RecyclerView initiativeRecycler;
    private View attacker;
    private List<CharacterModel> defenders = SetUniqueList.setUniqueList(new ArrayList<CharacterModel>());
    private MutableLiveData<List<CharacterModel>> initiatives = new MutableLiveData<>();
    private DefendersAdapter defendersAdapter;
    private RecyclerView defenderRecycler;
    private CharacterModel modifiedCharacter;
    private int combatPhase = NONE;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.activity_battle);
        battleVM = ViewModelProviders.of(this).get(BattleVM.class);
        NO_ATTACKER = findViewById(R.id.attacker);
        attacker = NO_ATTACKER;
        findViewById(R.id.attacker_frame).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && initiativeAdapter.getAttacker() != null) {
                    goToCharacterProfile(initiativeAdapter.getAttacker());
                    return true;
                }
                return false;
            }
        });


        Intent intent = this.getIntent();
        combat = (CombatModel) intent.getSerializableExtra("COMBAT");
        setupInitiativeList();
        setupDefendersList();

        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(combat.getName());
    }

    private void setupInitiativeList() {
        initiativeRecycler = findViewById(R.id.initiativeRecycler);
        initiativeRecycler.setLayoutManager(new LinearLayoutManager(BattleActivity.this,
                LinearLayoutManager.VERTICAL, false));
        initiativeRecycler.setHasFixedSize(true);

        initiativeAdapter = new InitiativeAdapter();
        initiativeRecycler.setAdapter(initiativeAdapter);
        initiativeAdapter.setListener(new InitiativeAdapter.OnCharacterClickListener() {

            @Override
            public void onCharacterClick(InitiativeAdapter.InitiativeHolder holder) {
                if (initiativeAdapter.getAttacker() == null ||
                        initiativeAdapter.getCharacterAt(holder.getAdapterPosition()).getId()
                                != initiativeAdapter.getAttacker().getId()) {
                    changeAttacker(holder.getCopy());
                    holder.selectAsAttacker();
                } else {
                    changeAttacker(NO_ATTACKER);
                    holder.disselectAsAttacker();
                }
            }

            @Override
            public void addCharacter() {
                Intent intent = new Intent(BattleActivity.this, CharacterSelectionActivity.class);
                intent.putExtra("SELECTION_SCREEN", true);
                intent.putExtra("CAMPAIGN", combat.getCampaignId());
                startActivityForResult(intent, ADD_CHARACTERS);
            }
        });
        initiativeAdapter.submitList(new ArrayList<CharacterModel>());
        battleVM.getCharactersForCombat(combat.getId()).observe(this, new Observer<List<CharacterModel>>() {
            @Override
            public void onChanged(@Nullable List<CharacterModel> characterModels) {
                if (initiatives.getValue() == null) {
                    initiatives.setValue(characterModels);
                }
            }
        });

        initiatives.observe(this, new Observer<List<CharacterModel>>() {
            @Override
            public void onChanged(@Nullable List<CharacterModel> characterModels) {
                initiativeAdapter.submitList(characterModels);
            }
        });

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

                    final CharacterModel chaToRemove
                            = initiativeAdapter.getCharacterAt(holder.getAdapterPosition());
                    final int posRemoved = defendersAdapter.getItemPositionById(chaToRemove.getId());
                    if (posRemoved >= 0) {
                        chaToRemove.endCombat();
                        defenders.remove(chaToRemove);
                        defendersAdapter.submitList(defenders);
                        defendersAdapter.notifyItemRemoved(posRemoved);
                        initiativeAdapter.setDefenders(defenders);
                    }

                    battleVM.unlinkCharacterFromCombat(combat.getId(), chaToRemove.getId());


                    List<CharacterModel> charactersIni = initiatives.getValue();
                    assert charactersIni != null;
                    charactersIni.remove(holder.getAdapterPosition());
                    initiativeAdapter.notifyItemRemoved(holder.getAdapterPosition());
                } else {
                    final CharacterModel characterToAdd =
                            initiativeAdapter.getCharacterAt(holder.getAdapterPosition());
                    if (initiativeAdapter.getAttacker() == null) {
                        changeAttacker(NO_ATTACKER);
                    } else {

                        LayoutInflater inflater = getLayoutInflater();
                        View newView = inflater.inflate(R.layout.item_initiative_cell, null);

                        TextView newName = newView.findViewById(R.id.name_label);
                        newName.setText(initiativeAdapter.getAttacker().getName());
                        changeAttacker(newView);
                    }
                    defenders.add(characterToAdd);
                    defendersAdapter.submitList(defenders);
                    initiativeAdapter.setDefenders(defenders);
                    initiativeAdapter.notifyItemChanged(holder.getAdapterPosition());
                    endAttack();
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
        defendersAdapter.setListener(new DefendersAdapter.OnCharacterClickListener() {
            @Override
            public void onCharacterClick(CharacterModel characterModel) {
                if (characterModel != null) {
                    goToCharacterProfile(characterModel);
                }
            }
        });
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
                initiativeAdapter.setDefenders(defenders);
                defendersAdapter.notifyItemMoved(source.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof DefendersAdapter.EmptyHolder) {
                    return 0;
                } else {
                    return super.getSwipeDirs(recyclerView, viewHolder);
                }
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder holder, int direction) {

                final CharacterModel chaToRemove =
                        defendersAdapter.getCharacterAt(holder.getAdapterPosition());
                chaToRemove.endCombat();
                defenders.remove(holder.getAdapterPosition());
                defendersAdapter.notifyItemRemoved(holder.getAdapterPosition());
                defendersAdapter.submitList(defenders);
                initiativeAdapter.setDefenders(defenders);
                initiativeAdapter.notifyDataSetChanged();

            }
        }).attachToRecyclerView(defenderRecycler);
    }

    private void changeAttacker(View characterCell) {
        endAttack();
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
            ArrayList<Integer> charactersIds = new ArrayList<>();
            for (CharacterModel c : charactersToAdd) {
                charactersIds.add(c.getId());
            }

            List<CharacterModel> newList = initiatives.getValue();
            assert newList != null;
            newList.addAll(charactersToAdd);
            newList.removeAll(Collections.singleton(null));
            initiatives.setValue(new ArrayList<>(new HashSet<>(newList)));
            initiativeAdapter.notifyDataSetChanged();
            battleVM.linkCharactersToCombat(combat.getId(), charactersIds);
        } else if (requestCode == SEE_CHARACTER) {
            CharacterModel cha = (CharacterModel) data.getSerializableExtra("CHARACTER");

            modifiedCharacter.setLife(cha.getLife());
            modifiedCharacter.setInitiative(cha.getInitiative());
            modifiedCharacter.setInitiativeMod(cha.getInitiativeMod());
            modifiedCharacter.setAttack(cha.getAttack());
            modifiedCharacter.setAttackMod(cha.getAttackMod());
            modifiedCharacter.setWeaponDamage(cha.getWeaponDamage());
            modifiedCharacter.setDefense(cha.getDefense());
            modifiedCharacter.setDefenseMod(cha.getDefenseMod());
        }
    }

    public void recalculateIni(View view) {

        final Quicksort<CharacterModel> quicksort = new Quicksort<CharacterModel>() {
            @Override
            protected int compare(CharacterModel a, CharacterModel b) {
                return b.getIniRoll() - a.getIniRoll();
            }
        };
        List<CharacterModel> sortedList = initiatives.getValue();
        if (sortedList != null) {
            sortedList.removeAll(Collections.singleton(null));
            for (CharacterModel character : sortedList) {
                character.rollIni();
            }
            quicksort.sort(sortedList);
            initiatives.setValue(sortedList);
            initiativeAdapter.notifyDataSetChanged();
        }
    }

    public void recalculateCombat(View view) {
        final CharacterModel attackerModel = initiativeAdapter.getAttacker();
        switch (combatPhase) {
            case NONE:
                combatPhase = ATTACK;
                defendersAdapter.setCombatPhase(combatPhase);
                if (attackerModel != null) {
                    attackerModel.rollAttack();
                    final TextView roll = attacker.findViewById(R.id.roll);
                    roll.setText(String.valueOf(attackerModel.getAttackRoll()));
                }
                for (CharacterModel defender : defenders) {
                    defender.rollDefense();
                }
                break;
            case ATTACK:
                combatPhase = DAMAGE;
                defendersAdapter.setCombatPhase(combatPhase);
                for (CharacterModel defender : defenders) {
                    if (attackerModel != null) {
                        defender.setLastHit(attackerModel.calculateDamage(defender.getDefenseRoll()));
                    }
                }
                break;
            case DAMAGE:
                for (CharacterModel defender : defenders) {
                    if (attackerModel != null) {
                        defender.hit(defender.getLastHit());
                    }
                }
                endAttack();
                break;
        }

        battleVM.updateCharacters(defenders);
        defendersAdapter.notifyDataSetChanged();
    }

    public void endAttack() {
        combatPhase = NONE;
        defendersAdapter.setCombatPhase(NONE);
        final List<CharacterModel> allCha = Objects.requireNonNull(initiatives.getValue());
        allCha.removeAll(Collections.singleton(null));
        for (final CharacterModel cha : allCha) {
            cha.endCombat();
        }
        if (attacker != NO_ATTACKER) {
            ((TextView) attacker.findViewById(R.id.roll)).setText("");
        }
        battleVM.updateCharacters(allCha);
        initiatives.setValue(allCha);
        defendersAdapter.notifyDataSetChanged();
    }

    private void goToCharacterProfile(CharacterModel characterModel) {
        modifiedCharacter = characterModel;
        Intent intent = new Intent(this, CharacterProfileActivity.class);
        intent.putExtra("CHARACTER", characterModel);
        intent.putExtra("FROMCOMBAT", true);
        startActivityForResult(intent, 2);
    }

}
