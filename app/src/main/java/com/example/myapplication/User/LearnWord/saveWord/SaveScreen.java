package com.example.myapplication.User.LearnWord.saveWord;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.User.LearnWord.saveWord.source.SaveSqliteHelper;
import com.example.myapplication.User.DTO.Word;
import com.google.firebase.FirebaseApp;


import java.util.ArrayList;

public class SaveScreen extends AppCompatActivity {

    RecyclerView rcl;
    ArrayList<Word> listItem = new ArrayList<>();
    SaveAdapter adapter ;
    SaveSqliteHelper sqliteHelper;
    Button returnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saveword_screen);
        FirebaseApp.initializeApp(getBaseContext());

        sqliteHelper = new SaveSqliteHelper(getBaseContext());
        AddItem();
        setControl();

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void AddItem() {

        sqliteHelper.fetchData(listItem);
    }

    private void setControl(){
        //anh xa
        rcl =(RecyclerView) findViewById(R.id.rcl_saveScreen);
        returnButton = findViewById(R.id.saveWord_btn_return);
        // tao ra mot doi tuong adapter
        adapter = new SaveAdapter(getBaseContext(),listItem,sqliteHelper);
        //manager de custom hien thi len recycle view
        LinearLayoutManager manager = new GridLayoutManager(this,1);

        //set cac gia tri len recycler
        rcl.setItemAnimator(new DefaultItemAnimator());// Gán hiệu ứng cho Recyclerview
        rcl.setAdapter(adapter);
        rcl.setLayoutManager(manager);

    }
}
