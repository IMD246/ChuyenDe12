package com.example.EnglishBeginner.fragment.LearnWord.WordManagement;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.EnglishBeginner.DAO.DAOToeic;
import com.example.EnglishBeginner.DTO.Word;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.fragment.LearnWord.saveWord.source.SaveSqliteHelper;
import com.example.EnglishBeginner.fragment.LearnWord.word.source.MySingleton;


import org.json.JSONArray;
import org.json.JSONObject;

public class ToeicManagement extends AppCompatActivity {

    private RecyclerView rcvToeic;
    private WordToeicIetlsAdapter wordToeicIetlsAdapter;
    private AutoCompleteTextView atcToeic;
    private SearchView svToeic;
    private DAOToeic daoToeic;
    private Button returnButton;
    MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toeic_management);
        initUI();
        getDataFirebase();
    }
    private void initUI() {
        daoToeic = new DAOToeic(this);
        rcvToeic = findViewById(R.id.rcvToeic);
        wordToeicIetlsAdapter = new WordToeicIetlsAdapter(this);
        svToeic = findViewById(R.id.svToeic);
        atcToeic = findViewById(R.id.atcTypeWord);
        returnButton = findViewById(R.id.toeic_returnButton);
        atcToeic.setAdapter(new ArrayAdapter<String>(this, R.layout.listoptionitem, R.id.tvOptionItem,getResources().getStringArray(R.array.typeWord)));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvToeic.setLayoutManager(linearLayoutManager);
        wordToeicIetlsAdapter.setWordList(daoToeic.getWordList());
        rcvToeic.setAdapter(wordToeicIetlsAdapter);
        wordToeicIetlsAdapter.setMyDelegationLevel(new WordToeicIetlsAdapter.MyDelegationLevel() {
            @Override
            public void saveItem(Word word) {
                saveWord(word);
            }

            @Override
            public void speechItem(Word word) {
                getAudioLink(word.getWord());
            }
        });
//        imgAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openDialog(Gravity.CENTER,1,null);
//            }
//        });
        svToeic.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        atcToeic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                wordToeicIetlsAdapter.setListDependOnTypeWord(atcToeic.getText().toString());
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
        daoToeic.getDataFromRealTimeToList(wordToeicIetlsAdapter);
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

    }

    private void PlaySong(String url) {
        try {
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
    public void saveWord(Word word) {
        SaveSqliteHelper sqliteHelper = new SaveSqliteHelper(getBaseContext());
        sqliteHelper.addSaveWordByButtonFirebase(word);

    }
}