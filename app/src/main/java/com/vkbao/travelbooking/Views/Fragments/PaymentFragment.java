package com.vkbao.travelbooking.Views.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vkbao.travelbooking.Adapters.ItemPaymentAdapter;
import com.vkbao.travelbooking.Helper.Helper;
import com.vkbao.travelbooking.Models.Invoice;
import com.vkbao.travelbooking.Models.Order;
import com.vkbao.travelbooking.Models.Ticket;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.InvoiceViewModel;
import com.vkbao.travelbooking.ViewModels.ItemViewModel;
import com.vkbao.travelbooking.ViewModels.TicketViewModel;
import com.vkbao.travelbooking.databinding.FragmentPaymentBinding;
import com.vkbao.travelbooking.databinding.PaymentItemBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class PaymentFragment extends Fragment {
    private FragmentPaymentBinding binding;

    private ItemViewModel itemViewModel;
    private InvoiceViewModel invoiceViewModel;
    private TicketViewModel ticketViewModel;

    private final String TAG = "PaymentFragment";

    private Order order;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        invoiceViewModel = new ViewModelProvider(requireActivity()).get(InvoiceViewModel.class);
        ticketViewModel = new ViewModelProvider(requireActivity()).get(TicketViewModel.class);

        initOrder();
        initEvent();
    }

    public void initOrder() {
        if (getArguments() != null) {
            order = (Order) getArguments().getSerializable("order");
        }

        if (order != null) {
            Map<String, Order.OrderItem> orderItemMap = order.getItem();

            CompletableFuture.runAsync(() -> {
                List<CompletableFuture<Void>> futureList = new ArrayList<>();
                List<ItemPaymentAdapter.ItemOrderPayment> itemPaymentList = new ArrayList<>();

                orderItemMap.forEach((s, orderItem) -> {
                    CompletableFuture<Void> future = itemViewModel.getItemByIDFuture(s)
                            .thenAccept(item -> {
                                itemPaymentList.add(
                                        new ItemPaymentAdapter.ItemOrderPayment(
                                                item, orderItem.getQuantity(),
                                                orderItem.getUnit_price(),
                                                orderItem.getTotal_price()));
                            });
                    futureList.add(future);
                });

                CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]))
                        .thenRun(() -> {
                            ItemPaymentAdapter adapter = new ItemPaymentAdapter(itemPaymentList);

                            binding.recyclerItemPayment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            binding.recyclerItemPayment.setAdapter(adapter);
                        });
            });

            binding.tourSubtotal.setText(String.valueOf(order.getAmount()));
            binding.totalPayment.setText(String.valueOf(order.getAmount()));
        }
    }

    public void initEvent() {
        binding.backBtn.setOnClickListener(view -> {
            getParentFragmentManager().popBackStack();
        });

        binding.radioButtonZalo.setOnClickListener(view -> {
            binding.radioButtonMomo.setChecked(!binding.radioButtonZalo.isChecked());
        });

        binding.radioButtonMomo.setOnClickListener(view -> {
            binding.radioButtonZalo.setChecked(!binding.radioButtonMomo.isChecked());
        });

        binding.paymentBtn.setOnClickListener(view -> {
            if (order != null) {
                String invoice_id = invoiceViewModel.createID();
                Invoice invoice = new Invoice(invoice_id,
                        order.getOrder_id(),
                        Invoice.PaymentStatus.Pending.name(),
                        Helper.getCurrentTimeString(),
                        order.getAmount());
                invoiceViewModel.createInvoice(invoice)
                        .thenAccept(success -> {
                            //handle payment, if success -> update invoice
                            invoiceViewModel.waitingForPayment(invoice_id).observe(getViewLifecycleOwner(), aBoolean -> {
                                if (aBoolean) {
                                    //create ticket
                                    order.getItem().forEach((s, orderItem) -> {
                                        for (int i = 0; i < orderItem.getQuantity(); ++i)
                                            createTicket(s);
                                    });
                                }
                            });

                            //simulate paid
                            Invoice paidInvoice = invoice;
                            paidInvoice.setPayment_status(Invoice.PaymentStatus.Paid.name());
                            invoiceViewModel.createInvoice(paidInvoice);

                            Log.d(TAG, "create invoice: " + success);
                        });
            }
        });
    }

    public CompletableFuture<Boolean> createTicket(String item_id) {
        String ticket_id = ticketViewModel.createID();
        Ticket ticket = new Ticket(ticket_id, order.getOrder_id(), item_id, order.getAccount_id());
        return ticketViewModel.addTicket(ticket);
    }
}