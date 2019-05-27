package com.aganchiran.chimera.chimerafront.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.chimerafront.activities.CharacterProfileActivity;
import com.aganchiran.chimera.viewmodels.CombatProfileVM;

public class CombatProfileFragment extends Fragment {

    private static final String ARG_CHARACTER_MODEL = "character_model";


    public CombatProfileFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CombatProfileFragment newInstance(CharacterModel characterModel) {
        CombatProfileFragment fragment = new CombatProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CHARACTER_MODEL, characterModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater
                .inflate(R.layout.fragment_combat_profile, container, false);
        final CombatProfileVM combatProfileVM =
                ViewModelProviders.of(this).get(CombatProfileVM.class);

        if (getArguments() != null) {
            final CharacterModel characterModel = (CharacterModel) getArguments()
                    .getSerializable(ARG_CHARACTER_MODEL);

            if (characterModel != null) {

                final EditText lifeValue = rootView.findViewById(R.id.lifeValue);
                final EditText iniValue = rootView.findViewById(R.id.ini_value);
                final EditText iniMod = rootView.findViewById(R.id.ini_mod);
                final EditText attackValue = rootView.findViewById(R.id.attackValue);
                final EditText attackMod = rootView.findViewById(R.id.attackMod);
                final EditText weaponDamage = rootView.findViewById(R.id.damageValue);
                final EditText defValue = rootView.findViewById(R.id.defValue);
                final EditText defMod = rootView.findViewById(R.id.defMod);

                lifeValue.setText(String.valueOf(characterModel.getLife()));
                iniValue.setText(String.valueOf(characterModel.getInitiative()));
                iniMod.setText(String.valueOf(characterModel.getInitiativeMod()));
                attackValue.setText(String.valueOf(characterModel.getAttack()));
                attackMod.setText(String.valueOf(characterModel.getAttackMod()));
                weaponDamage.setText(String.valueOf(characterModel.getWeaponDamage()));
                defValue.setText(String.valueOf(characterModel.getDefense()));
                defMod.setText(String.valueOf(characterModel.getDefenseMod()));

                Button saveButton = rootView.findViewById(R.id.save_button);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int[] numValues = new int[8];
                        String[] strValues = {
                                lifeValue.getText().toString(),
                                iniValue.getText().toString(),
                                iniMod.getText().toString(),
                                attackValue.getText().toString(),
                                attackMod.getText().toString(),
                                weaponDamage.getText().toString(),
                                defValue.getText().toString(),
                                defMod.getText().toString()};

                        for (int i = 0; i < numValues.length; i++) {
                            if (strValues[i].trim().isEmpty()) {
                                numValues[i] = 0;
                            } else {
                                numValues[i] = Integer.parseInt(strValues[i]);
                            }
                        }

                        characterModel.setLife(numValues[0]);
                        characterModel.setInitiative(numValues[1]);
                        characterModel.setInitiativeMod(numValues[2]);
                        characterModel.setAttack(numValues[3]);
                        characterModel.setAttackMod(numValues[4]);
                        characterModel.setWeaponDamage(numValues[5]);
                        characterModel.setDefense(numValues[6]);
                        characterModel.setDefenseMod(numValues[7]);

                        ((CharacterProfileActivity) getActivity()).setCharacter(characterModel);
                        combatProfileVM.update(characterModel);
                        Toast.makeText(getActivity(), "Combat profile saved.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_general, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
