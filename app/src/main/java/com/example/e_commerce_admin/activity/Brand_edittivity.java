package com.example.e_commerce_admin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.UploadRequest;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.e_commerce_admin.R;
import com.example.e_commerce_admin.model.Brand;
 import com.example.e_commerce_admin.utils.FirebaseConstants;
import com.example.e_commerce_admin.utils.ImagePickerHelper;
import com.example.e_commerce_admin.utils.Loader;
import com.example.e_commerce_admin.utils.PermissionHelper;
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

import static com.example.e_commerce_admin.utils.PermissionHelper.setUpPermission;

public class Brand_edittivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_back,iv_edit_profile;
    EditText et_name;
    TextView tv_save;
    ImagePicker imagePicker;
    CircleImageView iv_img;
    Loader loader;
    private Gson gson = new Gson();
    private String type, id;
    private Brand brand;


    private UploadRequest imageRequest;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.Brand.key);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_edittivity2);
        type = getIntent().getStringExtra("type");

        init();

        if (type.equals("edit")) {
            id = getIntent().getStringExtra("id");
            brand = gson.fromJson(getIntent().getStringExtra("Brand"), Brand.class);


                et_name.setText(brand.getName());
            Picasso.get().load(brand.getImage()).into(iv_img);
        }


        iv_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(setUpPermission(Brand_edittivity.this))
                    imagePicker.choosePicture(true /*show camera intents*/);
            }
        });
    }

    private void init() {
        imagePicker = ImagePickerHelper.getInstance(this);
        iv_back=findViewById(R.id.iv_back);
        et_name=findViewById(R.id.et_name);
        tv_save=findViewById(R.id.tv_save);
        iv_img = findViewById(R.id.iv_img);
        iv_edit_profile = findViewById(R.id.iv_edit_profile);

        iv_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        loader=new Loader(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissionsList, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
        if(PermissionHelper.handlePermission(requestCode, permissionsList, grantResults))
            imagePicker.choosePicture(true /*show camera intents*/);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode, requestCode, data);
        Uri uri = ImagePickerHelper.handleActivityResult(requestCode,resultCode, data);
        if (uri != null) {
            iv_img.setImageURI(null);
            iv_img.setImageURI(uri);

            imageRequest = MediaManager.get().upload(uri)
                    .option("public_id", reference.getKey())
                    .option("folder", "E-commerce/Brand/");

        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                Intent intent=new Intent(Brand_edittivity.this,BrandActivity.class);
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
        }
       else {
            if (et_name.getText().toString().length() == 0) {
                Toast.makeText(this, "Enter name...", Toast.LENGTH_SHORT).show();
                return;
            }
        }


            loader.show();

        if (imageRequest != null){
            imageRequest.callback(new UploadCallback() {
                @Override
                public void onStart(String requestId) {
                    // your code here
                }
                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {
                    // example code starts here
                    Double progress = (double) bytes/totalBytes;
                    // post progress to app UI (e.g. progress bar, notification)
                    // example code ends here
                }
                @Override
                public void onSuccess(String requestId, Map resultData) {
                    // your code here

                    DatabaseReference databaseReference;
                    if (type.equals("add"))
                        databaseReference = reference.push();
                    else {
                        databaseReference = FirebaseDatabase.getInstance().getReference()
                                .child(FirebaseConstants.Brand.key)
                                .child(id);
                    }


                    Map<String,Object> map = new HashMap<>();
                    map.put(FirebaseConstants.Brand.name,et_name.getText().toString());
                    map.put(FirebaseConstants.Brand.brand_id,databaseReference.getKey() );
                    map.put(FirebaseConstants.Brand.image,resultData.get("secure_url"));
                    map.put(FirebaseConstants.Brand.image_format,resultData.get("format"));

                    databaseReference.updateChildren(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    loader.dismiss();
                                    Toast.makeText(Brand_edittivity.this, "brand updated...", Toast.LENGTH_SHORT).show();
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
                    Log.i("sdhfbdfed", "onSuccess: "+error.getDescription());
                }})
                    .dispatch();
        }
        else {
            Map<String, Object> map = new HashMap<>();
            map.put(FirebaseConstants.Brand.name, et_name.getText().toString());
            map.put(FirebaseConstants.Brand.brand_id, reference.getKey());

            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseConstants.Brand.key)
                    .child(id)
                    .updateChildren(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            loader.dismiss();
                            Toast.makeText(Brand_edittivity.this, "brand Updated...", Toast.LENGTH_SHORT).show();

                        }
                    });
        }

    }
}