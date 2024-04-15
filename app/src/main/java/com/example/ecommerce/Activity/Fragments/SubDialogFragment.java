package com.example.ecommerce.Activity.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ecommerce.Activity.vendor.VendorSubCategoryListActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.SubCategoryResponse.AddSubCategoryResponse;
import com.example.ecommerce.Responces.CategoryResponse.GetCategoryResponse;
import com.example.ecommerce.Responces.SubCategoryResponse.EditSubCategoryResponse;
import com.example.ecommerce.network.RestCall;
import com.example.ecommerce.network.RestClient;
import com.example.ecommerce.utils.PreferenceManager;
import com.example.ecommerce.utils.VariableBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SubDialogFragment extends DialogFragment {

    Spinner spinner;
    TextView textView;
    EditText editText;
    Button cancel,submit;
    Context context;
    List<GetCategoryResponse.Message> Data;
    PreferenceManager preferenceManager;
    public SubDialogFragment(String editName) {
        EditName = editName;
    }
    String EditName;
    RestCall restCall;
    public SubDialogFragment(List<GetCategoryResponse.Message> data) {
        Data = data;
    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.subdialog,container,false);
        spinner=view.findViewById(R.id.spinner_category);
        restCall= RestClient.createService(RestCall.class,VariableBag.BASE_URL,VariableBag.API_KEY);
        editText=view.findViewById(R.id.sub_txt_input);
        editText.setVisibility(View.INVISIBLE);
        preferenceManager=new PreferenceManager(getDialog().getContext());
        textView=view.findViewById(R.id.sub_text);
        cancel=view.findViewById(R.id.sub_btn_cancel);
        submit=view.findViewById(R.id.sub_btn_okay);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog alertDialog = builder.create();

        if (preferenceManager.getIsEdit()) {
            editText.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            editText.setText(EditName);
            textView.setText("Edit SubCategory");
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editsubcatgeory(editText.getText().toString());
                    ((VendorSubCategoryListActivity) requireActivity()).getsubcategory(VariableBag.category_id);
                    dismiss();

                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        } else {
            setupSpinner();
            builder.setView(view);
            alertDialog.setCanceledOnTouchOutside(false);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addsubcatgeory();
                    dismiss();
                    ((VendorSubCategoryListActivity ) requireActivity()).getsubcategory(VariableBag.category_id);

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

    private void setupSpinner() {
        List<String> adapterData = new ArrayList<>();
        adapterData.add("select category");
        for (GetCategoryResponse.Message item : Data) {
            adapterData.add(item.getCategoryName());
        }
        ArrayAdapter<String> CategorySpinnerAdapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, adapterData);
        CategorySpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(CategorySpinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String categoryName = adapterView.getItemAtPosition(i).toString();
                if (!categoryName.equals("select category")) {
                    editText.setVisibility(View.VISIBLE);
                    for (GetCategoryResponse.Message item : Data) {
                        if (item.getCategoryName().equalsIgnoreCase(categoryName)) {
                            VariableBag.category_id = item.getCategoryId().toString();
                            break;
                        }
                    }
                } else {
                    editText.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void addsubcatgeory()
    {
        restCall.addSubCategory("add_sub_category",preferenceManager.getVendorID(),VariableBag.category_id,editText.getText().toString())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<AddSubCategoryResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d("add error"," "+e.getMessage());

                    }

                    @Override
                    public void onNext(AddSubCategoryResponse addSubCategoryResponse) {
                            if (addSubCategoryResponse.getStatus().equals(VariableBag.SUCCESS_CODE))
                            {
                                ((VendorSubCategoryListActivity) requireActivity()).getsubcategory(VariableBag.category_id);
                            }
                            else
                            {
                                Toast.makeText(getContext(), " "+addSubCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    }
                });
    }
    public void editsubcatgeory(String editName)
    {
        restCall.editSubCategory("edit_sub_category",preferenceManager.getVendorID(),VariableBag.category_id,preferenceManager.getSubCategoryID(),editName).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<EditSubCategoryResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(EditSubCategoryResponse editSubCategoryResponse) {
                        if (editSubCategoryResponse.getStatus().equals(VariableBag.SUCCESS_CODE)) {
                            Toast.makeText(getContext(), "Edit succesfully", Toast.LENGTH_SHORT).show();
                            ((VendorSubCategoryListActivity) requireActivity()).getsubcategory(VariableBag.category_id);
                        }
                        else
                        {
                            Log.d("Message"," "+editSubCategoryResponse.getMessage());

                        }
                    }
                });
    }
}
