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
import com.vkbao.travelbooking.Models.Cart;
import com.vkbao.travelbooking.Models.Item;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.AccountViewModel;
import com.vkbao.travelbooking.ViewModels.CartViewModel;
import com.vkbao.travelbooking.databinding.FragmentTourDetailBinding;

public class TourDetailFragment extends Fragment {
    FragmentTourDetailBinding binding;

    private CartViewModel cartViewModel;
    private AccountViewModel accountViewModel;

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

        initItem();
        setEvent();
    }

    public void initItem() {
        if (getArguments() != null) {
            item = getArguments().getParcelable("item");
        }

        if (item != null) {
            Glide.with(requireContext())
                            .load(item.getBanner())
                                    .into(binding.banner);
            binding.title.setText(item.getTitle());
            binding.address.setText(item.getAddress());
            binding.description.setText(item.getDescription());
            binding.price.setText(item.getPrice());
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
                    .thenCompose(account -> cartViewModel.getCartByID(account.getCart_id()))
                    .thenCompose(cart -> {
                        String cartID = cart.getCart_id();
                        Cart.CartItem cartItem = new Cart.CartItem(item.getItem_id(), 1);
                        return cartViewModel.addCartItem(cartID, cartItem);
                    }).thenApply(success -> {
                        if (success) Toast.makeText(requireContext(), "add to cart successfully", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show();
                        return null;
                    }).exceptionally(e -> {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show();
                        return null;
                    });
        });
    }
}