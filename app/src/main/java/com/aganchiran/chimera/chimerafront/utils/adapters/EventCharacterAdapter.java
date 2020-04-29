package com.aganchiran.chimera.chimerafront.utils.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.character.CharacterModel;

public class EventCharacterAdapter extends CharacterAdapter {

    @NonNull
    @Override
    public EventCharacterHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int position) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_character, parent, false);
        return new EventCharacterHolder(itemView);
    }

    class EventCharacterHolder extends CharacterHolder {

        EventCharacterHolder(@NonNull final View itemView) {
            super(itemView);
        }

        @Override
        protected int getPopupMenu() {
            return R.menu.menu_event_character_item;
        }

        @Override
        protected PopupMenu.OnMenuItemClickListener getPopupItemClickListener() {
            return new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    final CharacterModel character = EventCharacterAdapter.this.getItemAt(
                            EventCharacterAdapter.EventCharacterHolder.this.getAdapterPosition());

                    switch (menuItem.getItemId()) {
                        case R.id.unlink_character:
                            ((EventMenuActions) menuActions).unlinkCharacter(character);
                            return true;
                        default:
                            return EventCharacterHolder.super.
                                    getPopupItemClickListener().onMenuItemClick(menuItem);
                    }
                }
            };
        }
    }

    public void setMenuActions(EventMenuActions menuActions) {
        this.menuActions = menuActions;
    }

    public interface EventMenuActions extends MenuActions {
        void unlinkCharacter(final CharacterModel characterModel);
    }
}
