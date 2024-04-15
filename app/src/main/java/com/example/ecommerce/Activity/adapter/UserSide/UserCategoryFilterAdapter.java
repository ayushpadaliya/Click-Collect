package com.example.ecommerce.Activity.adapter.UserSide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.Responces.AllDataResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserCategoryFilterAdapter extends RecyclerView.Adapter<UserCategoryFilterAdapter.view_filterCategory> {

    LayoutInflater layoutInflater;
    List<AllDataResponse.CategoryData> category_list;
    Context context;
    UserSubCategoryFilterAdapter userSubCategoryFilterAdapter;
    subClick subClick1;
    List<Button> ButtonList=new ArrayList<>();


    public UserCategoryFilterAdapter(List<AllDataResponse.CategoryData> category_list, Context context) {
        this.category_list = category_list;
        this.context = context;
    }
    public interface subClick{
        void subCat(AllDataResponse.CategoryData categoryData);
    }
    public void setUp(subClick subClick)
    {
        subClick1=subClick;
    }

    @NonNull
    @Override
    public view_filterCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.categorychoice,parent,false);
        return new view_filterCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull view_filterCategory holder, int position) {
        holder.category_filter_btn.setText(category_list.get(position).getCategoryName().toLowerCase(Locale.ROOT));
        ButtonList.add(holder.category_filter_btn);
        holder.category_filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subClick1.subCat(category_list.get(position));
                for(Button item:ButtonList)
                {
                    item.setBackgroundTintList(context.getColorStateList(R.color.yellow));
                }
                holder.category_filter_btn.setBackgroundTintList(context.getColorStateList(R.color.greenbg));

            }
        });

    }

    @Override
    public int getItemCount() {
        return category_list.size();
    }

    public static class view_filterCategory extends RecyclerView.ViewHolder {
        Button category_filter_btn;
        RecyclerView subRecycle;
        public view_filterCategory(@NonNull View itemView) {
            super(itemView);
            category_filter_btn=itemView.findViewById(R.id.button_category_filter);

        }
    }
}
