package com.example.ecommerce.Activity.LauncherActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Activity.Fragments.GetMobileFragment;
import com.example.ecommerce.Activity.user.UserMainActivity;
import com.example.ecommerce.Activity.vendor.VendorMainActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.LauncherRespose.UserLoginResponse;
import com.example.ecommerce.Responces.LauncherRespose.UserRegisterResponse;
import com.example.ecommerce.Responces.LauncherRespose.VendorLoginResponse;
import com.example.ecommerce.network.RestCall;
import com.example.ecommerce.network.RestClient;
import com.example.ecommerce.utils.PreferenceManager;
import com.example.ecommerce.utils.VariableBag;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    Button register, login;
    EditText email;
    EditText password;
    RestCall restCall;
    TextView textView;
    CheckBox checkBox;


    PreferenceManager preferenceManager;
    GetMobileFragment getMobileFragment;

    ImageView image;
    GoogleSignInClient signInClient;
    GoogleSignInAccount account;
    int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_demo);
        register = findViewById(R.id.btn_register);
        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
        login = findViewById(R.id.btn_login);
        preferenceManager = new PreferenceManager(this);
        image = findViewById(R.id.vendor_image);
        textView = findViewById(R.id.login_text);
        Drawable mydrawable = getResources().getDrawable(R.drawable.marketing);
        restCall = RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);
        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this,gso);
        account=GoogleSignIn.getLastSignedInAccount(this);
        ImageView signInButton = findViewById(R.id.sign_in_button);
        if (account!=null)
        {
            String personName=account.getDisplayName();
            String personGivenName=account.getGivenName();
            String FamilyName=account.getFamilyName();
            String personEmail=account.getEmail();
            String password=account.getId();
            String requested= account.getRequestedScopes().toString();
            String granted=account.getGrantedScopes().toString();
            Uri photo=account.getPhotoUrl();
            Intent get=getIntent();

            if (get!=null && get.getStringExtra("user")!=null)
            {

                adduser(personName,personEmail,password,VariableBag.mobileNo);
                signInButton.setVisibility(View.GONE);
            }
            else {
                Toast.makeText(this, "vendor", Toast.LENGTH_SHORT).show();
            }

        }*/
  /*      signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });*/
        Intent get = getIntent();
        if (get != null && get.getStringExtra("user") != null) {
            textView.setText(get.getStringExtra("user"));
            image.setImageDrawable(mydrawable);
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (get != null && get.getStringExtra("user") != null) {
                    Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                    i.putExtra("user", "user_registration");
                    startActivity(i);
                } else {
                    Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                    i.putExtra("vendor", "vendor_registration");
                    startActivity(i);
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (get != null && get.getStringExtra("user") != null) {
                    loginUser();
                } else {
                    loginVendor();
                }
            }
        });
    }

    public void loginUser() {
        restCall.UserLogin("user_login", email.getText().toString(), password.getText().toString()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<UserLoginResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LoginActivity.this, " " + e, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(UserLoginResponse loginResponse) {
                        if (loginResponse.getStatus().equals(VariableBag.SUCCESS_CODE)) {
                            preferenceManager.setUserProduct(VariableBag.User_product_id, loginResponse.getData().get(0).toString());
                            Toast.makeText(LoginActivity.this, "login succesfully ! ", Toast.LENGTH_SHORT).show();
                            preferenceManager.loginUser(true);
                            startActivity(new Intent(LoginActivity.this, UserMainActivity.class));
                            finishAffinity();
                        } else {
                            Toast.makeText(LoginActivity.this, " " + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void loginVendor() {
        restCall.VendorLogin("vendor_login", email.getText().toString(), password.getText().toString()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<VendorLoginResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LoginActivity.this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(VendorLoginResponse vendorLoginResponse) {
                        if (vendorLoginResponse.getStatus().equals(VariableBag.SUCCESS_CODE)) {
                            preferenceManager.loginVendor(true);
                            preferenceManager.setVendorID(VariableBag.Vendor_id, vendorLoginResponse.getMessage().get(0).toString());
                            Toast.makeText(LoginActivity.this, "Login Successfully !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, VendorMainActivity.class));
                            finishAffinity();

                        } else {
                            Toast.makeText(LoginActivity.this, " " + vendorLoginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
/*
    public void signIn()
    {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            task.getResult().getEmail();
            adduser(task.getResult().getGivenName(),task.getResult().getEmail(),task.getResult().getId(),VariableBag.mobileNo);
        }
    }

*/


/*    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            GoogleSignInAccount account1=GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
            if (account1!=null)
            {
                String personName=account1.getDisplayName();
                String personGivenName=account1.getGivenName();
                String FamilyName=account1.getFamilyName();
                String personEmail=account1.getEmail();
                String password=account1.getId();
                String requested= account1.getRequestedScopes().toString();
                String granted=account1.getGrantedScopes().toString();
                Uri photo=account1.getPhotoUrl();
                Intent get=getIntent();

                if (get!=null && get.getStringExtra("user")!=null)
                {
                    Toast.makeText(this, "kevin", Toast.LENGTH_SHORT).show();
                }
                else {

                }


            }
            Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this,UserMainActivity.class));
        } catch (ApiException e) {
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
        }
    }*/
/*
    public void adduser(String name,String email,String password,String phone)
    {
        restCall.UserRegister("user_register",name,email,password,phone)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<UserRegisterResponse>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LoginActivity.this, "register1 "+e, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(UserRegisterResponse registerResponse) {
                        if(registerResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE))
                        {
*/
/*                            preferenceManager.loginUser(true);
                            preferenceManager.setUserProduct(VariableBag.User_product_id,registerResponse.getData().getUserId());*//*

                            Toast.makeText(LoginActivity.this, "Register successfully !", Toast.LENGTH_SHORT).show();
                            loginUser1(email,password);
                            finish();
                        }
                        else if (registerResponse.getMessage().equalsIgnoreCase("email already exist")){
                                    loginUser1(email,password);
                        }
                        else {
                            Toast.makeText(LoginActivity.this, " reg "+registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            loginUser1(email,password);

                        }

                    }
                });
    }
    public void loginUser1(String email,String password)
    {
        restCall.UserLogin("user_login",email,password).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<UserLoginResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("leonel","messi"+e);
                        Toast.makeText(LoginActivity.this, "login 1"+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(UserLoginResponse loginResponse) {
                        if (loginResponse.getStatus().equals(VariableBag.SUCCESS_CODE))
                        {
                            preferenceManager.setUserProduct(VariableBag.User_product_id,loginResponse.getData().get(0).toString());
                            preferenceManager.loginUser(true);
                            Toast.makeText(LoginActivity.this, "welcome ! ", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(LoginActivity.this, UserMainActivity.class));
                            finishAffinity();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, " log "+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}*/
