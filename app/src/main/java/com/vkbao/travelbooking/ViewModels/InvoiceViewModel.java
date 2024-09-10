package com.vkbao.travelbooking.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vkbao.travelbooking.Models.CreateOrderResponse;
import com.vkbao.travelbooking.Models.Invoice;
import com.vkbao.travelbooking.Repositories.InvoiceRepository;

import java.util.concurrent.CompletableFuture;

public class InvoiceViewModel extends AndroidViewModel {
    private InvoiceRepository invoiceRepository;

    public InvoiceViewModel(@NonNull Application application) {
        super(application);
        invoiceRepository = new InvoiceRepository();
    }

    public String createID() {
        return invoiceRepository.createID();
    }

    public CompletableFuture<Boolean> createInvoice(Invoice invoice) {
        return invoiceRepository.createInvoice(invoice);
    }

    public LiveData<Boolean> waitingForPayment(String invoiceID) {
        return invoiceRepository.waitingForPayment(invoiceID);
    }

    public CompletableFuture<CreateOrderResponse> zalopay_AddOrder(Invoice invoice) {
        return invoiceRepository.zalopay_AddOrder(invoice);
    }
}
