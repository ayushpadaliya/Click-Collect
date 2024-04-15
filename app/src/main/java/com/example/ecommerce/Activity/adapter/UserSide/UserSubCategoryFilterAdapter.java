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

public class UserSubCategoryFilterAdapter extends RecyclerView.Adapter<UserSubCategoryFilterAdapter.view_subCategoryFilter> {

    Context context;
    List<Button> ButtonList=new ArrayList<>();
    public UserSubCategoryFilterAdapter(Context context, List<AllDataResponse.SubcategoryData> messageList) {
        this.context = context;
        this.messageList = messageList;
    }
    ProductInterface productInterface;
    public interface ProductInterface{
        void setProduct(AllDataResponse.SubcategoryData subcategoryData);
    }
    public void setUpProduct(ProductInterface product)
    {
        productInterface=product;
    }

    List<AllDataResponse.SubcategoryData> messageList;

    LayoutInflater layoutInflater;

    @NonNull
    @Override
    public view_subCategoryFilter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater= LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.categorychoice,parent,false);
        return new view_subCategoryFilter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull view_subCategoryFilter holder, int position) {
        holder.subCategory_filter_btn.setText(messageList.get(position).getSubCategoryName());
        ButtonList.add(holder.subCategory_filter_btn);
        holder.subCategory_filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productInterface.setProduct(messageList.get(position));
                for(Button item:ButtonList)
                {
                    item.setBackgroundTintList(context.getColorStateList(R.color.yellow));
                }
                holder.subCategory_filter_btn.setBackgroundTintList(context.getColorStateList(R.color.greenbg));
            }
        });



    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class view_subCategoryFilter extends RecyclerView.ViewHolder {
        Button subCategory_filter_btn;
        public view_subCategoryFilter(@NonNull View itemView) {
            super(itemView);
            subCategory_filter_btn=itemView.findViewById(R.id.button_category_filter);
        }
    }
}
