package com.example.EnglishBeginner.fragment.LearnWord.WordManagement;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.EnglishBeginner.Adapter.WordToeicIetlsAdapter;
import com.example.EnglishBeginner.DAO.DAOIetls;
import com.example.EnglishBeginner.DTO.Word;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.fragment.LearnWord.saveWord.source.SaveSqliteHelper;
import com.example.EnglishBeginner.fragment.LearnWord.word.source.MySingleton;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

public class IetlsManagement extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private RecyclerView rcvIetls;

    private AutoCompleteTextView atcIetls;
    private SearchView svIetls;

    private Button returnButton;
    MediaPlayer player;
    private WordToeicIetlsAdapter wordToeicIetlsAdapter;
    private DAOIetls daoIetls;
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
        daoIetls = new DAOIetls(this);
        rcvIetls = findViewById(R.id.rcvIetls);
        wordToeicIetlsAdapter = new WordToeicIetlsAdapter(this);
        returnButton = findViewById(R.id.ielts_returnButton);
        svIetls = findViewById(R.id.svIetls);
        atcIetls = findViewById(R.id.atcTypeWord);
        atcIetls.setAdapter(new ArrayAdapter<String>(this, R.layout.listoptionitem, R.id.tvOptionItem,getResources().getStringArray(R.array.typeWord)));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvIetls.setLayoutManager(linearLayoutManager);
        wordToeicIetlsAdapter.setWordList(daoIetls.getWordList());
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

        svIetls.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                wordToeicIetlsAdapter.getFilter().filter(newText);
                return false;
            }
        });
        atcIetls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                wordToeicIetlsAdapter.setListDependOnTypeWord(atcIetls.getText().toString());
            }
        });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void getDataFirebase() {
        daoIetls.getDataFromRealTimeToList(wordToeicIetlsAdapter);
    }


    private int getSelectedSpinner(Spinner spnTypeWord, String valueOf) {
        for (int i=0; i< spnTypeWord.getCount();i++)
        {
            if (spnTypeWord.getItemAtPosition(i).toString().equalsIgnoreCase(valueOf))
            {
                return i;
            }
        }
        return 0;
    }

    // Xây dựng một Hộp thoại thông báo
    private void saveWord(Word word) {
        SaveSqliteHelper sqliteHelper = new SaveSqliteHelper(getBaseContext());
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