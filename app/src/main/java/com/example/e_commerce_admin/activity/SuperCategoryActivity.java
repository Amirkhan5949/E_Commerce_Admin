package com.example.e_commerce_admin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.e_commerce_admin.R;
import com.example.e_commerce_admin.adapter.Super_Cat_Adapter;
import com.example.e_commerce_admin.model.SuperCategory;
import com.example.e_commerce_admin.utils.FirebaseConstants;
import com.example.e_commerce_admin.utils.utils;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SuperCategoryActivity extends AppCompatActivity {
    RecyclerView rv_dash;
    FloatingActionButton floating_action_button;
    ImageView iv_back;
    Super_Cat_Adapter adapter;
    final DatabaseReference base = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.SuperCategory.key);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super__category_);

        rv_dash=findViewById(R.id.rv_dash);
        iv_back=findViewById(R.id.iv_back);
        floating_action_button=findViewById(R.id.floating_action_button);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SuperCategoryActivity.this,DashBoardActivity.class);
                startActivity(intent);
            }
        });


        rv_dash.setLayoutManager(new GridLayoutManager(rv_dash.getContext(),3));
        rv_dash.addItemDecoration(new GridSpacingItemDecoration(3, utils.dpToPx(rv_dash.getContext(),16),true));

        FirebaseRecyclerOptions<SuperCategory> options =
                new FirebaseRecyclerOptions.Builder<SuperCategory>()
                        .setQuery(base, SuperCategory.class)
                        .build();

        adapter=new Super_Cat_Adapter(options);
        rv_dash.setAdapter(adapter);



        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SuperCategoryActivity.this, AddSuperCategoryActivity.class);
                intent.putExtra("type","add");
                startActivity(intent);
            }
        });
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