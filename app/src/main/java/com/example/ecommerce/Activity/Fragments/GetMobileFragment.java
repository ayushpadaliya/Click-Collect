package com.example.ecommerce.Activity.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ecommerce.R;
import com.example.ecommerce.utils.VariableBag;

public class GetMobileFragment extends DialogFragment {

EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_get_mobile, container, false);
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = alertDialog.create();
        editText=view.findViewById(R.id.numberEdit);
        Button mobile=view.findViewById(R.id.mobileBtn);
        dialog.setCanceledOnTouchOutside(false);
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString()!=null && isValidPhone(editText.getText().toString()))
                {
                    VariableBag.mobileNo=editText.getText().toString();
                    dismiss();
                }
                else {
                    editText.requestFocus();
                    editText.setError("Enter valid mobileNo");
                }
            }
        });




        return  view;

    }
    private boolean isValidPhone(String phone) {
        String passwordRegex = "^\\+(?:[0-9] ?){6,14}[0-9]$";
        return phone.matches(passwordRegex);
    }
}