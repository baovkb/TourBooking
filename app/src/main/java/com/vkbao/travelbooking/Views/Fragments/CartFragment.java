package com.vkbao.travelbooking.Views.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.vkbao.travelbooking.Adapters.CartAdapter;
import com.vkbao.travelbooking.Adapters.ItemPaymentAdapter;
import com.vkbao.travelbooking.Helper.Helper;
import com.vkbao.travelbooking.Helper.ItemTouchDelete;
import com.vkbao.travelbooking.Models.Cart;
import com.vkbao.travelbooking.Models.Order;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.AccountViewModel;
import com.vkbao.travelbooking.ViewModels.CartViewModel;
import com.vkbao.travelbooking.ViewModels.ItemViewModel;
import com.vkbao.travelbooking.ViewModels.OrderViewModel;
import com.vkbao.travelbooking.databinding.FragmentCartBinding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class CartFragment extends Fragment {
    private FragmentCartBinding binding;

    private AccountViewModel accountViewModel;
    private ItemViewModel itemViewModel;
    private CartViewModel cartViewModel;
    private OrderViewModel orderViewModel;

    private ItemTouchHelper itemTouchHelper;

    private int total_price = 0;

    private Context context;

    public CartFragment() {
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
        binding = FragmentCartBinding.inflate(inflater, container, false);
        this.context = container.getContext();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);

        initCartList();
    }

    private void initCartList() {
        accountViewModel.getAccountByUIDFuture(accountViewModel.getUser().getUid())
                .thenAccept(account -> {
                    cartViewModel.getCartByID(account.getCart_id()).observe(getViewLifecycleOwner(), cart -> {
                        if (cart == null || cart.getItem() == null) return;

                        List<ItemPaymentAdapter.ItemOrderPayment> itemCartList = new ArrayList<>();
                        List<CompletableFuture<Void>> futureList = new ArrayList<>();
                        total_price = 0;

                        cart.getItem().forEach((s, cartItem) -> {
                            CompletableFuture<Void> future = itemViewModel.getItemByIDFuture(s)
                                    .thenAccept(item -> {
                                        if (item != null) {
                                            itemCartList.add(
                                                    new ItemPaymentAdapter.ItemOrderPayment(
                                                            item,
                                                            cartItem.getQuantity(),
                                                            item.getPrice(),
                                                            cartItem.getQuantity() * item.getPrice())
                                            );
                                            total_price += cartItem.getQuantity() * item.getPrice();
                                        }
                                    });
                            futureList.add(future);
                        });

                        CompletableFuture.runAsync(() -> {
                            CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]))
                                    .thenRun(() -> {
                                        itemCartList.sort(Comparator.comparing(itemCart -> itemCart.getItem().getItem_id()));
                                        loadAndHandleCartItem(itemCartList, cart);
                                    });
                        });
                    });
                }).exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                });
    }

    private void loadAndHandleCartItem(List<ItemPaymentAdapter.ItemOrderPayment> itemCartList, Cart cart) {
        CartAdapter cartAdapter = new CartAdapter(itemCartList);
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        cartAdapter.setOnAmountChangedListener((item, value) -> {
            executorService.execute(() -> {
                CompletableFuture<Void> updateCartItemFuture = new CompletableFuture<>();

                cartViewModel.updateCartItem(cart.getCart_id(), new Cart.CartItem(item.getItem().getItem_id(), value))
                        .thenRun(() -> updateCartItemFuture.complete(null));

                updateCartItemFuture.join();
            });
        });

        getActivity().runOnUiThread(() -> {
            //set up delete action
            if (itemTouchHelper != null) itemTouchHelper.attachToRecyclerView(null);
            ItemTouchDelete itemTouchDelete = new ItemTouchDelete(requireContext(), cartAdapter);
            itemTouchDelete.setOnItemSwipeListener(position -> {
                String cartID = cart.getCart_id();
                String itemID = itemCartList.get(position).getItem().getItem_id();

                cartViewModel.deleteCartItem(cartID, itemID)
                        .thenAccept(success -> {
                            if (success) Toast.makeText(getActivity(), getString(R.string.cart_delete_item_success), Toast.LENGTH_SHORT).show();
                                else Toast.makeText(getActivity(), getContext().getString(R.string.delete_user_error), Toast.LENGTH_SHORT).show();
                        });
            });

            itemTouchHelper = new ItemTouchHelper(itemTouchDelete);
            itemTouchHelper.attachToRecyclerView(binding.recyclerCart);

            binding.recyclerCart.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            binding.recyclerCart.setAdapter(cartAdapter);
            binding.totalPayment.setText(String.valueOf(total_price));

        });

        binding.bookingBtn.setOnClickListener(view -> {
            Map<String, Order.OrderItem> itemMap = new HashMap<>();
            cartAdapter.getSelectedItemList().forEach(itemOrderPayment -> {
                Order.OrderItem orderItem = new Order.OrderItem(
                        itemOrderPayment.getItem().getItem_id(),
                        itemOrderPayment.getQuantity(),
                        itemOrderPayment.getUnit_price(),
                        itemOrderPayment.getTotal_price());


                itemMap.put(orderItem.getItem_id(), orderItem);
            });

            String order_id = orderViewModel.createID();
            Order order = new Order(
                    order_id,
                    accountViewModel.getUser().getUid(),
                    Helper.getCurrentTimeString(),
                    total_price,
                    itemMap);

            orderViewModel.addOrder(order)
                    .thenAccept(success -> {
                        if (success) {
                            //navigate to payment fragment
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("order", order);
                            PaymentFragment paymentFragment = new PaymentFragment();
                            paymentFragment.setArguments(bundle);

                            getParentFragment().getParentFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main, paymentFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                        else Toast.makeText(getActivity(), getContext().getString(R.string.delete_user_error), Toast.LENGTH_SHORT).show();
                    });
        });
    }

}