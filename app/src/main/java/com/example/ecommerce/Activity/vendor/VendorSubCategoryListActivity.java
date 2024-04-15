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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ecommerce.Activity.Fragments.SubDialogFragment;
import com.example.ecommerce.Activity.adapter.Vendorside.VendorSubCategoryAdapter;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.CategoryResponse.GetCategoryResponse;
import com.example.ecommerce.Responces.SubCategoryResponse.DeleteSubCategoryResponse;
import com.example.ecommerce.Responces.SubCategoryResponse.GetSubCategoryResponse;
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

public class VendorSubCategoryListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    PreferenceManager preferenceManager;
    VendorSubCategoryAdapter vendorSubCategoryAdapter;
    SubDialogFragment subDialogFragment;
    RestCall restCall;
    FloatingActionButton sub_add;

    GetCategoryResponse finalgetCategoryResponse;
    Spinner FilterCategory;
    GetSubCategoryResponse finalGetSubCategoryResponse;
    EditText SearchSub;
    List<GetCategoryResponse.Message> FilterList;
    ImageView imageView;
    List<GetSubCategoryResponse.Message> subCategory_search_list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_sub_category_list);

        recyclerView=findViewById(R.id.categoryrecycle_sub);
        FilterCategory=findViewById(R.id.Category_recycle);
        sub_add=findViewById(R.id.floating_btn_sub);
        restCall= RestClient.createService(RestCall.class, VariableBag.BASE_URL,VariableBag.API_KEY);
        preferenceManager=new PreferenceManager(this);
        getcategory();
        FilterList=new ArrayList<>();
        SearchSub=findViewById(R.id.SearchSubCategory);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        imageView=findViewById(R.id.notFound3);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy<0 && !sub_add.isShown())
                    sub_add.show();
                else if(dy>0 && sub_add.isShown())
                    sub_add.hide();
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        SearchSub.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                subCategory_search_list=new ArrayList<>();
                subCategory_search_list=check1(charSequence);
                if (vendorSubCategoryAdapter!=null) {
                    vendorSubCategoryAdapter.updateList(subCategory_search_list);
                    imageView.setVisibility(subCategory_search_list.isEmpty() ? View.VISIBLE:View.GONE);
                }
                else {
                    imageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        FilterCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String query = adapterView.getItemAtPosition(i).toString();
                if (!query.equals("select category")) {
                    if (FilterList != null) {
                        for (GetCategoryResponse.Message item : FilterList) {
                            if (item.getCategoryName().equalsIgnoreCase(query)) {
                                String category_id = item.getCategoryId();
                                VariableBag.category_id = item.getCategoryId();
                                getsubcategory(category_id);
                                break;
                            }
                        }
                    } else {
                        Toast.makeText(VendorSubCategoryListActivity.this, "Filter list is empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    recyclerView.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        finalgetCategoryResponse=new GetCategoryResponse();
        sub_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalgetCategoryResponse!=null) {
                    preferenceManager.setIsEdit(false);
                    subDialogFragment = new SubDialogFragment(finalgetCategoryResponse.getMessage());
                    subDialogFragment.show(getSupportFragmentManager(), "add_sub");



                }
            }
        });

    }

    public void getcategory()
    {

        restCall.getCategory("get_category",preferenceManager.getVendorID()).subscribeOn(Schedulers.io()).
                observeOn(Schedulers.newThread()).subscribe(new Subscriber<GetCategoryResponse>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(VendorSubCategoryListActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNext(GetCategoryResponse getCategoryResponse) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (getCategoryResponse.getStatus().equals(VariableBag.SUCCESS_CODE))
                                {

                                    finalgetCategoryResponse=getCategoryResponse;
                                    FilterList = getCategoryResponse.getMessage();

                                    List<String> MainList = new ArrayList<>();
                                    MainList.add("select category");
                                    for (GetCategoryResponse.Message item : FilterList) {
                                        MainList.add(item.getCategoryName());
                                    }

                                    ArrayAdapter<String> CategoryAdapter = new ArrayAdapter<>(VendorSubCategoryListActivity.this, R.layout.spinner_item, MainList);
                                    CategoryAdapter.setDropDownViewResource(R.layout.spinner_item);
                                    FilterCategory.setAdapter(CategoryAdapter);
                                }
                            }
                        });

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
                       Toast.makeText(getApplicationContext(), " "+e.getMessage(), Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onNext(GetSubCategoryResponse getSubCategoryResponse) {
                       if (getSubCategoryResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE))
                       {
                           List<GetSubCategoryResponse.Message> subCategories = getSubCategoryResponse.getMessage();
                           if (subCategories != null && !subCategories.isEmpty()) {
                               vendorSubCategoryAdapter = new VendorSubCategoryAdapter(VendorSubCategoryListActivity.this, getSubCategoryResponse.getMessage());
                               finalGetSubCategoryResponse=getSubCategoryResponse;
                               recyclerView.setAdapter(vendorSubCategoryAdapter);
                               vendorSubCategoryAdapter.setInterface(new VendorSubCategoryAdapter.EditSub() {
                                   @Override
                                   public void SubEdit(GetSubCategoryResponse.Message message) {
                                       preferenceManager.setIsEdit(true);
                                       preferenceManager.setSubCategoryId(VariableBag.sub_category_id, message.getSubCategoryId());
                                       subDialogFragment = new SubDialogFragment(message.getSubCategoryName());
                                       subDialogFragment.show(getSupportFragmentManager(), "edit_subCategory");

                                   }

                                   @Override
                                   public void SubDelete(GetSubCategoryResponse.Message message) {

                                       AlertDialog.Builder builder = new AlertDialog.Builder(VendorSubCategoryListActivity.this);
                                       builder.setMessage("Do you want to Delete ?");
                                       builder.setTitle("Delete Category !");
                                       builder.setCancelable(false);
                                       builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                                           deletesubcategory(message.getSubCategoryId());
                                           dialog.cancel();
                                       });
                                       builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                                           dialog.cancel();
                                       });
                                       AlertDialog alertDialog = builder.create();
                                       alertDialog.show();
                                   }
                               });
                           }else {
                               recyclerView.setAdapter(null);
                           }
                       }
                       else {
                           Toast.makeText(VendorSubCategoryListActivity.this, " "+getSubCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   }
               });

   }
   public void deletesubcategory(String id)
   {
       restCall.deleteSubCategory("delete_sub_category",preferenceManager.getVendorID(),VariableBag.category_id,id).subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<DeleteSubCategoryResponse>() {
                   @Override
                   public void onCompleted() {
                       
                   }

                   @Override
                   public void onError(Throwable e) {

                   }

                   @Override
                   public void onNext(DeleteSubCategoryResponse deleteSubCategoryResponse) {
                            if (deleteSubCategoryResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE))
                            {
                                Toast.makeText(VendorSubCategoryListActivity.this, "delete successfully !", Toast.LENGTH_SHORT).show();
                                getsubcategory(VariableBag.category_id);

                            }
                   }
               });
   }
    public List<GetSubCategoryResponse.Message> check1(CharSequence charSequence) {
        if (finalGetSubCategoryResponse == null || finalGetSubCategoryResponse.getMessage() == null) {
            return new ArrayList<>();
        } else {
            String query = charSequence.toString().toLowerCase(Locale.getDefault());
            List<GetSubCategoryResponse.Message> filteredList = new ArrayList<>();
            for (GetSubCategoryResponse.Message item : finalGetSubCategoryResponse.getMessage()) {
                if (item.getSubCategoryName().toLowerCase(Locale.getDefault()).contains(query)) {
                    filteredList.add(item);
                }

            }
            return filteredList;

        }
    }
}