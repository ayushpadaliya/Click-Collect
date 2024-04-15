package com.example.ecommerce.Activity.adapter.Vendorside;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.Responces.CategoryResponse.GetCategoryResponse;

import java.util.ArrayList;
import java.util.List;

public class VendorCategoryAdapter extends RecyclerView.Adapter<VendorCategoryAdapter.Category_View> {

    LayoutInflater layoutInflater;
    Context context;
    EditInterface editInterface;
    List<GetCategoryResponse.Message> datalist;
    public interface EditInterface{
        public void edit(GetCategoryResponse.Message message);
        public void delete(GetCategoryResponse.Message message);
    }
    public void setEdit(EditInterface editInterface)
    {
        this.editInterface=editInterface;

    }

    public VendorCategoryAdapter(Context context, List<GetCategoryResponse.Message> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public Category_View onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.addcategory,parent,false);
        return new Category_View(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Category_View holder, int position) {
        int reversePosition = getItemCount() - 1 - position;

        holder.text.setText(datalist.get(reversePosition).getCategoryName());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editInterface!=null)
                {
                    editInterface.edit(datalist.get(reversePosition));
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editInterface!=null)
                {
                    editInterface.delete(datalist.get(reversePosition));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public static class Category_View extends RecyclerView.ViewHolder {
        TextView text;
        ImageButton edit,delete;

        public Category_View(@NonNull View itemView) {
            super(itemView);
            text=itemView.findViewById(R.id.CategoryName);
            edit=itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);

        }
    }
    public void updateList(List<GetCategoryResponse.Message> newList)
    {
        datalist=new ArrayList<>(newList);
        notifyDataSetChanged();
    }
}
