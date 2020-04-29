/*
 This file is part of CHIMERA: Companion for Humans Intending to
 Master Extreme Role Adventures ("CHIMERA").

 CHIMERA is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 CHIMERA is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with CHIMERA.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.aganchiran.chimera.chimerafront.fragments;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.viewmodels.CombatProfileVM;

import java.util.Objects;

public class ChaCombatFragment extends Fragment {

    private static final String ARG_CHARACTER_MODEL = "character_model";

    private Activity activity;
    private CombatProfileVM combatProfileVM;
    private EditText lifeValue;
    private EditText iniValue;
    private EditText iniMod;
    private EditText attackValue;
    private EditText attackMod;
    private EditText weaponDamage;
    private EditText defValue;
    private EditText defMod;
    private CheckBox attackRoll;
    private CheckBox defenseRoll;

    public ChaCombatFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ChaCombatFragment newInstance(CharacterModel characterModel) {
        ChaCombatFragment fragment = new ChaCombatFragment();
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
        combatProfileVM = ViewModelProviders.of(this).get(CombatProfileVM.class);
        activity = getActivity();

        if (getArguments() != null) {
            final CharacterModel characterModel = (CharacterModel) getArguments()
                    .getSerializable(ARG_CHARACTER_MODEL);

            if (characterModel != null) {

                lifeValue = rootView.findViewById(R.id.lifeValue);
                iniValue = rootView.findViewById(R.id.ini_value);
                iniMod = rootView.findViewById(R.id.ini_mod);
                attackValue = rootView.findViewById(R.id.attackValue);
                attackMod = rootView.findViewById(R.id.attackMod);
                weaponDamage = rootView.findViewById(R.id.damageValue);
                defValue = rootView.findViewById(R.id.defValue);
                defMod = rootView.findViewById(R.id.defMod);
                attackRoll = rootView.findViewById(R.id.attackRoll);
                defenseRoll = rootView.findViewById(R.id.defenseRoll);

                lifeValue.setText(String.valueOf(characterModel.getLife()));
                iniValue.setText(String.valueOf(characterModel.getInitiative()));
                iniMod.setText(String.valueOf(characterModel.getInitiativeMod()));
                attackValue.setText(String.valueOf(characterModel.getAttack()));
                attackMod.setText(String.valueOf(characterModel.getAttackMod()));
                weaponDamage.setText(String.valueOf(characterModel.getWeaponDamage()));
                defValue.setText(String.valueOf(characterModel.getDefense()));
                defMod.setText(String.valueOf(characterModel.getDefenseMod()));
                attackRoll.setChecked(characterModel.isAttackEnabled());
                defenseRoll.setChecked(characterModel.isDefenseEnabled());

                Button saveButton = rootView.findViewById(R.id.save_button);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        saveCombatProfile(characterModel);
                    }
                });
            }

        }

        Objects.requireNonNull(getActivity()).getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        return rootView;
    }

    private void saveCombatProfile(CharacterModel characterModel) {
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
        characterModel.setAttackEnabled(attackRoll.isChecked());
        characterModel.setDefenseEnabled(defenseRoll.isChecked());

        combatProfileVM.update(characterModel);
        Toast.makeText(getActivity(), "Combat profile saved.", Toast.LENGTH_SHORT).show();
        closeKeyboard();
    }

    private void closeKeyboard() {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
