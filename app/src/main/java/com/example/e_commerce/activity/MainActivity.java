package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.e_commerce.R;
import com.example.e_commerce.adapter.SigningviewAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout=findViewById( R.id.tabLayout);
        viewPager=findViewById( R.id.viewPager);

        viewPager.setAdapter(new SigningviewAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }
}