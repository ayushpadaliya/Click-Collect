package com.example.ecommerce.Activity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Activity.adapter.UserSide.UserCartDetailsAdapter;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.OrderResponse.AddOrderResponse;
import com.example.ecommerce.Responces.LauncherRespose.CartResponse.CartDetailsResponse;
import com.example.ecommerce.Responces.LauncherRespose.CartResponse.ViewCartResponse;
import com.example.ecommerce.network.RestCall;
import com.example.ecommerce.network.RestClient;
import com.example.ecommerce.utils.PreferenceManager;
import com.example.ecommerce.utils.VariableBag;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserCardViewActivity extends AppCompatActivity {
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    RestCall restCall;
    PreferenceManager preferenceManager;
    UserCartDetailsAdapter userCartDetailsAdapter;
    TextView textView;
    ImageView imageView,notFound;
    TextView total,subtotal;
    ImageButton checkout;
    LinearLayout cartBill;
    String cart_quantity;
    String Total;
    ProgressBar progressBar;
    Handler handler;
    List<ViewCartResponse.Message> cart_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_card_view);
        progressBar=findViewById(R.id.progressBarCart);
        progressBar.setVisibility(View.VISIBLE);
        handler=new Handler();
        preferenceManager=new PreferenceManager(this);
        restCall= RestClient.createService(RestCall.class,VariableBag.BASE_URL,VariableBag.API_KEY);

        imageView=findViewById(R.id.arrow);
        notFound=findViewById(R.id.cart_data);
        recyclerView=findViewById(R.id.reycle_cart);
        cartItemDetails();
        total=findViewById(R.id.Total);
        cartBill=findViewById(R.id.cart_bill);
        checkout=findViewById(R.id.checkoutButton);
        subtotal=findViewById(R.id.subTotal);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                cartView();
            }
        },1000);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserCardViewActivity.this);
                builder.setMessage("Are You Sure About Your Order?");
                builder.setTitle(" CONFIRM !");
                Drawable mydrawable = getResources().getDrawable(R.drawable.marketing);
                builder.setIcon(mydrawable);
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    Intent i=new Intent(UserCardViewActivity.this, UserMainActivity.class);
                    addOrder();
                    i.putExtra("openOrderFragment", true);
                    startActivity(i);
                    finishAffinity();

                });
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog= builder.create();
                alertDialog.setOnShowListener(dialog -> {
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.lightgreen));
                    alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.lightgreen));
                });
                alertDialog.show();
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
                        Toast.makeText(UserCardViewActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

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
    public void cartView()
    {
        restCall.showCart("get_cart",preferenceManager.getCartID()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ViewCartResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ViewCartResponse viewCartResponse) {
                        cart_list=new ArrayList<>();
                        cartBill.setVisibility(View.VISIBLE);
                        if (viewCartResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)) {
                            cart_list=viewCartResponse.getMessage();
                            userCartDetailsAdapter = new UserCartDetailsAdapter(UserCardViewActivity.this, cart_list);
                                recyclerView.setAdapter(userCartDetailsAdapter);
                                userCartDetailsAdapter.setupInterface(new UserCartDetailsAdapter.SendData() {
                                    @Override
                                    public void passTotal(String data) {
                                        total.setText("₹ "+data);

                                        Total=data;

                                    }
                                    @Override
                                    public void passQuan(String digit) {
                                        cart_quantity=digit;
                                        subtotal.setText( "Qts "+digit);
                                        Toast.makeText(UserCardViewActivity.this, " "+digit, Toast.LENGTH_SHORT).show();
                                    }
                                });
                        }
                        else if (viewCartResponse.getMessage().isEmpty()){

                            notFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            cartBill.setVisibility(View.GONE );
                        }
                        else {

                        }
                    }
                });
    }
    public void addOrder()
    {
        Toast.makeText(this, " "+preferenceManager.getCartID(), Toast.LENGTH_SHORT).show();
        restCall.AddOrder("add_order",preferenceManager.getCartID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddOrderResponse>() {
                    @Override
                    public void onCompleted() {
                        
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AddOrderResponse addOrderResponse) {
                            if (addOrderResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE))
                            {
                                Toast.makeText(UserCardViewActivity.this, "order sent !", Toast.LENGTH_SHORT).show();
                            }
                            
                    }
                });
    }
}