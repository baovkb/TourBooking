package com.vkbao.travelbooking.Views.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vkbao.travelbooking.Adapters.MyTicketAdapter;
import com.vkbao.travelbooking.Helper.ItemTouchDelete;
import com.vkbao.travelbooking.Models.Ticket;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.AccountViewModel;
import com.vkbao.travelbooking.ViewModels.ItemViewModel;
import com.vkbao.travelbooking.ViewModels.TicketViewModel;
import com.vkbao.travelbooking.Views.Dialogs.ConfirmDialog;
import com.vkbao.travelbooking.databinding.DialogConfirmBinding;
import com.vkbao.travelbooking.databinding.FragmentMyTicketBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MyTicketFragment extends Fragment {
    private FragmentMyTicketBinding binding;

    private AccountViewModel accountViewModel;
    private ItemViewModel itemViewModel;
    private TicketViewModel ticketViewModel;

    private ItemTouchHelper itemTouchHelper;
    private final String TAG = "MyTicketFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyTicketBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        ticketViewModel = new ViewModelProvider(requireActivity()).get(TicketViewModel.class);

        initTicketList();
        setUpEvent();
    }

    private void initTicketList() {
        String uid = accountViewModel.getUser().getUid();

        ticketViewModel.getTicketsByUID(uid).observe(getViewLifecycleOwner(), tickets -> {
            List<MyTicketAdapter.MyTicket> myTicketList = new ArrayList<>();

            List<CompletableFuture<Void>> futureList = new ArrayList<>();

            tickets.forEach(ticket -> {
                CompletableFuture<Void> handleItemFuture = itemViewModel.getItemByIDFuture(ticket.getItem_id())
                        .thenAccept((item) -> {
                            if (item != null)
                                myTicketList.add(new MyTicketAdapter.MyTicket(item, ticket));
                        });

                futureList.add(handleItemFuture);
            });

            CompletableFuture.runAsync(() -> {
                CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]))
                        .thenRun(() -> loadTicketList(myTicketList));
            });

        });
    }

    private void loadTicketList(List<MyTicketAdapter.MyTicket> myTicketList) {
        getActivity().runOnUiThread(() -> {
            MyTicketAdapter adapter = new MyTicketAdapter(myTicketList);

            if (itemTouchHelper != null) itemTouchHelper.attachToRecyclerView(null);

            ItemTouchDelete itemTouchDelete = new ItemTouchDelete(requireContext(), adapter);
            itemTouchDelete.setOnItemSwipeListener(position -> {
                Ticket swipedTicket = myTicketList.get(position).getTicket();

                //delete ticket
                deleteTicket(swipedTicket.getTicket_id());

            });
            itemTouchHelper = new ItemTouchHelper(itemTouchDelete);
            itemTouchHelper.attachToRecyclerView(binding.recyclerViewMyTicket);

            binding.recyclerViewMyTicket.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            binding.recyclerViewMyTicket.setAdapter(adapter);
        });
    }

    public void setUpEvent() {
        binding.backBtn.setOnClickListener(view -> {
            getParentFragmentManager().popBackStack();
        });
    }

    public void deleteTicket(String ticketID) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setMessage(getString(R.string.ticket_delete_message));
        dialog.setPositiveBtn(() -> {
            ticketViewModel.deleteTicketByID(ticketID)
                    .thenAccept(success -> {
                        if (success) Toast.makeText(requireContext(), getString(R.string.ticket_delete_success), Toast.LENGTH_SHORT).show();
                        else Toast.makeText(requireContext(), getString(R.string.ticket_delete_error), Toast.LENGTH_SHORT).show();
                    });
        });
        dialog.show(getChildFragmentManager(), null);
    }
}