package com.example.e_commerce_admin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce_admin.R;
import com.example.e_commerce_admin.adapter.Order_Adapter;
import com.example.e_commerce_admin.model.Order;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import static androidx.core.view.GravityCompat.START;
import static com.example.e_commerce_admin.utils.FirebaseConstants.topic;

public class DashBoardActivity extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private Order_Adapter adapter;
    private RecyclerView rv_order;
    private Toolbar toolbar;

    private ActionBarDrawerToggle toggle;

    private TextView textView1;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        toolbar = findViewById(R.id.toolbar);
        rv_order = findViewById(R.id.rv_order);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation);


        rv_order.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        FirebaseRecyclerOptions<Order> options =
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Order"), Order.class)
                        .build();

        adapter = new Order_Adapter(options, this);
        rv_order.setAdapter(adapter);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("RightBuyAdmin");

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        LinearLayout ll_layout = (LinearLayout) navigationView.getHeaderView(0);

        textView1 = ll_layout.findViewById(R.id.name);
        textView2 = ll_layout.findViewById(R.id.email);

        textView1.setText("Aamirkhan");
        textView2.setText("AmirDeveloper@123gmail.com");
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawer(GravityCompat.START);

                switch (item.getItemId()) {
                    case R.id.supercategory:
                        Intent intent = new Intent(DashBoardActivity.this, SuperCategoryActivity.class);
                        DashBoardActivity.this.startActivity(intent);
                        break;

                    case R.id.category:
                        Intent intent1 = new Intent(DashBoardActivity.this, Category_Activity.class);
                        DashBoardActivity.this.startActivity(intent1);
                        break;

                    case R.id.brand:
                        Intent intent4 = new Intent(DashBoardActivity.this, BrandActivity.class);
                        DashBoardActivity.this.startActivity(intent4);
                        break;

                    case R.id.banner:
                        Intent intent8 = new Intent(DashBoardActivity.this, BannerActivity.class);
                        DashBoardActivity.this.startActivity(intent8);
                        break;

                    case R.id.order:
                        Intent intent6 = new Intent(DashBoardActivity.this, OrderActivity.class);
                        DashBoardActivity.this.startActivity(intent6);
                        break;

                    case R.id.product:
                        Intent intent3 = new Intent(DashBoardActivity.this, ProductListActivity.class);
                        DashBoardActivity.this.startActivity(intent3);
                        break;

                    case R.id.productlist:
                        Intent intent5 = new Intent(DashBoardActivity.this, ProductRecommendListActivity.class);
                        DashBoardActivity.this.startActivity(intent5);
                        break;


                    case R.id.logout:
                        Log.i("dhbcj", "onNavigationItemSelected: ");
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.i("dhbcj", "logout: ");
                                        Toast.makeText(DashBoardActivity.this, "", Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent = new Intent(DashBoardActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });
                        break;


                    default:
                        return true;

                }
                return true;
            }
        });


    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(START)) {
            drawerLayout.closeDrawer(START);
        } else
            super.onBackPressed();
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