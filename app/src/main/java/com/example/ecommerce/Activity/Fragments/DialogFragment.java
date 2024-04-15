package com.example.ecommerce.Activity.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ecommerce.Activity.vendor.VendorCategoryList;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.CategoryResponse.EditCategoryResponse;
import com.example.ecommerce.network.RestCall;
import com.example.ecommerce.network.RestClient;
import com.example.ecommerce.utils.PreferenceManager;
import com.example.ecommerce.utils.VariableBag;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class DialogFragment extends androidx.fragment.app.DialogFragment {

    EditText txt_inputText;
    Button btn_cancel,btn_okay;
    TextView textView;
    RestCall restCall;
    PreferenceManager preferenceManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =  inflater.inflate(R.layout.customdialog,container,false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog alertDialog = builder.create();
        textView=view.findViewById(R.id.text3);
        preferenceManager=new PreferenceManager(getContext());
        txt_inputText = view.findViewById(R.id.txt_input);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        restCall= RestClient.createService(RestCall.class, VariableBag.BASE_URL,VariableBag.API_KEY);
        btn_okay = view.findViewById(R.id.btn_okay);
        builder.setView(view);
        alertDialog.setCanceledOnTouchOutside(false);
        if (preferenceManager.getIsEdit()) {
            textView.setText(R.string.edit_category);
            txt_inputText.setText(VariableBag.Edit_Category_name);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            btn_okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VariableBag.category_name = txt_inputText.getText().toString();
                    editcategory(); // Call editCategory() instead of addCategory()
                    dismissAllowingStateLoss();
                    ((VendorCategoryList) requireActivity()).getcategory();

                }
            });
        } else {
            // This part is for adding a new category
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            btn_okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VariableBag.category_name = txt_inputText.getText().toString();
                    ((VendorCategoryList) requireActivity()).addCategory();
                    ((VendorCategoryList) requireActivity()).getcategory();
                    dismiss();

                }
            });
        }
        return view;

    }
    public void editcategory() {
        restCall.editCategory("edit_category", preferenceManager.getVendorID(), VariableBag.category_id, VariableBag.category_name).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread()).subscribe(new Subscriber<EditCategoryResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(EditCategoryResponse editCategoryResponse) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (editCategoryResponse.getStatus().equals(VariableBag.SUCCESS_CODE)) {
                                    Toast.makeText(getContext(), "edit successfully !", Toast.LENGTH_SHORT).show();
                                    ((VendorCategoryList) getActivity()).getcategory();

                                } else {
                                    Toast.makeText(getContext(), " " + editCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
    }
}
