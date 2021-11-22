package com.example.EnglishBeginner.Admin.LearnManagement;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
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
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.EnglishBeginner.Admin.Adapter.QuestionAdapter;
import com.example.EnglishBeginner.Admin.Adapter.TopicSpinnerAdapter;
import com.example.EnglishBeginner.Admin.Adapter.TypeQuestionSpinnerAdapter;
import com.example.EnglishBeginner.Admin.DAO.DAOQuestion;
import com.example.EnglishBeginner.Admin.DTO.Question;
import com.example.EnglishBeginner.Admin.DTO.Topic;
import com.example.EnglishBeginner.Admin.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.Admin.DTO.Word;
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

    private final DAOQuestion daoQuestion;
    private SearchView svQuestion;
    private QuestionInterface questionInterface;
    private AutoCompleteTextView atcTopic, atcTypeQuestion;
    String topic = DEFAULTVALUE.TOPIC, typeQuestion = DEFAULTVALUE.TYPEQUESTION;
    private QuestionAdapter questionAdapter;
    private View v;

    public QuestionManagementFragment() {
        daoQuestion = new DAOQuestion(getContext());
        questionAdapter = new QuestionAdapter(getContext());
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
        List<String>list = Arrays.asList(getResources().getStringArray(R.array.typeQuestion));
        atcTypeQuestion.setAdapter(new TypeQuestionSpinnerAdapter(getContext(), R.layout.listoptionitem,
                R.id.tvOptionItem, list));
        atcTopic.setAdapter(new TopicSpinnerAdapter(getContext(), R.layout.listoptionitem,
                R.id.tvOptionItem, questionInterface.daoTopic.getTopicList()));
    }

    private void initUI(View v) {
        questionInterface = (QuestionInterface) getActivity();
        RecyclerView rcvQuestion = v.findViewById(R.id.rcvQuestion);
        svQuestion = v.findViewById(R.id.svQuestion);
        ImageView imgAdd = v.findViewById(R.id.imgAddQuestion);
        imgAdd.setOnClickListener(this);
        atcTopic = v.findViewById(R.id.atcQuestion_Topic);
        atcTypeQuestion = v.findViewById(R.id.atcQuestion_TypeQuestion);
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
        svQuestion.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                questionAdapter.getFilter().filter(newText);
                return false;
            }
        });
        atcTypeQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (atcTypeQuestion.getText().toString().isEmpty())
                {}
                else {
                    typeQuestion = atcTypeQuestion.getText().toString();
                    questionAdapter.setListDependOnTopicAndTypeQuestion(topic, typeQuestion);
                }
            }
        });
        atcTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (atcTopic.getText().toString().isEmpty())
                {
                }
                else {
                    topic = atcTopic.getText().toString();
                    questionAdapter.setListDependOnTopicAndTypeQuestion(topic, typeQuestion);
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
        daoQuestion.getDataFromRealTimeToList(questionAdapter,null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgAddQuestion:
                openDialog(Gravity.CENTER);
        }
    }

    public void openDialog(int center) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addquestion);

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
        AutoCompleteTextView svTitleQuestion = dialog.findViewById(R.id.svTitleQuestion);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listWord");
        List<String> listWord = new ArrayList<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listWord!=null)
                {
                    listWord.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Word word = dataSnapshot.getValue(Word.class);
                    listWord.add(word.getWord());
                }
                ArrayAdapter<String>arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,listWord);
                svTitleQuestion.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        EditText edtCorrectAnswer = dialog.findViewById(R.id.edtCorrectAnswer);
        Spinner spnTopic = dialog.findViewById(R.id.spnQuestion_Topic);
        Spinner spnTypeQuestion = dialog.findViewById(R.id.spnQuestion_TypeQuestion);
        List<String> listTopic = new ArrayList<>();
        List<String>list = Arrays.asList(getResources().getStringArray(R.array.typeQuestion));
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        btnYes.setText("Add");
        for (Topic topic : questionInterface.daoTopic.getTopicList()) {
            listTopic.add(topic.getNameTopic());
        }
        spnTopic.setAdapter(new ArrayAdapter<>(getContext(), R.layout.listoptionitem, R.id.tvOptionItem, listTopic));
        spnTypeQuestion.setAdapter(new ArrayAdapter<>(getContext(), R.layout.listoptionitem, R.id.tvOptionItem, list));
        btnNo.setOnClickListener(v -> dialog.dismiss());
        btnYes.setOnClickListener(v -> {
            Question question = new Question();
            if (daoQuestion.getQuestionList().size()>0) {
                question.setId(daoQuestion.getQuestionList().get(daoQuestion.getQuestionList().size() - 1).getId() + 1);
                Log.d("Test", "openDialog: ");
            }
            else {
                question.setId(1);
            }
            question.setTitle(svTitleQuestion.getText().toString());
            question.setCorrectAnswer(edtCorrectAnswer.getText().toString());
            question.setNameTopic(spnTopic.getSelectedItem().toString());
            question.setNameTypeQuestion(spnTypeQuestion.getSelectedItem().toString());
            for (Topic topic : questionInterface.daoTopic.getTopicList()) {
                if (question.getNameTopic().equalsIgnoreCase(topic.getNameTopic())) {
                    question.setIdTopic(topic.getId());
                    break;
                }
            }
            daoQuestion.setContext(getContext());
            daoQuestion.addDataToFireBase(question, svTitleQuestion,edtCorrectAnswer);
        });
        dialog.show();
    }
}