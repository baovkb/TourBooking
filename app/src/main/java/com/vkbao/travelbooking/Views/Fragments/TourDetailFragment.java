package com.vkbao.travelbooking.Views.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vkbao.travelbooking.Helper.Helper;
import com.vkbao.travelbooking.Models.Cart;
import com.vkbao.travelbooking.Models.Item;
import com.vkbao.travelbooking.Models.Order;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.Repositories.OrderRepository;
import com.vkbao.travelbooking.ViewModels.AccountViewModel;
import com.vkbao.travelbooking.ViewModels.CartViewModel;
import com.vkbao.travelbooking.ViewModels.OrderViewModel;
import com.vkbao.travelbooking.databinding.BottomSheetBookingBinding;
import com.vkbao.travelbooking.databinding.FragmentTourDetailBinding;

import java.util.HashMap;
import java.util.Map;

public class TourDetailFragment extends Fragment {
    FragmentTourDetailBinding binding;

    private CartViewModel cartViewModel;
    private AccountViewModel accountViewModel;
    private OrderViewModel orderViewModel;

    private Item item;

    public TourDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTourDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);

        initItem();
        setEvent();
    }

    public void initItem() {
        if (getArguments() != null) {
            item = (Item)getArguments().getSerializable("item");
        }

        if (item != null) {
            Glide.with(requireContext())
                            .load(item.getBanner())
                                    .into(binding.banner);
            binding.title.setText(item.getTitle());
            binding.address.setText(item.getAddress());
            binding.description.setText(item.getDescription());
            binding.price.setText(String.valueOf(item.getPrice()));
            binding.valDuration.setText(item.getDuration());
            binding.valStartDate.setText(item.getDateTour());
        }
    }

    public void setEvent() {
        binding.backBtn.setOnClickListener(view -> getParentFragmentManager().popBackStack());

        binding.cartBtn.setOnClickListener(view -> {
            String uid = accountViewModel.getUser().getUid();
            accountViewModel
                    .getAccountByUIDFuture(uid)
                    .thenCompose(account -> cartViewModel.getCartByIDFuture(account.getCart_id()))
                    .thenCompose(cart -> {
                        String cartID = cart.getCart_id();
                        Cart.CartItem cartItem = new Cart.CartItem(item.getItem_id(), 1);
                        return cartViewModel.addCartItem(cartID, cartItem);
                    }).thenAccept(success -> {
                        if (success) Toast.makeText(requireContext(), "add to cart successfully", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show();
                    }).exceptionally(e -> {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show();
                        return null;
                    });
        });

        binding.bookingBtn.setOnClickListener(view -> {
            BottomSheetBookingBinding bindingBottomSheet = BottomSheetBookingBinding.inflate(getLayoutInflater(), null, false);

            int amountTicket = 1;

            bindingBottomSheet.numTicket.setText(String.valueOf(amountTicket));
            bindingBottomSheet.subtractBtn.setOnClickListener(view1 -> {
                int numTicket = Integer.parseInt(bindingBottomSheet.numTicket.getText().toString());
                if (numTicket > 0) {
                    bindingBottomSheet.numTicket.setText(String.valueOf(--numTicket));
                    bindingBottomSheet.totalVal.setText(String.valueOf(numTicket * item.getPrice()));
                }
            });

            bindingBottomSheet.addBtn.setOnClickListener(view12 -> {
                int numTicket = Integer.parseInt(bindingBottomSheet.numTicket.getText().toString());
                bindingBottomSheet.numTicket.setText(String.valueOf(++numTicket));
                bindingBottomSheet.totalVal.setText(String.valueOf(numTicket * item.getPrice()));
            });

            bindingBottomSheet.totalVal.setText(String.valueOf(amountTicket * item.getPrice()));

            BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
            dialog.setContentView(bindingBottomSheet.getRoot());
            dialog.show();

            bindingBottomSheet.bookingBtn.setOnClickListener(view13 -> {
                String order_id = orderViewModel.createID();
                int quantity = Integer.parseInt(bindingBottomSheet.numTicket.getText().toString());

                Order.OrderItem orderItem = new Order.OrderItem(
                        item.getItem_id(),
                        quantity,
                        item.getPrice(),
                        item.getPrice() * quantity);
                Map<String, Order.OrderItem> itemMap = new HashMap<>();
                itemMap.put(orderItem.getItem_id(), orderItem);

                Order order = new Order(
                        order_id,
                        accountViewModel.getUser().getUid(),
                        Helper.getCurrentTimeString(),
                        orderItem.getTotal_price(),
                        itemMap);

                orderViewModel.addOrder(order)
                        .thenAccept(success -> {
                            if (success) {
                                //close bottom sheet
                                dialog.dismiss();

                                //navigate to payment fragment
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("order", order);
                                PaymentFragment paymentFragment = new PaymentFragment();
                                paymentFragment.setArguments(bundle);

                                getParentFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.main, paymentFragment)
                                        .addToBackStack(null)
                                        .commit();
                            }
                            else Toast.makeText(getActivity(), getContext().getString(R.string.delete_user_error), Toast.LENGTH_SHORT).show();
                        });
            });

        });
    }
}