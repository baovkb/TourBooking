package com.vkbao.travelbooking.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vkbao.travelbooking.Models.Account;
import com.vkbao.travelbooking.databinding.ManageUserItemBinding;

import java.util.List;

public class ManageUserAdapter extends RecyclerView.Adapter<ManageUserAdapter.ManageUserViewHolder> {
    private List<Account> accountList;
    private OnItemClick<Account> listener;

    public ManageUserAdapter(List<Account> accountList) {
        this.accountList = accountList;
    }

    @NonNull
    @Override
    public ManageUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ManageUserItemBinding binding = ManageUserItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ManageUserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageUserViewHolder holder, int position) {
        Account account = accountList.get(position);
        holder.setName(account.getName());
        holder.setEmail(account.getEmail());
        holder.setUID(account.getUid());
        holder.setCreateAt(account.getCreate_at());
        holder.setRole(account.getRole());

        holder.binding.getRoot().setOnClickListener(view -> {
            if (listener != null) listener.onItemClick(accountList.get(position));
        });
    }

    public void setOnItemClick(OnItemClick<Account> listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    class ManageUserViewHolder extends RecyclerView.ViewHolder {
        ManageUserItemBinding binding;

        public ManageUserViewHolder(ManageUserItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void setName(String name) {
            binding.name.setText(name);
        }

        public void setUID(String uid) {
            binding.uid.setText(uid);
        }

        public void setEmail(String email) {
            binding.email.setText(email);
        }

        public void setCreateAt(String createAt) {
            binding.createAt.setText(createAt);
        }

        public void setRole(String role) {
            binding.role.setText(role);
        }
    }

    interface OnItemClick<T> {
        public void onItemClick(T account);
    }
}
