package com.example.myapplication.User.LearnWord.word;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;
import com.example.myapplication.User.LearnWord.word.source.MySingleton;
import com.example.myapplication.User.LearnWord.word.source.SqlLiteHelper;
import com.example.myapplication.User.DTO.Word;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class WordScreen extends AppCompatActivity{



    private Button prevPage,nextPage,searchButton,returnButton;
    private RecyclerView danhsach;
    private  TextView currentPage;
    int page = 0;
    private AutoCompleteTextView autoCompleteTextView;
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

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    private void prepareSQL() {
         databaseHelper = new SqlLiteHelper(this,"Dictionary.db",3);
        try {
            databaseHelper.CopyDatabase();
        }catch (Exception e){e.printStackTrace();}

        try {
            databaseHelper.OpenDatabase();

        }catch (Exception e){e.printStackTrace();}
    }

    private void setEvent() {
        pageniteAndAPI(wordItems);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<String>temp = new ArrayList<>();
                databaseHelper.fetchWordByLetter(autoCompleteTextView.getText().toString(),temp);

                setAdapterForAutoComplete(temp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoCompleteTextView.getText().toString().trim()==null){

                }
                else{
                    ArrayList<Word> tempList = new ArrayList<>();
                    tempList =   databaseHelper.fetchWordByInput
                            (autoCompleteTextView.getText().toString().trim());
                    setAdapterForListView(tempList);

                    autoCompleteTextView.setText(null);
                    nextPage.setVisibility(View.GONE);
                    currentPage.setVisibility(View.GONE);
                    prevPage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            page = 0;
                            pageniteAndAPI(wordItems);
                            setAdapterForListView(wordItems);
                            nextPage.setVisibility(View.VISIBLE);
                            currentPage.setVisibility(View.VISIBLE);
                        }
                    });
                }

            }
        });

    }


    private void pageniteAndAPI(ArrayList<Word> wordList){
        ArrayList<Word> tempList = new ArrayList<>();
        databaseHelper.fetchData(wordList,page);
        adapter.notifyDataSetChanged();
        currentPage.setText(page+"");


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
        returnButton = findViewById(R.id.word_btn_return);
        prevPage = findViewById(R.id.word_btn_prvPage);
        nextPage = findViewById(R.id.word_btn_nextPage);
        currentPage = findViewById(R.id.word_txt_currentPage);
        autoCompleteTextView = findViewById(R.id.word_autocomplete);
        //searchView =  (SearchView) findViewById(R.id.simpleSearchView);
        danhsach =(RecyclerView) findViewById(R.id.word_recycleItem);
        searchButton = findViewById(R.id.word_btn_searchButton);

        setAdapterForListView(wordItems) ;

    }

    private void setAdapterForListView(ArrayList<Word> listToSetAdapter) {
        adapter= new WordAdapter(getBaseContext(), listToSetAdapter,databaseHelper);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);// Tạo layout manager
        danhsach.setItemAnimator(new DefaultItemAnimator());// Gán hiệu ứng cho Recyclerview
        danhsach.setLayoutManager(layoutManager);// Gán layout manager cho recyclerview
        danhsach.setAdapter(adapter);//gán adapter cho Recyclerview.


    }

    private void setAdapterForAutoComplete(ArrayList<String> listItem){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(WordScreen.this, android.R.layout.simple_expandable_list_item_1,
                listItem);
        autoCompleteTextView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


 }
