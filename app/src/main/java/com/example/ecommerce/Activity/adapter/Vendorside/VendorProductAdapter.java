package com.example.ecommerce.Activity.adapter.Vendorside;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.Responces.ProductResponse.GetProductResponse;

import java.util.ArrayList;
import java.util.List;

public class VendorProductAdapter extends RecyclerView.Adapter<VendorProductAdapter.view_product> {

    Context context;
    LayoutInflater layoutInflater;
    Product product;
    GetProductResponse.Message message;

    public interface Product{
        public void EditProduct(GetProductResponse.Message message);


        public  void DeleteProduct(GetProductResponse.Message message);
    }
    public void setProduct(Product pr)
    {
        this.product=pr;
    }


    public VendorProductAdapter(Context context, List<GetProductResponse.Message> product_List) {
        this.context = context;
        Product_List = product_List;
    }

    List<GetProductResponse.Message> Product_List;
    @NonNull
    @Override
    public view_product onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.product,parent,false);
        return new view_product(view);
    }

    @Override
    public void onBindViewHolder(@NonNull view_product holder, int position) {
           int reversePosition = getItemCount() - 1 - position;
            holder.product.setText(Product_List.get(reversePosition).getProductName());
            holder.prize.setText(Product_List.get(reversePosition).getProductPrice());
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    product.DeleteProduct(Product_List.get(reversePosition));
                }
            });
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    product.EditProduct(Product_List.get(reversePosition));
                }
            });
    }

    @Override
    public int getItemCount() {
        return Product_List.size();
    }

    public static class view_product extends RecyclerView.ViewHolder {
        TextView product,prize;
        ImageView delete,edit;
        public view_product(@NonNull View itemView) {
            super(itemView);
            product=itemView.findViewById(R.id.ProductName);
            prize=itemView.findViewById(R.id.ProductPrize);
            delete=itemView.findViewById(R.id.product_delete);
            edit=itemView.findViewById(R.id.product_edit);
        }
    }
    public void updateList(List<GetProductResponse.Message> newList)
    {
        Product_List=new ArrayList<>(newList);
        notifyDataSetChanged();
    }
}
