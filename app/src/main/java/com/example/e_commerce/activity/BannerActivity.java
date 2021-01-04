package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.e_commerce.R;
import com.example.e_commerce.adapter.BannerAdapter;
import com.example.e_commerce.model.Banner;
import com.example.e_commerce.utils.FirebaseConstants;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BannerActivity extends AppCompatActivity {

    RecyclerView rv_banner;
    ImageView iv_back;
    FloatingActionButton floating_action_button;
    private BannerAdapter adapter;
    final DatabaseReference base = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.Banner_Slider.key);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        floating_action_button=findViewById(R.id.floating_action_button);
        iv_back=findViewById(R.id.iv_back);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BannerActivity.this,DashBoardActivity.class);
                startActivity(intent);
            }
        });



        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(floating_action_button.getContext(), Banner_Slider_Add_Activity.class);
                intent.putExtra("type","add");
                startActivity(intent);
            }
        });
        rv_banner=findViewById(R.id.rv_banner);
        rv_banner.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false ));
        FirebaseRecyclerOptions<Banner> options =
                new FirebaseRecyclerOptions.Builder<Banner>()
                        .setQuery(base, Banner.class)
                        .build();

        adapter=new BannerAdapter(options);
        rv_banner.setAdapter(adapter);
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