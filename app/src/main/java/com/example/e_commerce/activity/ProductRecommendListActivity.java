package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.e_commerce.R;
import com.example.e_commerce.adapter.RecommendedAdapter;
import com.example.e_commerce.model.ProductLists;
import com.example.e_commerce.utils.FirebaseConstants;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductRecommendListActivity extends AppCompatActivity {
    RecyclerView rv_recommended;
    ImageView iv_back;
    FloatingActionButton floating_action_button;
    private RecommendedAdapter adapter;
    final DatabaseReference base = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.ProductRecommendedList.key);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_recommend_list2);

        floating_action_button=findViewById(R.id.floating_action_button);
        rv_recommended=findViewById(R.id.rv_recommended);
        iv_back=findViewById(R.id.iv_back);

        rv_recommended.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


        FirebaseRecyclerOptions<ProductLists> options =
                new FirebaseRecyclerOptions.Builder<ProductLists>()
                        .setQuery(base, ProductLists.class)
                        .build();

        adapter=new RecommendedAdapter(options);
        rv_recommended.setAdapter(adapter);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProductRecommendListActivity.this,DashBoardActivity.class);
                startActivity(intent);
            }
        });



        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProductRecommendListActivity.this, AddProductRecommendListActivity.class);
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
        adapter.startListening();
    }
}