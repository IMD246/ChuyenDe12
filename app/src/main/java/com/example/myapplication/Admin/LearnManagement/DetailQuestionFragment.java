package com.example.myapplication.Admin.LearnManagement;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Admin.LearnManagement.Adapter.AnswerAdapter;
import com.example.myapplication.Admin.LearnManagement.DAO.DAOAnswer;
import com.example.myapplication.Admin.LearnManagement.DAO.DAOImageStorage;
import com.example.myapplication.Admin.LearnManagement.DAO.DAOQuestion;
import com.example.myapplication.Admin.LearnManagement.DAO.DAOTopic;
import com.example.myapplication.Admin.LearnManagement.DAO.DAOTypeQuestion;
import com.example.myapplication.Admin.LearnManagement.DTO.Answer;
import com.example.myapplication.Admin.LearnManagement.DTO.Question;
import com.example.myapplication.Admin.LearnManagement.DTO.Topic;
import com.example.myapplication.Admin.LearnManagement.DTO.TypeQuestion;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DetailQuestionFragment extends Fragment implements View.OnClickListener{

    private View view;
    private DAOAnswer daoAnswer;
    private AnswerAdapter answerAdapter;
    private DAOTopic daoTopic;
    FloatingActionButton flAdd,flEdit;
    private DAOQuestion daoQuestion;
    private DAOTypeQuestion daoTypeQuestion;
    private DAOImageStorage daoImageStorage;
    private RecyclerView rcvAnswer;
    private Question question;
    public DetailQuestionFragment() {
        daoAnswer = new DAOAnswer(getContext());
        daoImageStorage = new DAOImageStorage(getContext());
        daoTypeQuestion = new DAOTypeQuestion(getContext());
        daoTopic = new DAOTopic(getContext());
        daoQuestion = new DAOQuestion(getContext());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_detail_question_fragment, container, false);
        Bundle bundleReceiver = getArguments();
        if (bundleReceiver != null)
        {
            question = (Question) bundleReceiver.get("object_question");
        }
        initUI();
        getDataFromFirebase();
        return view;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (getView() == null) {
            return;
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().getSupportFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }
    private void initUI() {
        rcvAnswer = view.findViewById(R.id.rcvAnswer);
        flAdd = view.findViewById(R.id.flAdd);
        flAdd.setOnClickListener(this);
        flEdit = view.findViewById(R.id.flEdit);
        flEdit.setOnClickListener(this);
        answerAdapter = new AnswerAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvAnswer.setLayoutManager(linearLayoutManager);
        answerAdapter.setAnswerList(daoAnswer.getAnswerList());
        rcvAnswer.setAdapter(answerAdapter);
        answerAdapter.setMyDelegationLevel(new AnswerAdapter.MyDelegationLevel() {
            @Override
            public void editItem(Answer answer) {

            }

            @Override
            public void deleteItem(Answer answer) {

            }
        });
    }
    private void getDataFromFirebase()
    {
        if (question!=null)
        {
            daoAnswer.getDataFromFirebase(question.getId());
        }
        daoTopic.getDataFromRealTimeFirebase(null);
        daoTypeQuestion.getDataFromRealTimeToList(null);
    }
    private int getSelectedSpinner(Spinner spinner , String word)
    {
        for (int i=0; i< spinner.getCount();i++)
        {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(word))
            {
                return i;
            }
        }
        return 0;
    }
    public void openDialogEditQuestion(int center,Question question) {
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
        TextView tvThemSua = dialog.findViewById(R.id.tvThemSua);
        Spinner spnTopic = dialog.findViewById(R.id.spnQuestion_Topic);
        Spinner spnTypeQuestion = dialog.findViewById(R.id.spnQuestion_TypeQuestion);
        List<String> listTopic = new ArrayList<>();
        List<String> listTypeQuestion = new ArrayList<>();
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        btnYes.setText("Sửa");
        tvThemSua.setText("Sửa dữ liệu");
        for (Topic topic : daoTopic.getTopicList()) {
            listTopic.add(topic.getNameTopic());
        }
        for (TypeQuestion typeQuestion : daoTypeQuestion.getTypeQuestionList()) {
            listTypeQuestion.add(typeQuestion.getTypeQuestionName());
        }
        spnTopic.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.listoptionitem, R.id.tvOptionItem, listTopic));
        spnTypeQuestion.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.listoptionitem, R.id.tvOptionItem, listTypeQuestion));
        if (question != null)
        {
            spnTopic.setSelection(getSelectedSpinner(spnTopic,question.getNameTopic()));
            spnTypeQuestion.setSelection(getSelectedSpinner(spnTopic,question.getNameTypeQuestion()));
            edtTitle.setText(question.getTitle());
            edtCorrectAnswer.setText(question.getCorrectAnswer());
        }
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question question1 = new Question();
                question.setId(question.getId());
                question.setTitle(edtTitle.getText().toString());
                question.setCorrectAnswer(edtCorrectAnswer.getText().toString());
                question.setNameTopic(spnTopic.getSelectedItem().toString());
                question.setNameTypeQuestion(spnTypeQuestion.getSelectedItem().toString());
                for (Topic topic : daoTopic.getTopicList()) {
                    if (question.getNameTopic().equalsIgnoreCase(topic.getNameTopic())) {
                        question.setIdTopic(topic.getId());
                        break;
                    }
                }
                for (TypeQuestion typeQuestion : daoTypeQuestion.getTypeQuestionList()) {
                    if (question.getNameTypeQuestion().equalsIgnoreCase(typeQuestion.getTypeQuestionName())) {
                        question.setIdTypeQuestion(typeQuestion.getId());
                        break;
                    }
                }
                daoQuestion.setContext(getContext());
                daoQuestion.editDataToFireBase(question, edtTitle,edtCorrectAnswer);
            }
        });
        dialog.show();
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.flEdit: openDialogEditQuestion(Gravity.CENTER,question);
            break;
            case R.id.flAdd:
                break;
        }
    }
}