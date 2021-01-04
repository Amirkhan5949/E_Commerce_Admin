package com.example.e_commerce.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.R;
import com.example.e_commerce.activity.AddProductRecommendListActivity;
import com.example.e_commerce.model.ProductLists;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.gson.Gson;

public class RecommendedAdapter extends FirebaseRecyclerAdapter<ProductLists,RecommendedAdapter.RecommendedAdapter_View> {
    private Gson gson = new Gson();

    public RecommendedAdapter(@NonNull FirebaseRecyclerOptions<ProductLists> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecommendedAdapter_View holder, int position, @NonNull ProductLists model) {

        String id=getRef(position).getKey();
        holder.tv_name.setText(model.getName());

        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.iv_edit.getContext(), AddProductRecommendListActivity.class);
                intent.putExtra("type","edit");
                intent.putExtra("id",id);
                intent.putExtra("product",gson.toJson(model));
                holder.iv_edit.getContext().startActivity(intent);
            }
        });

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @NonNull
    @Override
    public RecommendedAdapter_View onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recomededlist, parent, false);
        return new RecommendedAdapter_View(view) ;    }

    public class RecommendedAdapter_View extends RecyclerView.ViewHolder {
        TextView tv_id,tv_name;
        ImageView iv_edit,iv_delete;
        public RecommendedAdapter_View(@NonNull View itemView) {
            super(itemView);
            tv_id=itemView.findViewById(R.id.tv_id);
            tv_name=itemView.findViewById(R.id.tv_name);
            iv_edit=itemView.findViewById(R.id.iv_edit);
            iv_delete=itemView.findViewById(R.id.iv_delete);
        }
    }
}
