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

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.chimerafront.dialogs.CreateEditCharacterDialog;
import com.aganchiran.chimera.viewmodels.CharacterDetailsVM;
import com.bumptech.glide.Glide;

public class ChaDetailsFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_CHARACTER_MODEL = "character_model";
    private CharacterDetailsVM characterDetailsVM;


    public ChaDetailsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ChaDetailsFragment newInstance(CharacterModel characterModel) {
        ChaDetailsFragment fragment = new ChaDetailsFragment();
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
                .inflate(R.layout.fragment_character_details, container, false);
        characterDetailsVM = ViewModelProviders.of(this).get(CharacterDetailsVM.class);

        final CharacterModel characterModel = (CharacterModel) getArguments()
                .getSerializable(ARG_CHARACTER_MODEL);

        LiveData<CharacterModel> characterLiveData =
                characterDetailsVM.getCharacterById(characterModel.getId());
        characterLiveData.observe(this, new Observer<CharacterModel>() {
            @Override
            public void onChanged(@Nullable CharacterModel characterModel) {
                assert characterModel != null;
                ((TextView) rootView.findViewById(R.id.name))
                        .setText(characterModel.getName());
                ((TextView) rootView.findViewById(R.id.description_text_view))
                        .setText(characterModel.getDescription());
                final ImageView image = rootView.findViewById(R.id.image);
                if (characterModel.getImage() != null) {
                    Glide.with(ChaDetailsFragment.this)
                            .load(Uri.parse(characterModel.getImage()))
                            .centerCrop()
                            .into(image);
                } else {
                    image.setImageResource(R.drawable.ic_character);
                }
            }
        });


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_character_item, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit_character:
                CreateEditCharacterDialog dialog = new CreateEditCharacterDialog();
                dialog.setListener(new CreateEditCharacterDialog.CreateCharacterDialogListener() {
                    @Override
                    public void saveCharacter(String newName, String newDescription, String image) {
                        final CharacterModel character = (CharacterModel) getArguments()
                                .getSerializable(ARG_CHARACTER_MODEL);
                        character.setName(newName);
                        character.setDescription(newDescription);
                        character.setImage(image);

                        characterDetailsVM.update(character);
                    }

                    @Override
                    public CharacterModel getCharacter() {
                        final CharacterModel character = (CharacterModel) getArguments()
                                .getSerializable(ARG_CHARACTER_MODEL);
                        return character;
                    }
                });
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "edit character");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
