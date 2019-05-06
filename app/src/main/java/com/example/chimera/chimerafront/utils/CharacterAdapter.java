package com.example.chimera.chimerafront.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chimera.R;
import com.example.chimera.chimeracore.CharacterModel;

import java.util.ArrayList;
import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterHolder> {

    private List<CharacterModel> characterModels = new ArrayList<>();
    private OnItemClickListener listener;

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
    }

    @Override
    public int getItemCount() {
        return characterModels.size();
    }

    public CharacterModel getCharacterAt(int position){
        return characterModels.get(position);
    }

    public List<CharacterModel> getCharacterModels(){
        return characterModels;
    }

    public void setCharacterModels(List<CharacterModel> characterModels){
        this.characterModels = characterModels;
        notifyDataSetChanged();
    }

    class CharacterHolder extends RecyclerView.ViewHolder{
        private TextView textViewName;

        public CharacterHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.character_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(characterModels.get(position));
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(CharacterModel characterModel);
    }

}
