package com.example.e_commerce.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.e_commerce.R;
import com.example.e_commerce.model.Brand;
import com.example.e_commerce.model.Category;

import java.util.List;

public class BrandAdapter extends BaseAdapter {

    List<Brand> s;

    public BrandAdapter(List<Brand> s){
        this.s=s;
    }

    @Override
    public int getCount() {
        return s.size();
    }

    @Override
    public Object getItem(int i) {
        return  s;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.items, viewGroup, false);
        }


        TextView text = view.findViewById(R.id.text);
        text.setText(s.get(i).getName());
        return view;
    }
}
