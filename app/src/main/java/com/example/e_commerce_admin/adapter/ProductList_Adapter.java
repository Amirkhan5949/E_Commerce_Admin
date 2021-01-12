package com.example.e_commerce_admin.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce_admin.R;
import com.example.e_commerce_admin.activity.AddProductActivity;
import com.example.e_commerce_admin.activity.ProductDetailActivity;
import com.example.e_commerce_admin.model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class ProductList_Adapter extends FirebaseRecyclerAdapter<Product,ProductList_Adapter.ProductList_Adapter_View> {

    private Gson gson = new Gson();

    public ProductList_Adapter(@NonNull FirebaseRecyclerOptions<Product> options) {
        super(options);
    }

    @NonNull
    @Override
    public ProductList_Adapter_View onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.productlist, parent, false);
        return new ProductList_Adapter_View(view);
    }



    @Override
    protected void onBindViewHolder(@NonNull ProductList_Adapter_View holder, int position, @NonNull Product model) {

        String id=getRef(position).getKey();
        holder.p_name.setText(model.getName());
        holder.total.setText("₹" +model.getSelling_price());
        holder.color.setText(model.getDetails());
        Picasso.get().load(model.getImg()).into(holder.p_img);

        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.iv_edit.getContext(), AddProductActivity.class);
                intent.putExtra("type","edit");
                intent.putExtra("id",id);
                intent.putExtra("product",gson.toJson(model));
                holder.iv_edit.getContext().startActivity(intent);
            }
        });

        holder.ll_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.ll_rel.getContext(), ProductDetailActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("product",gson.toJson(model));
                holder.ll_rel.getContext().startActivity(intent);
            }
        });

    }


    class ProductList_Adapter_View extends RecyclerView.ViewHolder {

        ImageView iv_edit,iv_delete,p_img;
        TextView p_name,total,color;
        LinearLayout ll_rel;

        public ProductList_Adapter_View(@NonNull View itemView) {
            super(itemView);
            p_img=itemView.findViewById(R.id.p_img);
            iv_edit=itemView.findViewById(R.id.iv_edit);
            iv_delete=itemView.findViewById(R.id.iv_delete);
            color=itemView.findViewById(R.id.color);
            total=itemView.findViewById(R.id.total);
            p_name=itemView.findViewById(R.id.p_name);
            ll_rel=itemView.findViewById(R.id.ll_rel);
        }
    }
}
