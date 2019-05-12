package com.aganchiran.chimera.chimerafront.utils;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.CharacterModel;

import java.util.ArrayList;
import java.util.List;


public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterHolder> {

    private List<CharacterModel> characterModels = new ArrayList<>();
    private List<CharacterModel> checkedCharacterModels = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private ObservableBoolean deleteModeEnabled = new ObservableBoolean(false);

    @NonNull
    @Override

    public CharacterHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_character, parent, false);
        return new CharacterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterHolder holder, int position) {
        CharacterModel currentCharacter = characterModels.get(position);
        holder.textViewName.setText(currentCharacter.getName());
        if (deleteModeEnabled.get()) {
            if (checkedCharacterModels.contains(getCharacterAt(holder.getAdapterPosition()))){
                holder.checkItem();
            }else {
                holder.uncheckItem();
            }
        } else {
            holder.disableCheck();
        }
    }

    @Override
    public int getItemCount() {
        return characterModels.size();
    }

    public CharacterModel getCharacterAt(int position) {
        return characterModels.get(position);
    }

    public int getPositionOf(CharacterModel characterModel) {
        return characterModels.indexOf(characterModel);
    }

    public List<CharacterModel> getCharacterModels() {
        return characterModels;
    }

    public List<CharacterModel> getCheckedCharacterModels(){
        return checkedCharacterModels;
    }

    public void setCharacterModels(List<CharacterModel> characterModels) {
        this.characterModels = characterModels;
        notifyDataSetChanged();
    }

    public boolean isDeleteModeEnabled(){
        return deleteModeEnabled.get();
    }

    public void enableDeleteMode(){
        deleteModeEnabled.set(true);
    }

    public void disableDeleteMode(){
        deleteModeEnabled.set(false);
    }

    class CharacterHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private boolean checked = false;

        CharacterHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.character_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteModeEnabled.get()) {
                        if (checked) {
                            uncheckItem();
                            CharacterModel characterModel = characterModels.get(getAdapterPosition());
                            checkedCharacterModels.remove(characterModel);
                        } else {
                            checkItem();
                            CharacterModel characterModel = characterModels.get(getAdapterPosition());
                            checkedCharacterModels.add(characterModel);
                        }
                    } else {
                        final int position = getAdapterPosition();
                        if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(characterModels.get(position));
                        }
                    }
                }
            });

            deleteModeEnabled.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    if (deleteModeEnabled.get()) {
                        uncheckItem();
                    } else {
                        disableCheck();
                    }
                }
            });
        }

        void checkItem() {
            ImageView check = itemView.findViewById(R.id.check);
            check.setColorFilter(itemView.getResources().getColor(R.color.contrastColor));
            check.setVisibility(View.VISIBLE);
            checked = true;
        }

        void uncheckItem() {
            ImageView check = itemView.findViewById(R.id.check);
            check.setColorFilter(itemView.getResources().getColor(R.color.lightTextColor));
            check.setVisibility(View.VISIBLE);
            checked = false;
        }

        void disableCheck() {
            ImageView check = itemView.findViewById(R.id.check);
            check.setVisibility(View.INVISIBLE);
            checked = false;
            checkedCharacterModels.clear();
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(CharacterModel characterModel);
    }

}
