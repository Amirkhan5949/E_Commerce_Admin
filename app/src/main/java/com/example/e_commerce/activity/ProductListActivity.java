package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.e_commerce.R;
import com.example.e_commerce.adapter.Cat_dash_Adapter;
import com.example.e_commerce.adapter.ProductList_Adapter;
import com.example.e_commerce.model.Category;
import com.example.e_commerce.model.Product;
import com.example.e_commerce.utils.FirebaseConstants;
import com.example.e_commerce.utils.utils;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductListActivity extends AppCompatActivity {

    RecyclerView rv_productlist;
    ImageView iv_back;
    FloatingActionButton floating_action_button;
    private ProductList_Adapter adapter;
    final DatabaseReference base = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.Product.key);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        rv_productlist=findViewById(R.id.rv_productlist);
        iv_back=findViewById(R.id.iv_back);

        floating_action_button=findViewById(R.id.floating_action_button);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProductListActivity.this,DashBoardActivity.class);
                startActivity(intent);
            }
        });



        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProductListActivity.this, AddProductActivity.class);
                intent.putExtra("type","add");
                startActivity(intent);
            }
        });

        rv_productlist.setLayoutManager(new GridLayoutManager(this,2));
        rv_productlist.addItemDecoration(new GridSpacingItemDecoration(2, utils.dpToPx(rv_productlist.getContext(),0),true));

        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(base, Product.class)
                        .build();

        adapter=new ProductList_Adapter(options);
        rv_productlist.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }

}