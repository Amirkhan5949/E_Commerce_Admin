package com.example.e_commerce_admin.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.example.e_commerce_admin.model.Banner;
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

public class Banner_Slider_Add_Activity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView iv_img;
    private TextView tv_save;
    private ImageView iv_edit_profile, iv_back;
    private RelativeLayout ll_main;
    private ImagePicker imagePicker;
    private Loader loader;
    private String id,type;
    private Banner banner;
    Gson gson = new Gson();
    private LottieAnimationView shoftLoader;
    private UploadRequest imageRequest;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.Banner_Slider.key).push();
    private List<Banner> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner__slider_);

        init();
        getBanner();

        type=getIntent().getStringExtra("type");
        if (type.equals("edit")){
            id=getIntent().getStringExtra("id");
            banner = gson.fromJson(getIntent().getStringExtra("Banner"), Banner.class);
            Picasso.get().load(banner.getImage()).into(iv_img);
        }


    }

    private void init() {
        iv_edit_profile = findViewById(R.id.iv_edit_profile);
        iv_img = findViewById(R.id.iv_img);
        iv_back = findViewById(R.id.iv_back);
        shoftLoader = findViewById(R.id.shoftLoader);
        tv_save = findViewById(R.id.tv_save);
        ll_main = findViewById(R.id.ll_main);
        loader = new Loader(this);
        tv_save.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_edit_profile.setOnClickListener(this);
        imagePicker = ImagePickerHelper.getInstance(this);
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
                    .option("folder", "E-commerce/Banner/");
        }
    }

    private void getBanner() {

        ll_main.setVisibility(View.GONE);
        shoftLoader.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseConstants.Banner_Slider.key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            list.add(dataSnapshot.getValue(Banner.class));
                        }
                        shoftLoader.setVisibility(View.GONE);
                        ll_main.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void save() {

        if (type.equals("add")){
            if (imageRequest == null) {
                Toast.makeText(this, "Select Image...", Toast.LENGTH_SHORT).show();
                return;
            }

        }


        loader.show();

        if (imageRequest!=null){
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
                    map.put(FirebaseConstants.Banner_Slider.image, resultData.get("secure_url"));
                    map.put(FirebaseConstants.Banner_Slider.image_format, resultData.get("format"));

                    DatabaseReference databaseReference;
                    if (type.equals("type")){
                        databaseReference=reference;
                    }else {
                        databaseReference=FirebaseDatabase.getInstance().getReference()
                                .child(FirebaseConstants.Banner_Slider.key)
                                .child(id);
                    }

                    databaseReference.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            loader.dismiss();
                            Toast.makeText(Banner_Slider_Add_Activity.this, "Banner Added", Toast.LENGTH_SHORT).show();

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

        }else {
            Map<String, Object> map = new HashMap<>();
            map.put(FirebaseConstants.Banner_Slider.banner_id, reference.getKey());

            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseConstants.Banner_Slider.key)
                    .child(id)
                    .updateChildren(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            loader.dismiss();
                            Toast.makeText(Banner_Slider_Add_Activity.this, "banner Updated...", Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                Intent intent = new Intent(Banner_Slider_Add_Activity.this, BannerActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_save:
                save();
                break;

            case R.id.iv_edit_profile:
                if (setUpPermission(Banner_Slider_Add_Activity.this))
                    imagePicker.choosePicture(true /*show camera intents*/);
                break;

        }

    }
}