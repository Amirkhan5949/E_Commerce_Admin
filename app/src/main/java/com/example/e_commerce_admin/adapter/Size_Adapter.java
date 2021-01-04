package com.example.e_commerce_admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce_admin.R;
import com.example.e_commerce_admin.model.Size;

import java.util.ArrayList;
import java.util.List;

public class Size_Adapter extends RecyclerView.Adapter<Size_Adapter.Size_Adapter_View> {


    List<Size> size;
    List<Size> selectedSize = new ArrayList<>();

    public Size_Adapter(List<Size> size,List<Size> selectedSize) {
        this.selectedSize = selectedSize;
        this.size = size;
    }

    @NonNull
    @Override
    public Size_Adapter_View onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.size,parent,false);
        return new Size_Adapter_View(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Size_Adapter_View holder, int position) {


        holder.tv_size.setText(size.get(position).getTitle() );

        holder.tv_size.setBackgroundResource((!checkSize(position)?R.drawable.bg_gray:R.drawable.bg_gray_with_stroke));

        holder.tv_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedSize.indexOf(size.get(position))==-1){
                    selectedSize.add(size.get(position));
                    notifyDataSetChanged();
                }
                else {
                    selectedSize.remove(size.get(position));
                    notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return size.size();
    }

    class Size_Adapter_View extends RecyclerView.ViewHolder {

        TextView tv_size;
        public Size_Adapter_View(@NonNull View itemView) {
            super(itemView);

            tv_size=itemView.findViewById(R.id.tv_size);
        }
    }

    public List<Size> getSelectedSize(){
        return selectedSize;
    }

    boolean checkSize(int position){
        for(Size size : selectedSize){
            if(size.getTitle().equals(this.size.get(position).getTitle()))
                return true;
        }
        return false;
    }

}
