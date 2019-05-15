package com.aganchiran.chimera.chimerafront.utils;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.ConsumableModel;


public class ConsumableAdapter extends ItemAdapter<ConsumableModel, ConsumableAdapter.ConsumableHolder> {


    @NonNull
    @Override
    public ConsumableHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_consumable, parent, false);
        return new ConsumableHolder(itemView);
    }

    @Override
    public void onBindItemHolder(@NonNull ConsumableHolder holder, int position) {
        ConsumableModel currentConsumable = getItemAt(position);
        holder.getTextViewName().setText(currentConsumable.getName());
        holder.setCurrentValue(currentConsumable.getCurrentValue());
    }

    class ConsumableHolder extends ItemAdapter.ItemHolder {
        private TextView textViewName;
        private TextView textViewCurrentValue;
//        private ImageButton editBttn;

        ConsumableHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.consumable_name);
            textViewCurrentValue = itemView.findViewById(R.id.consumable_value);
//            editBttn = itemView.findViewById(R.id.edit_bttn);

//            editBttn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    final int position = getAdapterPosition();
//                    if (getListener() != null && position != RecyclerView.NO_POSITION) {
//                        getListener().onEditClick(getItemModels().get(position));
//                    }
//                }
//            });
        }

        private void setCurrentValue(long numberWithSign) {
            final String formattedNumber;
            final long number = Math.abs(numberWithSign);
            long sign = (number != 0) ? numberWithSign / number : 0;


            if (number >= 1000000) {
                if (number < 10000000) {
                    formattedNumber = (Math.floor((number * sign / 1000000.0) * 10) / 10.0) + "M";
                    textViewCurrentValue.setText(formattedNumber);
                } else {
                    formattedNumber = number * sign / 1000000 + "M";
                    textViewCurrentValue.setText(formattedNumber);
                }
            } else if (number >= 1000) {
                if (number < 10000) {
                    formattedNumber = (Math.floor((number * sign / 1000.0) * 10) / 10.0) + "K";
                    textViewCurrentValue.setText(formattedNumber);
                } else {
                    formattedNumber = number * sign / 1000 + "K";
                    textViewCurrentValue.setText(formattedNumber);
                }
            } else {
                textViewCurrentValue.setText(String.valueOf(number * sign));
            }
        }

        public TextView getTextViewName() {
            return textViewName;
        }
    }

//    private List<ConsumableModel> consumableModels = new ArrayList<>();
//    private List<ConsumableModel> checkedConsumableModels = new ArrayList<>();
//    private OnItemClickListener listener;
//    private ObservableBoolean deleteModeEnabled = new ObservableBoolean(false);
//
//    @NonNull
//    @Override
//
//    public ConsumableHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_consumable, parent, false);
//        return new ConsumableHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ConsumableHolder holder, int position) {
//        ConsumableModel currentConsumable = consumableModels.get(position);
//        holder.textViewName.setText(currentConsumable.getName());
//        holder.setCurrentValue(currentConsumable.getCurrentValue());
//        if (deleteModeEnabled.get()) {
//            if (checkedConsumableModels.contains(getConsumableAt(holder.getAdapterPosition()))) {
//                holder.checkItem();
//            } else {
//                holder.uncheckItem();
//            }
//        } else {
//            holder.disableCheck();
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return consumableModels.size();
//    }
//
//    public ConsumableModel getConsumableAt(int position) {
//        return consumableModels.get(position);
//    }
//
//    public int getPositionOf(ConsumableModel consumableModel) {
//        return consumableModels.indexOf(consumableModel);
//    }
//
//    public List<ConsumableModel> getConsumableModels() {
//        return consumableModels;
//    }
//
//    public List<ConsumableModel> getCheckedConsumableModels() {
//        return checkedConsumableModels;
//    }
//
//    public void setConsumableModels(List<ConsumableModel> consumableModels) {
//        this.consumableModels = consumableModels;
//        notifyDataSetChanged();
//    }
//
//    public boolean isDeleteModeEnabled() {
//        return deleteModeEnabled.get();
//    }
//
//    public void enableDeleteMode() {
//        deleteModeEnabled.set(true);
//    }
//
//    public void disableDeleteMode() {
//        deleteModeEnabled.set(false);
//    }
//
//    class ConsumableHolder extends RecyclerView.ViewHolder {
//        private TextView textViewName;
//        private TextView textViewCurrentValue;
//        private ImageButton editBttn;
//        private boolean checked = false;
//
//        ConsumableHolder(@NonNull View itemView) {
//            super(itemView);
//            textViewName = itemView.findViewById(R.id.consumable_name);
//            textViewCurrentValue = itemView.findViewById(R.id.consumable_value);
//            editBttn = itemView.findViewById(R.id.edit_bttn);
//
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (deleteModeEnabled.get()) {
//                        if (checked) {
//                            uncheckItem();
//                            ConsumableModel consumableModel = consumableModels.get(getAdapterPosition());
//                            checkedConsumableModels.remove(consumableModel);
//                        } else {
//                            checkItem();
//                            ConsumableModel consumableModel = consumableModels.get(getAdapterPosition());
//                            checkedConsumableModels.add(consumableModel);
//                        }
//                    } else {
//                        final int position = getAdapterPosition();
//                        if (listener != null && position != RecyclerView.NO_POSITION) {
//                            listener.onItemClick(consumableModels.get(position));
//                        }
//                    }
//                }
//            });
//
//            editBttn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    final int position = getAdapterPosition();
//                    if (listener != null && position != RecyclerView.NO_POSITION) {
//                        listener.onEditClick(consumableModels.get(position));
//                    }
//                }
//            });
//
//            deleteModeEnabled.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//                @Override
//                public void onPropertyChanged(Observable sender, int propertyId) {
//                    if (deleteModeEnabled.get()) {
//                        uncheckItem();
//                    } else {
//                        disableCheck();
//                    }
//                }
//            });
//        }
//
//        void checkItem() {
//            ImageView check = itemView.findViewById(R.id.check);
//            check.setColorFilter(itemView.getResources().getColor(R.color.contrastColor));
//            check.setVisibility(View.VISIBLE);
//            checked = true;
//        }
//
//        void uncheckItem() {
//            ImageView editBttn = itemView.findViewById(R.id.edit_bttn);
//            editBttn.setVisibility(View.INVISIBLE);
//            ImageView check = itemView.findViewById(R.id.check);
//            check.setColorFilter(itemView.getResources().getColor(R.color.lightTextColor));
//            check.setVisibility(View.VISIBLE);
//            checked = false;
//        }
//
//        void disableCheck() {
//            ImageView editBttn = itemView.findViewById(R.id.edit_bttn);
//            editBttn.setVisibility(View.VISIBLE);
//            ImageView check = itemView.findViewById(R.id.check);
//            check.setVisibility(View.INVISIBLE);
//            checked = false;
//            checkedConsumableModels.clear();
//        }
//
//        private void setCurrentValue(long numberWithSign) {
//            final String formattedNumber;
//            final long number = Math.abs(numberWithSign);
//            long sign = (number != 0)? numberWithSign / number : 0;
//
//
//            if (number >= 1000000) {
//                if (number < 10000000) {
//                    formattedNumber = (Math.floor((number * sign / 1000000.0) * 10) / 10.0) + "M";
//                    textViewCurrentValue.setText(formattedNumber);
//                } else {
//                    formattedNumber = number * sign / 1000000 + "M";
//                    textViewCurrentValue.setText(formattedNumber);
//                }
//            } else if (number >= 1000) {
//                if (number < 10000) {
//                    formattedNumber = (Math.floor((number * sign / 1000.0) * 10) / 10.0) + "K";
//                    textViewCurrentValue.setText(formattedNumber);
//                } else {
//                    formattedNumber = number * sign / 1000 + "K";
//                    textViewCurrentValue.setText(formattedNumber);
//                }
//            } else {
//                textViewCurrentValue.setText(String.valueOf(number * sign));
//            }
//        }
//
//    }
//
//    public void setListener(OnItemClickListener listener) {
//        this.listener = listener;
//    }
//
//    public interface OnItemClickListener {
//        void onItemClick(ConsumableModel consumableModel);
//
//        void onEditClick(ConsumableModel consumableModel);
//    }

}
