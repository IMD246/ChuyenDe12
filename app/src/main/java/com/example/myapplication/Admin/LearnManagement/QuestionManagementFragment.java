package com.example.myapplication.Admin.LearnManagement;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.myapplication.Admin.Adapter.QuestionAdapter;
import com.example.myapplication.Admin.Adapter.TopicSpinnerAdapter;
import com.example.myapplication.Admin.Adapter.TypeQuestionSpinnerAdapter;
import com.example.myapplication.Admin.DAO.DAOQuestion;
import com.example.myapplication.Admin.DAO.DAOTopic;
import com.example.myapplication.Admin.DAO.DAOTypeQuestion;
import com.example.myapplication.Admin.DTO.Question;
import com.example.myapplication.Admin.DTO.Topic;
import com.example.myapplication.Admin.DTO.TypeQuestion;
import com.example.myapplication.DEFAULTVALUE;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;


public class QuestionManagementFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rcvQuestion;
    private DAOQuestion daoQuestion;
    private ImageView imgAdd;
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
        atcTypeQuestion.setAdapter(new TypeQuestionSpinnerAdapter(getContext(), R.layout.listoptionitem,
                R.id.tvOptionItem, questionInterface.daoTypeQuestion.getTypeQuestionList()));
        atcTopic.setAdapter(new TopicSpinnerAdapter(getContext(), R.layout.listoptionitem,
                R.id.tvOptionItem, questionInterface.daoTopic.getTopicList()));
    }

    private void initUI(View v) {
        questionInterface = (QuestionInterface) getActivity();
        rcvQuestion = v.findViewById(R.id.rcvQuestion);
        svQuestion = v.findViewById(R.id.svQuestion);
        imgAdd = v.findViewById(R.id.imgAddQuestion);
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
                questionInterface.setFlag(true);
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
//        daoTopic.getDataFromRealTimeFirebase(null);
//        daoTypeQuestion.getDataFromRealTimeToList(null);
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
        EditText edtTitle = dialog.findViewById(R.id.edtTitle);
        EditText edtCorrectAnswer = dialog.findViewById(R.id.edtCorrectAnswer);
        Spinner spnTopic = dialog.findViewById(R.id.spnQuestion_Topic);
        Spinner spnTypeQuestion = dialog.findViewById(R.id.spnQuestion_TypeQuestion);
        List<String> listTopic = new ArrayList<>();
        List<String> listTypeQuestion = new ArrayList<>();
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        btnYes.setText("Add");
        for (Topic topic : questionInterface.daoTopic.getTopicList()) {
            listTopic.add(topic.getNameTopic());
        }
        for (TypeQuestion typeQuestion : questionInterface.daoTypeQuestion.getTypeQuestionList()) {
            listTypeQuestion.add(typeQuestion.getTypeQuestionName());
        }
        spnTopic.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.listoptionitem, R.id.tvOptionItem, listTopic));
        spnTypeQuestion.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.listoptionitem, R.id.tvOptionItem, listTypeQuestion));
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question question = new Question();
                if (daoQuestion.getQuestionList().size()>0) {
                    question.setId(daoQuestion.getQuestionList().get(daoQuestion.getQuestionList().size() - 1).getId() + 1);
                }
                else {
                    question.setId(1);
                }
                question.setTitle(edtTitle.getText().toString());
                question.setCorrectAnswer(edtCorrectAnswer.getText().toString());
                question.setNameTopic(spnTopic.getSelectedItem().toString());
                question.setNameTypeQuestion(spnTypeQuestion.getSelectedItem().toString());
                for (Topic topic : questionInterface.daoTopic.getTopicList()) {
                    if (question.getNameTopic().equalsIgnoreCase(topic.getNameTopic())) {
                        question.setIdTopic(topic.getId());
                        break;
                    }
                }
                for (TypeQuestion typeQuestion : questionInterface.daoTypeQuestion.getTypeQuestionList()) {
                    if (question.getNameTypeQuestion().equalsIgnoreCase(typeQuestion.getTypeQuestionName())) {
                        question.setIdTypeQuestion(typeQuestion.getId());
                        break;
                    }
                }
                daoQuestion.setContext(getContext());
                daoQuestion.addDataToFireBase(question, edtTitle,edtCorrectAnswer);
                daoQuestion.addDataQuestionToFireBaseTopic(question,edtTitle,edtCorrectAnswer);
                daoQuestion.addDataQuestionToFireBaseTypeQuestion(question,edtTitle,edtCorrectAnswer);
            }
        });
        dialog.show();
    }
}