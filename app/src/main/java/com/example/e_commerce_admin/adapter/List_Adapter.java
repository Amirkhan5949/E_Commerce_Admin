package com.example.e_commerce_admin.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce_admin.R;
import com.example.e_commerce_admin.model.ProductLists;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

public class List_Adapter extends FirebaseRecyclerAdapter<ProductLists,List_Adapter.List_Adapter_View> {

    List<ProductLists> selectedProductLists;


    public List_Adapter(@NonNull FirebaseRecyclerOptions<ProductLists> options,
                        List<ProductLists> selectedProductLists) {
        super(options);
        this.selectedProductLists = selectedProductLists;
    }

    @NonNull
    @Override
    public List_Adapter_View onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_view, parent, false);
        return new List_Adapter_View(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull List_Adapter_View holder, int position, @NonNull ProductLists model) {

        check(model, new CheckProduct() {
            @Override
            public void check(boolean found, int index, ProductLists productLists) {
                holder.cb_list.setChecked(found);
            }
        }) ;

        holder.cb_list.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    selectedProductLists.add(model);
                }
                else{
                    check(model, new CheckProduct() {
                        @Override
                        public void check(boolean found, int index, ProductLists productLists) {
                            selectedProductLists.remove(index);
                        }
                    }) ;
                }



                Log.i("sdhfdsfsd", "onClick: "+selectedProductLists.toString());
            }
        });



        holder.tv_listname.setText(model.getName());


    }


    public class List_Adapter_View extends RecyclerView.ViewHolder {
        CheckBox cb_list;
         TextView tv_listname;
     public List_Adapter_View(@NonNull View itemView) {
         super(itemView);
         cb_list=itemView.findViewById(R.id.cb_list);
         tv_listname=itemView.findViewById(R.id.tv_listname);
     }
 }

    public List<ProductLists> getSelectedProductLists() {
        return selectedProductLists;
    }


     void check(ProductLists model,CheckProduct checkProduct){
       for(int i = 0 ; i < selectedProductLists.size();i++){
           if(model.getId().equals(selectedProductLists.get(i).getId())){
               checkProduct.check(true,i,selectedProductLists.get(i));
               return;
           }
       }
         checkProduct.check(false,-1,null);
    }

    interface CheckProduct{
        void check (boolean found, int index,ProductLists productLists );
    }

}
