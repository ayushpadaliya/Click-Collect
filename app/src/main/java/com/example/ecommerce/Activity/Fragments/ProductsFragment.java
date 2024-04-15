package com.example.ecommerce.Activity.Fragments;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ecommerce.Activity.vendor.ProductListActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.CategoryResponse.GetCategoryResponse;
import com.example.ecommerce.Responces.ProductResponse.AddProductResponse;
import com.example.ecommerce.Responces.ProductResponse.EditProductResponse;
import com.example.ecommerce.Responces.SubCategoryResponse.GetSubCategoryResponse;
import com.example.ecommerce.network.RestCall;
import com.example.ecommerce.network.RestClient;
import com.example.ecommerce.utils.PreferenceManager;
import com.example.ecommerce.utils.VariableBag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ProductsFragment extends DialogFragment {
    EditText product_name,product_prize;
    Button submit,cancel,camera;
    RestCall restCall;
    PreferenceManager preferenceManager;
    Spinner setCategory,setSubCategory;
    List<GetCategoryResponse.Message> Category_data;
    List<String> SubCategory_data;
    List<GetSubCategoryResponse.Message> message;
    Context mcontext;
    TextView text;
    String EditName;
    File file;
    String EditPrice;
    ImageView imageView;
    ProductUpdateListener productUpdateListener;

    private static final int cameraRequest=12;


    public ProductsFragment(Context context,String editName,String editPrice) {
        mcontext=context;
        EditName = editName;
        EditPrice=editPrice;
    }

    public ProductsFragment(Context context, List<GetCategoryResponse.Message> category_data) {
        mcontext=context;
        Category_data = category_data;
    }
    public interface ProductUpdateListener {
        void onUpdate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==cameraRequest)
        {
            Bitmap bitmap=(Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            String directory= String.valueOf(mcontext.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "click" + timeStamp + ".jpg";
            file=new File(directory,imageFileName);
            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream);
                outputStream.flush();
                outputStream.close();
                Log.d("path"," "+file.getAbsolutePath());

            } catch (IOException e) {
                Toast.makeText(mcontext, "catch "+e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.productdailog,container,false);
        preferenceManager=new PreferenceManager(getContext());
        restCall= RestClient.createService(RestCall.class, VariableBag.BASE_URL,VariableBag.API_KEY);
        imageView=view.findViewById(R.id.imageput);
        product_name=view.findViewById(R.id.product_txt_input);
        product_prize=view.findViewById(R.id.product_prize_input);
        camera=view.findViewById(R.id.takeImage);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,cameraRequest);
            }
        });
        submit=view.findViewById(R.id.product_sub_btn_okay);
        cancel=view.findViewById(R.id.product_btn_cancel);
        text=view.findViewById(R.id.product_text);
        setCategory=view.findViewById(R.id.spinner_product_category);
        setSubCategory=view.findViewById(R.id.spinner_product_sub);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        SubCategory_data=new ArrayList<>();
        SubCategory_data.add("Select SubCategory ");
        ArrayAdapter<String> SubCategorySpinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, SubCategory_data);
        SubCategorySpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        setSubCategory.setAdapter(SubCategorySpinnerAdapter);
        SubCategorySpinnerAdapter.notifyDataSetChanged();
        if (preferenceManager.getIsEdit()) {
            product_name.setVisibility(View.VISIBLE);
            product_prize.setVisibility(View.VISIBLE);
            setCategory.setVisibility(View.GONE);
            setSubCategory.setVisibility(View.GONE);
            product_name.setText(EditName);
            product_prize.setText(EditPrice);
            text.setText("Edit SubCategory");
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editProduct(product_name.getText().toString(),product_prize.getText().toString());
                    dismiss();
                    ((ProductListActivity) requireActivity()).getProduct();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
        else {
            setupSpinner();
            builder.setView(view);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addProduct(VariableBag.sub_category_id,file);
                    dismiss();
                    ((ProductListActivity) requireActivity()).getProduct();

                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
        return view;
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

                        Toast.makeText(mcontext, " "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(GetSubCategoryResponse getSubCategoryResponse) {
                        if (getSubCategoryResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE))
                        {
                            message=getSubCategoryResponse.getMessage();

                            for (GetSubCategoryResponse.Message item:getSubCategoryResponse.getMessage())
                            {
                                SubCategory_data.add(item.getSubCategoryName());
                            }
                            ArrayAdapter<String> SubCategorySpinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, SubCategory_data);
                            SubCategorySpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
                            setSubCategory.setAdapter(SubCategorySpinnerAdapter);
                            SubCategorySpinnerAdapter.notifyDataSetChanged();

                        }
                    }
                });

    }
    private void setupSpinner() {
        List<String> adapterData = new ArrayList<>();
        adapterData.add("select category");

        for (GetCategoryResponse.Message item : Category_data) {
            adapterData.add(item.getCategoryName());
        }
        ArrayAdapter<String> CategorySpinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, adapterData);


        CategorySpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        setCategory.setAdapter(CategorySpinnerAdapter);
        setCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String categoryName = adapterView.getItemAtPosition(i).toString();
                if (!categoryName.equals("select category")) {
                    for (GetCategoryResponse.Message item : Category_data) {
                        if (item.getCategoryName().equalsIgnoreCase(categoryName)) {
                            VariableBag.category_id = item.getCategoryId().toString();
                            SubCategory_data=new ArrayList<>();
                            SubCategory_data.add("Select SubCategory ");
                            setSubCategory.setSelection(0);
                            getsubcategory(VariableBag.category_id);
                            break;
                        }
                    }
                } else {
                    product_name.setVisibility(View.GONE);
                    product_prize.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        setSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String subCategory=adapterView.getItemAtPosition(i).toString();
                if (!subCategory.equals("select category")) {
                    if (message!=null) {
                        for (GetSubCategoryResponse.Message item : message) {
                            if (item.getSubCategoryName().equalsIgnoreCase(subCategory)) {
                                VariableBag.sub_category_id = item.getSubCategoryId().toString();
                                product_name.setVisibility(View.VISIBLE);
                                product_prize.setVisibility(View.VISIBLE);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    public void addProduct(String sub_id,File file)
    {
        Toast.makeText(mcontext, "hello" , Toast.LENGTH_SHORT).show();
        RequestBody tag=RequestBody.create(MediaType.parse("text/plain"),"add_product");
        RequestBody v_id=RequestBody.create(MediaType.parse("text/plain"),preferenceManager.getVendorID());
        RequestBody sub_cat_id=RequestBody.create(MediaType.parse("text/plain"),sub_id);
        RequestBody cat_id=RequestBody.create(MediaType.parse("text/plain"),VariableBag.category_id);
        RequestBody p_name=RequestBody.create(MediaType.parse("text/plain"),product_name.getText().toString());
        RequestBody p_price=RequestBody.create(MediaType.parse("text/plain"),product_prize.getText().toString());
        MultipartBody.Part p_img=MultipartBody.Part.createFormData("file_path", file.getName(),RequestBody.create(MediaType.parse("image/*"),file));
        restCall.addProduct(tag,v_id,cat_id,sub_cat_id,p_name,p_price,p_img)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddProductResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("storage"," "+e.getMessage());
                        Toast.makeText(mcontext, " "+e
                                .getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(AddProductResponse addProductResponse) {
                        if (addProductResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE))
                        {
                            ((ProductListActivity) mcontext).getProduct();
                            Toast.makeText(mcontext, "success", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(mcontext, " "+addProductResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void editProduct(String editName,String editPrice)
    {
        restCall.editProduct("edit_product",preferenceManager.getVendorID(),VariableBag.category_id,VariableBag.sub_category_id,VariableBag.product_id,editName.toLowerCase(),editPrice.toLowerCase()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<EditProductResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(EditProductResponse editProductResponse) {
                            if (editProductResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE))
                            {
                                    productUpdateListener.onUpdate();
                            }
                            else {
                                Toast.makeText(mcontext, " "+editProductResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    }
                });
    }

    }

