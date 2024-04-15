package com.example.ecommerce.Activity.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ecommerce.Activity.adapter.Vendorside.VendorAcceptAdapter;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.OrderResponse.VendorOrderResponse;

import java.util.ArrayList;
import java.util.List;

public class VendorAcceptActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<VendorOrderResponse.Data>orderAccept;
    List<VendorOrderResponse.Data>acceptList;
    VendorAcceptAdapter vendorAcceptAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_accept);
        recyclerView=findViewById(R.id.VendorRecycleViewOrder);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        acceptList=new ArrayList<>();
        orderAccept=new ArrayList<>();
        Intent i =getIntent();
        String id=i.getStringExtra("orderId");
        orderAccept=(List<VendorOrderResponse.Data>) i.getSerializableExtra("myList");
        for (VendorOrderResponse.Data item:orderAccept)
        {

            if (id.contains(item.getOrderId()))
            {
                acceptList.add(item);
            }
        }
        vendorAcceptAdapter=new VendorAcceptAdapter(VendorAcceptActivity.this,acceptList);
        recyclerView.setAdapter(vendorAcceptAdapter);
    }
}