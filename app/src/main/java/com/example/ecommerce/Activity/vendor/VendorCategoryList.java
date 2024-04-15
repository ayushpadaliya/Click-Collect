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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.Activity.Fragments.DialogFragment;
import com.example.ecommerce.Activity.adapter.Vendorside.VendorCategoryAdapter;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.CategoryResponse.AddCategoryResponse;
import com.example.ecommerce.Responces.CategoryResponse.DeleteCategoryResponse;
import com.example.ecommerce.Responces.CategoryResponse.GetCategoryResponse;
import com.example.ecommerce.network.RestCall;
import com.example.ecommerce.network.RestClient;
import com.example.ecommerce.utils.PreferenceManager;
import com.example.ecommerce.utils.VariableBag;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VendorCategoryList extends AppCompatActivity {

    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    RestCall restCall;
    String text;
    VendorCategoryAdapter vendorCategoryAdapter;
    VendorCategoryAdapter vendorCategoryAdapter2;
    PreferenceManager preferenceManager;
    FloatingActionButton floatingActionButton;
    GetCategoryResponse finalGetCategoryResponse;
    List<String> Data;
    DialogFragment dialogFragment;
    EditText Search;
    ImageView imageView;
    List<GetCategoryResponse.Message> Category_Search_List;

    @Override
    protected void onResume() {
        super.onResume();
        getcategory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_add_category);
        preferenceManager = new PreferenceManager(this);
        restCall = RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.categoryrecycle);
        recyclerView.setLayoutManager(linearLayoutManager);
        Search = findViewById(R.id.SearchCategory);
        imageView=findViewById(R.id.notFound1);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy<0 && !floatingActionButton.isShown())
                    floatingActionButton.show();
                else if(dy>0 && floatingActionButton.isShown())
                    floatingActionButton.hide();
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Category_Search_List=new ArrayList<>();
                Category_Search_List=check(charSequence);
                if (vendorCategoryAdapter!=null) {
                    vendorCategoryAdapter.updateList(Category_Search_List);
                    imageView.setVisibility(Category_Search_List.isEmpty() ? View.VISIBLE : View.GONE);
                }
                else {
                    imageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        finalGetCategoryResponse = new GetCategoryResponse();
        floatingActionButton = findViewById(R.id.floating_btn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferenceManager.setIsEdit(false);
                dialogFragment = new DialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "add");
            }
        });
    }


    public void getcategory() {
        restCall.getCategory("get_category", preferenceManager.getVendorID()).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetCategoryResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(VendorCategoryList.this, " " + e, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(GetCategoryResponse getCategoryResponse) {
                        if (getCategoryResponse.getStatus().equals(VariableBag.SUCCESS_CODE)) {
                            finalGetCategoryResponse = getCategoryResponse;
                            Data=new ArrayList<>();

                            vendorCategoryAdapter = new VendorCategoryAdapter(VendorCategoryList.this, getCategoryResponse.getMessage());

                            recyclerView.setAdapter(vendorCategoryAdapter);
                            vendorCategoryAdapter.setEdit(new VendorCategoryAdapter.EditInterface() {
                                @Override
                                public void edit(GetCategoryResponse.Message message) {
                                    preferenceManager.setIsEdit(true);
                                    VariableBag.Edit_Category_name = message.getCategoryName();
                                    VariableBag.category_id = message.getCategoryId();
                                    dialogFragment = new DialogFragment();
                                    dialogFragment.show(getSupportFragmentManager(), "edit");
                                }

                                @Override
                                public void delete(GetCategoryResponse.Message message) {
                                    VariableBag.category_id = message.getCategoryId();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(VendorCategoryList.this);
                                    builder.setMessage("Do you want to Delete ?");
                                    builder.setTitle("Delete Category !");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                                        deletecategory(message.getCategoryId());
                                        dialog.cancel();
                                    });
                                    builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                                        dialog.cancel();
                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();

                                }
                            });
                        } else {
                            Toast.makeText(VendorCategoryList.this, " " + getCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void addCategory() {
        restCall.AddCategory("add_category", VariableBag.category_name, preferenceManager.getVendorID()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<AddCategoryResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(VendorCategoryList.this, " " + e, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(AddCategoryResponse addCategoryResponse) {
                        if (addCategoryResponse.getStatus().equals(VariableBag.SUCCESS_CODE)) {
                            Toast.makeText(VendorCategoryList.this, "category added sucesfully", Toast.LENGTH_SHORT).show();
                            getcategory();
                            finish();
                        } else {
                            Toast.makeText(VendorCategoryList.this, " " + addCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void deletecategory(String category_id) {
        restCall.deleteCategory("delete_category", preferenceManager.getVendorID(), category_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<DeleteCategoryResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(VendorCategoryList.this, " " + e, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(DeleteCategoryResponse deleteCategoryResponse) {
                        if (deleteCategoryResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)) {
                            Toast.makeText(VendorCategoryList.this, "deleted successfully", Toast.LENGTH_SHORT).show();
                            getcategory();
                        } else {

                            Toast.makeText(VendorCategoryList.this, " " + deleteCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public List<GetCategoryResponse.Message> check(CharSequence charSequence) {
        if (finalGetCategoryResponse == null || finalGetCategoryResponse.getMessage() == null) {
            return new ArrayList<>();
        } else {
            String query = charSequence.toString().toLowerCase(Locale.getDefault());
            List<GetCategoryResponse.Message> filteredList = new ArrayList<>();
            for (GetCategoryResponse.Message item : finalGetCategoryResponse.getMessage()) {
                if (item.getCategoryName().toLowerCase(Locale.getDefault()).contains(query)) {
                    filteredList.add(item);
                }

            }
            return filteredList;

        }
    }
}