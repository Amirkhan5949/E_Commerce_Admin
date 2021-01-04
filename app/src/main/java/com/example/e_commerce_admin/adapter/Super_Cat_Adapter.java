package com.example.e_commerce_admin.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce_admin.R;
import com.example.e_commerce_admin.activity.AddSuperCategoryActivity;
import com.example.e_commerce_admin.model.SuperCategory;
import com.example.e_commerce_admin.utils.FirebaseConstants;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class Super_Cat_Adapter extends FirebaseRecyclerAdapter<SuperCategory,Super_Cat_Adapter.Super_Cat_Adapter_View> {
    private Gson gson = new Gson();

    public Super_Cat_Adapter(@NonNull FirebaseRecyclerOptions<SuperCategory> options) {
        super(options);
    }

    @NonNull
    @Override
    public Super_Cat_Adapter_View onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.super_dash,parent,false);
        return new Super_Cat_Adapter_View(view);
    }



    @Override
    protected void onBindViewHolder(@NonNull Super_Cat_Adapter_View holder, int position, @NonNull SuperCategory model) {

        String id = getRef(position).getKey();

        holder.name_cat.setText(model.getName());
        Picasso.get().load(model.getImage()).into(holder.iv_cat);
        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.tv_edit.getContext(), AddSuperCategoryActivity.class);
                intent.putExtra("type","edit");
                intent.putExtra("id",id);
                intent.putExtra("superCategory", gson.toJson(model));
                holder.tv_edit.getContext().startActivity(intent);
            }
        });

        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.SuperCategory.key)
                        .child(model.getSuper_category_id())
                        .removeValue();
            }
        });
    }


    class Super_Cat_Adapter_View extends RecyclerView.ViewHolder {
        ImageView iv_cat;
        TextView name_cat,tv_edit,tv_delete;
        public Super_Cat_Adapter_View(@NonNull View itemView) {
            super(itemView);

            iv_cat=itemView.findViewById(R.id.iv_cat);
            name_cat=itemView.findViewById(R.id.name_cat);
            tv_edit=itemView.findViewById(R.id.tv_edit);
            tv_delete=itemView.findViewById(R.id.tv_delete);
        }
    }
}
