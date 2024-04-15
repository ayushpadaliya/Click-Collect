package com.example.ecommerce.Activity.user;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Activity.adapter.UserSide.UserCategoryFilterAdapter;
import com.example.ecommerce.Activity.adapter.UserSide.UserProductFilterAdapter;
import com.example.ecommerce.Activity.adapter.UserSide.UserSubCategoryFilterAdapter;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.AllDataResponse;
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

public class UserProductChoiceActivity extends AppCompatActivity {
    TextView textView;
    RecyclerView recyclerView;
    RecyclerView recyclerView1;
    ProgressBar progressBar;
    RecyclerView recyclerViewProduct;
    int count;
    RestCall restCall;
    List<ViewCartResponse.Message> cart_list;
    UserCategoryFilterAdapter userCategoryFilterAdapter;
    List<AllDataResponse.CategoryData> category_list;
    List<AllDataResponse.SubcategoryData> subCategory_list;
    List<AllDataResponse.ProductData> product_list;
    UserSubCategoryFilterAdapter userSubCategoryFilterAdapter;
    PreferenceManager preferenceManager;
    UserProductFilterAdapter userProductFilterAdapter;
    EditText editText;
    ImageView imageView,cartButton,search;
    Handler handler;
    String cart_id;
    String cat_id;

    String sub_id;
    ImageButton imageButton;
    TextView textView1;
    List<AllDataResponse.CategoryData> categoryDataList;
    List<AllDataResponse.SubcategoryData> subcategoryDataList;
    List<AllDataResponse.ProductData> productDataList;
    LinearLayout linearLayout,linearLayout1;
    com.google.android.material.appbar.AppBarLayout appBarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product_choice);
        restCall = RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);
        recyclerView = findViewById(R.id.userMainRecycle);
        preferenceManager = new PreferenceManager(this);
        progressBar=findViewById(R.id.progressBarProduct);
        handler=new Handler(Looper.getMainLooper());
        appBarLayout=findViewById(R.id.barProduct);
        search=findViewById(R.id.searchProducts);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appBarLayout.setVisibility(View.GONE);
                editText.setVisibility(View.VISIBLE);
            }
        });
        this.getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (editText.getVisibility()==View.VISIBLE) {
                    editText.setVisibility(View.GONE);
                    appBarLayout.setVisibility(View.VISIBLE);
                }
                else {
                    finish();
                }
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                allData();
            }
        },1000);

        cartItemDetails();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        category_list = new ArrayList<>();
        textView1 = findViewById(R.id.textOne);
        imageButton = findViewById(R.id.cancel_button);
        recyclerView1 = findViewById(R.id.userMainRecycleSub);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(linearLayoutManager1);
        subCategory_list = new ArrayList<>();
        recyclerViewProduct = findViewById(R.id.userMainRecycleProduct);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(UserProductChoiceActivity.this, 2);
        recyclerViewProduct.setLayoutManager(gridLayoutManager);
        linearLayout = findViewById(R.id.subLayout);
        linearLayout1 = findViewById(R.id.ProductLayout);
        linearLayout1.setVisibility(View.GONE);
        editText = findViewById(R.id.search);
        imageView = findViewById(R.id.found);
        cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserProductChoiceActivity.this, UserCardViewActivity.class);
                startActivity(i);
            }
        });



    }
    public void allData()
    {
//        Toast.makeText(UserProductChoiceActivity.this, " "+preferenceManager.getVendorProduct(), Toast.LENGTH_SHORT).show();
        restCall.allData("all_data_fetch",preferenceManager.getVendorProduct(),preferenceManager.getUserProduct())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AllDataResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(UserProductChoiceActivity.this, " "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(AllDataResponse allDataResponse) {
                        categoryDataList=new ArrayList<>();
                        productDataList=new ArrayList<>();
                        category_list=new ArrayList<>();
                        subcategoryDataList=new ArrayList<>();
                    if (allDataResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE))
                    {
                        progressBar.setVisibility(View.GONE);
                        categoryDataList = allDataResponse.getCategoryData();
                        productDataList = allDataResponse.getProductData();
                        subcategoryDataList=allDataResponse.getSubcategoryData();
                        for (AllDataResponse.CategoryData category : categoryDataList) {
                            boolean categoryMatched = false;
                            for (AllDataResponse.ProductData product : productDataList) {
                                if (category.getCategoryId().equalsIgnoreCase(product.getCategoryId())) {
                                    categoryMatched = true;
                                    break;
                                }
                            }
                            if (categoryMatched) {
                                category_list.add(category);
                            }
                        }

                        userCategoryFilterAdapter = new UserCategoryFilterAdapter(category_list, UserProductChoiceActivity.this);
                        recyclerView.setAdapter(userCategoryFilterAdapter);
                        userCategoryFilterAdapter.setUp(new UserCategoryFilterAdapter.subClick() {

                            @Override
                            public void subCat(AllDataResponse.CategoryData message) {
                                cat_id=message.getCategoryId();
                                subCategory_list=new ArrayList<>();
                                for (AllDataResponse.SubcategoryData subcategoryData:subcategoryDataList)
                                {
                                    boolean subCategoryMatched = false;
                                    for (AllDataResponse.ProductData productData:productDataList) {
                                        if (subcategoryData.getCategoryId().equalsIgnoreCase(cat_id) && subcategoryData.getSubCategoryId().equalsIgnoreCase(productData.getSubCategoryId())) {
                                            subCategoryMatched = true;
                                            break;
                                        }
                                    }
                                    if (subCategoryMatched)
                                    {
                                        subCategory_list.add(subcategoryData);
                                    }
                                }
                                imageView.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.VISIBLE);
                                linearLayout1.setVisibility(View.GONE);
                                LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(UserProductChoiceActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                recyclerView1.setLayoutManager(linearLayoutManager2);
                                userSubCategoryFilterAdapter = new UserSubCategoryFilterAdapter(UserProductChoiceActivity.this, subCategory_list);
                                recyclerView1.setAdapter(userSubCategoryFilterAdapter);
                                userSubCategoryFilterAdapter.setUpProduct(new UserSubCategoryFilterAdapter.ProductInterface() {
                                    @Override
                                    public void setProduct(AllDataResponse.SubcategoryData subcategoryData) {
                                        sub_id=subcategoryData.getSubCategoryId();
                                        product_list=new ArrayList<>();
                                        boolean productMatched = false;
                                        for (AllDataResponse.ProductData productData:productDataList)
                                        {

                                            if (productData.getCategoryId().equalsIgnoreCase(cat_id) && productData.getSubCategoryId().equalsIgnoreCase(sub_id))
                                            {
                                                product_list.add(productData);
                                            }
                                        }
//                                        progressBar.setVisibility(View.VISIBLE);
                                        imageView.setVisibility(View.GONE);
                                        UserProductFilterAdapter userProductFilterAdapter = new UserProductFilterAdapter(UserProductChoiceActivity.this, product_list);
                                        recyclerViewProduct.setAdapter(userProductFilterAdapter);
                                        linearLayout1.setVisibility(View.VISIBLE);
                                        userProductFilterAdapter.setUpButton(new UserProductFilterAdapter.SetCart() {
                                            @Override
                                            public void passId(String id) {
                                                textView1.setVisibility(View.VISIBLE);
                                                int po= Integer.parseInt(textView1.getText().toString());
                                                int temp=Integer.parseInt(id)+po;
                                                textView1.setText(String.valueOf(temp));
                                                if (textView1.getText().toString().equalsIgnoreCase("0"))
                                                {
                                                    textView1.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                                        for (AllDataResponse.ProductData item:product_list)
                                        {
                                            if (!item.getProductQuantity().equalsIgnoreCase("0"))
                                            {

                                                count++;
                                            }
                                        }
                                     /*   if (count!=0)
                                        {
                                            textView1.setVisibility(View.VISIBLE);
                                            textView1.setText(String.valueOf(count));
                                        }
                                        else
                                        {
                                            textView1.setVisibility(View.GONE);
                                        }*/
                                        editText.addTextChangedListener(new TextWatcher() {
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
                                                if (userProductFilterAdapter!=null && productDataList != null) {
                                                    userProductFilterAdapter.updateList(check(charSequence));
                                                    imageView.setVisibility (check(charSequence).isEmpty()? View.VISIBLE:View.GONE);
                                                }
                                                else
                                                {
                                                    imageView.setVisibility(View.VISIBLE);
                                                }
                                            }

                                            @Override
                                            public void afterTextChanged(Editable editable) {

                                            }
                                        });
                                        imageButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                editText.setText("");
                                            }
                                        });

                                    }
                                });
                                
                            }
                        });
                    }
                    else {
                        Toast.makeText(UserProductChoiceActivity.this, " "+allDataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    }
                });
    }
    public List<AllDataResponse.ProductData> check(CharSequence char1)
    {
        String query=char1.toString().toLowerCase();
        if (productDataList==null)
        {
            return new ArrayList<>();
        }
        else
        {
            List<AllDataResponse.ProductData> filteredList=new ArrayList<>();
            for (AllDataResponse.ProductData item:productDataList)
            {
                if (item.getProductName().toLowerCase().contains(query))
                {

                    filteredList.add(item);
                }
                else {

                }
            }
            return filteredList;
        }

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
                        Toast.makeText(UserProductChoiceActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(CartDetailsResponse cartDetailsResponse) {
                        if (cartDetailsResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE))
                        {
                            preferenceManager.setCArtID(VariableBag.cart_id,cartDetailsResponse.getMessage().get(0).getCartId().toString());
                            cartView();
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
                        cart_list =new ArrayList<>();
                        if (viewCartResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)) {
                            cart_list=viewCartResponse.getMessage();
                            if (cart_list!=null) {
                                int temp1=0;
                                for (ViewCartResponse.Message item:cart_list)
                                {
                                    temp1+=Integer.valueOf(item.getProductQuantity());
                                }
                                textView1.setVisibility(View.VISIBLE);
                                textView1.setText(String.valueOf(temp1));
                            }
                            else {
                                textView.setVisibility(View.GONE);
                            }
                        }

                    }
                });
    }
}