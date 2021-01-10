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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.UploadRequest;
import com.example.e_commerce_admin.R;
import com.example.e_commerce_admin.adapter.BrandAdapter;
import com.example.e_commerce_admin.adapter.CategoryAdapter;
import com.example.e_commerce_admin.adapter.Color_Adapter;
import com.example.e_commerce_admin.adapter.Size_Adapter;
import com.example.e_commerce_admin.adapter.SuperCategoryAdapter;
import com.example.e_commerce_admin.model.Brand;
import com.example.e_commerce_admin.model.Category;
import com.example.e_commerce_admin.model.Color;
import com.example.e_commerce_admin.model.Images;
import com.example.e_commerce_admin.model.Product;
import com.example.e_commerce_admin.model.Size;
import com.example.e_commerce_admin.model.SuperCategory;
import com.example.e_commerce_admin.utils.FirebaseConstants;
import com.example.e_commerce_admin.utils.ImageOserbableCreator;
import com.example.e_commerce_admin.utils.ImagePickerHelper;
import com.example.e_commerce_admin.utils.Loader;
import com.example.e_commerce_admin.utils.PermissionHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import static com.example.e_commerce_admin.utils.PermissionHelper.setUpPermission;
public class AddProductActivity extends AppCompatActivity implements ColorPickerDialogListener, View.OnClickListener {

    private static final int DIALOG_ID = 0;
    private Spinner supercat_spinner, cat_spinner, brand_spinner;
    private RecyclerView rv_size, rv_color;
    private ImageView iv_back, iv_first, iv_2nd, iv_3rd, iv_4th, iv_5th, iv_add;
    private EditText et_MrpPrice,et_SellingPrice, et_detail,et_name;
    private TextView tv_save;
    private LottieAnimationView shoftLoader;
    private LinearLayout ll_main;
    private ImagePicker imagePicker;
    private List<Color> list = new ArrayList<>();
    private List<Color> selectedColor = new ArrayList<>();
    private List<SuperCategory> supercat_list = new ArrayList<>();
    private List<Category> cat_list = new ArrayList<>();
    private List<Brand> brand_list = new ArrayList<>();
    private List<Size> selectedSize = new ArrayList<>();


    private Product product;
    private Gson gson = new Gson();
    private String type, id;



    private Size_Adapter size_adapter;
    private Color_Adapter colorAdapter;
    private SuperCategoryAdapter adapter;
    private CategoryAdapter categoryAdapter;
    private BrandAdapter brandAdapter;


    private int flag;
    private Loader loader;
    private Disposable disposable;
    private int catSelecteditem = 0, supercatSelecteditem = 0, brandSelecteditem = 0;

    private String image = "";
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.Product.key).push();
    private List<UploadRequest> uploadRequests = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        type = getIntent().getStringExtra("type");


        init();
        getSuperCategory();
        getCategory();
        getBrand();
        initSpinnerListener();
        setUpColor();




        adapter = new SuperCategoryAdapter(supercat_list);
        supercat_spinner.setAdapter(adapter);

        categoryAdapter = new CategoryAdapter(cat_list);
        cat_spinner.setAdapter(categoryAdapter);

        brandAdapter = new BrandAdapter(brand_list);
        brand_spinner.setAdapter(brandAdapter);

        rv_size.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        size_adapter = new Size_Adapter(getSize(),selectedSize);
        rv_size.setAdapter(size_adapter);


        imagePicker = ImagePickerHelper.getInstance(this, new OnImagePickedListener() {
            @Override
            public void onImagePicked(Uri imageUri) {
                UCrop.of(imageUri, ImagePickerHelper.getTempUri(flag))
                        .withAspectRatio(1, 1)
                        .start(AddProductActivity.this);
            }
        });
        iv_add.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        iv_back.setOnClickListener(this);


        if (type.equals("edit")) {
            id = getIntent().getStringExtra("id");
            product = gson.fromJson(getIntent().getStringExtra("product"), Product.class);


            et_name.setText(product.getName());
            et_MrpPrice.setText(product.getMrp_price());
            et_SellingPrice.setText(product.getSelling_price());
            et_detail.setText(product.getDetails());

            Map<String,Size> size = product.getSize();
            if(size!=null&&size.size()>0){
                List<Size> sizes = new ArrayList<>();
                for (Map.Entry<String,Size> entry : size.entrySet()) {
                    sizes.add(entry.getValue());
                }
                selectedSize.addAll(sizes);
                size_adapter.notifyDataSetChanged();

            }



            Map<String,Color> color = product.getColor();
            if(color!=null&&color.size()>0){
                List<Color> colors = new ArrayList<>();
                for (Map.Entry<String,Color> entry : color.entrySet()) {
                    colors.add(entry.getValue());
                }
                list.clear();
                list.addAll(colors);
            }

            Map<String,Color> selectedColors = product.getSelectedColor();
            if(selectedColors!=null&selectedColors.size()>0){
                List<Color> colors = new ArrayList<>();
                for (Map.Entry<String,Color> entry : selectedColors.entrySet()) {
                    colors.add(entry.getValue());
                }
                selectedColor.addAll(colors);
                colorAdapter.notifyDataSetChanged();
            }



            Map<String,Images> imagesMap = product.getImage();
            if (imagesMap!=null&imagesMap.size()>0){
                List <Images>images =new ArrayList<>();
                for (Map.Entry<String,Images> entry : imagesMap.entrySet()){
                    images.add (entry.getValue());
                }

                Log.i("gfgftdfdf", "onCreate: "+images.toString());


                for (int i = 0 ; i < images.size() ; i++){
                    switch (i){
                        case 0:
                            Picasso.get().load(images.get(i).getImg()).into(iv_first);
                            break;

                        case 1:
                            Picasso.get().load(images.get(i).getImg()).into(iv_2nd);
                            break;

                        case 2:
                            Picasso.get().load(images.get(i).getImg()).into(iv_3rd);
                            break;

                        case 3:
                            Picasso.get().load(images.get(i).getImg()).into(iv_4th);
                            break;

                        case 4:
                            Picasso.get().load(images.get(i).getImg()).into(iv_5th);
                            break;

                    }
                }
            }
        }

    }

    private void setUpColor() {
        getcolor();
        rv_color.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        colorAdapter = new Color_Adapter(list,selectedColor ,new Color_Adapter.ClickCallBack() {
            @Override
            public void click(int i) {
                ColorPickerDialog.newBuilder()
                        .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                        .setAllowPresets(false)
                        .setDialogId(DIALOG_ID)
                        .setColor(android.graphics.Color.BLACK)
                        .setShowAlphaSlider(true)
                        .show(AddProductActivity.this);
            }
        });
        rv_color.setAdapter(colorAdapter);
    }

    private void initSpinnerListener() {
        cat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                catSelecteditem = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        supercat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                supercatSelecteditem = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        brand_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brandSelecteditem = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void getBrand() {

        ll_main.setVisibility(View.GONE);
        shoftLoader.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseConstants.Brand.key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                            brand_list.add(dataSnapshot.getValue(Brand.class));
                        Log.i("rrgrgf", "onDataChange: " + brand_list.toString());

                        brandAdapter.notifyDataSetChanged();
                        shoftLoader.setVisibility(View.GONE);
                        ll_main.setVisibility(View.VISIBLE);

                        if (type.equals("edit")){
                            getSelectedBrand(new SelectBrandCallback() {
                                @Override
                                public void get(boolean found, int index, Brand brand) {
                                    brand_spinner.setSelection(index);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void getCategory() {
        Log.i("rrgrgf", "getBrand: " + 2433);

        ll_main.setVisibility(View.GONE);
        shoftLoader.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseConstants.Category.key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                            cat_list.add(dataSnapshot.getValue(Category.class));
                        Log.i("rrgrgf", "onDataChange: " + cat_list.toString());
                        categoryAdapter.notifyDataSetChanged();
                        shoftLoader.setVisibility(View.GONE);
                        ll_main.setVisibility(View.VISIBLE);

                        if (type.equals("edit")){
                            getSelectedCategory(new SelectCategoryCallback() {
                                @Override
                                public void get(boolean found, int index, Category category) {
                                    cat_spinner.setSelection(index);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void getSuperCategory() {

        ll_main.setVisibility(View.GONE);
        shoftLoader.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseConstants.SuperCategory.key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                            supercat_list.add(dataSnapshot.getValue(SuperCategory.class));
                        adapter.notifyDataSetChanged();
                        shoftLoader.setVisibility(View.GONE);
                        ll_main.setVisibility(View.VISIBLE);

                        if(type.equals("edit")){
                            getSelectedSuperCategory(new SelectSuperCategoryCallback() {
                                @Override
                                public void get(boolean found, int index, SuperCategory superCategory) {
                                   if (found)
                                       supercat_spinner.setSelection(index);
                                }
                            });
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void init() {

        loader = new Loader(this);


        et_MrpPrice = findViewById(R.id.et_MrpPrice);
        et_SellingPrice = findViewById(R.id.et_SellingPrice);
        et_name = findViewById(R.id.et_name);

        iv_first = findViewById(R.id.iv_first);
        iv_2nd = findViewById(R.id.iv_2nd);
        iv_3rd = findViewById(R.id.iv_3rd);
        iv_4th = findViewById(R.id.iv_4th);
        iv_5th = findViewById(R.id.iv_5th);
        iv_add = findViewById(R.id.iv_add);
        tv_save = findViewById(R.id.tv_save);
        et_detail = findViewById(R.id.et_detail);

        supercat_spinner = findViewById(R.id.supercat_spinner);
        shoftLoader = findViewById(R.id.shoftLoader);
        ll_main = findViewById(R.id.ll_main);
        iv_back = findViewById(R.id.iv_back);
        cat_spinner = findViewById(R.id.cat_spinner);
        brand_spinner = findViewById(R.id.brand_spinner);
        rv_size = findViewById(R.id.rv_size);
        rv_color = findViewById(R.id.rv_color);

        String type = getIntent().getExtras().getString("type");

    }

    private void getSelectedSuperCategory(SelectSuperCategoryCallback seletecSperCategoryCallback) {
        if (supercat_list != null&&supercat_list.size()>0) {
            for (int i =0 ; i < supercat_list.size() ; i++){
                Log.i("hashhas", "product: "+product.getSuper_category_id());
                Log.i("hashhas", "supercat_list: "+supercat_list.get(i).getSuper_category_id());
                if(product.getSuper_category_id().equals(supercat_list.get(i).getSuper_category_id())){
                    seletecSperCategoryCallback.get(true,i,supercat_list.get(i));
                    Log.i("hashhas", "true: ");
                    return;
                }
            }
        }
        seletecSperCategoryCallback.get(false,-1,null);
    }

    private void getSelectedCategory(SelectCategoryCallback categoryCallback) {
        if (cat_list != null&&cat_list.size()>0) {
            for (int i =0 ; i < cat_list.size() ; i++){
                if(product.getCategory_id().equals(cat_list.get(i).getCategory_id())){
                    categoryCallback.get(true,i,cat_list.get(i));
                    Log.i("hashhas", "true: ");
                    return;
                }
            }
        }
        categoryCallback.get(false,-1,null);
    }

    private void getSelectedBrand(SelectBrandCallback brandCallback) {
        if (brand_list != null&&brand_list.size()>0) {
            for (int i =0 ; i < brand_list.size() ; i++){
                if(product.getBrand_id().equals(brand_list.get(i).getBrand_id())){
                    brandCallback.get(true,i,brand_list.get(i));
                    Log.i("hashhas", "true: ");
                    return;
                }
            }
        }
        brandCallback.get(false,-1,null);
    }


    private List<Size> getSize() {
        List<Size> list = new ArrayList<>();
        list.add(new Size("Small"));
        list.add(new Size("Large"));
        list.add(new Size("Medium"));
        list.add(new Size("Extra Large"));
        list.add(new Size("Double Extra Large"));
        return list;
    }

    private void getcolor() {
        list.add(new Color("#000000"));
        list.add(new Color("#FF113F"));
        list.add(new Color("#FFFFFF"));
        list.add(new Color("#0019FE"));
        list.add(new Color("#00DC1D"));
        list.add(new Color("#FFC107"));
        list.add(new Color("#000000"));
    }


    @Override
    public void onColorSelected(int dialogId, int color) {
        switch (dialogId) {
            case DIALOG_ID:
                list.remove(list.size() - 1);
                list.add(new Color("#" + Integer.toHexString(color)));
                list.add(new Color("#000000"));
                colorAdapter.notifyDataSetChanged();
                Toast.makeText(AddProductActivity.this, "Selected Color: #" + Integer.toHexString(color), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onDialogDismissed(int dialogId) {

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



            switch (flag) {
                case 1:
                    if (uploadRequests.size() < 1)
                        uploadRequests.add(MediaManager.get().upload(uri)
                                .option("public_id", reference.getKey() + "_A1")
                                .option("folder", "E-commerce/Product/" + reference.getKey() + "/"));
                    else
                        uploadRequests.set(0, MediaManager.get().upload(uri)
                                .option("public_id", reference.getKey() + "_A1")
                                .option("folder", "E-commerce/Product/" + reference.getKey() + "/"));

                    iv_first.setImageURI(null);
                    iv_first.setImageURI(uri);
                    break;
                case 2:
                    if (uploadRequests.size() < 2)
                        uploadRequests.add(MediaManager.get().upload(uri)
                                .option("public_id", reference.getKey() + "_A2")
                                .option("folder", "E-commerce/Product/" + reference.getKey() + "/"));
                    else
                        uploadRequests.set(1, MediaManager.get().upload(uri)
                                .option("public_id", reference.getKey() + "_A2")
                                .option("folder", "E-commerce/Product/" + reference.getKey() + "/"));
                    iv_2nd.setImageURI(null);
                    iv_2nd.setImageURI(uri);
                    break;
                case 3:
                    if (uploadRequests.size() < 3)
                        uploadRequests.add(MediaManager.get().upload(uri)
                                .option("public_id", reference.getKey() + "_A3")
                                .option("folder", "E-commerce/Product/" + reference.getKey() + "/"));
                    else
                        uploadRequests.set(2, MediaManager.get().upload(uri)
                                .option("public_id", reference.getKey() + "_A3")
                                .option("folder", "E-commerce/Product/" + reference.getKey() + "/"));
                    iv_3rd.setImageURI(null);
                    iv_3rd.setImageURI(uri);
                    break;
                case 4:
                    if (uploadRequests.size() < 4)
                        uploadRequests.add(MediaManager.get().upload(uri)
                                .option("public_id", reference.getKey() + "_A4")
                                .option("folder", "E-commerce/Product/" + reference.getKey() + "/"));
                    else
                        uploadRequests.set(3, MediaManager.get().upload(uri)
                                .option("public_id", reference.getKey() + "_A4")
                                .option("folder", "E-commerce/Product/" + reference.getKey() + "/"));
                    iv_4th.setImageURI(null);
                    iv_4th.setImageURI(uri);
                    break;

                case 5:
                    if (uploadRequests.size() < 5)
                        uploadRequests.add(MediaManager.get().upload(uri)
                                .option("public_id", reference.getKey() + "_A5")
                                .option("folder", "E-commerce/Product/" + reference.getKey() + "/"));
                    else
                        uploadRequests.set(4, MediaManager.get().upload(uri)
                                .option("public_id", reference.getKey() + "_A5")
                                .option("folder", "E-commerce/Product/" + reference.getKey() + "/"));
                    iv_5th.setImageURI(null);
                    iv_5th.setImageURI(uri);
                    break;

            }



        }
        for (UploadRequest request : uploadRequests) {
            Log.i("sdfjkdsf", "onActivityResult: "+request.getPayload().getData().toString());
        }
    }

    private void save() {

            if(uploadRequests.size()==0){
                Toast.makeText(this, "Select image.", Toast.LENGTH_SHORT).show();
                return;
            }
            else if(et_name.getText().toString().length()==0){
                Toast.makeText(this, "Enter  name...", Toast.LENGTH_SHORT).show();
                return;
            }

            else if(et_MrpPrice.getText().toString().length()==0){
                Toast.makeText(this, "Enter mrp price.", Toast.LENGTH_SHORT).show();
                return;
            }
            else if(et_SellingPrice.getText().toString().length()==0){
                Toast.makeText(this, "Enter selling price.", Toast.LENGTH_SHORT).show();
                return;
            }
            else if(et_detail.getText().toString().length()==0){
                Toast.makeText(this, "Enter detail.", Toast.LENGTH_SHORT).show();
                return;
            }
            else if(colorAdapter.getSelectedColor().size()==0){
                Toast.makeText(this, "Select color.", Toast.LENGTH_SHORT).show();
                return;
            }
            else if(size_adapter.getSelectedSize().size()==0){
                Toast.makeText(this, "Select Size.", Toast.LENGTH_SHORT).show();
                return;
            }





        image = "";
        loader.show();
        final Map<String, Object> productMap = new HashMap<>();


        List<Observable<Map<String, String>>> list = new ArrayList<>();
        for (UploadRequest request : uploadRequests) {
            list.add(ImageOserbableCreator.get(request));
        }

        Observable.merge(list).subscribe(new Observer<Map<String, String>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Map<String, String> stringStringMap) {
                if(image.equals(""))
                    image = stringStringMap.get(FirebaseConstants.Product.img);
                DatabaseReference imageRef = reference.child(FirebaseConstants.Product.Image).push();
                productMap.put(FirebaseConstants.Product.Image+"/" + imageRef.getKey(), stringStringMap);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                loader.dismiss();
            }

            @Override
            public void onComplete() {

                for(Size size : size_adapter.getSelectedSize()){
                    DatabaseReference sizeRef = reference.child(FirebaseConstants.Product.Size).push();
                    productMap.put(FirebaseConstants.Product.Size+"/" + sizeRef.getKey(), size);
                }

                for(Color color : colorAdapter.getColor()){
                    DatabaseReference sizeRef = reference.child(FirebaseConstants.Product.Color).push();
                    productMap.put(FirebaseConstants.Product.Color+"/" + sizeRef.getKey(), color);
                }

                for(Color color : colorAdapter.getSelectedColor()){
                    DatabaseReference Ref = reference.child(FirebaseConstants.Product.SelectedColor).push();
                    productMap.put(FirebaseConstants.Product.SelectedColor+"/" + Ref.getKey(), color);
                }

                productMap.put(FirebaseConstants.Product.img,image);
                productMap.put(FirebaseConstants.Product.super_category_id,supercat_list.get(supercatSelecteditem).getSuper_category_id());
                productMap.put(FirebaseConstants.Product.super_category,supercat_list.get(supercatSelecteditem).getName());

                productMap.put(FirebaseConstants.Product.category_id,cat_list.get(catSelecteditem).getCategory_id());
                productMap.put(FirebaseConstants.Product.category,cat_list.get(catSelecteditem).getName());

                productMap.put(FirebaseConstants.Product.brand_id,brand_list.get(brandSelecteditem).getBrand_id());
                productMap.put(FirebaseConstants.Product.brand,brand_list.get(brandSelecteditem).getName());

                productMap.put(FirebaseConstants.Product.name,et_name.getText().toString());
                productMap.put(FirebaseConstants.Product.items_cross_rs,et_MrpPrice.getText().toString());
                productMap.put(FirebaseConstants.Product.items_rs,et_SellingPrice.getText().toString());
                productMap.put(FirebaseConstants.Product.details,et_detail.getText().toString());




                reference.updateChildren(productMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AddProductActivity.this, "Product Added", Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                                finish();
                            }
                        });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                if (flag > 0 && flag < 5)
                    flag = flag + 1;
                else
                    flag = 1;
                if (setUpPermission(AddProductActivity.this))
                    imagePicker.choosePicture(true /*show camera intents*/);
                break;

            case R.id.tv_save:
                save();
                break;

            case R.id.iv_back:
                finish();
                break;
        }
    }

    interface SelectSuperCategoryCallback {
        void get(boolean found,int index,SuperCategory superCategory);
    }


    interface SelectCategoryCallback {
        void get(boolean found,int index,Category category);
    }

    interface SelectBrandCallback {
        void get(boolean found,int index,Brand brand);
    }

}