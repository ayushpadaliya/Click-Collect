package com.example.ecommerce.Activity.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.ecommerce.Activity.LauncherActivity.SelectTypeActivity;
import com.example.ecommerce.Activity.adapter.UserSide.HomeViewAdapter;
import com.example.ecommerce.Activity.user.FaceRecognitionActivity;
import com.example.ecommerce.Activity.user.ReminderTaskActivity;
import com.example.ecommerce.Activity.user.ScanQrActivity;
import com.example.ecommerce.Activity.user.ServiceActivity;
import com.example.ecommerce.Activity.user.UserCardViewActivity;
import com.example.ecommerce.Activity.vendor.VendorMainActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.Responces.LauncherRespose.VendorGetData;
import com.example.ecommerce.network.RestCall;
import com.example.ecommerce.network.RestClient;
import com.example.ecommerce.utils.PreferenceManager;
import com.example.ecommerce.utils.VariableBag;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserHomeFragment extends Fragment {

RecyclerView recyclerView;
GridLayoutManager gridLayoutManager;
HomeViewAdapter homeViewAdapter;
DrawerLayout drawerLayout;
NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;
EditText editText;
RestCall restCall;
ImageView shift;
ImageButton imageButton;
GoogleSignInClient signInClient;
ImageView imageView_logout,not_found,searchHome;
VendorGetData finalvendorGetData;
GoogleSignInAccount account;
    String  personGivenName;
    Uri photo;
    com.google.android.material.appbar.AppBarLayout barLayout;
PreferenceManager preferenceManager;
    public UserHomeFragment() {

    }
    @Override
    public void onResume() {
        super.onResume();
        getVendorData();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.fragment_user_home, container, false);
        recyclerView=rootview.findViewById(R.id.home_recycle);
        preferenceManager=new PreferenceManager(getContext());
        drawerLayout=rootview.findViewById(R.id.drawerLayout);
        navigationView=rootview.findViewById(R.id.navigationView);
        toolbar=rootview.findViewById(R.id.tool);
        imageView_logout=rootview.findViewById(R.id.user_log1);
        imageButton=rootview.findViewById(R.id.cancel_button);
        not_found=rootview.findViewById(R.id.found1);
        searchHome=rootview.findViewById(R.id.homeSearchFragment);
//        shift=rootview.findViewById(R.id.img1);
        barLayout=rootview.findViewById(R.id.barHome);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.open_navigation_drawer,R.string.close_navigation_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        toolbar.setNavigationIcon(R.drawable.navigation);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id=menuItem.getItemId();
                if (id==R.id.notification_feature)
                {
                    startActivity(new Intent(getContext(), ReminderTaskActivity.class));
                } else if (id==R.id.Event_feature) {
                    startActivity(new Intent(getContext(), ServiceActivity.class));
                } else if (id==R.id.scan_feature) {
                    startActivity(new Intent(getContext(), ScanQrActivity.class));
                } else if (id==R.id.face_recognition) {
                    startActivity(new Intent(getContext(), FaceRecognitionActivity.class));
                }
                return true;
            }
        });
        getActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (editText.getVisibility()==View.VISIBLE)
                {
                    editText.setVisibility(View.GONE);
                    barLayout.setVisibility(View.VISIBLE);
                }
                else {
                    getActivity().finish();
                }
            }
        });
//        shift.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), VendorMainActivity.class));
//            }
//        });
        searchHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barLayout.setVisibility(View.GONE);
                editText.setVisibility(View.VISIBLE);
            }
        });

        imageView_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("ARE YOU SURE YOU WANT TO LOGOUT ?");
                builder.setTitle("Exit");
                Drawable mydrawable = getResources().getDrawable(R.drawable.icon);
                builder.setIcon(mydrawable);
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    preferenceManager.loginUser(false);
                    Toast.makeText(getActivity(), " "+preferenceManager.getLoginUser(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), SelectTypeActivity.class));
//                    signUp();
                    getActivity().finish();

                });
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }


        });
        gridLayoutManager=new GridLayoutManager(getContext(),2);
        editText=rootview.findViewById(R.id.search);
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
                if (homeViewAdapter!=null){

                    homeViewAdapter.updateList(check(charSequence));
                    not_found.setVisibility(check(charSequence).isEmpty()? View.VISIBLE:View.GONE);
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
        recyclerView.setLayoutManager(gridLayoutManager);
        restCall= RestClient.createService(RestCall.class, VariableBag.BASE_URL,VariableBag.API_KEY);

        return rootview;
    }

    public void getVendorData()
    {
        restCall.VendorGetData("get_vendor_data").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<VendorGetData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), " "+e, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(VendorGetData vendorGetData) {
                        if (vendorGetData.getStatus().equals(VariableBag.SUCCESS_CODE))
                        {
                            finalvendorGetData=vendorGetData;
                            homeViewAdapter=new HomeViewAdapter(getContext(),finalvendorGetData.getData());
                            recyclerView.setAdapter(homeViewAdapter);
                        }
                        else
                        {
                            Toast.makeText(getContext(), " "+vendorGetData.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }
    private List<VendorGetData.Data> check(CharSequence charSequence) {
        if (finalvendorGetData == null || finalvendorGetData.getData() == null) {
            return new ArrayList<>(); // Return an empty list if vendorGetData or its data is null
        }else {
            String query = charSequence.toString().toLowerCase(Locale.getDefault());
            List<VendorGetData.Data> filteredList = new ArrayList<>();
            for (VendorGetData.Data item : finalvendorGetData.getData()) {
                if (item.getVendorName().toLowerCase().contains(query)) {
                    filteredList.add(item);
                }
            }
            return filteredList;

        }

    }
    /*private void signUp() {
                    signInClient.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                                    getActivity().finish();
                        }
                    });
    }*/
    /*        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(getActivity(),gso);
        account=GoogleSignIn.getLastSignedInAccount(getActivity());
        GoogleSignInAccount account1=GoogleSignIn.getLastSignedInAccount(getActivity());
        if (account1!=null);
        {
            String personName=account1.getDisplayName();
            personGivenName=account1.getGivenName();
            String FamilyName=account1.getFamilyName();
            String personEmail=account1.getEmail();
            String password=account1.getId();
            photo=account1.getPhotoUrl();
            Glide.with(getActivity()).load(String.valueOf(photo)).into(imageView_logout);

        }*/


}