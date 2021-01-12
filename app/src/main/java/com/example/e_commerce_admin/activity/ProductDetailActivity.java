package com.example.e_commerce_admin.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce_admin.R;
import com.example.e_commerce_admin.adapter.Color_Adapter;
import com.example.e_commerce_admin.adapter.List_Adapter;
import com.example.e_commerce_admin.adapter.MainSliderAdapter;
import com.example.e_commerce_admin.adapter.ProductReview_Adapter;
import com.example.e_commerce_admin.adapter.Size_Adapter;
import com.example.e_commerce_admin.model.Color;
import com.example.e_commerce_admin.model.Images;
import com.example.e_commerce_admin.model.Product;
import com.example.e_commerce_admin.model.ProductLists;
import com.example.e_commerce_admin.model.Size;
import com.example.e_commerce_admin.utils.FirebaseConstants;
import com.example.e_commerce_admin.utils.Loader;
import com.example.e_commerce_admin.utils.utils;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ss.com.bannerslider.Slider;

public class ProductDetailActivity extends AppCompatActivity {

    private static final int DIALOG_ID = 0;
    List<Images> imagesList = new ArrayList<>();
    private Slider banner_slider;
    private RecyclerView rv_color, rv_size;
    private TextView tv_p_name, tv_detail,
            tv_SellingPrice, tv_mrp, ṭv_offer,tv_supercategory,tv_category,tv_brand;
    private ImageView iv_list;
    private List_Adapter adapter;
    private DatabaseReference productListRef = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.ProductRecommendedList.key);
    private List<Size> selectedSize = new ArrayList<>();
    private List<Color> selectedColor = new ArrayList<>();
    private List<Color> list = new ArrayList<>();
    private List<ProductLists> selectedProductLists = new ArrayList<>();
    private List<ProductLists> productLists = new ArrayList<>();


    private Color_Adapter colorAdapter;
    private Gson gson = new Gson();
    private Product product;
    private String id;

    private MainSliderAdapter mainSliderAdapter;

    private Size_Adapter size_adapter;

    private DialogPlus dialog;
    private Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);


        init();
        setUpColor();
        createDialog();

        FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.ProductRecommendedList.key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            productLists.add(dataSnapshot.getValue(ProductLists.class));
                            Log.i("sfsfdgd", "onDataChange: " + productLists.toString());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        iv_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });


        rv_size.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        size_adapter = new Size_Adapter(getSize(), selectedSize);
        rv_size.setAdapter(size_adapter);

        Map<String, Size> size = product.getSize();
        if (size != null && size.size() > 0) {
            List<Size> sizes = new ArrayList<>();
            for (Map.Entry<String, Size> entry : size.entrySet()) {
                sizes.add(entry.getValue());
            }
            selectedSize.addAll(sizes);
            size_adapter.notifyDataSetChanged();

        }


        Map<String, Color> color = product.getColor();
        if (color != null && color.size() > 0) {
            List<Color> colors = new ArrayList<>();
            for (Map.Entry<String, Color> entry : color.entrySet()) {
                colors.add(entry.getValue());
            }
            list.clear();
            list.addAll(colors);
        }

        Map<String, Color> selectedColors = product.getSelectedColor();
        if (selectedColors != null && selectedColors.size() > 0) {
            List<Color> colors = new ArrayList<>();
            for (Map.Entry<String, Color> entry : selectedColors.entrySet()) {
                colors.add(entry.getValue());
            }
            selectedColor.addAll(colors);
            colorAdapter.notifyDataSetChanged();
        }


        Map<String, Images> imagesMap = product.getImage();
        Log.i("ettfbgfff", "onCreate: " + product.getImage().toString());
        if (imagesMap != null & imagesMap.size() > 0) {
            List<Images> images = new ArrayList<>();
            for (Map.Entry<String, Images> entry : imagesMap.entrySet()) {
                images.add(entry.getValue());
            }

            Log.i("ettfbgf", "onCreate: " + images.toString());
            imagesList.addAll(images);

            banner_slider.setAdapter(new MainSliderAdapter(images));
            banner_slider.setInterval(4000);
        }
    }

    private void createDialog() {
        dialog = DialogPlus.newDialog(iv_list.getContext())
                .setContentHolder(new ViewHolder(R.layout.list))
                .setCancelable(false)
                .setGravity(Gravity.CENTER)
                .setMargin
                        (utils.dpToPx(iv_list.getContext(), 20),
                                utils.dpToPx(iv_list.getContext(), 150),
                                utils.dpToPx(iv_list.getContext(), 20),
                                utils.dpToPx(iv_list.getContext(), 150))
                .create();


        View viewholder = dialog.getHolderView();
        Button btn_cancel = viewholder.findViewById(R.id.btn_cancel);
        Button btn_ok = viewholder.findViewById(R.id.btn_ok);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                Map<String, ProductLists> recommendedMap = new HashMap<>();

                Log.i("sdjbcnskj", "onClick: " + selectedProductLists.toString());
                selectedProductLists = adapter.getSelectedProductLists();
                for (ProductLists recommended : selectedProductLists) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                            .child(FirebaseConstants.Product.key)
                            .child(id)
                            .child(FirebaseConstants.Product.SelectedProductList)
                            .push();

                    recommendedMap.put(reference.getKey(), recommended);
                }

                Map<String, Object> map = new HashMap<>();


                map.put(FirebaseConstants.Product.SelectedProductList, recommendedMap);


                loader.show();
                FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseConstants.Product.key)
                        .child(id)
                        .updateChildren(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });

                Map<String, Object> objectMap = new HashMap<>();
                for (ProductLists productLists : productLists) {
                    boolean flag = check(productLists);
                    if (flag) {
                        Log.i("sjdknfksjd", "true: " + product.toString());
                        objectMap.put(productLists.getId() + "/" + FirebaseConstants.ProductRecommendedList.Product + "/" + id, product);
                    } else {
                        Log.i("sjdknfksjd", "false: ");
                        objectMap.put(productLists.getId() + "/" + FirebaseConstants.ProductRecommendedList.Product + "/" + id, null);
                    }

                }


//                Log.i("sdjfhsdvsv", "map : "+objectMap.toString());
                FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseConstants.ProductRecommendedList.key)
                        .updateChildren(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i("dkfjke", "onSuccess: ");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("dkfjke", "onFailure: ");
                    }
                });


            }
        });
        RecyclerView rv_list = viewholder.findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(rv_list.getContext(), LinearLayoutManager.VERTICAL, false));

        FirebaseRecyclerOptions<ProductLists> options =
                new FirebaseRecyclerOptions.Builder<ProductLists>()
                        .setQuery(productListRef, ProductLists.class)
                        .build();

        Map<String, ProductLists> recommendedMap = product.getSelectedProductLists();

        Log.i("dskjchjdsc", "createDialog: " + recommendedMap);

        if (recommendedMap != null && recommendedMap.size() > 0) {
            List<ProductLists> productLists = new ArrayList<>();
            for (Map.Entry<String, ProductLists> entry : recommendedMap.entrySet()) {
                productLists.add(entry.getValue());
            }
            selectedProductLists.clear();
            selectedProductLists.addAll(productLists);
        }


        adapter = new List_Adapter(options, selectedProductLists);
        rv_list.setAdapter(adapter);


    }

    private void init() {

        loader = new Loader(this);
        banner_slider = findViewById(R.id.banner_slider);
        tv_SellingPrice = findViewById(R.id.tv_SellingPrice);
        tv_brand = findViewById(R.id.tv_brand);
        tv_supercategory = findViewById(R.id.tv_supercategory);
        tv_category = findViewById(R.id.tv_category);
        tv_mrp = findViewById(R.id.tv_mrp);
        ṭv_offer = findViewById(R.id.ṭv_offer);
        iv_list = findViewById(R.id.iv_list);
        tv_p_name = findViewById(R.id.tv_p_name);
        rv_size = findViewById(R.id.rv_size);
        tv_detail = findViewById(R.id.tv_details);

        rv_color = findViewById(R.id.rv_color);

        id = getIntent().getStringExtra("id");
        product = gson.fromJson(getIntent().getStringExtra("product"), Product.class);


        tv_p_name.setText(product.getName());

        tv_category.setText("Category :-  "+product.getCategory());
        tv_supercategory.setText("SuperCategory :- "+product.getSuper_category());
        tv_brand.setText("Brand :- "+product.getBrand());



         int a = ((Integer.parseInt(product.getMrp_price()) - Integer.parseInt(product.getSelling_price())) * 100)
                / Integer.parseInt(product.getMrp_price());
        ṭv_offer.setText("" + (Integer.parseInt(product.getMrp_price()) - Integer.parseInt(product.getSelling_price()))+"% off");
        tv_SellingPrice.setText("₹" + a + "");
        tv_mrp.setText("₹" + product.getMrp_price() + "");
        tv_detail.setText(product.getDetails());


    }

    private void setUpColor() {
        getcolor();
        rv_color.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        colorAdapter = new Color_Adapter(list, selectedColor, new Color_Adapter.ClickCallBack() {
            @Override
            public void click(int i) {
                ColorPickerDialog.newBuilder()
                        .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                        .setAllowPresets(false)
                        .setDialogId(DIALOG_ID)
                        .setColor(android.graphics.Color.BLACK)
                        .setShowAlphaSlider(true)
                        .show(ProductDetailActivity.this);
            }
        });
        rv_color.setAdapter(colorAdapter);
    }


    boolean check(ProductLists model) {

        Log.i("sdghhs", "check: " + model.getId());
        Log.i("sdghhs", "check: " + selectedProductLists.toString());

        for (ProductLists recommended : selectedProductLists) {
            if (model.getId().equals(recommended.getId())) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.startListening();
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
}