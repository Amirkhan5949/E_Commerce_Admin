package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.e_commerce.R;
import com.example.e_commerce.adapter.Brand_Adapter;
import com.example.e_commerce.adapter.Super_Cat_Adapter;
import com.example.e_commerce.activity.BrandActivity;
import com.example.e_commerce.model.Brand;
import com.example.e_commerce.utils.FirebaseConstants;
import com.example.e_commerce.utils.utils;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BrandActivity extends AppCompatActivity {

    RecyclerView rv_brand;
    ImageView iv_back;
    FloatingActionButton floating_action_button;
    Brand_Adapter adapter;
    final DatabaseReference base = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.Brand.key);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand);

        rv_brand = findViewById(R.id.rv_brand);
        iv_back = findViewById(R.id.iv_back);
        floating_action_button = findViewById(R.id.floating_action_button);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BrandActivity.this, DashBoardActivity.class);
                startActivity(intent);
            }
        });

        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BrandActivity.this, Brand_edittivity.class);
                intent.putExtra("type","add");
                startActivity(intent);
            }
        });


        rv_brand.setLayoutManager(new GridLayoutManager(rv_brand.getContext(), 3));
        rv_brand.addItemDecoration(new GridSpacingItemDecoration(3, utils.dpToPx(rv_brand.getContext(), 16), true));

        FirebaseRecyclerOptions<Brand> options =
                new FirebaseRecyclerOptions.Builder<Brand>()
                        .setQuery(base, Brand.class)
                        .build();
        adapter = new Brand_Adapter(options);
        rv_brand.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}