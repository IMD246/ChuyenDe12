package com.example.menu_right.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.menu_right.R;
import com.example.menu_right.main_interface.UserInterfaceActivity;

public class EditProfileActivity extends AppCompatActivity {
    //kháo báo các phần tử
    private Button btnBack, btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setControl();
        setEvent();
    }

    private void setEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, UserInterfaceActivity.class));
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //xử lí thay đổi và lưu thông tin
            }
        });
    }

    //Ánh xạ
    private void setControl() {
        btnBack = findViewById(R.id.btn_edit_profile_back);
        btnSave = findViewById(R.id.btn_edit_profile_save);
    }
}