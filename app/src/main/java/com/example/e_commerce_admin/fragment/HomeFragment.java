package com.example.e_commerce_admin.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e_commerce_admin.R;
import com.example.e_commerce_admin.activity.Dashboard_Items;
import com.example.e_commerce_admin.activity.GridSpacingItemDecoration;
import com.example.e_commerce_admin.adapter.DashboardAdapter;
import com.example.e_commerce_admin.utils.utils;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

   private RecyclerView rv_dashboard;
   private   View view;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view=inflater.inflate(R.layout.fragment_home, container, false);
        rv_dashboard=view.findViewById(R.id.rv_dashboard);

        rv_dashboard.setLayoutManager(new GridLayoutManager(rv_dashboard.getContext(),2));
        rv_dashboard.addItemDecoration(new GridSpacingItemDecoration(2, utils.dpToPx(rv_dashboard.getContext(),16),true));
        rv_dashboard.setAdapter(new DashboardAdapter(getitems()));



        return view;
    }

    private List<Dashboard_Items> getitems(){
        List<Dashboard_Items> list = new ArrayList<>();
        list.add(new Dashboard_Items("Super Category",R.drawable.aamir,"S_cat_action"));
        list.add(new Dashboard_Items("Category",R.drawable.whatapp,"cat_action"));
        list.add(new Dashboard_Items("Product",R.drawable.back1,"Product_action"));
        list.add(new Dashboard_Items( "Banner_Slider",R.drawable.nike,"Banner_action"));
        list.add(new Dashboard_Items("Brand",R.drawable.red,"Brand_action"));
        list.add(new Dashboard_Items("Recommended_Product_List",R.drawable.din,"Recommended_action"));
        list.add(new Dashboard_Items("Order",R.drawable.order,"Order"));
        list.add(new Dashboard_Items("logout",R.drawable.red,"logout"));
        return list;
    }
}