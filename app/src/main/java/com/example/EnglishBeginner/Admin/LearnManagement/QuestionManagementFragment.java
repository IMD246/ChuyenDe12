package com.example.EnglishBeginner.Admin.LearnManagement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.Admin.Adapter.QuestionAdapter;
import com.example.EnglishBeginner.Admin.DAO.DAOImageStorage;
import com.example.EnglishBeginner.Admin.DAO.DAOQuestion;
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
import java.util.List;


public class QuestionManagementFragment extends Fragment implements View.OnClickListener {

    private DAOQuestion daoQuestion;
    private QuestionInterface questionInterface;
    private AutoCompleteTextView atcTopic,svQuestion;
    String topic = DEFAULTVALUE.TOPIC, typeQuestion = DEFAULTVALUE.TYPEQUESTION;
    private QuestionAdapter questionAdapter;
    private DAOImageStorage daoImageStorage;
    private View v;
    private ImageView imgQuestion;
    private int check = 0;

    public QuestionManagementFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_question_management, container, false);
        initUI(v);
        getDataFromRealTime();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        initUI(v);
        getDataFromRealTime();
    }
    private void initUI(View v) {
        questionInterface = (QuestionInterface) getActivity();
        daoQuestion = new DAOQuestion(getContext());
        questionAdapter = new QuestionAdapter(getContext());
        daoImageStorage = new DAOImageStorage(getContext());
        RecyclerView rcvQuestion = v.findViewById(R.id.rcvQuestion);
        svQuestion = v.findViewById(R.id.svQuestion);
        ImageView imgAdd = v.findViewById(R.id.imgAddQuestion);
        imgAdd.setOnClickListener(this);
        atcTopic = v.findViewById(R.id.atcQuestion_Topic);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(rcvQuestion.VERTICAL);
        rcvQuestion.setLayoutManager(linearLayoutManager);
        questionAdapter.setQuestionList(daoQuestion.getQuestionList());
        rcvQuestion.setAdapter(questionAdapter);
        questionAdapter.setMyDelegationLevel(new QuestionAdapter.MyDelegationLevel() {
            @Override
            public void editItem(Question question) {
                questionInterface.goToDetailQuestionFragment(question);
            }
            @Override
            public void deleteItem(Question question) {
                alertDialog(question);
            }
        });
        svQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                questionAdapter.getFilter().filter(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        atcTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (atcTopic.getText().toString().isEmpty()) {
                } else {
                    topic = atcTopic.getText().toString();
                    questionAdapter.setListDependOnTopic(topic);
                }
            }
        });
    }

    // Xây dựng một Hộp thoại thông báo
    private void alertDialog(Question question) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage("Bạn có muốn xóa không?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Có",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        daoQuestion.setContext(getContext());
                        daoQuestion.deleteDataToFire(question);
                    }
                });

        builder1.setNegativeButton(
                "Không",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void getDataFromRealTime() {
        daoQuestion.getDataFromRealTimeToList(questionAdapter);
        List<String>titleList = new ArrayList<>();
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("listquestion");
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (titleList!=null)
                {
                    titleList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Question question = dataSnapshot.getValue(Question.class);
                    titleList.add(question.getTitle());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        svQuestion.setAdapter(new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,titleList));
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
                atcTopic.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,topicList));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgAddQuestion:
                openDialog(Gravity.CENTER);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data !=null && data.getData()!=null)
        {
            daoImageStorage.setmImgURL(data.getData());
            imgQuestion.setImageURI(daoImageStorage.getmImgURL());
        }
    }
    // hàm mở file chọn ảnh trong thiết bị
    public void openFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }
    public void openDialog(int center) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addeditquestion);

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
        EditText edtTitleQuestion = dialog.findViewById(R.id.svTitleQuestion);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Thêm dữ liệu");
        EditText edtCorrectAnswer = dialog.findViewById(R.id.edtCorrectAnswer);
        Spinner spnTopic = dialog.findViewById(R.id.spnQuestion_Topic);
        Spinner spnCategoryWord = dialog.findViewById(R.id.spnCategoryWord);
        List<String> listTopic = new ArrayList<>();
        EditText edtWord = dialog.findViewById(R.id.edtWordItem);
        EditText edtMeaning = dialog.findViewById(R.id.edtMeaningItem);
        Spinner spnTypeWord = dialog.findViewById(R.id.spnTypeWordItem);
        EditText edtExample = dialog.findViewById(R.id.edtExample);
        EditText edtGrammar = dialog.findViewById(R.id.edtGrammar);
        EditText edtExampleMeaning = dialog.findViewById(R.id.edtExampleMeaning);
        Button btnPickImage = dialog.findViewById(R.id.btnPickImageQuestion);
        imgQuestion = dialog.findViewById(R.id.imgEditQuestion);
        btnPickImage.setOnClickListener(v -> openFileChoose());
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        btnYes.setText("Thêm");
        for (Topic topic : questionInterface.daoTopic.getTopicList()) {
            listTopic.add(topic.getNameTopic());
        }
        spnTopic.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listTopic));
        btnNo.setOnClickListener(v -> dialog.dismiss());
        btnYes.setOnClickListener(v -> {
            Question question = new Question();
            if (daoQuestion.getQuestionList().size() > 0) {
                question.setId(daoQuestion.getQuestionList().get(daoQuestion.getQuestionList().size() - 1).getId() + 1);
            } else {
                question.setId(1);
            }
            question.setWord(edtWord.getText().toString());
            question.setCategoryWord(spnCategoryWord.getSelectedItem().toString());
            question.setWordMeaning(edtMeaning.getText().toString());
            question.setExample(edtExample.getText().toString());
            question.setExampleMeaning(edtExampleMeaning.getText().toString());
            question.setTitle(edtTitleQuestion.getText().toString());
            question.setCorrectAnswer(edtCorrectAnswer.getText().toString());
            question.setNameTopic(spnTopic.getSelectedItem().toString());
            question.setTypeWord(spnTypeWord.getSelectedItem().toString());
            question.setGrammar(edtGrammar.getText().toString());
            for (Topic topic : questionInterface.daoTopic.getTopicList()) {
                if (question.getNameTopic().equalsIgnoreCase(topic.getNameTopic())) {
                    question.setIdTopic(topic.getId());
                    break;
                }
            }
            boolean[] check = new boolean[4];
            Arrays.fill(check, true);
            if (question.getTitle().trim().isEmpty()) {
                check[0] = false;
            } else if (question.getCorrectAnswer().trim().isEmpty()) {
                check[1] = false;
            } else if (question.getWord().trim().isEmpty()) {
                check[2] = false;
            } else {
                if (daoQuestion.getQuestionList().size() > 0) {
                    for (Question question1 : daoQuestion.getQuestionList()) {
                        if (question1.getNameTopic().equalsIgnoreCase(question.getNameTopic()) &&
                                question1.getTitle().equalsIgnoreCase(question.getTitle())
                        && question1.getWord().equalsIgnoreCase(question.getWord())) {
                            check[3] = false;
                            break;
                        }
                    }
                }
            }
            if (!check[0]) {
                edtTitleQuestion.setError("Không bỏ trống");
                edtTitleQuestion.requestFocus();
            } else if (!check[1]) {
                edtCorrectAnswer.setError("Không để trống");
                edtCorrectAnswer.requestFocus();
            }
            else if (!check[2]) {
                edtWord.setError("Không bỏ trống");
                edtWord.requestFocus();
            }
            else if (!check[3]) {
                edtTitleQuestion.setError("Trùng dữ liệu , hãy kiểm tra lại dữ liệu");
                edtTitleQuestion.requestFocus();
            } else {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listquestion");
                databaseReference.child(String.valueOf(question.getId())).setValue(question).addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        edtCorrectAnswer.setText("");
                        edtExample.setText("");
                        edtTitleQuestion.setText("");
                        edtGrammar.setText("");
                        edtMeaning.setText("");
                        edtExampleMeaning.setText("");
                        edtWord.setText("");
                        Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            daoImageStorage.uploadFileImageToQuestion("Question "+question.getId(),question,imgQuestion,1);
        });
        dialog.show();
    }
}