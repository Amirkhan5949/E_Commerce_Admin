package com.example.e_commerce_admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.e_commerce_admin.R;
import com.example.e_commerce_admin.model.Category;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    List<Category>s;

    public CategoryAdapter(List<Category> s){
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
