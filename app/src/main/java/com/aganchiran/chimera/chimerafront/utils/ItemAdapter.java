package com.aganchiran.chimera.chimerafront.utils;

import android.content.ClipData;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.aganchiran.chimera.R;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemAdapter<M, VH extends ItemAdapter.ItemHolder> extends RecyclerView.Adapter<VH> {

    private List<M> itemModels = new ArrayList<>();
    private List<M> checkedItemModels = new ArrayList<>();
    private int flyingItemPos = -1;
    private OnItemClickListener<M> listener;
    private ObservableBoolean deleteModeEnabled = new ObservableBoolean(false);

    public abstract void onBindItemHolder(@NonNull VH holder, int position);

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (isDeleteModeEnabled()) {
            if (getCheckedItemModels().contains(getItemAt(holder.getAdapterPosition()))) {
                holder.checkItem();
            } else {
                holder.uncheckItem();
            }
        } else {
            holder.disableCheck();
        }

        if (getFlyingItemPos() == holder.getAdapterPosition()) {
            holder.setVisibility(View.INVISIBLE);
        } else {
            holder.setVisibility(View.VISIBLE);
        }
        onBindItemHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    public M getItemAt(int position) {
        return itemModels.get(position);
    }

    public int getPositionOf(M itemModel) {
        return itemModels.indexOf(itemModel);
    }

    public List<M> getItemModels() {
        return itemModels;
    }

    public List<M> getCheckedItemModels() {
        return checkedItemModels;
    }

    public void setItemModels(List<M> itemModels) {
        this.itemModels = itemModels;
        notifyDataSetChanged();
    }

    public void addItem(int position, M item) {
        itemModels.add(position, item);
        notifyDataSetChanged();
    }

    public M removeItem(int position) {
        M itemRemoved = itemModels.remove(position);
        notifyDataSetChanged();
        return itemRemoved;
    }

    public boolean isDeleteModeEnabled() {
        return deleteModeEnabled.get();
    }

    public void enableDeleteMode() {
        deleteModeEnabled.set(true);
    }

    public void disableDeleteMode() {
        deleteModeEnabled.set(false);
    }

    public int getFlyingItemPos() {
        return flyingItemPos;
    }

    public void setFlyingItemPos(int flyingItemPos) {
        this.flyingItemPos = flyingItemPos;
    }

    public abstract class ItemHolder extends RecyclerView.ViewHolder {

        private boolean checked = false;
        private GestureDetector gestureDetector;
        private boolean longClicked = false;
        private PopupMenu popup;

        ItemHolder(@NonNull View itemView) {
            super(itemView);
            gestureDetector = new GestureDetector(itemView.getContext(), new ItemGestureListener(this));

            popup = new PopupMenu(itemView.getContext(), itemView);
            popup.setOnMenuItemClickListener(getPopupItemClickListener());
            popup.inflate(getPopupMenu());

            itemView.setOnTouchListener(new View.OnTouchListener() {

                private static final int DRAG_THRESHOLD = 5;
                private float downX;
                private float downY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            downX = event.getX();
                            downY = event.getY();
                            setLongClicked(false);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            boolean dragOnX = Math.abs(downX - event.getX()) > DRAG_THRESHOLD;
                            boolean dragOnY = Math.abs(downY - event.getY()) > DRAG_THRESHOLD;

                            if (longClicked && (dragOnX || dragOnY)) {
                                popup.dismiss();

                                final ClipData clipData = ClipData.newPlainText("", "");

                                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    v.startDragAndDrop(clipData, shadowBuilder, v, 0);
                                } else {
                                    v.startDrag(clipData, shadowBuilder, v, 0);
                                }

                                v.setVisibility(View.INVISIBLE);
                            } else {
                                return false;
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            setLongClicked(false);
                            break;
                    }

                    return gestureDetector.onTouchEvent(event);
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

        private void showPopup() {
            popup.show();
        }

        protected abstract int getPopupMenu();

        protected abstract PopupMenu.OnMenuItemClickListener getPopupItemClickListener();

        void checkItem() {
            ImageView check = itemView.findViewById(R.id.check);
            if (check == null) {
                throw new RuntimeException("ERROR: Item view does not have a check icon");
            }
            check.setColorFilter(itemView.getResources().getColor(R.color.contrastColor));
            check.setVisibility(View.VISIBLE);
            checked = true;
        }

        void uncheckItem() {
            ImageView check = itemView.findViewById(R.id.check);
            if (check == null) {
                throw new RuntimeException("ERROR: Item view does not have a check icon");
            }
            check.setColorFilter(itemView.getResources().getColor(R.color.lightTextColor));
            check.setVisibility(View.VISIBLE);
            checked = false;
        }

        void disableCheck() {
            ImageView check = itemView.findViewById(R.id.check);
            if (check == null) {
                throw new RuntimeException("ERROR: Item view does not have a check icon");
            }
            check.setVisibility(View.INVISIBLE);
            checked = false;
            checkedItemModels.clear();
        }

        void setVisibility(int visibility) {
            itemView.setVisibility(visibility);
        }

        public boolean isLongClicked() {
            return longClicked;
        }

        public void setLongClicked(boolean longClicked) {
            this.longClicked = longClicked;
        }
    }

    protected OnItemClickListener<M> getListener() {
        return listener;
    }

    public void setListener(OnItemClickListener<M> listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener<M> {
        void onItemClick(M itemModel);
    }

    class ItemGestureListener extends GestureDetector.SimpleOnGestureListener {

        private ItemHolder itemHolder;

        ItemGestureListener(ItemHolder itemHolder) {
            this.itemHolder = itemHolder;
        }

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (deleteModeEnabled.get()) {
                if (itemHolder.checked) {
                    itemHolder.uncheckItem();
                    M itemModel = itemModels.get(itemHolder.getAdapterPosition());
                    checkedItemModels.remove(itemModel);
                } else {
                    itemHolder.checkItem();
                    M itemModel = itemModels.get(itemHolder.getAdapterPosition());
                    checkedItemModels.add(itemModel);
                }
            } else {
                final int position = itemHolder.getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(itemModels.get(position));
                }
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (!isDeleteModeEnabled()) {
                itemHolder.showPopup();
                itemHolder.setLongClicked(true);
            }
        }
    }
}
