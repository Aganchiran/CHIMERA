package com.aganchiran.chimera.chimerafront.utils.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.bumptech.glide.Glide;


public class CharacterAdapter extends ItemAdapter<CharacterModel, CharacterAdapter.CharacterHolder> {

    private MenuActions menuActions;

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
        if (currentCharacter.getImage() != null){
            Glide.with(holder.itemView)
                    .load(Uri.parse(currentCharacter.getImage()))
                    .centerCrop()
                    .into(holder.portrait);
        } else {
            holder.portrait.setImageResource(R.drawable.ic_character);
        }
    }

    class CharacterHolder extends ItemAdapter.ItemHolder {
        private TextView textViewName;
        private ImageView portrait;

        CharacterHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.character_name);
            portrait = itemView.findViewById(R.id.character_image);
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
                            menuActions.editCharacter(CharacterAdapter.this.getItemAt(
                                    CharacterHolder.this.getAdapterPosition()));
                            return true;
                        default:
                            return false;
                    }
                }
            };
        }
    }

    public void setMenuActions(MenuActions menuActions) {
        this.menuActions = menuActions;
    }

    public interface MenuActions {
        void editCharacter(CharacterModel characterModel);
    }

}
