package com.example.ecommerce.Activity.adapter.UserSide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Activity.user.UserViewOrderActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.OrderResponse.OrderResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.view_order> {
    LayoutInflater layoutInflater;
    Context context;
    String temp;
    List<OrderResponse.Data> messages;

//    OrderInterface orderInterface;
    List<OrderResponse.Data> orderList;
    List<String> orderList1=new ArrayList<>();
//    ProgressBar progressBar;
    int price=0;
    List<String> product_quantity=new ArrayList<>();
    public OrderAdapter(Context context, List<OrderResponse.Data> message,List<OrderResponse.Data> orderList) {
        this.context=context;
        this.messages=message;
        this.orderList=orderList;
//        this.progressBar=progressBar;
    }
 /*   public interface OrderInterface{
        void orderData(List<OrderResponse.Data> orderList);
    }
    public void setViewOrder(OrderInterface order)
    {
        orderInterface=order;
    }*/


    @NonNull
    @Override
    public view_order onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.orderlayout,parent,false);
        return new view_order(view);
    }

    @Override
    public void onBindViewHolder(@NonNull view_order holder, int position) {

        int reversePosition = getItemCount() - 1 - position;
        holder.orderDate.setText(orderList.get(reversePosition).getCartDate());
        holder.order_id.setText(String.format("ord%s", orderList.get(reversePosition).getOrderId()));
        if (orderList.get(reversePosition).getConfirmationStatus().equalsIgnoreCase("1"))
        {
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.greenbg));
        }
        if (orderList.get(reversePosition).getConfirmationStatus().equalsIgnoreCase("2"))
        {
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.back1));

        }        for(OrderResponse.Data item:messages)
        {
            if (item.getOrderId().equalsIgnoreCase(orderList.get(reversePosition).getOrderId()))
            {
                try {
                    int productPrice = Integer.parseInt(item.getProductPrice());
                    int qunatity=Integer.parseInt(item.getProductQuantity());
                    price += productPrice*qunatity;

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            String product=item.getProductName();
            if (!product_quantity.contains(product) && item.getOrderId().equalsIgnoreCase(orderList.get(reversePosition).getOrderId()))
            {
                product_quantity.add(product);
            }
        }
        holder.orderQuantity.setText(String.valueOf(product_quantity.size()));
        product_quantity.clear();
        holder.orderPrice.setText(String.format("₹%d", price));
        price=0;

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserViewOrderActivity.class);
                intent.putExtra("orderID",orderList.get(reversePosition).getOrderId());
                intent.putExtra("myKey", (Serializable)messages);
                context.startActivity(intent);
            }
        });
    }
    public void updateList(List<OrderResponse.Data> check)
    {
        orderList=new ArrayList<>(check);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class view_order extends RecyclerView.ViewHolder {

        TextView order_id,orderDate,orderQuantity,orderPrice;
        Button view;
        LinearLayout cardView;

        public view_order(@NonNull View itemView) {
            super(itemView);
         order_id=itemView.findViewById(R.id.order_id);

        orderDate=itemView.findViewById(R.id.order_Date);
        orderQuantity=itemView.findViewById(R.id.order_quantity);
        orderPrice=itemView.findViewById(R.id.order_price);
        view=itemView.findViewById(R.id.view_button);
        cardView=itemView.findViewById(R.id.layout_orderCard);
        }
    }




}



