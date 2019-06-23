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

package com.aganchiran.chimera.chimerafront.utils.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.bumptech.glide.Glide;

import org.apache.commons.collections4.list.SetUniqueList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InitiativeAdapter extends ListAdapter<CharacterModel, RecyclerView.ViewHolder> {

    private CharacterModel addButton = null;
    private CharacterModel attacker;
    private List<CharacterModel> defenders = SetUniqueList.setUniqueList(new ArrayList<CharacterModel>());
    private OnCharacterClickListener listener;

    private static final int CHARACTER_VIEW = 0;
    private static final int ADD_BUTTON_VIEW = 1;

    public InitiativeAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<CharacterModel> DIFF_CALLBACK
            = new DiffUtil.ItemCallback<CharacterModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull CharacterModel c1, @NonNull CharacterModel c2) {
            return c1.equals(c2);
        }

        @Override
        public boolean areContentsTheSame(@NonNull CharacterModel c1, @NonNull CharacterModel c2) {
            return c1.contentsTheSame(c2);
        }
    };

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == CHARACTER_VIEW) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_initiative_cell, parent, false);
            return new InitiativeAdapter.InitiativeHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_add_cell, parent, false);
            return new InitiativeAdapter.AddButtonHolder(itemView);
        }
    }

    @Override
    public void submitList(@Nullable List<CharacterModel> list) {
        if (list == null) {
            list = new ArrayList<>();
        }

        list.removeAll(Collections.singleton(null));
        list.add(addButton);
        super.submitList(list);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == CHARACTER_VIEW) {
            CharacterModel currentCharacter = getCharacterAt(position);
            ((InitiativeHolder) holder).textViewName.setText(currentCharacter.getName());
            ((InitiativeHolder) holder).iniRoll.setText(String.valueOf(currentCharacter.getIniRoll()));
            if (currentCharacter.getImage() != null) {
                Glide.with(holder.itemView)
                        .load(Uri.parse(currentCharacter.getImage()))
                        .centerCrop()
                        .into(((InitiativeHolder) holder).portrait);
            } else {
                ((InitiativeHolder) holder).portrait.setImageResource(R.drawable.ic_character);
            }

            ((InitiativeHolder) holder).attackerIcon.setVisibility(View.INVISIBLE);
            if (attacker != null && currentCharacter.getId() == attacker.getId()) {
                ((InitiativeHolder) holder).selectAsAttacker();
            }
            ((InitiativeHolder) holder).disselectAsDefender();
            for (CharacterModel defender : defenders) {
                if (defender.getId() == currentCharacter.getId()) {
                    ((InitiativeHolder) holder).selectAsDefender();
                }
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (getItem(position) == null) {
            return ADD_BUTTON_VIEW;
        } else {
            return CHARACTER_VIEW;
        }
    }

    public int getItemPositionById(int id) {

        for (int i = 0; i < getItemCount(); i++) {
            if (getCharacterAt(i).getId() == id) {
                return i;
            }
        }
        return -1;

    }

    public CharacterModel getCharacterAt(int position) {
        return getItem(position);
    }

    public CharacterModel getAttacker() {
        return attacker;
    }

    public void setAttacker(CharacterModel attacker) {
        this.attacker = attacker;
    }

    public List<CharacterModel> getDefenders() {
        return defenders;
    }

    public void setDefenders(List<CharacterModel> defenders) {
        this.defenders = defenders;
    }

    public class InitiativeHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView iniRoll;
        private ImageView portrait;
        private View attackerIcon;
        private View defenderIcon;
        private GestureDetector gestureDetector;

        InitiativeHolder(@NonNull final View characterView) {
            super(characterView);
            textViewName = itemView.findViewById(R.id.name_label);
            iniRoll = itemView.findViewById(R.id.roll);
            portrait = itemView.findViewById(R.id.character_image);
            attackerIcon = itemView.findViewById(R.id.attacker_icon);
            defenderIcon = itemView.findViewById(R.id.defender_icon);
            gestureDetector = new GestureDetector(itemView.getContext(),
                    new ItemGestureListener(this));


            characterView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });

        }

        public View getCopy() {
            LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
            View newView = inflater.inflate(R.layout.item_initiative_cell, null);
            final CharacterModel selectedCharacter = InitiativeAdapter.this.getCharacterAt(getAdapterPosition());

            TextView newName = newView.findViewById(R.id.name_label);
            newName.setText(selectedCharacter.getName());

            ImageView portrait = newView.findViewById(R.id.character_image);
            if (selectedCharacter.getImage() != null) {
                Glide.with(itemView)
                        .load(Uri.parse(selectedCharacter.getImage()))
                        .centerCrop()
                        .into(portrait);
            } else {
                portrait.setImageResource(R.drawable.ic_character);
            }

            return newView;
        }

        private void selectCell() {
            if (listener != null) {
                listener.onCharacterClick(this);
            }
        }

        public void selectAsAttacker() {
            attackerIcon.setVisibility(View.VISIBLE);
            attacker = getCharacterAt(getAdapterPosition());
        }

        public void disselectAsAttacker() {
            attackerIcon.setVisibility(View.INVISIBLE);
            attacker = null;
        }

        public void selectAsDefender() {
            defenderIcon.setVisibility(View.VISIBLE);
        }

        public void disselectAsDefender() {
            defenderIcon.setVisibility(View.INVISIBLE);
        }
    }

    public class AddButtonHolder extends RecyclerView.ViewHolder {

        AddButtonHolder(@NonNull View characterView) {
            super(characterView);

            characterView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_UP:
                            if (listener != null) {
                                listener.addCharacter();
                            }
                            return true;
                        default:
                            return false;
                    }

                }
            });

        }

    }

    public void setListener(OnCharacterClickListener listener) {
        this.listener = listener;
    }

    public interface OnCharacterClickListener {
        void onCharacterClick(InitiativeHolder holder);

        void addCharacter();
    }

    class ItemGestureListener extends GestureDetector.SimpleOnGestureListener {

        private InitiativeHolder itemHolder;

        ItemGestureListener(InitiativeHolder itemHolder) {
            this.itemHolder = itemHolder;
        }

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            itemHolder.selectCell();
            notifyDataSetChanged();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }
    }
}
