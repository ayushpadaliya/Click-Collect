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
import com.example.ecommerce.Responces.SubCategoryResponse.GetSubCategoryResponse;

import java.util.ArrayList;
import java.util.List;

public class VendorSubCategoryAdapter extends RecyclerView.Adapter<VendorSubCategoryAdapter.SubCategory_view> {
    LayoutInflater layoutInflater;
    Context context;
    List<GetSubCategoryResponse.Message> DataList;
    EditSub editSub;

    public VendorSubCategoryAdapter(Context context, List<GetSubCategoryResponse.Message> dataList) {
        this.context = context;
        DataList = dataList;
    }
    public interface EditSub{
        public void SubEdit(GetSubCategoryResponse.Message message);

        public void SubDelete(GetSubCategoryResponse.Message message);
    }
    public void setInterface(EditSub editSub)
    {
     this.editSub=editSub;
    }

    @NonNull
    @Override
    public SubCategory_view onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.subcategory,parent,false);
        return new SubCategory_view(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategory_view holder, int position) {
        int reversePosition = getItemCount() - 1 - position;

        holder.SubCategoryName.setText(DataList.get(reversePosition).getSubCategoryName());
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editSub.SubEdit(DataList.get(reversePosition));
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editSub.SubDelete(DataList.get(reversePosition));
                }
            });
    }

    @Override
    public int getItemCount() {
        return DataList.size();
    }

    public static class SubCategory_view extends RecyclerView.ViewHolder {
        TextView SubCategoryName;
        ImageButton edit,delete;
        public SubCategory_view(@NonNull View itemView) {
            super(itemView);
            SubCategoryName=itemView.findViewById(R.id.SubCategoryName);
            edit=itemView.findViewById(R.id.sub_edit);
            delete=itemView.findViewById(R.id.sub_delete);
        }
    }
    public void updateList(List<GetSubCategoryResponse.Message> newList)
    {
        DataList=new ArrayList<>(newList);
        notifyDataSetChanged();
    }
}
