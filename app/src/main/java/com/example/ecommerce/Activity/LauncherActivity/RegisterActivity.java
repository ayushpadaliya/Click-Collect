package com.example.ecommerce.Activity.LauncherActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.Responces.LauncherRespose.UserRegisterResponse;
import com.example.ecommerce.Responces.LauncherRespose.VendorRegisterResponse;
import com.example.ecommerce.network.RestCall;
import com.example.ecommerce.network.RestClient;
import com.example.ecommerce.utils.VariableBag;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {

    Button register;
    EditText name,email,password,cpassword,phone;
    RestCall restCall;
    TextView textView;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=findViewById(R.id.UserName);
        email=findViewById(R.id.email);
        password=findViewById(R.id.Password);
        cpassword=findViewById(R.id.cpassword);
        phone=findViewById(R.id.phone);
        image=findViewById(R.id.vendor_image);
        textView=findViewById(R.id.TextRegistration);
        Intent get_intent=getIntent();
        restCall= RestClient.createService(RestCall.class, VariableBag.BASE_URL,VariableBag.API_KEY);
        register=findViewById(R.id.register_create);
        Drawable draw= getResources().getDrawable(R.drawable.marketing);
        if (get_intent!=null && get_intent.getStringExtra("user")!=null){
                    textView.setText(R.string.user_registration);
                    image.setImageDrawable(draw);
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username_str=name.getText().toString();
                String email_str=email.getText().toString();
                String password_str=password.getText().toString();
                String cpassword_str=cpassword.getText().toString();
                String phone_str=phone.getText().toString();
                if ( username_str.equals("") || email_str.equals("") || password_str.equals("") ||  cpassword_str.equals("") || phone_str.equals(""))
                {
//                    Toast.makeText(Registration.this, "fill the blank ", Toast.LENGTH_SHORT).show();
                    if (username_str.equals("")) {
                        name.requestFocus();
                        name.setError("fill username");
                    }

                    else if(email_str.equals("")){
                        email.requestFocus();
                        email.setError("fill email");
                    }
                    else if(password_str.equals("")){
                        password.requestFocus();
                        password.setError("fill password");
                    }
                    else if(cpassword_str.equals("")){
                        cpassword.requestFocus();
                        cpassword.setError("fill confirm password");
                    }
                    else {
                        phone.requestFocus();
                        phone.setError("fill phone number");
                    }
                }
                else if(!isValidEmail(email_str))
                {
                    email.requestFocus();
                    email.setError("Enter Valid Email");
                }
                else if(!isValidPassword(password_str))
                {
                    password.requestFocus();
                    password.setError("Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character:");
                }
                else if (!password_str.equals(cpassword_str)) {
                    cpassword.requestFocus();
                    cpassword.setError("Password doesn't match");
                }
                else if (get_intent!=null && get_intent.getStringExtra("user")!=null) {
                    adduser();
                }else {
                    addVendor();
                }
            }
        });

    }
    private boolean isValidPassword(String passwordField) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return passwordField.matches(passwordRegex);
    }
    private boolean isValidEmail(String emailField) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        return emailField.matches(emailRegex);
    }

    public void adduser()
    {
        restCall.UserRegister("user_register",name.getText().toString(),email.getText().toString(),password.getText().toString(),phone.getText().toString())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<UserRegisterResponse>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RegisterActivity.this, " "+e, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(UserRegisterResponse registerResponse) {
                        if(registerResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE))
                        {/*
                            Toast.makeText(RegisterActivity.this, "reached", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
                            Log.d("RegisterActivity", "Intent created");
                            startActivity(i);*/
                            Toast.makeText(RegisterActivity.this, "Register successfully !", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, " "+registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    public void addVendor()
    {
        restCall.VendorRegister("vendor_register",name.getText().toString(),email.getText().toString(),password.getText().toString(),phone.getText().toString()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<VendorRegisterResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RegisterActivity.this, " "+e, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(VendorRegisterResponse vendorRegisterResponse) {
                        if (vendorRegisterResponse.getStatus().equals(VariableBag.SUCCESS_CODE))
                        {
                            Toast.makeText(RegisterActivity.this, "Register successfully !", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, " "+vendorRegisterResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}