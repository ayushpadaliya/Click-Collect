package com.example.ecommerce.Activity.vendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ecommerce.Activity.Fragments.ProductsFragment;
import com.example.ecommerce.Activity.adapter.Vendorside.VendorProductAdapter;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.CategoryResponse.GetCategoryResponse;
import com.example.ecommerce.Responces.ProductResponse.DeleteProductResponse;
import com.example.ecommerce.Responces.ProductResponse.GetProductResponse;
import com.example.ecommerce.Responces.SubCategoryResponse.GetSubCategoryResponse;
import com.example.ecommerce.network.RestCall;
import com.example.ecommerce.network.RestClient;
import com.example.ecommerce.utils.PreferenceManager;
import com.example.ecommerce.utils.VariableBag;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ProductListActivity extends AppCompatActivity implements ProductsFragment.ProductUpdateListener {

    FloatingActionButton add;
    RestCall restCall;
    ProductsFragment productsFragment;
    LinearLayoutManager linearLayoutManager;
    PreferenceManager preferenceManager;
    List<GetCategoryResponse.Message> Category_data;
    List<GetCategoryResponse.Message> Category_Edit_Data;
    RecyclerView recyclerView;
    Spinner CategorySpinner,SubCategory;
    List<String> SubCategoryList;
    VendorProductAdapter vendorProductAdapter;
    List<GetSubCategoryResponse.Message> DataListSub;
    EditText SearchProduct;
    List<GetProductResponse.Message> finalMessage;
    ImageView imageView;
    List<GetProductResponse.Message> messageList;



    @Override
    protected void onResume() {
        super.onResume();
        getcategory();
        setupSubCategorySpinner();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        preferenceManager=new PreferenceManager(this);
        restCall= RestClient.createService(RestCall.class, VariableBag.BASE_URL,VariableBag.API_KEY);
        getcategory();
        add=findViewById(R.id.floating_btn_product);
        recyclerView=findViewById(R.id.productrecycle_sub);
        imageView=findViewById(R.id.notFound);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy<0 && !add.isShown())
                    add.show();
                else if(dy>0 && add.isShown())
                    add.hide();
                super.onScrolled(recyclerView, dx, dy);

            }
        });
        SearchProduct=findViewById(R.id.SearchProduct);
        CategorySpinner=findViewById(R.id.product_spinner);
        SubCategory=findViewById(R.id.product_spinner1);
        SubCategoryList=new ArrayList<>();
        SubCategoryList.add("select subcategory");
        ArrayAdapter<String> SubAdapter = new ArrayAdapter<>(ProductListActivity.this, R.layout.spinner_item, SubCategoryList);
        SubAdapter.setDropDownViewResource(R.layout.spinner_item);
        SubAdapter.notifyDataSetChanged();
        SubCategory.setAdapter(SubAdapter);
        setupSubCategorySpinner();
        SearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                messageList=new ArrayList<>();
                if (vendorProductAdapter!=null)
                {
                    messageList=check(charSequence);
                    vendorProductAdapter.updateList(messageList);
                    imageView.setVisibility(messageList.isEmpty() ? View.VISIBLE : View.GONE);


                }
                else {
                    imageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Category_data!=null) {
                    preferenceManager.setIsEdit(false);
                    productsFragment = new ProductsFragment(ProductListActivity.this,Category_data);
                    productsFragment.show(getSupportFragmentManager(), "add");
                }
            }
        });
        List<String> MainList = new ArrayList<>();
        MainList.add("select category");
        SubCategory.setSelection(0);

        if (Category_data!=null) {
            for (GetCategoryResponse.Message item : Category_data) {
                MainList.add(item.getCategoryName());
            }
        }
        ArrayAdapter<String> CategoryAdapter = new ArrayAdapter<>(ProductListActivity.this, R.layout.spinner_item, MainList);
        CategoryAdapter.setDropDownViewResource(R.layout.spinner_item);
        CategorySpinner.setAdapter(CategoryAdapter);
        CategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String query=adapterView.getItemAtPosition(i).toString();
                if (Category_data!=null)
                {
                    for (GetCategoryResponse.Message item:Category_data)
                    {
                        if(item.getCategoryName().equals(query))
                        {
                            VariableBag.category_id=item.getCategoryId();
                            getsubcategory(VariableBag.category_id);

                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
    public void getcategory()
    {
        restCall.getCategory("get_category",preferenceManager.getVendorID()).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetCategoryResponse>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ProductListActivity.this, " "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNext(GetCategoryResponse getCategoryResponse) {
                        if (getCategoryResponse.getStatus().equals(VariableBag.SUCCESS_CODE))
                        {
                            Category_data=getCategoryResponse.getMessage();
                            setupCategorySpinner();
                        }
                    }
                });
    }
 public void getProduct()
 {
     restCall.getProduct("get_product",preferenceManager.getVendorID(),VariableBag.category_id,VariableBag.sub_category_id)
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(new Subscriber<GetProductResponse>() {
                 @Override
                 public void onCompleted() {

                 }

                 @Override
                 public void onError(Throwable e) {

                 }

                 @Override
                 public void onNext(GetProductResponse getProductResponse) {
                            if (getProductResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE))
                            {
                                finalMessage=getProductResponse.getMessage();
                                vendorProductAdapter=new VendorProductAdapter(ProductListActivity.this,getProductResponse.getMessage());
                                recyclerView.setAdapter(vendorProductAdapter);
                                vendorProductAdapter.notifyDataSetChanged();
                                vendorProductAdapter.setProduct(new VendorProductAdapter.Product() {
                                    @Override
                                    public void EditProduct(GetProductResponse.Message message) {
                                        VariableBag.product_id=message.getProductId();
                                        preferenceManager.setIsEdit(true);
                                        productsFragment=new ProductsFragment(ProductListActivity.this,message.getProductName(),message.getProductPrice());
                                        productsFragment.show(getSupportFragmentManager(),"edit");
                                    }
                                    @Override
                                    public void DeleteProduct(GetProductResponse.Message message) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ProductListActivity.this);
                                        builder.setMessage("Do you want to Delete ?");
                                        builder.setTitle("Delete Category !");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                                            deleteProduct(message.getProductId());
                                            dialog.cancel();
                                        });
                                        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                                            dialog.cancel();
                                        });
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                    }
                                });
                            }

                 }
             });
 }



    public void getsubcategory(String category_id)
    {
        restCall.getSubCategory("get_sub_category",preferenceManager.getVendorID(),category_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetSubCategoryResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ProductListActivity.this, "hello "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("hello"," "+e.getMessage());
                    }

                    @Override
                    public void onNext(GetSubCategoryResponse getSubCategoryResponse) {
                        DataListSub=new ArrayList<>();

                        if (getSubCategoryResponse.getStatus().equals(VariableBag.SUCCESS_CODE))
                        {
                            DataListSub=getSubCategoryResponse.getMessage();
                            setupSubCategorySpinner();
                        }

                    }
                });

    }
    public void setupCategorySpinner()
    {
        List<String> MainList = new ArrayList<>();
        MainList.add("select category");
        SubCategory.setSelection(0);

        if (Category_data!=null) {
            for (GetCategoryResponse.Message item : Category_data) {
                MainList.add(item.getCategoryName());
            }
        }
        ArrayAdapter<String> CategoryAdapter = new ArrayAdapter<>(ProductListActivity.this, R.layout.spinner_item, MainList);
        CategoryAdapter.setDropDownViewResource(R.layout.spinner_item);
        CategorySpinner.setAdapter(CategoryAdapter);
        CategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String query=adapterView.getItemAtPosition(i).toString();
                if (Category_data!=null)
                {
                    for (GetCategoryResponse.Message item:Category_data)
                    {
                        if(item.getCategoryName().equals(query))
                        {
                            VariableBag.category_id=item.getCategoryId();
                            getsubcategory(VariableBag.category_id);

                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void setupSubCategorySpinner()
    {
        SubCategoryList=new ArrayList<>();
        SubCategoryList.add("select subcategory");
        if (DataListSub!=null) {
            for (GetSubCategoryResponse.Message item : DataListSub) {
                SubCategoryList.add(item.getSubCategoryName());
            }

            ArrayAdapter<String> SubAdapter = new ArrayAdapter<>(ProductListActivity.this, R.layout.spinner_item, SubCategoryList);
            SubAdapter.setDropDownViewResource(R.layout.spinner_item);
            SubAdapter.notifyDataSetChanged();
            SubCategory.setAdapter(SubAdapter);
            SubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String Query=adapterView.getItemAtPosition(i).toString();
                    if (DataListSub!=null)
                    {
                        for (GetSubCategoryResponse.Message item:DataListSub)
                        {
                            if(item.getSubCategoryName().equals(Query))
                            {
                                VariableBag.sub_category_id=item.getSubCategoryId();
                                getProduct();
                            }
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }
    }
    public void deleteProduct(String id)
    {
        restCall.deleteProduct("delete_product",preferenceManager.getVendorID(),VariableBag.category_id,VariableBag.sub_category_id,id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<DeleteProductResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ProductListActivity.this, " "+e, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(DeleteProductResponse deleteProductResponse) {
                            if (deleteProductResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE))
                            {
                                Toast.makeText(ProductListActivity.this, "Delete successfully !", Toast.LENGTH_SHORT).show();
                                getProduct();
                            }
                            else {
                                Toast.makeText(ProductListActivity.this, " "+deleteProductResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    }
                });
    }
    public List<GetProductResponse.Message> check(CharSequence char1)
    {
        String query=char1.toString().toLowerCase();
        if (finalMessage==null)
        {
            return new ArrayList<>();
        }
        else
        {
            List<GetProductResponse.Message> filteredList=new ArrayList<>();
            for (GetProductResponse.Message item:finalMessage)
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

    @Override
    public void onUpdate() {
        getProduct();
    }
}