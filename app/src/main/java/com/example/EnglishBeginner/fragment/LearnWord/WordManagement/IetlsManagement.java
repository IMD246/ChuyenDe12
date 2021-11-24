package com.example.EnglishBeginner.fragment.LearnWord.WordManagement;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import java.util.List;
import java.util.Locale;

public class IetlsManagement extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private RecyclerView rcvIetls;

    private AutoCompleteTextView atcIetls,svIetls;

    private Button returnButton;
    private WordToeicIetlsAdapter wordToeicIetlsAdapter;
    private DAOWord daoWord;
    TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ietls_management);
        textToSpeech =new TextToSpeech(getBaseContext(),this);
        initUI();
        getDataFirebase();
    }
    private void initUI() {
        daoWord = new DAOWord(this);
        rcvIetls = findViewById(R.id.rcvIetls);
        wordToeicIetlsAdapter = new WordToeicIetlsAdapter(this);
        returnButton = findViewById(R.id.ielts_returnButton);
        svIetls = findViewById(R.id.svIetls);
        atcIetls = findViewById(R.id.atcTypeWord);
        List<String> listTypeWord = new ArrayList<>();
        listTypeWord.add(DEFAULTVALUE.ALL);
        for (String i : getResources().getStringArray(R.array.typeWord))
        {
            listTypeWord.add(i);
        }
        atcIetls.setAdapter(new ArrayAdapter<String>(this, R.layout.listoptionitem, R.id.tvOptionItem,listTypeWord));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvIetls.setLayoutManager(linearLayoutManager);
        wordToeicIetlsAdapter.setWordList(daoWord.getWordList());
        rcvIetls.setAdapter(wordToeicIetlsAdapter);
        wordToeicIetlsAdapter.setMyDelegationLevel(new WordToeicIetlsAdapter.MyDelegationLevel() {
            @Override
            public void saveItem(Word word) {
                saveWord(word);
            }

            @Override
            public void speechItem(Word word) {

                texttoSpeak(word.getWord());
            }
        });

        svIetls.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                wordToeicIetlsAdapter.getFilter().filter(String.valueOf(charSequence));
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        atcIetls.setOnItemClickListener((parent, view, position, id) -> wordToeicIetlsAdapter.setListDependOnTypeWord(atcIetls.getText().toString()));
        returnButton.setOnClickListener(v -> finish());
    }
    private void getDataFirebase() {
        daoWord.getDataFromRealTimeToList(wordToeicIetlsAdapter,DEFAULTVALUE.IETLS);
        List<String>listWord = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listquestion");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listWord!=null)
                {
                    listWord.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Question question = dataSnapshot.getValue(Question.class);
                    listWord.add(question.getWord());
                }
                svIetls.setAdapter(new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1,listWord));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    // Xây dựng một Hộp thoại thông báo
    private void saveWord(Word word) {
        SaveSqliteHelper sqliteHelper = new SaveSqliteHelper(IetlsManagement.this);
        Toast.makeText(this, "w", Toast.LENGTH_SHORT).show();
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