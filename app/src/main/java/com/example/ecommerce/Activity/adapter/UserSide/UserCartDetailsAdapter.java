package com.example.ecommerce.Activity.adapter.UserSide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.LauncherRespose.CartResponse.ViewCartResponse;

import java.util.ArrayList;
import java.util.List;

public class UserCartDetailsAdapter extends RecyclerView.Adapter<UserCartDetailsAdapter.view_cart> {

    Context context;
    List<ViewCartResponse.Message> cart_list;
    SendData sendData1;
    Integer Total=0;
    Integer quantity=0;
    List<String> product_quantity=new ArrayList<>();
    public interface SendData{
        void passTotal(String data);
        void passQuan(String digit);
    }
    public void setupInterface(SendData sendData)
    {
        sendData1=sendData;
    }



    public UserCartDetailsAdapter(Context context, List<ViewCartResponse.Message> cart_list) {
        this.context = context;
        this.cart_list = cart_list;
    }

    @NonNull
    @Override
    public view_cart onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.cartlist,parent,false);
        return new view_cart(view);
    }

    @Override
    public void onBindViewHolder(@NonNull view_cart holder, int position) {
        Glide.with(context).load(cart_list.get(position).getImage_path()).into(holder.imageView);
        holder.itemName.setText(cart_list.get(position).getProductName());
        holder.itemQuantity.setText( "Qts "+cart_list.get(position).getProductQuantity());
        Integer price=Integer.parseInt(cart_list.get(position).getProductPrice())*Integer.parseInt(cart_list.get(position).getProductQuantity());
        holder.itemPrice.setText(" ₹"+String.valueOf(price));
        Total=Total+price;
        sendData1.passTotal(String.valueOf(Total));
        if (!product_quantity.contains(cart_list.get(position).getProductName()))
        {
            product_quantity.add(cart_list.get(position).getProductName());
            quantity+=1;
            sendData1.passQuan(String.valueOf(quantity));
        }

    }

    @Override
    public int getItemCount() {
        return cart_list.size();
    }

    public class view_cart extends RecyclerView.ViewHolder {
        TextView itemName,itemQuantity,itemPrice;
        ImageView imageView;
        public view_cart(@NonNull View itemView) {
            super(itemView);
            itemName=itemView.findViewById(R.id.cartItem);
            imageView=itemView.findViewById(R.id.cartImage);
            itemQuantity=itemView.findViewById(R.id.cartQuantity);
            itemPrice=itemView.findViewById(R.id.cartPrice);
        }
    }
}
