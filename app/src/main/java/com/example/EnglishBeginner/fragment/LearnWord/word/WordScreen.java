package com.example.EnglishBeginner.fragment.LearnWord.word;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.DTO.Word;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.fragment.LearnWord.word.source.SqlLiteHelper;

import java.util.ArrayList;
import java.util.Locale;

public class WordScreen extends AppCompatActivity implements TextToSpeech.OnInitListener{


    private Button prevPage, nextPage, searchButton, returnButton;
    private RecyclerView danhsach;
    private TextView currentPage;
    int page = 0;
    private AutoCompleteTextView autoCompleteTextView;
    ArrayList<Word> wordItems = new ArrayList<>();

    WordAdapter adapter;
    SqlLiteHelper databaseHelper;
    private TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_screen);

        textToSpeech = new TextToSpeech(getBaseContext(),this);

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
        databaseHelper = new SqlLiteHelper(this, "Dictionary.db", 3);
        try {
            databaseHelper.CopyDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            databaseHelper.OpenDatabase();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEvent() {
        pageniteAndAPI(wordItems);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<String> temp = new ArrayList<>();
                databaseHelper.fetchWordByLetter(autoCompleteTextView.getText().toString(), temp);

                setAdapterForAutoComplete(temp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoCompleteTextView.getText().toString().trim() == null) {

                } else {
                    ArrayList<Word> tempList = new ArrayList<>();
                    tempList = databaseHelper.fetchWordByInput
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


    private void pageniteAndAPI(ArrayList<Word> wordList) {
        ArrayList<Word> tempList = new ArrayList<>();
        databaseHelper.fetchData(wordList, page);
        adapter.notifyDataSetChanged();
        currentPage.setText(page + "");


        prevPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page == 0) {

                } else {

                    wordList.clear();
                    page = page - 1;
                    currentPage.setText(page + "");
                    databaseHelper.fetchData(wordList, page);
                    adapter.notifyDataSetChanged();
                }

            }
        });

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordList.clear();
                page = page + 1;
                currentPage.setText(page + "");

                databaseHelper.fetchData(wordList, page);
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
        danhsach = (RecyclerView) findViewById(R.id.word_recycleItem);
        searchButton = findViewById(R.id.word_btn_searchButton);

        setAdapterForListView(wordItems);

    }

    private void setAdapterForListView(ArrayList<Word> listToSetAdapter) {
        adapter = new WordAdapter(WordScreen.this, listToSetAdapter, databaseHelper,textToSpeech);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);// T???o layout manager
        danhsach.setItemAnimator(new DefaultItemAnimator());// G??n hi???u ???ng cho Recyclerview
        danhsach.setLayoutManager(layoutManager);// G??n layout manager cho recyclerview
        danhsach.setAdapter(adapter);//g??n adapter cho Recyclerview.


    }

    private void setAdapterForAutoComplete(ArrayList<String> listItem) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(WordScreen.this, android.R.layout.simple_expandable_list_item_1,
                listItem);
        autoCompleteTextView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            textToSpeech.setSpeechRate(1.0f);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("error", "This Language is not supported");
            }
        } else {
            Log.e("error", "Failed to Initialize");
        }
    }
    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }


}
