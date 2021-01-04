package com.example.e_commerce.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.UploadRequest;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.e_commerce.R;
import com.example.e_commerce.model.SuperCategory;
import com.example.e_commerce.utils.FirebaseConstants;
import com.example.e_commerce.utils.ImagePickerHelper;
import com.example.e_commerce.utils.Loader;
import com.example.e_commerce.utils.PermissionHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.e_commerce.utils.PermissionHelper.setUpPermission;

public class AddSuperCategoryActivity extends AppCompatActivity implements View.OnClickListener {

   private ImageView iv_edit_profile, iv_back;
    private ImagePicker imagePicker;
    private CircleImageView iv_img;
    private TextView tv_save;
    private EditText et_name;
    private String type,id;


    private Loader loader;
    private UploadRequest imageRequest;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.SuperCategory.key).push();
    private SuperCategory superCategory;
    private Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_category);

        type=getIntent().getStringExtra("type");

        init();

        if(type.equals("edit")){
            id = getIntent().getStringExtra("id");
            superCategory = gson.fromJson(getIntent().getStringExtra("superCategory"), SuperCategory.class);
            Log.i("sfsfdgd", "onCreate: "+superCategory.toString());

            et_name.setText(superCategory.getName());
            Picasso.get().load(superCategory.getImage()).into(iv_img);
        }



        iv_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setUpPermission(AddSuperCategoryActivity.this))
                      imagePicker.choosePicture(true /*show camera intents*/);
            }
        });

        imagePicker = ImagePickerHelper.getInstance(this);

    }

    private void init() {

        loader = new Loader(this);

        iv_edit_profile = findViewById(R.id.iv_edit_profile);
        iv_back = findViewById(R.id.iv_back);

        iv_img = findViewById(R.id.iv_img);
        tv_save = findViewById(R.id.tv_save);
        et_name = findViewById(R.id.et_name);

        tv_save.setOnClickListener(this);
        iv_back.setOnClickListener(this);

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
                    .option("folder", "E-commerce/SuperCategory/");

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_save:
                save();
                break;

            case R.id.iv_back:
                Intent intent = new Intent(AddSuperCategoryActivity.this, SuperCategoryActivity.class);
                startActivity(intent);
                break;
        }
    }


    private void save() {
        if(type.equals("add")){
            if (et_name.getText().toString().length() == 0) {
                Toast.makeText(this, "Select name...", Toast.LENGTH_SHORT).show();
                return;
            } else if (imageRequest == null) {
                Toast.makeText(this, "Select Image...", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else {
            if (et_name.getText().toString().length() == 0) {
                Toast.makeText(this, "Select name...", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        loader.show();
        if(imageRequest!=null){
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

                    Map<String, Object> map = new HashMap<>();

                    map.put(FirebaseConstants.SuperCategory.name, et_name.getText().toString());
                    map.put(FirebaseConstants.SuperCategory.super_category_id, reference.getKey());
                    map.put(FirebaseConstants.SuperCategory.image, resultData.get("secure_url"));
                    map.put(FirebaseConstants.SuperCategory.image_format, resultData.get("format"));

                    DatabaseReference databaseReference;
                    if(type.equals("add"))
                          databaseReference = reference;
                    else
                          databaseReference =  FirebaseDatabase.getInstance().getReference()
                                  .child(FirebaseConstants.SuperCategory.key)
                                  .child(id);



                    databaseReference.updateChildren(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    loader.dismiss();
                                    Toast.makeText(AddSuperCategoryActivity.this, "Super category added...", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
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
        }
        else {

            Map<String, Object> map = new HashMap<>();

            map.put(FirebaseConstants.SuperCategory.name, et_name.getText().toString());
            map.put(FirebaseConstants.SuperCategory.super_category_id, reference.getKey());


            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseConstants.SuperCategory.key)
                    .child(id)
                    .updateChildren(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            loader.dismiss();
                            Toast.makeText(AddSuperCategoryActivity.this, "Super category Updated...", Toast.LENGTH_SHORT).show();

                        }
                    });

        }
    }
}