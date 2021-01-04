package com.example.e_commerce_admin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e_commerce_admin.R;
import com.example.e_commerce_admin.model.ProductLists;
import com.example.e_commerce_admin.utils.FirebaseConstants;
import com.example.e_commerce_admin.utils.Loader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class AddProductRecommendListActivity extends AppCompatActivity {

    EditText et_text;
    Button btn_save;
    private Gson gson = new Gson();
    private String type, id;
    private Loader loader;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.ProductRecommendedList .key);

    private ProductLists recommended;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_recommend_list);

        et_text=findViewById(R.id.et_text);
        loader=new Loader(this);
        btn_save=findViewById(R.id.btn_save);

        type = getIntent().getStringExtra("type");

        if (type.equals("edit")) {
            id = getIntent().getStringExtra("id");
            recommended = gson.fromJson(getIntent().getStringExtra("product"), ProductLists.class);

            et_text.setText(recommended.getName());

        }





            btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loader.show();

                DatabaseReference databaseReference;
                if(type.equals("add")){
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.i("dshbcjds", "onDataChange: "+snapshot.toString());
                            if(snapshot.exists()){
                                long i = snapshot.getChildrenCount();

                                setData(FirebaseDatabase.getInstance().getReference()
                                        .child(FirebaseConstants.ProductRecommendedList.key)
                                        .child((i+1)+""));
                            }
                            else {
                                setData(FirebaseDatabase.getInstance().getReference()
                                        .child(FirebaseConstants.ProductRecommendedList.key)
                                        .child((1)+""));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.i("dshbcjds", "onDataChange: "+error.toString());
                        }
                    });
                }
                else{
                    databaseReference =  FirebaseDatabase.getInstance().getReference()
                            .child(FirebaseConstants.ProductRecommendedList.key)
                            .child(id);

                    setData(databaseReference);

                }





            }
        });
     }

    private void setData(DatabaseReference databaseReference) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseConstants.ProductRecommendedList.name, et_text.getText().toString());
        map.put(FirebaseConstants.ProductRecommendedList.id, databaseReference.getKey());



        databaseReference.updateChildren(map).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loader.dismiss();
                        Toast.makeText(AddProductRecommendListActivity.this, "Recommended Product Added", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loader.dismiss();
            }
        });
    }
}