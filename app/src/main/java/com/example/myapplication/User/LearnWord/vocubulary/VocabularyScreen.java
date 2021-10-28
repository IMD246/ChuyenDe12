package com.example.myapplication.User.LearnWord.vocubulary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.User.LearnWord.WordManagement.IetlsManagement;
import com.example.myapplication.User.LearnWord.WordManagement.ToeicManagement;


import java.util.ArrayList;

public class VocabularyScreen extends AppCompatActivity {
    private RecyclerView danhsach;
    ArrayList<VocubularyItem> vocabularyItems = new ArrayList<>();
    ImageView toeic_image,ielts_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocubulary_screen);




        AddItem();
        setControl();
        setEvent();
    }

    private void setEvent() {

        ielts_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), IetlsManagement.class);
                startActivity(intent);
            }
        });

        toeic_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ToeicManagement.class);
                startActivity(intent);
            }
        });


    }

    private void AddItem(){
        VocubularyItem item1 = new VocubularyItem("Save",R.drawable.save);
        VocubularyItem item2 = new VocubularyItem("History",R.drawable.history);
        VocubularyItem item3 = new VocubularyItem("All Word",R.drawable.word);
        VocubularyItem item4 = new VocubularyItem("Practice",R.drawable.practice);
        vocabularyItems.add(item1);
        vocabularyItems.add(item2);
        vocabularyItems.add(item3);
        vocabularyItems.add(item4);
    }

    private void setControl() {
        danhsach =(RecyclerView) findViewById(R.id.vocubulary_recycleItem);
        VocubularyAapter adapter= new VocubularyAapter(this, vocabularyItems);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);// Tạo layout manager
        danhsach.setItemAnimator(new DefaultItemAnimator());// Gán hiệu ứng cho Recyclerview
        danhsach.setLayoutManager(layoutManager);// Gán layout manager cho recyclerview
        danhsach.setAdapter(adapter);//gán adapter cho Recyclerview.


        toeic_image = findViewById(R.id.img_toeic);
        ielts_image = findViewById(R.id.img_ielts);
    }
}
