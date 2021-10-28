package com.example.myapplication.User.LearnWord.practice;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myapplication.R;


import com.example.myapplication.User.LearnWord.saveWord.source.SaveSqliteHelper;
import com.example.myapplication.User.LearnWord.vocubulary.VocabularyScreen;
import com.example.myapplication.User.LearnWord.word.source.MySingleton;
import com.example.myapplication.User.LearnWord.word.source.SqlLiteHelper;
import com.example.myapplication.User.DTO.Word;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class PracticeScreen extends AppCompatActivity {

    private RecyclerView danhsach;

    ArrayList<String> listAnswer = new ArrayList<>();
    ArrayList<Boolean> listOfResult = new ArrayList<Boolean>();
    String result = "";
    //list of word get from sqlite
    ArrayList<Word> listItem = new ArrayList<>();
    SaveSqliteHelper sqliteHelper;
    SqlLiteHelper databaseHelper;
    PracticeAdapter adapter;

    MediaPlayer player;

    ImageView speakerIcon,showHint;
    Button submitButton;

    TextView hintText, questionText;
    int currentpos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practive_screen);
        FirebaseApp.initializeApp(getBaseContext());

        sqliteHelper = new SaveSqliteHelper(getBaseContext());
        //lay du lieu saveword trong sqlite roi dua vao list
        sqliteHelper.fetchData(listItem);
        //random list
        Collections.shuffle(listItem);
        ///-----------------------------///

        prepareSQL();

        setControl();

        setEvent();
        AddItem();
        if (listItem.size() == 0) {
            ifDataIsNull();
        }
    }

    private void setEvent() {
        AddItem();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (currentpos < listItem.size()-1&&currentpos<10) {
                        currentpos = currentpos + 1;
                        listAnswer.clear();
                        AddItem();

                    } else {
                        listAnswer.clear();
                        setAdapter(1);
                        afterThePractice();
                    }
                } catch (Exception ex) {
                }

            }
        });


    }

    private void prepareSQL() {
        databaseHelper = new SqlLiteHelper(this, "Dictionary.db", 1);
        try {
            databaseHelper.checkDb();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            databaseHelper.OpenDatabase();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setControl() {
        speakerIcon = findViewById(R.id.practice_imgSpeak);
        showHint = findViewById(R.id.practice_imgShowHint);
        submitButton = findViewById(R.id.practice_btnSubmit);
        hintText = findViewById(R.id.practice_txt_hint);
        //checkingText = findViewById(R.id.practice_txtCheckAnswer);
        danhsach = (RecyclerView) findViewById(R.id.practice_rclAnswer);

    }

    private void setAdapter(int resultPos){
        adapter = new PracticeAdapter(this, translateText(listAnswer), resultPos,listOfResult);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);// Tạo layout manager
        danhsach.setItemAnimator(new DefaultItemAnimator());// Gán hiệu ứng cho Recyclerview
        danhsach.setLayoutManager(layoutManager);// Gán layout manager cho recyclerview
        danhsach.setAdapter(adapter);//gán adapter cho Recyclerview.

    }
    private void AddItem() {
        try {
            //reset the hint
            hintText.setText("");
            //save word get from sqlite to listitem
            databaseHelper.fetchRandomWord(listItem.get(currentpos).getWord(), listAnswer);
            //get  word to a new list
            listAnswer.add(listItem.get(currentpos).getWord());
            Collections.shuffle(listAnswer);

            setAdapter(getResultPos(listAnswer,listItem.get(currentpos).getWord()));
            //set am thanh
            speakerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        getAudioLink(listItem.get(currentpos).getWord());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            });
            //set hint
            showHint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        hintText.setText(listItem.get(currentpos).getWord());
                }
            });

            adapter.notifyDataSetChanged();


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private void afterThePractice() {





        speakerIcon.setVisibility(View.GONE);
        hintText.setText("Your Score is:"+getScore()+"/"+listItem.size());
        hintText.setTextSize(30);
        submitButton.setText("Return to main");
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), VocabularyScreen.class);
                startActivity(intent);
            }
        });
        adapter.notifyDataSetChanged();
    }

    private int getScore() {
        int score=0;
        for (Boolean i:listOfResult
             ) {
            if(i==true){
                score++;
            }
        }
        return score;
    }

    private void ifDataIsNull() {

        speakerIcon.setVisibility(View.GONE);

        submitButton.setText("Return to main");
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), VocabularyScreen.class);
                startActivity(intent);
            }
        });
    }

    private void getAudioLink(String word) {
        //lay ra chuoi trong search view
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            JSONObject jsonObject = response.getJSONObject(0);


                            JSONArray jsonArray = jsonObject.getJSONArray("phonetics");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            String audio = jsonObject1.getString("audio");

                            PlaySong(audio);

                        } catch (Exception exception) {
                            Toast.makeText(getBaseContext(), "bug found.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "am thanh khong co san....", Toast.LENGTH_SHORT).show();

            }
        });
        MySingleton.getInstance(getBaseContext()).addToRequestQueue(jsonArrayRequest);

        //goi len api

        //yeu cau api gui ve file json


    }

    private void PlaySong(String url) {
        try {
            Toast.makeText(getBaseContext(), url, Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse("https:" + url);
            player = new MediaPlayer();
            player.setDataSource(getBaseContext(), uri);
            player.prepare();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        } catch (Exception exception) {
            Log.d(exception.toString(), "PlaySong: ");
        }

    }
    private ArrayList<String> translateText(ArrayList<String> listToTranSlate) {
        ArrayList<String> listPossibleResult = new ArrayList<>();
        try {

            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                    .setTargetLanguage(FirebaseTranslateLanguage.VI)
                    .build();
            FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);

            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();

            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    int temp=0;
                    for (String i: listToTranSlate) {
                        translator.translate(i.trim().toString().toLowerCase()).addOnSuccessListener(new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String s) {


                               listPossibleResult.add(s);

                           adapter.notifyDataSetChanged();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getBaseContext(), "fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getBaseContext(), "fail", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listPossibleResult;
    }
    private int getResultPos(ArrayList<String> list,String answer){
        int pos =0;
        for (String i:list)
        {

            if(answer.equals(i)){
                 break;
             }
            pos++;

        }
        return pos;
    }
}
