package com.example.myapplication.User.LearnWord.saveWord;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.User.LearnWord.saveWord.source.SaveSqliteHelper;
import com.example.myapplication.User.LearnWord.word.source.WordClass;


import java.util.ArrayList;

public class SaveScreen extends AppCompatActivity {

    RecyclerView rcl;
    ArrayList<WordClass> listItem = new ArrayList<>();
    SaveAdapter adapter ;
    SaveSqliteHelper sqliteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saveword_screen);
        getSupportActionBar().hide();

        sqliteHelper = new SaveSqliteHelper(getBaseContext());
        AddItem();
        setControl();
    }

    private void AddItem() {

        sqliteHelper.fetchData(listItem);
    }

    private void setControl(){
        //anh xa
        rcl =(RecyclerView) findViewById(R.id.rcl_saveScreen);
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
