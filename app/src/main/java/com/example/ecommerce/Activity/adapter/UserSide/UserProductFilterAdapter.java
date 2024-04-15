package com.example.ecommerce.Activity.adapter.UserSide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.AllDataResponse;
import com.example.ecommerce.Responces.LauncherRespose.CartResponse.CartDetailsResponse;
import com.example.ecommerce.Responces.LauncherRespose.CartResponse.UserAddCartItemResponse;
import com.example.ecommerce.network.RestCall;
import com.example.ecommerce.network.RestClient;
import com.example.ecommerce.utils.PreferenceManager;
import com.example.ecommerce.utils.VariableBag;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserProductFilterAdapter extends RecyclerView.Adapter<UserProductFilterAdapter.View_ProductFilter> {

    Context context;


    RestCall restCall;
    PreferenceManager preferenceManager;
    String cart_id;
    SetCart set;
    Integer po;
    Picasso picasso;
    String count;
    Glide glide;
    String temp;

    List<AllDataResponse.ProductData> ProductList;
    public UserProductFilterAdapter(Context context, List<AllDataResponse.ProductData> ProductList) {
        this.context=context;
        this.ProductList=ProductList;
    }
    public interface SetCart{
        void passId(String id);
    }
    public void setUpButton(SetCart setCart)
    {
        set=setCart;
    }

    @NonNull
    @Override
    public View_ProductFilter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.userproductlist,parent,false);
        return new View_ProductFilter(view);
    }

    public static void displayImage(Context ctx, ImageView img, String url) {
        try {
            Glide.with(ctx).load(url).apply(new RequestOptions().placeholder(R.drawable.product).error(R.drawable.addcart))
                    .into(img);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    @Override
    public void onBindViewHolder(@NonNull View_ProductFilter holder, int position) {
        restCall= RestClient.createService(RestCall.class, VariableBag.BASE_URL,VariableBag.API_KEY);
        preferenceManager=new PreferenceManager(context.getApplicationContext());
        cartItemDetails();
        holder.product_name.setText(ProductList.get(position).getProductName());
        holder.product_price.setText("₹ "+ProductList.get(position).getProductPrice());
        displayImage(context,holder.imageView,ProductList.get(position).getImage_path());
        if (ProductList.get(position).getProductQuantity().equalsIgnoreCase("0"))
        {
            holder.cart.setVisibility(View.VISIBLE);
            holder.linearLayout.setVisibility(View.GONE);

        }
        else {

            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.cart.setVisibility(View.GONE);
            holder.product_count.setText(ProductList.get(position).getProductQuantity());
        }
        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set.passId("1");
                addCartItems("1",ProductList.get(position).getProductId(),ProductList.get(position).getProductName(),ProductList.get(position).getProductPrice());
                holder.cart.setVisibility(View.GONE);
                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.product_count.setText("1");

            }
        });
        holder.addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set.passId("1");
                    temp=holder.product_count.getText().toString();
                    po=Integer.parseInt(temp)+1;
                    String digit=String.valueOf(po);
                    holder.product_count.setText(digit);
                    addCartItems(digit,ProductList.get(position).getProductId(),ProductList.get(position).getProductName(),ProductList.get(position).getProductPrice());
            }
        });
        holder.minProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp=holder.product_count.getText().toString();
                 po=Integer.parseInt(temp)-1;

                if (po<1) {
                    set.passId("-1");
                    holder.cart.setVisibility(View.VISIBLE);
                    holder.linearLayout.setVisibility(View.GONE);
                    addCartItems("0",ProductList.get(position).getProductId(),ProductList.get(position).getProductName(),ProductList.get(position).getProductPrice());
                }
                else
                {
                    set.passId("-1");
                    holder.product_count.setText(String.valueOf(po));
                    count=String.valueOf(po);
                    addCartItems(count,ProductList.get(position).getProductId(),ProductList.get(position).getProductName(),ProductList.get(position).getProductPrice());
                }
            }
        });
    }





    @Override
    public int getItemCount() {
        return ProductList.size();
    }

    public static class View_ProductFilter extends RecyclerView.ViewHolder {
        TextView product_name,product_price,product_count;
        ImageButton addProduct,minProduct;
        LinearLayout linearLayout;
        ImageView imageView,cart;
        public View_ProductFilter(@NonNull View itemView) {
            super(itemView);
            product_name=itemView.findViewById(R.id.UserProductName);
            product_price=itemView.findViewById(R.id.UserProductPrice);
            cart=itemView.findViewById(R.id.cartIcon);
            linearLayout=itemView.findViewById(R.id.layoutCart);
            product_count=itemView.findViewById(R.id.product_count);
            addProduct=itemView.findViewById(R.id.addProduct);
            minProduct=itemView.findViewById(R.id.minProduct);
            imageView=itemView.findViewById(R.id.productImage);
        }
    }
    public void updateList(List<AllDataResponse.ProductData> newList)
    {
        ProductList=new ArrayList<>(newList);
        notifyDataSetChanged();
    }
    public void addCartItems(String count,String id,String name,String price)
    {
        restCall.addCartItem("add_cart_item", preferenceManager.getCartID(), preferenceManager.getVendorProduct(),preferenceManager.getUserProduct(),id,count,name,price)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserAddCartItemResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UserAddCartItemResponse userAddCartItemResponse) {
                        if (userAddCartItemResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE));
                        {

                        }
                    }
                });
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
                        Log.d("cart_id"," "+e.getMessage());
                    }

                    @Override
                    public void onNext(CartDetailsResponse cartDetailsResponse) {
                        if (cartDetailsResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE))
                        {
                            preferenceManager.setCArtID(VariableBag.cart_id,cartDetailsResponse.getMessage().get(0).getCartId());


                        }
                    }
                });
    }
}
