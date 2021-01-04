package com.example.e_commerce_admin.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce_admin.R;
import com.example.e_commerce_admin.activity.Banner_Slider_Add_Activity;
import com.example.e_commerce_admin.model.Banner;
import com.example.e_commerce_admin.utils.FirebaseConstants;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class BannerAdapter extends FirebaseRecyclerAdapter<Banner,BannerAdapter.BannerAdapter_View> {

    Gson gson = new Gson();

    public BannerAdapter(@NonNull FirebaseRecyclerOptions<Banner> options) {
        super(options);
    }

    @NonNull
    @Override
    public BannerAdapter_View onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.banner,parent,false);
        return new BannerAdapter_View(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull BannerAdapter_View holder, int position, @NonNull Banner model) {
        Picasso.get().load(model.getImage()).into(holder.iv_img);
        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id=getRef(position).getKey();
                Intent intent=new Intent(holder.iv_edit.getContext(), Banner_Slider_Add_Activity.class);
                intent.putExtra("type","edit");
                intent.putExtra("id",id);
                intent.putExtra("Banner", gson.toJson(model));
                holder.iv_edit.getContext().startActivity(intent);
            }
        });

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.Banner_Slider.key)
                        .child(getRef(position).getKey())
                        .removeValue();
            }
        });
    }


    class BannerAdapter_View extends RecyclerView.ViewHolder {
        ImageView iv_img,iv_edit,iv_delete;
       public BannerAdapter_View(@NonNull View itemView) {
           super(itemView);
           iv_img=itemView.findViewById(R.id.iv_img);
           iv_edit=itemView.findViewById(R.id.iv_edit);
           iv_delete=itemView.findViewById(R.id.iv_delete);
       }
   }
}
