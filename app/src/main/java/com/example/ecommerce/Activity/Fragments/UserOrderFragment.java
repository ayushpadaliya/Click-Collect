package com.example.ecommerce.Activity.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Activity.adapter.UserSide.OrderAdapter;
import com.example.ecommerce.Activity.user.ServiceActivity;
import com.example.ecommerce.Activity.user.UserCardViewActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.OrderResponse.OrderResponse;
import com.example.ecommerce.network.RestCall;
import com.example.ecommerce.network.RestClient;
import com.example.ecommerce.utils.PreferenceManager;
import com.example.ecommerce.utils.VariableBag;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class UserOrderFragment extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RestCall restCall;
    PreferenceManager preferenceManager;
    OrderAdapter orderAdapter;
    EditText search;
    TextView textView;
    ImageButton imageButton;
    List<OrderResponse.Data> finalList;
    List<String> orderList;
    List<OrderResponse.Data> orderListResponse;
    ImageView imageView,user_log;
    ProgressBar progressBar;
    Handler handler;
    com.google.android.material.appbar.AppBarLayout barLayout;
  List< OrderResponse.Data> finalOrder;
    public UserOrderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview;
        rootview = inflater.inflate(R.layout.fragment_user_order, container, false);
        recyclerView=rootview.findViewById(R.id.orderRecycle);
        barLayout=rootview.findViewById(R.id.bar);
        linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        textView=rootview.findViewById(R.id.txt1);
        preferenceManager=new PreferenceManager(getContext());
        progressBar=rootview.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        imageView=rootview.findViewById(R.id.searchFragment);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        search=rootview.findViewById(R.id.searchOrder);
        finalOrder=new ArrayList<>();
        restCall= RestClient.createService(RestCall.class,VariableBag.BASE_URL,VariableBag.API_KEY);
        handler=new Handler();
        user_log=rootview.findViewById(R.id.user_log1);
        getActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (search.getVisibility()==View.VISIBLE)
                {
                    search.setVisibility(View.GONE);
                    barLayout.setVisibility(View.VISIBLE);
                }
                else {
                    getActivity().finish();
                }
            }
        });
/*        user_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), ServiceActivity.class);
                startActivity(i);
            }
        });*/
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barLayout.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
            }
        });
        imageButton=rootview.findViewById(R.id.cancel_button_order);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setText("");
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                orderPage();
            }
        },1000);


        return rootview;

    }
    public void orderPage()
    {
        restCall.orderPage("get_order",preferenceManager.getUserProduct())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OrderResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(OrderResponse orderResponse) {

                        orderList=new ArrayList<>();
                        orderListResponse=new ArrayList<>();
                        finalList=new ArrayList<>();
                        if (orderResponse.getStatus().equals(VariableBag.SUCCESS_CODE))
                        {
                            finalOrder=orderResponse.getData();
                            orderListResponse=orderResponse.getData();
                            search.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }
                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    if (charSequence.length() > 0) {
                                        imageButton.setVisibility(View.VISIBLE);
                                    } else {
                                        imageButton.setVisibility(View.GONE);
                                    }
                                    if (orderAdapter!=null && finalOrder!=null) {
                                        orderAdapter.updateList(check(charSequence));
//                    imageView.setVisibility (check(charSequence).isEmpty()? View.VISIBLE:View.GONE);
                                    }
                                    else {Toast.makeText(getActivity(), " "+check(charSequence).get(0), Toast.LENGTH_LONG).show();
                                        Log.d("search","hello"+check(charSequence).get(0).getOrderId());
                                    }

                                }
                                @Override
                                public void afterTextChanged(Editable editable) {

                                }
                            });
                            for (OrderResponse.Data item : orderListResponse) {
                                if (!orderList.contains(item.getOrderId())) {
                                    finalList.add(item);
                                    orderList.add(item.getOrderId());
                                    Log.d("orderID","here it "+item.getOrderId());
                                }
                                else {
                                    Log.d("repeat","order"+item.getOrderId());
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                            orderAdapter=new OrderAdapter(getContext(),orderResponse.getData(),finalList);
                            recyclerView.setAdapter(orderAdapter);
                        }
                    }
                });
    }
    public List<OrderResponse.Data> check(CharSequence charSequence)
    {
        String query = charSequence.toString().toLowerCase();
        if (finalOrder==null)
        {
            return new ArrayList<>(); // Return an empty list if vendorGetData or its data is null

        }
        else{
            List<OrderResponse.Data> filteredList = new ArrayList<>();
            for (OrderResponse.Data item : finalOrder) {
                if (item.getOrderId().toLowerCase().contains(query))
                {
                    filteredList.add(item);
                }
                else
                {

                }
            }
            return filteredList;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }


}