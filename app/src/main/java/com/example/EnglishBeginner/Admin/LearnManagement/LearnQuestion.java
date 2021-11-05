package com.example.EnglishBeginner.Admin.LearnManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.EnglishBeginner.Admin.Adapter.LearnQuestionAdapter;
import com.example.EnglishBeginner.Admin.Adapter.TopicSpinnerAdapter;
import com.example.EnglishBeginner.Admin.Adapter.TypeQuestionSpinnerAdapter;
import com.example.EnglishBeginner.Admin.DAO.DAOQuestion;
import com.example.EnglishBeginner.Admin.DAO.DAOTopic;
import com.example.EnglishBeginner.Admin.DAO.DAOTypeQuestion;
import com.example.EnglishBeginner.Admin.DTO.Question;
import com.example.EnglishBeginner.Admin.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LearnQuestion extends AppCompatActivity {

    private RecyclerView rcvQuestion;
    private DAOQuestion daoQuestion;
    private DAOTopic daoTopic;
    private ImageView imgAdd;
    private SearchView svQuestion;
    private AutoCompleteTextView atcTopic, atcTypeQuestion;
    String topic = DEFAULTVALUE.TOPIC, typeQuestion = DEFAULTVALUE.TYPEQUESTION;
    private LearnQuestionAdapter learnQuestionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_question);
        initUI();
        getDataFromRealTime();
    }
    private void initUI() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.typeQuestion));
        daoQuestion = new DAOQuestion(this);
        daoTopic = new DAOTopic(this);
        learnQuestionAdapter = new LearnQuestionAdapter(this);
        rcvQuestion = findViewById(R.id.rcvQuestion);
        atcTopic = findViewById(R.id.atcQuestion_Topic);
        svQuestion = findViewById(R.id.svQuestion);
        atcTypeQuestion = findViewById(R.id.atcQuestion_TypeQuestion);
        atcTypeQuestion.setAdapter(new TypeQuestionSpinnerAdapter(this, R.layout.listoptionitem,
                R.id.tvOptionItem, list));
        atcTopic.setAdapter(new TopicSpinnerAdapter(this, R.layout.listoptionitem, R.id.tvOptionItem, daoTopic.getTopicList()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvQuestion.setLayoutManager(linearLayoutManager);
        learnQuestionAdapter.setQuestionList(daoQuestion.getQuestionList());
        rcvQuestion.setAdapter(learnQuestionAdapter);
        learnQuestionAdapter.setMyDelegationLevel(new LearnQuestionAdapter.MyDelegationLevel() {
            @Override
            public void editItem(Question question) {
                openDialog(Gravity.CENTER,question);
            }
        });
        svQuestion.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                learnQuestionAdapter.getFilter().filter(newText);
                return false;
            }
        });
        atcTypeQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    typeQuestion = atcTypeQuestion.getText().toString();
                    learnQuestionAdapter.setListDependOnTopicAndTypeQuestion(topic, typeQuestion);
            }
        });
        atcTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    topic = atcTopic.getText().toString();
                    learnQuestionAdapter.setListDependOnTopicAndTypeQuestion(topic, typeQuestion);
            }
        });
    }
    private void getDataFromRealTime() {
        daoQuestion.getDataFromRealTimeToList(null,learnQuestionAdapter);
        daoTopic.getDataFromRealTimeFirebase(null);
    }
    private int setSelectedSpinner(Spinner spinner , String o)
    {
        for (int i=0; i<spinner.getCount();i++)
        {
            if (spinner.getItemAtPosition(i).toString().equals(o))
            {
                return i;
            }
        }
        return 0;
    }
    public void openDialog(int center,Question question) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editlearnquestion);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = center;
        window.setAttributes(windowAttributes);
        if (Gravity.CENTER == center) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        EditText edtWord = dialog.findViewById(R.id.edtWord);
        EditText edtExample = dialog.findViewById(R.id.edtExample);
        EditText edtGrammar = dialog.findViewById(R.id.edtGrammar);
        Spinner spnTypeWord = dialog.findViewById(R.id.spnTypeWordLearnQuestion);
        if (question.getTypeWord().isEmpty() || question.getTypeWord().equalsIgnoreCase(DEFAULTVALUE.DEFAULTVALUE)){}
        else
        {
            spnTypeWord.setSelection(setSelectedSpinner(spnTypeWord,question.getTypeWord()));
        }
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> map =  new HashMap<>();
                map.put("example",edtExample.getText().toString());
                map.put("typeWord",spnTypeWord.getSelectedItem().toString());
                map.put("word",edtWord.getText().toString());
                map.put("grammar",edtGrammar.getText().toString());
                daoQuestion.updateLearnQuestion(question.getId(),map,edtWord,edtExample,edtGrammar);
            }
        });
        dialog.show();
    }
}