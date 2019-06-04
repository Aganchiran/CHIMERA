package com.aganchiran.chimera.chimerafront.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
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
import android.widget.TextView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.viewmodels.CharacterDetailsVM;

public class ChaDetailsFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_CHARACTER_MODEL = "character_model";


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
        CharacterDetailsVM characterDetailsVM =
                ViewModelProviders.of(this).get(CharacterDetailsVM.class);

        if (getArguments() != null) {
            CharacterModel characterModel = (CharacterModel) getArguments()
                    .getSerializable(ARG_CHARACTER_MODEL);

            if (characterModel != null) {
                LiveData<CharacterModel> characterLiveData =
                        characterDetailsVM.getCharacterById(characterModel.getId());
                characterLiveData.observe(this, new Observer<CharacterModel>() {
                    @Override
                    public void onChanged(@Nullable CharacterModel characterModel) {
                        assert characterModel != null;
                        ((TextView) rootView.findViewById(R.id.character_portrait))
                                .setText(characterModel.getName());
                        ((TextView) rootView.findViewById(R.id.description_text_view))
                                .setText(characterModel.getDescription());
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
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.edit_character:
//                Intent intent = new Intent(getActivity(), CreateEditCharacterActivity.class);
//
//                assert getArguments() != null;
//                intent.putExtra("CHARACTER",getArguments().getSerializable(ARG_CHARACTER_MODEL));
//
//                startActivity(intent);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
