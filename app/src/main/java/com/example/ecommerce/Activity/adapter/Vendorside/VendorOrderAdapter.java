package com.example.ecommerce.Activity.adapter.Vendorside;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Activity.vendor.OrderActivity;
import com.example.ecommerce.Activity.vendor.VendorAcceptActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.OrderResponse.AddConfirmationResponse;
import com.example.ecommerce.Responces.OrderResponse.VendorOrderResponse;
import com.example.ecommerce.network.RestCall;
import com.example.ecommerce.network.RestClient;
import com.example.ecommerce.utils.PreferenceManager;
import com.example.ecommerce.utils.VariableBag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VendorOrderAdapter extends RecyclerView.Adapter<VendorOrderAdapter
        .view_vendorOrder> {

    LayoutInflater layoutInflater;
    List<VendorOrderResponse.Data> data;
    List<String> orderIDs=new ArrayList<>();
    Context context;
    int count_card;
    RestCall restCall;
    PreferenceManager preferenceManager;
    CardInterface cardInterface;

    public interface CardInterface{
        void count(String data);
    }
    public void setupCard(CardInterface cardInterface)
    {
        this.cardInterface=cardInterface;
    }


    public VendorOrderAdapter(Context context, List<VendorOrderResponse.Data> data) {
        this.context=context;
        this.data=data;
    }

    @NonNull
    @Override
    public view_vendorOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.demo,parent,false);
        return new view_vendorOrder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull view_vendorOrder holder, int position) {
        restCall= RestClient.createService(RestCall.class, VariableBag.BASE_URL,VariableBag.API_KEY);
        preferenceManager=new PreferenceManager(context);
            if (!orderIDs.contains(data.get(position).getOrderId()))
            {
                orderIDs.add(data.get(position).getOrderId());
                holder.name.setText(data.get(position).getUserName());
                holder.orderId.setText("ORD"+data.get(position).getOrderId());
                count_card+=1;
                cardInterface.count(String.valueOf(count_card));
            }
            else {
                holder.cardView.setVisibility(View.GONE);
            }
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(context, VendorAcceptActivity.class);
                    i.putExtra("orderId",data.get(position).getOrderId());
                    i.putExtra("myList",(Serializable) data);
                    context.startActivity(i);
                }
            });
        if (data.get(position).getConfirmationStatus().equalsIgnoreCase("1"))
        {
            holder.cardView.setCardBackgroundColor(context.getColor(R.color.greenbg));
            holder.reject.setVisibility(View.GONE);
            holder.accept.setVisibility(View.GONE);
        }
        else if (data.get(position).getConfirmationStatus().equalsIgnoreCase("2")){
            holder.cardView.setCardBackgroundColor(context.getColor(R.color.red));
            holder.accept.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);
        }
        else
        {
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.reject.setVisibility(View.GONE);
                    holder.accept.setVisibility(View.GONE);
                    holder.cardView.setCardBackgroundColor(context.getColor(R.color.greenbg));
                    getConfirm(data.get(position).getOrderId(),"1");
                }
            });
            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.accept.setVisibility(View.GONE);
                    holder.reject.setVisibility(View.GONE);
                    holder.cardView.setCardBackgroundColor(context.getColor(R.color.red));
                    getConfirm(data.get(position).getOrderId(),"2");

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class view_vendorOrder extends RecyclerView.ViewHolder {
        TextView name,orderId,price,quantity;
        CardView cardView;
        ImageView imageView,accept,reject;

        public view_vendorOrder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.VendorOrderClick);
            name=itemView.findViewById(R.id.VendorOrderName);
            orderId=itemView.findViewById(R.id.VendorOrderId);
            price=itemView.findViewById(R.id.VendorOrderPrice);
            quantity=itemView.findViewById(R.id.VendorOrderQuantity);
            imageView=itemView.findViewById(R.id.more);
            accept=itemView.findViewById(R.id.acceptOrders);
            reject=itemView.findViewById(R.id.cancelOrders);
        }
    }
    public void getConfirm(String orderId,String status)
    {
        restCall.getConfirm("add_confirmation",preferenceManager.getVendorID(),orderId,status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddConfirmationResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AddConfirmationResponse addConfirmationResponse) {
                                if (addConfirmationResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE))
                                {
                                    Toast.makeText(context, "order accepted !", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(context, " "+addConfirmationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                    }
                });
    }
}
