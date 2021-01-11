package com.example.e_commerce_admin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.e_commerce_admin.R;
import com.example.e_commerce_admin.adapter.Order_Adapter;
import com.example.e_commerce_admin.model.Order;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class OrderActivity extends AppCompatActivity {

   private RecyclerView order_recycler;
    private Order_Adapter adapter;
    private ImageView iv_back;
    private ProgressBar progress;
    private TextView tv_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        order_recycler=findViewById(R.id.order_recycler);
        iv_back=findViewById(R.id.iv_back);
        progress=findViewById(R.id.progress);
        tv_data=findViewById(R.id.tv_data);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        order_recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        FirebaseRecyclerOptions<Order> options=
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Order"), Order.class)
                        .build();

        adapter=new Order_Adapter(options,this,progress, new Order_Adapter.ClickCallBack() {
            @Override
            public void count(int i) {
                if (i==0){
                    tv_data.setVisibility(View.VISIBLE);
                }
            }
        });
        order_recycler.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}