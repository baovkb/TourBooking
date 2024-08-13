package com.vkbao.travelbooking.Helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.vkbao.travelbooking.Models.Account;
import com.vkbao.travelbooking.R;

public class UserItemTouch extends ItemTouchHelper.SimpleCallback {
    public interface OnSwipeItem<T> {
        public void onSwipe(T position);
    }

    private Drawable iconDelete;
    private OnSwipeItem<Integer> listener;
    RecyclerView.Adapter adapter;

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    public UserItemTouch(Context context, RecyclerView.Adapter adapter) {
        super(0, ItemTouchHelper.LEFT);
        iconDelete = context.getDrawable(R.drawable.trash);
        this.adapter = adapter;
    }

    public void setOnItemSwipeListener(OnSwipeItem<Integer> listener) {
        this.listener = listener;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (listener != null) {
            listener.onSwipe(viewHolder.getAdapterPosition());
            if (adapter != null) adapter.notifyItemChanged(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX/4, dY, actionState, isCurrentlyActive);

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;

            int iconMarginHorizontal = (itemView.getWidth()/4 - iconDelete.getIntrinsicWidth()) / 2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - iconDelete.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + iconDelete.getIntrinsicHeight();
            int iconLeft = itemView.getRight() - iconMarginHorizontal - iconDelete.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMarginHorizontal;

            if (dX < 0) {
                iconDelete.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            }

            iconDelete.draw(c);
        }
    }
}
