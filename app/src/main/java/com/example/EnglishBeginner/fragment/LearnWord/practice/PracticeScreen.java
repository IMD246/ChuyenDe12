package com.example.EnglishBeginner.fragment.LearnWord.practice;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.DTO.Word;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.fragment.LearnWord.saveWord.source.SaveSqliteHelper;
import com.example.EnglishBeginner.fragment.LearnWord.vocubulary.VocabularyFragment;
import com.example.EnglishBeginner.fragment.LearnWord.word.source.MySingleton;
import com.example.EnglishBeginner.fragment.LearnWord.word.source.SqlLiteHelper;
import com.google.firebase.FirebaseApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class PracticeScreen extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private RecyclerView danhsach;

    ArrayList<String> listAnswer = new ArrayList<>();
    ArrayList<Boolean> listOfResult = new ArrayList<Boolean>();
    private String result = "";

    //list of word get from sqlite
    ArrayList<Word> listItem = new ArrayList<>();
    private SaveSqliteHelper sqliteHelper;
    private SqlLiteHelper databaseHelper;
    private PracticeAdapter adapter;

    private ProgressBar progressBar;
    private ImageView speakerIcon, showHint;
    private Button submitButton, returnButton;
    private TextView hintText, questionText;
    int currentpos = 0;
    FirebaseApp firebaseApp;
    private TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseApp.initializeApp(PracticeScreen.this);
//        firebaseApp.getApplicationContext();
        setContentView(R.layout.practive_screen);

        textToSpeech = new TextToSpeech(getBaseContext(),this);
        sqliteHelper = new SaveSqliteHelper(getBaseContext());
        //lay du lieu saveword trong sqlite roi dua vao list
        sqliteHelper.fetchData(listItem);
        //random list
        Collections.shuffle(listItem);
        ///-----------------------------///

        prepareSQL();

        setControl();
        setEvent();
        AddItem(currentpos);
        progressBar.setMax(listItem.size());
        if (listItem.size() == 0) {
            ifDataIsNull();
        }
    }


    private void setEvent() {
        AddItem(currentpos);
//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    if (currentpos < listItem.size() - 1 && currentpos < 10) {
//                        currentpos = currentpos + 1;
////                        listAnswer.clear();
//                        AddItem(currentpos);
//                    } else {
//                        //  listAnswer.clear();
//                        setAdapter(1);
//                        afterThePractice();
//                    }
//                } catch (Exception ex) {
//
//                }
//
//            }
//        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void prepareSQL() {
        databaseHelper = new SqlLiteHelper(this, "Dictionary.db", 3);
//        try {
//            databaseHelper.checkDb();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            databaseHelper.OpenDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setControl() {
        progressBar = findViewById(R.id.practice_progress_bar);
        speakerIcon = findViewById(R.id.practice_imgSpeak);
        showHint = findViewById(R.id.practice_imgShowHint);
        hintText = findViewById(R.id.practice_txt_hint);
        //checkingText = findViewById(R.id.practice_txtCheckAnswer);
        danhsach = (RecyclerView) findViewById(R.id.practice_rclAnswer);
        returnButton = findViewById(R.id.practice_btn_return);
    }

    private void setAdapter(int resultPos) {
        adapter = new PracticeAdapter(getBaseContext(), listAnswer, resultPos, listOfResult);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);// Tạo layout manager
        danhsach.setLayoutManager(layoutManager);// Gán layout manager cho recyclerview
        danhsach.setAdapter(adapter);//gán adapter cho Recyclerview.

        adapter.setSubmitEventTemp(new PracticeAdapter.submitEvent() {
            @Override
            public void delayAndPushDataWhenSubmit() {
                new CountDownTimer(3000, 1000) {
                    public void onFinish() {

                        try {

                            if (currentpos < listItem.size() - 1 && currentpos < 10) {
                                currentpos = currentpos + 1;
//                        listAnswer.clear();

                                AddItem(currentpos);

                            } else {
                                //  listAnswer.clear();
                                setAdapter(1);
                                afterThePractice();
                            }
                        } catch (Exception ex) {

                        }
                    }

                    public void onTick(long millisUntilFinished) {
                    }
                }.start();


            }
        });
    }

    public void AddItem(int currentpos) {
        try {
            progressBar.setProgress(currentpos+1);
            //reset the hint
            //save word get from sqlite to listitem
            hintText.setText("");
            databaseHelper.fetchRandomWord(listItem.get(currentpos).getWord(), listAnswer);
            //get  word to a new list
            listAnswer.add(listItem.get(currentpos).getWord());
            Collections.shuffle(listAnswer);
            Toast.makeText(getBaseContext(), listAnswer.get(0), Toast.LENGTH_SHORT).show();
            setAdapter(getResultPos(listAnswer, listItem.get(currentpos).getWord()));
            //set am thanh
            speakerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        texttoSpeak(listItem.get(currentpos).getWord());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            });
            //set hint
            showHint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // translateText(listItem.get(currentpos).getWord().toString(),hintText);

                   hintText.setText(listItem.get(currentpos).getMeaning());
                }
            });

            adapter.notifyDataSetChanged();
            danhsach.scheduleLayoutAnimation();

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private void afterThePractice() {
        listAnswer.clear();
        progressBar.setMax(listItem.size());
        speakerIcon.setVisibility(View.GONE);
        hintText.setText("Your Score is: " + getScore() + "/" + listItem.size());
        hintText.setTextSize(30);
        showHint.setVisibility(View.GONE);
      //  returnButton.setVisibility(View.GONE);
        submitButton.setText("Thoát");
        submitButton.setPadding(0,30,0,0);
        submitButton.setVisibility(View.VISIBLE);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), VocabularyFragment.class);
                startActivity(intent);
            }
        });
        adapter.notifyDataSetChanged();
    }

    private int getScore() {
        int score = 0;
        for (Boolean i : listOfResult
        ) {
            if (i == true) {
                score++;
            }
        }
        return score;
    }

    private void ifDataIsNull() {
        speakerIcon.setVisibility(View.GONE);
        showHint.setVisibility(View.GONE);
  //      submitButton.setText("Return to main");
        hintText.setText("your save word list is empty");
//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getBaseContext(), VocabularyFragment.class);
//                startActivity(intent);
//            }
//        });
    }

    private int getResultPos(ArrayList<String> list, String answer) {
        int pos = 0;
        for (String i : list) {

            if (answer.equals(i)) {
                break;
            }
            pos++;

        }
        return pos;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            textToSpeech.setSpeechRate(0.5f);
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
    private void texttoSpeak(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
