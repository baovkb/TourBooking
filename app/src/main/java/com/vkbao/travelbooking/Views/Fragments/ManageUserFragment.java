package com.vkbao.travelbooking.Views.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vkbao.travelbooking.Adapters.ManageUserAdapter;
import com.vkbao.travelbooking.Helper.UserItemTouch;
import com.vkbao.travelbooking.Clients.AccountClient;
import com.vkbao.travelbooking.Services.AccountService;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.AccountViewModel;
import com.vkbao.travelbooking.Views.Dialogs.ConfirmDialog;
import com.vkbao.travelbooking.databinding.FragmentManageUserBinding;

import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageUserFragment extends Fragment {
    private FragmentManageUserBinding binding;
    private AccountViewModel accountViewModel;
    private AccountService accountService;

    private ItemTouchHelper itemTouchHelper;

    private static String TAG = "ManageUserFragment";

    public ManageUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentManageUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        accountService = AccountClient.getClient().create(AccountService.class);

        initUsers();
    }

    private void initUsers() {
        accountViewModel.getAllAccount().observe(getViewLifecycleOwner(), accounts -> {
            ManageUserAdapter adapter = new ManageUserAdapter(accounts);

            if (itemTouchHelper != null) itemTouchHelper.attachToRecyclerView(null);
            UserItemTouch userItemTouch = new UserItemTouch(requireContext(), adapter);
            userItemTouch.setOnItemSwipeListener(position -> {
                handleDeleteAction(accounts.get(position).getUid(), adapter, position);
            });
            itemTouchHelper = new ItemTouchHelper(userItemTouch);

            itemTouchHelper.attachToRecyclerView(binding.recyclerViewUsers);
            binding.recyclerViewUsers.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
            binding.recyclerViewUsers.setAdapter(adapter);
        });
    }

    private void handleDeleteAction(String uid, ManageUserAdapter adapter, int position) {
        ConfirmDialog dialog = new ConfirmDialog();

        dialog.setMessage(getString(R.string.delete_user_message));

        dialog.setPositiveBtn(() -> {
            deleteUser(uid).thenAccept(success -> {
                if (success) Toast.makeText(getActivity(), getContext().getString(R.string.delete_user_success), Toast.LENGTH_SHORT).show();
                else Toast.makeText(getActivity(), getContext().getString(R.string.delete_user_error), Toast.LENGTH_SHORT).show();
            });
        });

        dialog.show(getChildFragmentManager(), null);
    }

    private CompletableFuture<Boolean> deleteUser(String accountID) {
        CompletableFuture<Boolean> resultCompletable = new CompletableFuture<>();

        accountService.deleteAccount(accountID).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //200 - 299
                if (response.isSuccessful()) {
                    accountViewModel.deleteUser(accountID, success -> {
                        resultCompletable.complete(success);
                    });
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                resultCompletable.complete(false);
            }
        });

        return resultCompletable;
    }
}