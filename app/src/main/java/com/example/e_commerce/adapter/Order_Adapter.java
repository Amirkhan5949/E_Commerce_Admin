package com.example.e_commerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.R;
import com.example.e_commerce.model.Order;
import com.example.e_commerce.utils.FirebaseConstants;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Order_Adapter extends FirebaseRecyclerAdapter<Order,Order_Adapter.Order_Adapter_View> {

     public Order_Adapter(@NonNull FirebaseRecyclerOptions<Order> options, Context context) {

        super(options);


    }

    @NonNull
    @Override
    public Order_Adapter_View onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.orders, parent, false);
        return new Order_Adapter_View(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final Order_Adapter_View holder, final int position, @NonNull final Order model) {

        holder.tv_cod.setText(model.getPayment_type());
        holder.tv_order_rs.setText(model.getProduct().getSelling_price());
        holder.tv_name.setText(model.getProduct().getName());
        holder.tv_description.setText(model.getProduct().getDetails());
        holder.tv_size.setText(model.getProduct().getSize().toString());
        holder.tv_sellingp.setText(model.getProduct().getSelling_price());
        holder.tv_mrp.setText(model.getProduct().getMrp_price());
        holder.tv_quantity.setText(model.getQuantity()+"");



        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String dateString = formatter.format(new Date(model.getTime()));
        holder.tv_time.setText("Date : " +dateString);


        Log.i("fdgrgr", "onBindViewHolder: "+model.toString());

        Picasso.get().load(model.getProduct().getImg()).into(holder.iv_image);

        Log.i("dgdgdf", "onBindViewHolder: "+model.getQuantity());
        Log.i("dgdgdf", "onBindViewHolder: "+model.toString());




//        holder.tv_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final DialogPlus dialog = DialogPlus.newDialog(holder.tv_cancel.getContext())
//                        .setContentHolder(new ViewHolder(R.layout.confirmation))
//                        .setCancelable(false)
//                        .setMargin(util.dpToPx(holder.tv_cancel.getContext(),20)
//                                ,util.dpToPx(holder.tv_cancel.getContext(),200)
//                                ,util.dpToPx(holder.tv_cancel.getContext(),20)
//                                ,util.dpToPx(holder.tv_cancel.getContext(),200))
//                        .setGravity(Gravity.CENTER)
//                        .create();
//                dialog.show();
//
//                View viewholder = dialog.getHolderView();
//                Button btn_yes =viewholder.findViewById(R.id.btn_yes);
//                Button btn_no =viewholder.findViewById(R.id.btn_no);
//
//                btn_no.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//
//                btn_yes.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Map<String,Object> map = new HashMap<>();
//                        map.put("order_status","Cancel");
//                        FirebaseDatabase.getInstance().getReference()
//                                .child("Order")
//                                .child(getRef(position).getKey())
//                                .updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//
//
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//
//                                Log.i("cxxv", "onFailure: "+e.getMessage());
//                            }
//                        });
//
//                    }
//                });
//
//            }
//        });
    }

    class Order_Adapter_View extends RecyclerView.ViewHolder {

        LinearLayout order_layout;
        TextView tv_cod,tv_order_rs,tv_name,tv_description,tv_size,
                tv_quantity,tv_sellingp,tv_mrp,tv_help,tv_cancel,tv_time;
        View colorview;
        ImageView iv_expandqty,iv_image;
        public Order_Adapter_View(@NonNull View itemView) {
            super(itemView);
            order_layout=itemView.findViewById(R.id.order_layout);
            tv_cod=itemView.findViewById(R.id.tv_cod);
            tv_time=itemView.findViewById(R.id.tv_time);
            tv_order_rs=itemView.findViewById(R.id.tv_order_rs);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_description=itemView.findViewById(R.id.tv_description);
            tv_size=itemView.findViewById(R.id.tv_size);
            tv_quantity=itemView.findViewById(R.id.tv_quantity);
            tv_sellingp=itemView.findViewById(R.id.tv_sellingp);
            tv_mrp=itemView.findViewById(R.id.tv_mrp);
            tv_help=itemView.findViewById(R.id.tv_help);
            tv_cancel=itemView.findViewById(R.id.tv_cancel);
            colorview=itemView.findViewById(R.id.colorview);
            iv_expandqty=itemView.findViewById(R.id.iv_expandqty);
            iv_image=itemView.findViewById(R.id.iv_image);
        }
    }
}

