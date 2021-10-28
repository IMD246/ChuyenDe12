package com.example.myapplication.User.LearnWord.word;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myapplication.R;
import com.example.myapplication.User.LearnWord.word.source.MySingleton;
import com.example.myapplication.User.LearnWord.word.source.SqlLiteHelper;
import com.example.myapplication.User.DTO.Word;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class WordScreen extends AppCompatActivity{

    SearchView searchView;

    Button prevPage,nextPage;
    private RecyclerView danhsach;
    TextView currentPage;
    int page = 0;

    ArrayList<Word> wordItems = new ArrayList<>();


    WordAdapter adapter;
    SqlLiteHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_screen);



        prepareSQL();

        setControl();

        setEvent();




    }


    private void prepareSQL() {
         databaseHelper = new SqlLiteHelper(this,"Dictionary.db",3);
        try {
            databaseHelper.checkDb();
        }catch (Exception e){e.printStackTrace();}

        try {
            databaseHelper.OpenDatabase();

        }catch (Exception e){e.printStackTrace();}
    }

    private void setEvent() {
        pageniteAndAPI(wordItems);



    }


    private void pageniteAndAPI(ArrayList<Word> wordList){
        databaseHelper.fetchData(wordList,page);
        adapter.notifyDataSetChanged();



        prevPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page ==0){

                }
                else{

                    wordList.clear();
                    page = page -1;
                    currentPage.setText(page+"");
                    databaseHelper.fetchData(wordList,page);
                    adapter.notifyDataSetChanged();
                }

            }
        });

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordList.clear();
                page = page +1;
                currentPage.setText(page+"");

                databaseHelper.fetchData(wordList,page);
                adapter.notifyDataSetChanged();
            }
        });


    }

    private void setControl() {

        prevPage = findViewById(R.id.word_btn_prvPage);
        nextPage = findViewById(R.id.word_btn_nextPage);
        currentPage = findViewById(R.id.word_txt_currentPage);

        //searchView =  (SearchView) findViewById(R.id.simpleSearchView);
        danhsach =(RecyclerView) findViewById(R.id.word_recycleItem);


         adapter= new WordAdapter(getBaseContext(), wordItems,databaseHelper);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);// Tạo layout manager
        danhsach.setItemAnimator(new DefaultItemAnimator());// Gán hiệu ứng cho Recyclerview
        danhsach.setLayoutManager(layoutManager);// Gán layout manager cho recyclerview
        danhsach.setAdapter(adapter);//gán adapter cho Recyclerview.


    }

    //lay ra file am thanh cua tu` tren api



 }
