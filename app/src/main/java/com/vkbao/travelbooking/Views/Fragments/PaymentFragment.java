package com.vkbao.travelbooking.Views.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vkbao.travelbooking.Adapters.ItemPaymentAdapter;
import com.vkbao.travelbooking.Helper.Helper;
import com.vkbao.travelbooking.Models.Invoice;
import com.vkbao.travelbooking.Models.Order;
import com.vkbao.travelbooking.Models.Ticket;
import com.vkbao.travelbooking.Models.Voucher;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.InvoiceViewModel;
import com.vkbao.travelbooking.ViewModels.ItemViewModel;
import com.vkbao.travelbooking.ViewModels.TicketViewModel;
import com.vkbao.travelbooking.ViewModels.VoucherViewModel;
import com.vkbao.travelbooking.databinding.FragmentPaymentBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PaymentFragment extends Fragment {
    private FragmentPaymentBinding binding;

    private ItemViewModel itemViewModel;
    private InvoiceViewModel invoiceViewModel;
    private TicketViewModel ticketViewModel;
    private VoucherViewModel voucherViewModel;
    private Boolean shouldClearVoucher = true;
    private List<Voucher> vouchersTmp = new ArrayList<>();

    private final String TAG = "PaymentFragment";

    private Order order;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("selected_vouchers", this, (key, bundle) -> {
            shouldClearVoucher = bundle.getBoolean("should_clear_voucher", true);
            Log.d(TAG, "receive key from voucher fragment=" + shouldClearVoucher);
            if (shouldClearVoucher) {
                vouchersTmp = voucherViewModel.getSelectedVoucher();
                voucherViewModel.setSelectedVoucher(new ArrayList<>());
            } else {
                voucherViewModel.setSelectedVoucher(vouchersTmp);
            }

            initOrder();
            initVoucher();
            initEvent();
        });
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
        voucherViewModel = new ViewModelProvider(requireActivity()).get(VoucherViewModel.class);

        Log.d("PaymentFragment","shouldClearVoucher=" + shouldClearVoucher);
        if (shouldClearVoucher) {
            vouchersTmp = voucherViewModel.getSelectedVoucher();
            voucherViewModel.setSelectedVoucher(new ArrayList<>());
        }

        initOrder();
        initVoucher();
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

            int subTotal = order.getAmount();
            int discount = calcDiscount(subTotal);
            binding.tourSubtotal.setText(String.valueOf(subTotal));
            binding.discountValue.setText(String.valueOf(discount));

            int total = subTotal - discount;
            if (total < 0) total = 0;
            binding.totalPayment.setText(String.valueOf(total));
        }
    }

    public void initVoucher() {
        binding.voucherContainer.setOnClickListener(view -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, new VoucherSelectionFragment())
                    .addToBackStack(null)
                    .commit();
        });

        binding.voucherContent.setText(String.format(requireContext().getString(R.string.voucher_applied), String.valueOf(voucherViewModel.getSelectedVoucher().size())));
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

        binding.paymentBtn.setEnabled(true);
        binding.paymentBtn.setBackgroundResource(R.drawable.blue_bg);
        binding.paymentBtn.setOnClickListener(view -> {
            if (binding.radioButtonZalo.isChecked() && order != null) {
                String invoice_id = invoiceViewModel.createID();

                //check applied voucher
                int subTotal = order.getAmount();
                int discount = calcDiscount(subTotal);
                int total = subTotal - discount;
                if (total < 0) total = 0;

                Map<String, String> voucherMap = new HashMap<>();
                for(Voucher voucher : voucherViewModel.getSelectedVoucher()) {
                    voucherMap.put(voucher.getVoucher_id(), voucher.getVoucher_id());
                }

                //need to update due to adding voucher reference
                Invoice invoice = new Invoice(
                        invoice_id,
                        order.getOrder_id(),
                        Invoice.PaymentStatus.Pending.name(),
                        Helper.getCurrentTimeString(),
                        subTotal,
                        total,
                        voucherMap
                );
                invoiceViewModel.createInvoice(invoice)
                        .thenAccept(success -> {
                            //handle payment, if success -> update invoice
                            invoiceViewModel.waitingForPayment(invoice_id).observe(getViewLifecycleOwner(), aBoolean -> {
                                if (aBoolean) {
                                    Toast.makeText(getContext(), "Payment successful", Toast.LENGTH_SHORT).show();

                                    createTicketAndNavigate();
                                }
                            });

                            //simulate paid
                            Invoice paidInvoice = invoice;
                            paidInvoice.setPayment_status(Invoice.PaymentStatus.Paid.name());
                            invoiceViewModel.createInvoice(paidInvoice);

                            Log.d(TAG, "create invoice: " + success);
                        });

                binding.paymentBtn.setEnabled(false);
                binding.paymentBtn.setBackgroundResource(R.drawable.gray_bg);
            } else {
                Toast.makeText(getContext(), "You have not chosen payment method", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int calcDiscount(int subTotal) {
        int discount = 0;
        for(Voucher voucher : voucherViewModel.getSelectedVoucher()) {
            if (voucher.getVoucher_type().equals("fixed")) {
                discount += voucher.getVoucher_value();
            } else if (voucher.getVoucher_type().equals("percent")) {
                Double tmp = subTotal * ((double) voucher.getVoucher_value() / 100);
                discount = (int)Math.round(tmp);
            }
        }

        return discount;
    }

    public void createTicketAndNavigate() {
        CompletableFuture.runAsync(() -> {
            List<CompletableFuture<Boolean>> createTicketsFuture = new ArrayList<>();

            order.getItem().forEach((s, orderItem) -> {
                for (int i = 0; i < orderItem.getQuantity(); ++i) {
                    String ticket_id = ticketViewModel.createID();
                    Ticket ticket = new Ticket(ticket_id, order.getOrder_id(), orderItem.getItem_id(), order.getAccount_id());

                    createTicketsFuture.add(ticketViewModel.addTicket(ticket));
                }
            });

            CompletableFuture.allOf(createTicketsFuture.toArray(new CompletableFuture[0]));

            //navigate to my ticket fragment
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, new MyTicketFragment())
                    .commit();
        });

    }

    /*
    private void payOrderZaloPay(String token, Invoice invoice) {
        ZaloPaySDK.getInstance().payOrder(
                getActivity(),
                token,
                "demozpdk://app",
                new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            Toast.makeText(getContext(), "payment success", Toast.LENGTH_SHORT).show();
                            Log.d("payment", "success");
                        });

                        Invoice paidInvoice = invoice;
                        paidInvoice.setPayment_status(Invoice.PaymentStatus.Paid.name());
                        invoiceViewModel.createInvoice(paidInvoice);
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            Toast.makeText(getContext(), "payment success", Toast.LENGTH_SHORT).show();
                            Log.d("payment", "cancel");
                        });
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            Toast.makeText(getContext(), "payment success", Toast.LENGTH_SHORT).show();
                            Log.d("payment", "error");
                        });
                    }
                }
        );
    }

     */
}