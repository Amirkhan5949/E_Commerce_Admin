package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.e_commerce.R;
import com.example.e_commerce.adapter.Cat_dash_Adapter;
import com.example.e_commerce.model.Category;
import com.example.e_commerce.utils.FirebaseConstants;
import com.example.e_commerce.utils.utils;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Category_Activity extends AppCompatActivity {

    RecyclerView rv_cat_dash;
    FloatingActionButton floating_action_button;
    ImageView iv_back;
    Cat_dash_Adapter adapter;
    private LottieAnimationView shoftLoader;
    LinearLayout ll_main;



    final DatabaseReference base = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.Category.key);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_);

        rv_cat_dash=findViewById(R.id.rv_cat_dash);
        floating_action_button=findViewById(R.id.floating_action_button);
        iv_back=findViewById(R.id.iv_back);
        shoftLoader=findViewById(R.id.shoftLoader);
        ll_main=findViewById(R.id.ll_main);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Category_Activity.this,DashBoardActivity.class);
                startActivity(intent);
            }
        });

        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent=new Intent(floating_action_button.getContext(), Category_Add_Activity.class);
                intent.putExtra("type","add");
                startActivity(intent);
            }
        });
        rv_cat_dash.setLayoutManager(new GridLayoutManager(rv_cat_dash.getContext(),3));

        rv_cat_dash.addItemDecoration(new GridSpacingItemDecoration(3, utils.dpToPx(rv_cat_dash.getContext(),16),true));
        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(base, Category.class)
                        .build();

        adapter=new Cat_dash_Adapter(options);


        rv_cat_dash.setAdapter(adapter);

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