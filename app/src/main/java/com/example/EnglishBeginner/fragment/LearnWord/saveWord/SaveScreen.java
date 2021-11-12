package com.example.EnglishBeginner.fragment.LearnWord.saveWord;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.DTO.Word;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.fragment.LearnWord.saveWord.source.SaveSqliteHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class SaveScreen extends AppCompatActivity implements TextToSpeech.OnInitListener{
    private FloatingActionButton flAdd;

    private RecyclerView rcl;
    private ArrayList<Word> listItem = new ArrayList<>();
    private SaveAdapter adapter;
    private SaveSqliteHelper sqliteHelper;
    private Button returnButton;
    private TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saveword_screen);
        FirebaseApp.initializeApp(getBaseContext());
        textToSpeech = new TextToSpeech(getBaseContext(),this);
        sqliteHelper = new SaveSqliteHelper(getBaseContext());
        AddItem();
        setControl();
        setEvent();

    }

    private void setEvent() {
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        flAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFloatingAdd();
            }
        });
    }

    private void dialogFloatingAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("thêm từ bạn muốn");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        input.setHint("tu ban muon them");

        final EditText input2 = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input2.setInputType(InputType.TYPE_CLASS_TEXT );
        input2.setHint("nghia cua tu");
        builder.setView(input);

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(input);
        lay.addView(input2);
        builder.setView(lay);
// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String w_Text = input.getText().toString();
                String m_Text = input2.getText().toString();
                if(m_Text!=null&&w_Text!=null){
                    sqliteHelper = new SaveSqliteHelper(getBaseContext());
                    Word temp = new Word(listItem.size(),w_Text,m_Text);
                    sqliteHelper.addSaveWordByFloating(temp);
                    listItem.add(temp);
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getBaseContext(), "dung de trong ", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void AddItem() {

        sqliteHelper.fetchData(listItem);
        Collections.reverse(listItem);
    }

    private void setControl() {
        //anh xa
        rcl = (RecyclerView) findViewById(R.id.rcl_saveScreen);
        returnButton = findViewById(R.id.saveWord_btn_return);
        flAdd = findViewById(R.id.flAdd);

        // tao ra mot doi tuong adapter
        adapter = new SaveAdapter(getBaseContext(), listItem, sqliteHelper,textToSpeech);
        //manager de custom hien thi len recycle view
        LinearLayoutManager manager = new GridLayoutManager(this, 1);

        //set cac gia tri len recycler
        rcl.setItemAnimator(new DefaultItemAnimator());// Gán hiệu ứng cho Recyclerview
        rcl.setAdapter(adapter);
        rcl.setLayoutManager(manager);

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

}
