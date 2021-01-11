package com.example.e_commerce_admin.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.UploadRequest;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.e_commerce_admin.R;
import com.example.e_commerce_admin.adapter.SuperCategoryAdapter;
import com.example.e_commerce_admin.model.Category;
import com.example.e_commerce_admin.model.SuperCategory;
import com.example.e_commerce_admin.utils.FirebaseConstants;
import com.example.e_commerce_admin.utils.ImagePickerHelper;
import com.example.e_commerce_admin.utils.Loader;
import com.example.e_commerce_admin.utils.PermissionHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.e_commerce_admin.utils.PermissionHelper.setUpPermission;

public class Category_Add_Activity extends AppCompatActivity implements View.OnClickListener {

    CircleImageView iv_img;
    ImageView iv_edit_profile, iv_back;
    Spinner cat_spinner;
    EditText et_name;
    TextView tv_save;
    LinearLayout ll_main;

    ImagePicker imagePicker;
    private Loader loader;
    private LottieAnimationView shoftLoader;
    private UploadRequest imageRequest;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.Category.key);
    private int selecteditem = -1;

    private List<SuperCategory> list = new ArrayList<>();
    private SuperCategoryAdapter adapter;
    private Category category;
    private Gson gson = new Gson();
    private String type, id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        type = getIntent().getStringExtra("type");

        init();

        if (type.equals("edit")) {
            id = getIntent().getStringExtra("id");
            category = gson.fromJson(getIntent().getStringExtra("Category"), Category.class);


            et_name.setText(category.getName());
            Picasso.get().load(category.getImage()).into(iv_img);
        }

        getSuperCategory();

        adapter = new SuperCategoryAdapter(list);
        cat_spinner.setAdapter(adapter);


        cat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selecteditem = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        iv_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setUpPermission(Category_Add_Activity.this)){
                    if (type.equals("add"))
                        reference = reference.push();
                    else {
                        reference = FirebaseDatabase.getInstance().getReference()
                                .child(FirebaseConstants.Category.key)
                                .child(id);
                    }
                }
                imagePicker.choosePicture(true /*show camera intents*/);
            }
        });

        imagePicker = ImagePickerHelper.getInstance(this,11,16);
    }

    private void getSuperCategory() {

        ll_main.setVisibility(View.GONE);
        shoftLoader.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseConstants.SuperCategory.key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            list.add(dataSnapshot.getValue(SuperCategory.class));
                        }
                        adapter.notifyDataSetChanged();
                        shoftLoader.setVisibility(View.GONE);
                        ll_main.setVisibility(View.VISIBLE);


                        if(type.equals("edit"))
                            getSelectedSuperCategory(new SeletecSperCategoryCallback() {
                                @Override
                                public void get(boolean found, int index, SuperCategory superCategory) {
                                    if(found){
                                        cat_spinner.setSelection(index);
                                    }
                                }
                            });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getSelectedSuperCategory(SeletecSperCategoryCallback seletecSperCategoryCallback) {
        if (list != null&&list.size()>0) {
           for (int i =0 ; i < list.size() ; i++){
               if(category.getSuper_category_id().equals(list.get(i).getSuper_category_id())){
                   seletecSperCategoryCallback.get(true,i,list.get(i));
                   return;
               }
           }
        }
        seletecSperCategoryCallback.get(false,-1,null);
    }

    private void init() {
        iv_edit_profile = findViewById(R.id.iv_edit_profile);
        iv_img = findViewById(R.id.iv_img);
        iv_back = findViewById(R.id.iv_back);
        et_name = findViewById(R.id.et_name);
        tv_save = findViewById(R.id.tv_save);
        ll_main = findViewById(R.id.ll_main);
        cat_spinner = findViewById(R.id.cat_spinner);
        shoftLoader = findViewById(R.id.shoftLoader);
        loader = new Loader(this);

        iv_back.setOnClickListener(this);
        iv_edit_profile = findViewById(R.id.iv_edit_profile);
        iv_img = findViewById(R.id.iv_img);
        iv_back = findViewById(R.id.iv_back);
        et_name = findViewById(R.id.et_name);
        cat_spinner = findViewById(R.id.cat_spinner);
        loader = new Loader(this);

        iv_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissionsList, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissionsList, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
        if (PermissionHelper.handlePermission(requestCode, permissionsList, grantResults))
            imagePicker.choosePicture(true /*show camera intents*/);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode, requestCode, data);
        Uri uri = ImagePickerHelper.handleActivityResult(requestCode, resultCode, data);
        if (uri != null) {
            iv_img.setImageURI(null);
            iv_img.setImageURI(uri);

            imageRequest = MediaManager.get().upload(uri)
                    .option("public_id", reference.getKey())
                    .option("folder", "E-commerce/Category/");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                Intent intent = new Intent(Category_Add_Activity.this, Category_Activity.class);
                startActivity(intent);
                break;

            case R.id.tv_save:
                save();
                break;
        }
    }

    private void save() {


        if (type.equals("add")) {
            if (et_name.getText().toString().length() == 0) {
                Toast.makeText(this, "Enter name...", Toast.LENGTH_SHORT).show();
                return;
            } else if (imageRequest == null) {
                Toast.makeText(this, "Select Image...", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            if (et_name.getText().toString().length() == 0) {
                Toast.makeText(this, "Enter name...", Toast.LENGTH_SHORT).show();
                return;
            }

        }


        loader.show();

        if (imageRequest != null) {
            imageRequest.callback(new UploadCallback() {
                @Override
                public void onStart(String requestId) {
                    // your code here
                }

                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {
                    // example code starts here
                    Double progress = (double) bytes / totalBytes;
                    // post progress to app UI (e.g. progress bar, notification)
                    // example code ends here
                }

                @Override
                public void onSuccess(String requestId, Map resultData) {
                    // your code here

//                    DatabaseReference databaseReference;
//                    if (type.equals("add"))
//                        databaseReference = reference.push();
//                    else {
//                        databaseReference = FirebaseDatabase.getInstance().getReference()
//                                .child(FirebaseConstants.Category.key)
//                                .child(id);
//                    }

                    Map<String, Object> map = new HashMap<>();
                    map.put(FirebaseConstants.Category.category_id,reference.getKey());
                    map.put(FirebaseConstants.Category.super_category, list.get(selecteditem).getName());
                    map.put(FirebaseConstants.Category.super_id, list.get(selecteditem).getSuper_category_id());
                    map.put(FirebaseConstants.Category.name, et_name.getText().toString());
                    map.put(FirebaseConstants.Category.image, resultData.get("secure_url"));
                    map.put(FirebaseConstants.Category.image_format, resultData.get("format"));




                    reference.updateChildren(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    loader.dismiss();
                                    Toast.makeText(Category_Add_Activity.this, "Category added...", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loader.dismiss();
                        }
                    });
                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                    // your code here
                    loader.dismiss();
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {
                    // your code here
                    Log.i("sdhfbdfed", "onSuccess: " + error.getDescription());
                }
            })
                    .dispatch();

        } else {
            Map<String, Object> map = new HashMap<>();
            map.put(FirebaseConstants.Category.name, et_name.getText().toString());
            map.put(FirebaseConstants.Category.category_id, reference.getKey());

            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseConstants.Category.key)
                    .child(id)
                    .updateChildren(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            loader.dismiss();
                            Toast.makeText(Category_Add_Activity.this, "category Updated...", Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }

    interface SeletecSperCategoryCallback {
        void get(boolean found,int index,SuperCategory superCategory);
    }
}