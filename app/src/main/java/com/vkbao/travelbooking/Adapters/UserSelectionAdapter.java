package com.vkbao.travelbooking.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.imageview.ShapeableImageView;
import com.vkbao.travelbooking.Models.User;
import com.vkbao.travelbooking.R;
import java.util.ArrayList;
import java.util.List;

public class UserSelectionAdapter extends RecyclerView.Adapter<UserSelectionAdapter.UserViewHolder> {
    private List<User> users;
    private final List<String> selectedUserIds;
    private final OnUserSelectionListener listener;

    public interface OnUserSelectionListener {
        void onUserSelectionChanged(String userId, boolean isSelected);
    }

    public UserSelectionAdapter(List<User> users, OnUserSelectionListener listener) {
        this.users = users;
        this.listener = listener;
        this.selectedUserIds = new ArrayList<>();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_selection_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void updateUsers(List<User> newUsers) {
        this.users = newUsers;
        notifyDataSetChanged();
    }

    public List<String> getSelectedUserIds() {
        return new ArrayList<>(selectedUserIds);
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private final ShapeableImageView userAvatarImage;
        private final TextView userNameText;
        private final TextView userEmailText;
        private final MaterialCheckBox selectCheckBox;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatarImage = itemView.findViewById(R.id.userAvatarImage);
            userNameText = itemView.findViewById(R.id.userNameText);
            userEmailText = itemView.findViewById(R.id.userEmailText);
            selectCheckBox = itemView.findViewById(R.id.selectCheckBox);
        }

        void bind(User user) {
            userNameText.setText(user.getName());
            userEmailText.setText(user.getEmail());
            
            // Load user avatar using your image loading library (e.g., Glide, Picasso)
            // Glide.with(userAvatarImage).load(user.getAvatarUrl()).into(userAvatarImage);
            
            // Set checkbox state
            selectCheckBox.setChecked(selectedUserIds.contains(user.getUserId()));
            
            // Set checkbox listener
            selectCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedUserIds.add(user.getUserId());
                } else {
                    selectedUserIds.remove(user.getUserId());
                }
                listener.onUserSelectionChanged(user.getUserId(), isChecked);
            });
        }
    }
} 