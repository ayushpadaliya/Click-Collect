package com.example.ecommerce.Activity.adapter.UserSide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Activity.user.UserViewOrderActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.OrderResponse.OrderResponse;

import java.util.List;

public class UserOrderDetailsAdapter extends RecyclerView.Adapter<UserOrderDetailsAdapter.view_detail_order> {

    LayoutInflater layoutInflater;
    List<OrderResponse.Data> orderDetails;
    Context context;

    public UserOrderDetailsAdapter(Context context, List<OrderResponse.Data> orderDetails) {
        this.context=context;
        this.orderDetails=orderDetails;
    }


    @NonNull
    @Override
    public view_detail_order onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.demo1,parent,false);
        return new view_detail_order(view);
    }

    @Override
    public void onBindViewHolder(@NonNull view_detail_order holder, int position) {
        holder.vendorName.setText(orderDetails.get(position).getVendorName());
        holder.productPrice.setText("₹"+orderDetails.get(position).getProductPrice());
        holder.productName.setText(orderDetails.get(position).getProductName());
        holder.productQuantity.setText(orderDetails.get(position).getProductQuantity());
        if (orderDetails.get(position).getConfirmationStatus().equalsIgnoreCase("0"))
        {
            holder.pending.setVisibility(View.VISIBLE);
        } else if (orderDetails.get(position).getConfirmationStatus().equalsIgnoreCase("1")) {
            holder.accept.setVisibility(View.VISIBLE);
        }
        else {
            holder.cancel.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public static class view_detail_order extends RecyclerView.ViewHolder {

        TextView vendorName,productName,productQuantity,confirmation,productPrice;
        ImageView pending,accept,cancel;
        public view_detail_order(@NonNull View itemView) {
            super(itemView);
            vendorName=itemView.findViewById(R.id.orderVname);
            productName=itemView.findViewById(R.id.orderPname);
            productQuantity=itemView.findViewById(R.id.orderQuant);
            confirmation=itemView.findViewById(R.id.orderConfirm);
            productPrice=itemView.findViewById(R.id.orderprice);
            pending=itemView.findViewById(R.id.pending);
            accept=itemView.findViewById(R.id.accept);
            cancel=itemView.findViewById(R.id.cancel);        }
    }
}
