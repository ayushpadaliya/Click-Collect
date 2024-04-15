package com.example.ecommerce.Activity.adapter.UserSide;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Activity.user.UserProductChoiceActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.LauncherRespose.CartResponse.CartDetailsResponse;
import com.example.ecommerce.Responces.LauncherRespose.VendorGetData;
import com.example.ecommerce.network.RestCall;
import com.example.ecommerce.network.RestClient;
import com.example.ecommerce.utils.PreferenceManager;
import com.example.ecommerce.utils.VariableBag;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeViewAdapter extends RecyclerView.Adapter<HomeViewAdapter.View_home> {

    LayoutInflater layoutInflater;
    VendorGetData vendorGetData;
    Context context;
    List<VendorGetData.Data> Data;
    PreferenceManager preferenceManager;
    RestCall restCall;

    public HomeViewAdapter(Context context, List<VendorGetData.Data> data) {
        this.context = context;
        Data = data;
    }

    //    VendorDataInterface vendorDataInterface;
    /*public interface VendorDataInterface{
        void VendorData(VendorGetData vendorGetData);

    }

    public void setData(VendorDataInterface vendorDataInterface)
    {
        this.vendorDataInterface=vendorDataInterface;

    }
*/
    @NonNull
    @Override
    public View_home onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.homerecyclelayout,parent,false);
        return new View_home(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_home holder, int position) {
        restCall= RestClient.createService(RestCall.class, VariableBag.BASE_URL,VariableBag.API_KEY);
        preferenceManager=new PreferenceManager(context);
        if (Data!=null) {
            holder.textView.setText(Data.get(position).getVendorName());
            holder.phone.setText(Data.get(position).getVendorPhone());
            holder.phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + Data.get(position).getVendorPhone()));
                    holder.phone.getContext().startActivity(intent);
                }
            });
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, UserProductChoiceActivity.class);
                    i.putExtra("vendor_id",Data.get(position).getVendorName());
                    preferenceManager.setVendorProduct(VariableBag.Vendor_product_id,Data.get(position).getVendorId());
                    cartItemDetails();
                    context.startActivity(i);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public static class View_home extends RecyclerView.ViewHolder {


        TextView textView,phone;
        CardView card;
        public View_home(@NonNull View itemView) {
            super(itemView);
            card=itemView.findViewById(R.id.VendorClick);
            textView=itemView.findViewById(R.id.vendorName);
            phone=itemView.findViewById(R.id.VendorPhone);

        }

    }
    public void updateList(List<VendorGetData.Data> newList)
    {
        Data=new ArrayList<>(newList);
        notifyDataSetChanged();
    }
    public void cartItemDetails()
    {
        restCall.CartItem("add_cart",preferenceManager.getVendorProduct(),preferenceManager.getUserProduct()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CartDetailsResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(CartDetailsResponse cartDetailsResponse) {
                        if (cartDetailsResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE))
                        {
                            preferenceManager.setCArtID(VariableBag.cart_id,cartDetailsResponse.getMessage().get(0).getCartId().toString());
                        }
                    }
                });
    }
}
