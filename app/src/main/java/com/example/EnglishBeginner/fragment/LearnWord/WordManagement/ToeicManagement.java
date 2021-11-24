package com.example.EnglishBeginner.fragment.LearnWord.WordManagement;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.Adapter.WordToeicIetlsAdapter;
import com.example.EnglishBeginner.DAO.DAOWord;
import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.DTO.Question;
import com.example.EnglishBeginner.DTO.Word;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.fragment.LearnWord.saveWord.source.SaveSqliteHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ToeicManagement extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private WordToeicIetlsAdapter wordToeicIetlsAdapter;
    private AutoCompleteTextView atcToeic,svToeic;
    private DAOWord daoWord;
    TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toeic_management);
        textToSpeech = new TextToSpeech(getBaseContext(),this);
        initUI();
        getDataFirebase();
    }
    private void initUI() {
        daoWord = new DAOWord(this);
        RecyclerView rcvToeic = findViewById(R.id.rcvToeic);
        wordToeicIetlsAdapter = new WordToeicIetlsAdapter(this);
        svToeic = findViewById(R.id.svToeic);
        atcToeic = findViewById(R.id.atcTypeWord);
        Button returnButton = findViewById(R.id.toeic_returnButton);
        List<String> listTypeWord = new ArrayList<>();
        listTypeWord.add(DEFAULTVALUE.ALL);
        Collections.addAll(listTypeWord, getResources().getStringArray(R.array.typeWord));
        atcToeic.setAdapter(new ArrayAdapter<>(this, R.layout.listoptionitem, R.id.tvOptionItem, listTypeWord));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvToeic.setLayoutManager(linearLayoutManager);
        wordToeicIetlsAdapter.setWordList(daoWord.getWordList());
        rcvToeic.setAdapter(wordToeicIetlsAdapter);
        wordToeicIetlsAdapter.setMyDelegationLevel(new WordToeicIetlsAdapter.MyDelegationLevel() {
            @Override
            public void saveItem(Word word) {
                saveWord(word);
            }

            @Override
            public void speechItem(Word word) {
                textToSpeak(word.getWord());
            }
        });
        svToeic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                wordToeicIetlsAdapter.getFilter().filter(String.valueOf(charSequence));
                wordToeicIetlsAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        atcToeic.setOnItemClickListener((parent, view, position, id) -> wordToeicIetlsAdapter.setListDependOnTypeWord(atcToeic.getText().toString()));
        returnButton.setOnClickListener(v -> finish());
    }
    private void getDataFirebase() {
        daoWord.getDataFromRealTimeToList(wordToeicIetlsAdapter,DEFAULTVALUE.TOEIC);
        List<String>listWord = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listquestion");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listWord.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Question question = dataSnapshot.getValue(Question.class);
                    assert question != null;
                    listWord.add(question.getWord());
                }
                svToeic.setAdapter(new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1,listWord));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Xây dựng một Hộp thoại thông báo

    public void saveWord(Word word) {
        SaveSqliteHelper sqliteHelper = new SaveSqliteHelper(ToeicManagement.this);
        sqliteHelper.addSaveWordByButtonFirebase(word);

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
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
    private void textToSpeak(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}