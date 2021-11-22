package com.example.EnglishBeginner.Admin.LearnManagement;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.Admin.Adapter.LearnQuestionAdapter;
import com.example.EnglishBeginner.Admin.Adapter.TopicSpinnerAdapter;
import com.example.EnglishBeginner.Admin.Adapter.TypeQuestionSpinnerAdapter;
import com.example.EnglishBeginner.Admin.DAO.DAOImageStorage;
import com.example.EnglishBeginner.Admin.DAO.DAOQuestion;
import com.example.EnglishBeginner.Admin.DAO.DAOTopic;
import com.example.EnglishBeginner.Admin.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.Admin.DTO.Question;
import com.example.EnglishBeginner.Admin.DTO.Topic;
import com.example.EnglishBeginner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LearnQuestion extends AppCompatActivity {

    private DAOQuestion daoQuestion;
    private DAOImageStorage daoImageStorage;
    private AutoCompleteTextView atcTopic, atcTypeQuestion;
    String topic = DEFAULTVALUE.TOPIC, typeQuestion = DEFAULTVALUE.TYPEQUESTION;
    private LearnQuestionAdapter learnQuestionAdapter;
    private ImageView imgEditQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_question);
        initUI();
        getDataFromRealTime();
    }
    private void initUI() {
        daoImageStorage = new DAOImageStorage(this);
        daoQuestion = new DAOQuestion(this);
        learnQuestionAdapter = new LearnQuestionAdapter(this);
        RecyclerView rcvQuestion = findViewById(R.id.rcvQuestion);
        atcTopic = findViewById(R.id.atcQuestion_Topic);
        atcTopic.setText(DEFAULTVALUE.ALL);
        SearchView svQuestion = findViewById(R.id.svQuestion);
        atcTypeQuestion = findViewById(R.id.atcQuestion_TypeQuestion);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvQuestion.setLayoutManager(linearLayoutManager);
        learnQuestionAdapter.setQuestionList(daoQuestion.getQuestionList());
        rcvQuestion.setAdapter(learnQuestionAdapter);
        learnQuestionAdapter.setMyDelegationLevel(question -> openDialog(Gravity.CENTER,question));
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
        atcTypeQuestion.setOnItemClickListener((parent, view, position, id) -> {
                typeQuestion = atcTypeQuestion.getText().toString();
                learnQuestionAdapter.setListDependOnTopicAndTypeQuestion(topic, typeQuestion);
        });
        atcTopic.setOnItemClickListener((parent, view, position, id) -> {
                topic = atcTopic.getText().toString();
                learnQuestionAdapter.setListDependOnTopicAndTypeQuestion(topic, typeQuestion);
        });
    }
    private void getDataFromRealTime() {
        daoQuestion.getDataFromRealTimeToList(null,learnQuestionAdapter);
        List<String> topicList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listtopic");
        databaseReference.orderByChild("level").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (topicList != null) {
                    topicList.clear();
                    topicList.add(DEFAULTVALUE.ALL);
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Topic topic = dataSnapshot.getValue(Topic.class);
                    topicList.add(topic.getNameTopic());
                }
                atcTopic.setAdapter(new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1,topicList));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.typeQuestion));
        atcTypeQuestion.setAdapter(new TypeQuestionSpinnerAdapter(getBaseContext(), R.layout.listoptionitem,
                R.id.tvOptionItem, list));
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
    public void openFileChoose()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data !=null && data.getData()!=null)
        {
            daoImageStorage.setmImgURL(data.getData());
            imgEditQuestion.setImageURI(daoImageStorage.getmImgURL());
        }
    }

    public void openDialog(int center, Question question) {
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
        dialog.setCancelable(Gravity.CENTER == center);
        EditText edtWord = dialog.findViewById(R.id.edtWord);
        EditText edtExample = dialog.findViewById(R.id.edtExample);
        EditText edtGrammar = dialog.findViewById(R.id.edtGrammar);
        EditText edtWordMeaning = dialog.findViewById(R.id.edtMeaningWord);
        EditText edtExampleMeaning = dialog.findViewById(R.id.edtExampleMeaning);
        Button btnPickImage = dialog.findViewById(R.id.btnPickImageQuestion);
        imgEditQuestion = dialog.findViewById(R.id.imgEditQuestion);
        edtWord.setText(question.getWord());
        edtWordMeaning.setText(question.getWordMeaning());
        edtExample.setText(question.getExample());
        edtExampleMeaning.setText(question.getExampleMeaning());
        edtGrammar.setText(question.getGrammar());
        if (question.getUrlImage().trim().length()>0||!(question.getUrlImage().isEmpty()))
        {
            Glide.with(getBaseContext()).load(question.getUrlImage()).into(imgEditQuestion);
        }
        Spinner spnTypeWord = dialog.findViewById(R.id.spnTypeWordLearnQuestion);
        if (question.getTypeWord().isEmpty() || question.getTypeWord().equalsIgnoreCase(DEFAULTVALUE.DEFAULTVALUE)){
        }
        else
        {
            spnTypeWord.setSelection(setSelectedSpinner(spnTypeWord,question.getTypeWord()));
        }
        btnPickImage.setOnClickListener(v -> openFileChoose());
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(v -> dialog.dismiss());
        btnYes.setOnClickListener(v -> {
            Map<String,Object> map =  new HashMap<>();
            map.put("example",edtExample.getText().toString());
            map.put("typeWord",spnTypeWord.getSelectedItem().toString());
            map.put("word",edtWord.getText().toString());
            map.put("grammar",edtGrammar.getText().toString());
            map.put("wordMeaning",edtWordMeaning.getText().toString());
            map.put("exampleMeaning",edtExampleMeaning.getText().toString());
            if (question.getWord().equalsIgnoreCase(edtWord.getText().toString())&&
            question.getWordMeaning().equalsIgnoreCase(edtWordMeaning.getText().toString())&&
            question.getNameTypeQuestion().equalsIgnoreCase(spnTypeWord.getSelectedItem().toString())&&
                    question.getExampleMeaning().equalsIgnoreCase(edtExampleMeaning.getText().toString())&&
                    question.getExample().equalsIgnoreCase(edtExample.getText().toString()))
            { }
            else
            {
                daoQuestion.updateLearnQuestion(question.getId(),map,edtWord,edtExample,edtGrammar);
            }
            daoImageStorage.uploadFileImageToQuestion("Question"+question.getId(),question);
        });
        dialog.show();
    }
}