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
import com.example.e_commerce_admin.activity.Brand_edittivity;
import com.example.e_commerce_admin.model.Brand;
import com.example.e_commerce_admin.utils.FirebaseConstants;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class Brand_Adapter extends FirebaseRecyclerAdapter<Brand,Brand_Adapter.Brand_Adapter_View> {
    private Gson gson = new Gson();


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Brand_Adapter(@NonNull FirebaseRecyclerOptions<Brand> options) {
        super(options);
    }

    @NonNull
    @Override
    public Brand_Adapter_View onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.brand,parent,false);
        return new Brand_Adapter_View(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull Brand_Adapter_View holder, int position, @NonNull Brand model) {
        String id=getRef(position).getKey();

        holder.name_cat.setText(model.getName());
        Picasso.get().load(model.getImage()).into(holder.iv_cat);

        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.tv_edit.getContext(), Brand_edittivity.class);
                intent.putExtra("type","edit");
                intent.putExtra("id",id);
                intent.putExtra("Brand", gson.toJson(model));
                holder.tv_edit.getContext().startActivity(intent);
            }
        });


        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("dsfsfc", "onClick: "+model.getBrand_id());
                FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.Brand.key)
                        .child(model.getBrand_id())
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


    class Brand_Adapter_View extends RecyclerView.ViewHolder {
        TextView name_cat,tv_delete,tv_edit;
        ImageView iv_cat;
        public Brand_Adapter_View(@NonNull View itemView) {
            super(itemView);
            name_cat=itemView.findViewById(R.id.name_cat);
            iv_cat=itemView.findViewById(R.id.iv_cat);
            tv_delete=itemView.findViewById(R.id.tv_delete);
            tv_edit=itemView.findViewById(R.id.tv_edit);
        }
    }
}
