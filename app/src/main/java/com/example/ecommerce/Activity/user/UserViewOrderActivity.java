package com.example.ecommerce.Activity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.Activity.adapter.UserSide.OrderAdapter;
import com.example.ecommerce.Activity.adapter.UserSide.UserOrderDetailsAdapter;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.OrderResponse.OrderResponse;

import java.util.ArrayList;
import java.util.List;

public class UserViewOrderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    OrderAdapter orderAdapter;
    UserOrderDetailsAdapter userOrderDetailsAdapter;
    LinearLayoutManager linearLayoutManager;
    List<OrderResponse.Data> orderDetails;
    List<OrderResponse.Data> orderDetailsList;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_view_order);
        recyclerView=findViewById(R.id.orderRecycleView);
        orderDetails=new ArrayList<>();
        imageView=findViewById(R.id.arrow);
        Intent intent = getIntent();
        String order=intent.getStringExtra("orderID");
        orderDetails = (List<OrderResponse.Data>) intent.getSerializableExtra("myKey");
        orderDetailsList=new ArrayList<>();
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        for (OrderResponse.Data item:orderDetails) {
            if (order.contains(item.getOrderId()))
            {
                orderDetailsList.add(item);
            }
        }
        userOrderDetailsAdapter=new UserOrderDetailsAdapter(UserViewOrderActivity.this,orderDetailsList);
        recyclerView.setAdapter(userOrderDetailsAdapter);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}