package com.example.ecommerce.Activity.adapter.Vendorside;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Activity.vendor.VendorAcceptActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.OrderResponse.AddConfirmationResponse;
import com.example.ecommerce.Responces.OrderResponse.VendorOrderResponse;
import com.example.ecommerce.network.RestCall;
import com.example.ecommerce.network.RestClient;
import com.example.ecommerce.utils.PreferenceManager;
import com.example.ecommerce.utils.VariableBag;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class VendorAcceptAdapter extends RecyclerView.Adapter<VendorAcceptAdapter.view_accept> {

    LayoutInflater layoutInflater;
    List<VendorOrderResponse.Data> acceptList;
    Context  context;
    RestCall restCall;
    PreferenceManager preferenceManager;

    public VendorAcceptAdapter(Context  context, List<VendorOrderResponse.Data> acceptList) {
        this.context=context;
        this.acceptList=acceptList;
    }

    @NonNull
    @Override
    public view_accept onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.checkorders,parent,false);
        return new view_accept(view);
    }

    @Override
    public void onBindViewHolder(@NonNull view_accept holder, int position) {
        restCall= RestClient.createService(RestCall.class, VariableBag.BASE_URL,VariableBag.API_KEY);
        preferenceManager=new PreferenceManager(context);
                holder.name.setText(acceptList.get(position).getProductName());
                holder.price.setText(acceptList.get(position).getProductPrice());
                holder.quantity.setText(acceptList.get(position).getProductQuantity());
                /*if (acceptList.get(position).getConfirmationStatus().equalsIgnoreCase("1"))
                {
                    holder.reject.setVisibility(View.GONE);
                }
                else if (acceptList.get(position).getConfirmationStatus().equalsIgnoreCase("2")){
                    holder.accept.setVisibility(View.GONE);
                }
                else
                {
                    holder.accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.reject.setVisibility(View.GONE);
                            getConfirm(acceptList.get(position).getUser_id(),acceptList.get(position).getProduct_id(),"1");
                        }
                    });
                    holder.reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.accept.setVisibility(View.GONE);
                            getConfirm(acceptList.get(position).getUser_id(),acceptList.get(position).getProduct_id(),"2");

                        }
                    });
                }*/
    }

    @Override
    public int getItemCount() {
        return acceptList.size();
    }

    public static class view_accept extends RecyclerView.ViewHolder {

        TextView name,price,quantity;
        ImageView accept,reject;
        public view_accept(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.acceptName);
            price=itemView.findViewById(R.id.acceptPrice);
            quantity=itemView.findViewById(R.id.acceptQuantity);
/*            accept=itemView.findViewById(R.id.acceptOrders);
            reject=itemView.findViewById(R.id.cancelOrders);*/

        }
    }
/*    public void getConfirm(String userId,String ProductId,String status)
    {
        restCall.getConfirm("add_confirmation",preferenceManager.getVendorID(),userId,ProductId,status)
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

                    }
                });
    }*/

}
