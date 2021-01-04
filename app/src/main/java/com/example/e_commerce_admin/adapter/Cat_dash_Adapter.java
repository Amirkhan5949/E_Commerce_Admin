package com.example.e_commerce_admin.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce_admin.R;
import com.example.e_commerce_admin.activity.Category_Add_Activity;
import com.example.e_commerce_admin.model.Category;
import com.example.e_commerce_admin.utils.FirebaseConstants;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class Cat_dash_Adapter extends FirebaseRecyclerAdapter<Category,Cat_dash_Adapter.Cat_dash_Adapter_View> {

    private Gson gson = new Gson();


    public Cat_dash_Adapter(@NonNull FirebaseRecyclerOptions<Category> options) {
        super(options);
    }

    @NonNull
    @Override
    public Cat_dash_Adapter_View onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.cat_dash,parent,false);
        return new Cat_dash_Adapter_View(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull Cat_dash_Adapter_View holder, int position, @NonNull Category model) {

        String id=getRef(position).getKey();
        holder.name_cat.setText(model.getName());
        Picasso.get().load(model.getImage()).into(holder.iv_cat);

        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.tv_edit.getContext(), Category_Add_Activity.class);
                intent.putExtra("type","edit");
                intent.putExtra("id",id);
                intent.putExtra("Category", gson.toJson(model));
                holder.tv_edit.getContext().startActivity(intent);
            }
        });

        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("dsfsfc", "onClick: "+model.getCategory_id());
                FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.Category.key)
                        .child(model.getCategory_id())
                        .removeValue().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("dsfsfc", "onFailure: "+e.getMessage());
                        Log.i("dsfsfc", "onFailure: "+e.toString());
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("dsfsfc", "onSuccess: ");
                    }
                });

            }
        });

    }



    class Cat_dash_Adapter_View extends RecyclerView.ViewHolder {
        TextView name_cat,tv_edit,tv_delete;
        ImageView iv_cat;

        public Cat_dash_Adapter_View(@NonNull View itemView) {
            super(itemView);
            iv_cat=itemView.findViewById(R.id.iv_cat);
            name_cat=itemView.findViewById(R.id.name_cat);
            tv_edit=itemView.findViewById(R.id.tv_edit);
            tv_delete=itemView.findViewById(R.id.tv_delete);
        }
    }
}
