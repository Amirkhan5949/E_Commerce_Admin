package com.example.e_commerce_admin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.e_commerce_admin.R;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

public class DemoActivity extends AppCompatActivity implements ColorPickerDialogListener {


    Button tv_ok;
    private static final int DIALOG_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        tv_ok=findViewById(R.id.tv_ok);




        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerDialog.newBuilder()
                        .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                        .setAllowPresets(false)
                        .setDialogId(DIALOG_ID)
                        .setColor(Color.BLACK)
                        .setShowAlphaSlider(true)
                        .show(DemoActivity.this);
             }
        });



    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        Log.d("adsd", "onColorSelected() called with: dialogId = [" + dialogId + "], color = [" + color + "]");
        switch (dialogId) {
            case DIALOG_ID:

                Toast.makeText(DemoActivity.this, "Selected Color: #" + Integer.toHexString(color), Toast.LENGTH_SHORT).show();
                 break;
        }
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }
}