package com.example.e_commerce.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.R;
import com.example.e_commerce.activity.BannerActivity;
import com.example.e_commerce.activity.BrandActivity;
import com.example.e_commerce.activity.Category_Activity;
import com.example.e_commerce.activity.DashBoardActivity;
import com.example.e_commerce.activity.Dashboard_Items;
import com.example.e_commerce.activity.MainActivity;
import com.example.e_commerce.activity.OrderActivity;
import com.example.e_commerce.activity.ProductListActivity;
import com.example.e_commerce.activity.AddProductRecommendListActivity;
import com.example.e_commerce.activity.ProductRecommendListActivity;
import com.example.e_commerce.activity.SuperCategoryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import static com.example.e_commerce.utils.FirebaseConstants.topic;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardAdapter_View> {

    List<Dashboard_Items> items;
    public DashboardAdapter(List<Dashboard_Items> items) {
        this.items=items;
    }

    @NonNull
    @Override
    public DashboardAdapter_View onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.dashboard,parent,false);
        return new DashboardAdapter_View(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DashboardAdapter_View holder, final int position) {

        holder.tv_name.setText(items.get(position).getTitle());
        holder.iv_img.setImageResource(items .get(position).getImage());
        holder.ll_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (items.get(position).getActions()){
                    case "S_cat_action":
                        Intent intent=new Intent(holder.ll_dashboard.getContext(), SuperCategoryActivity.class);
                        holder.ll_dashboard.getContext().startActivity(intent);
                        break;

                    case "cat_action":
                        Intent intent1=new Intent(holder.ll_dashboard.getContext(), Category_Activity.class);
                        holder.ll_dashboard.getContext().startActivity(intent1);
                        break;

                    case "Banner_action":
                        Intent intent2=new Intent(holder.ll_dashboard.getContext(), BannerActivity.class);
                        holder.ll_dashboard.getContext().startActivity(intent2);
                        break;

                    case "Product_action":
                        Intent intent3=new Intent(holder.ll_dashboard.getContext(), ProductListActivity.class);
                        holder.ll_dashboard.getContext().startActivity(intent3);
                        break;

                    case "Brand_action":
                        Intent intent4=new Intent(holder.ll_dashboard.getContext(), BrandActivity.class);
                        holder.ll_dashboard.getContext().startActivity(intent4);
                        break;

                    case "Recommended_action":
                        Intent intent5=new Intent(holder.ll_dashboard.getContext(),ProductRecommendListActivity.class);
                        holder.ll_dashboard.getContext().startActivity(intent5);
                        break;

                    case "Order":
                        Intent intent6=new Intent(holder.ll_dashboard.getContext(), OrderActivity.class);
                        holder.ll_dashboard.getContext().startActivity(intent6);
                        break;

                    case "logout":
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.i("dhbcj", "logout: ");
                                        Toast.makeText(holder.ll_dashboard.getContext(), "logout.", Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent=new Intent(holder.ll_dashboard.getContext(), MainActivity.class);
                                        holder.ll_dashboard.getContext().startActivity(intent);
                                    }
                                });

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class DashboardAdapter_View extends RecyclerView.ViewHolder {
        TextView  tv_name;
        ImageView iv_img;
        LinearLayout ll_dashboard;
        public DashboardAdapter_View(@NonNull View itemView) {
            super(itemView);

            iv_img=itemView.findViewById(R.id.iv_img);
            tv_name=itemView.findViewById(R.id.tv_name);
            ll_dashboard=itemView.findViewById(R.id.ll_dashboard);
        }
    }
}
