package com.aganchiran.chimera.chimerafront.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.character.CharacterModel;


public class CharacterAdapter extends ItemAdapter<CharacterModel, CharacterAdapter.CharacterHolder> {

    private EditCharacter editCharacter;

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

        CharacterHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.character_name);
        }

        @Override
        protected int getPopupMenu() {
            return R.menu.menu_character_item;
        }

        @Override
        protected PopupMenu.OnMenuItemClickListener getPopupItemClickListener() {
            return new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.edit_character:
                            editCharacter.perform(CharacterAdapter.this.getItemAt(
                                    CharacterHolder.this.getAdapterPosition()));
                            return true;
                        default:
                            return false;
                    }
                }
            };
        }
    }

    public void setEditCharacter(EditCharacter editCharacter) {
        this.editCharacter = editCharacter;
    }

    public interface EditCharacter {
        void perform(CharacterModel characterModel);
    }

}
