package com.example.e_commerce_admin.adapter;


import com.example.e_commerce_admin.model.Images;

import java.util.List;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class MainSliderAdapter extends SliderAdapter {
    List<Images> list;

    public MainSliderAdapter(List<Images>list) {
        this.list=list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        switch (position) {
            case 0:
                viewHolder.bindImageSlide(list.get(position).getImg());
                break;
            case 1:
                viewHolder.bindImageSlide(list.get(position).getImg());
                break;
            case 2:
                viewHolder.bindImageSlide(list.get(position).getImg());
                break;
        }
    }
}

