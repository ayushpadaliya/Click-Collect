package com.example.ecommerce.Activity.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Activity.adapter.Vendorside.VendorOrderAdapter;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.OrderResponse.VendorOrderResponse;
import com.example.ecommerce.network.RestCall;
import com.example.ecommerce.network.RestClient;
import com.example.ecommerce.utils.PreferenceManager;
import com.example.ecommerce.utils.VariableBag;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RestCall restCall;
    PreferenceManager preferenceManager;
    VendorOrderAdapter vendorOrderAdapter;
    TextView total;
    String data1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        recyclerView=findViewById(R.id.vendorOrders);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        restCall= RestClient.createService(RestCall.class, VariableBag.BASE_URL,VariableBag.API_KEY);
        preferenceManager=new PreferenceManager(this);
        total=findViewById(R.id.total_orders);
        getOrder();


    }

    private void getOrder() {
        restCall.getOrder("view_order",preferenceManager.getVendorID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VendorOrderResponse>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(VendorOrderResponse vendorOrderResponse) {
                           if (vendorOrderResponse.getStatus().equals(VariableBag.SUCCESS_CODE))
                           {
                                vendorOrderAdapter=new VendorOrderAdapter(OrderActivity.this,vendorOrderResponse.getData());
                                recyclerView.setAdapter(vendorOrderAdapter);
                                vendorOrderAdapter.setupCard(new VendorOrderAdapter.CardInterface() {
                                    @Override
                                    public void count(String data) {
                                        Toast.makeText(OrderActivity.this, " "+data, Toast.LENGTH_SHORT).show();
                                        data1=data;
                                        total.setText("orders : "+data1);
                                    }
                                });
                           }
                    }
                });
    }
}