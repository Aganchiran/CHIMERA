package com.aganchiran.chimera.chimerafront.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.CharacterModel;


public class CharacterAdapter extends ItemAdapter<CharacterModel, CharacterAdapter.CharacterHolder> {

    @NonNull
    @Override
    public CharacterHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_character, parent, false);
        return new CharacterHolder(itemView);
    }

    @Override
    public void onBindItemHolder(@NonNull CharacterHolder holder, int position) {
        CharacterModel currentCharacter = getItemAt(position);
        holder.textViewName.setText(currentCharacter.getName());
    }

    class CharacterHolder extends ItemAdapter.ItemHolder {
        private TextView textViewName;
        private boolean checked = false;

        CharacterHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.character_name);
        }
    }

}
