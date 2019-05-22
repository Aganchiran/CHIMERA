package com.aganchiran.chimera.chimerafront.utils;

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
import android.widget.TextView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.character.CharacterModel;

import java.util.ArrayList;
import java.util.List;

public class DefendersAdapter extends ListAdapter<CharacterModel, RecyclerView.ViewHolder> {

    private CharacterModel empty = null;
    private DefenderHolder attacker;
    private OnCharacterClickListener listener;

    private static final int CHARACTER_VIEW = 0;
    private static final int EMPTY_VIEW = 1;

    public DefendersAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<CharacterModel> DIFF_CALLBACK
            = new DiffUtil.ItemCallback<CharacterModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull CharacterModel c1, @NonNull CharacterModel c2) {
            return c1.getId() == c2.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull CharacterModel c1, @NonNull CharacterModel c2) {
            return c1.getName().equals(c2.getName())
                    && c1.getDescription().equals(c2.getDescription());
        }
    };

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == CHARACTER_VIEW) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_character_icon, parent, false);
            return new DefenderHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_empty_defenders, parent, false);
            return new EmptyHolder(itemView);
        }
    }

    @Override
    public void submitList(@Nullable List<CharacterModel> list) {
        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
            list.add(empty);
        }
        super.submitList(list);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == CHARACTER_VIEW) {
            CharacterModel currentCharacter = getCharacterAt(position);
            ((DefenderHolder) holder).textViewName.setText(currentCharacter.getName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) == null) {
            return EMPTY_VIEW;
        } else {
            return CHARACTER_VIEW;
        }
    }

    public CharacterModel getCharacterAt(int position) {
        return getItem(position);
    }

    public class DefenderHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private GestureDetector gestureDetector;
        private View attackerIcon;

        DefenderHolder(@NonNull final View characterView) {
            super(characterView);
            textViewName = itemView.findViewById(R.id.name_label);
            attackerIcon = itemView.findViewById(R.id.attacker_icon);
            gestureDetector = new GestureDetector(itemView.getContext(),
                    new ItemGestureListener(this));


            characterView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });

        }

        private View getCopy(){
            LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
            View newView = inflater.inflate(R.layout.item_character_icon, null);

            TextView newName = newView.findViewById(R.id.name_label);
            newName.setText(textViewName.getText());

            return newView;
        }

        private void selectCell(){
            if (listener != null) {
                listener.onCharacterClick(getCopy());
            }
            selectAsAttacker();
        }

        public void selectAsAttacker(){
            if (attacker != null){
                attacker.disselectAsAttacker();
            }
            attackerIcon.setVisibility(View.VISIBLE);
            attacker = this;
        }

        public void disselectAsAttacker(){
            attackerIcon.setVisibility(View.INVISIBLE);
            attacker = null;
        }
    }

    public class EmptyHolder extends RecyclerView.ViewHolder {

        EmptyHolder(@NonNull View characterView) {
            super(characterView);
        }

    }

    public void setListener(OnCharacterClickListener listener) {
        this.listener = listener;
    }

    public interface OnCharacterClickListener{
        void onCharacterClick(View characterCell);
    }

    class ItemGestureListener extends GestureDetector.SimpleOnGestureListener {

        private DefenderHolder itemHolder;

        ItemGestureListener(DefenderHolder itemHolder) {
            this.itemHolder = itemHolder;
        }

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            itemHolder.selectCell();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }
    }
}
