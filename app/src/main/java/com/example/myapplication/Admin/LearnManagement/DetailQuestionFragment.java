package com.example.myapplication.Admin.LearnManagement;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.Admin.Adapter.AnswerAdapter;
import com.example.myapplication.Admin.DAO.DAOAnswer;
import com.example.myapplication.Admin.DAO.DAOImageStorage;
import com.example.myapplication.Admin.DAO.DAOQuestion;
import com.example.myapplication.Admin.DAO.DAOTopic;
import com.example.myapplication.Admin.DAO.DAOTypeQuestion;
import com.example.myapplication.Admin.DTO.Answer;
import com.example.myapplication.Admin.DTO.Question;
import com.example.myapplication.Admin.DTO.Topic;
import com.example.myapplication.Admin.DTO.TypeQuestion;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailQuestionFragment extends Fragment implements View.OnClickListener {

    private View view;
    private DAOAnswer daoAnswer;
    private AnswerAdapter answerAdapter;
    private QuestionInterface questionInterface;
    private FloatingActionButton flAdd, flEdit;
    private DAOQuestion daoQuestion;
    private DAOImageStorage daoImageStorage;
    private RecyclerView rcvAnswer;
    private ImageView imgAnswer;
    private Question question;
    private TextView tvTitle,tvCorrectAnswer;
    int idAnswer = 1;
    Question question2;

    public DetailQuestionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_question_fragment, container, false);
        Bundle bundleReceiver = getArguments();
        if (bundleReceiver != null) {
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
    // ánh xạ cho các view và set adapter
    private void initUI() {
        questionInterface = (QuestionInterface) getActivity();
        tvTitle = view.findViewById(R.id.tvTitleQuestionDetail);
        tvCorrectAnswer = view.findViewById(R.id.tvCorrectAnswerQuestion);
        tvTitle.setText("Câu hỏi: "+question.getTitle());
        tvCorrectAnswer.setText("Câu Trả Lời Chính xác: "+question.getCorrectAnswer());
        answerAdapter = new AnswerAdapter(getContext());
        daoAnswer = new DAOAnswer(getContext());
        daoImageStorage = new DAOImageStorage(getContext());
        daoQuestion = new DAOQuestion(getContext());
        rcvAnswer = view.findViewById(R.id.rcvAnswer);
        flAdd = view.findViewById(R.id.flAdd);
        flAdd.setOnClickListener(this);
        flEdit = view.findViewById(R.id.flEdit);
        flEdit.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvAnswer.setLayoutManager(linearLayoutManager);
        answerAdapter.setAnswerList(daoAnswer.getAnswerList());
        rcvAnswer.setAdapter(answerAdapter);
        answerAdapter.setMyDelegationLevel(new AnswerAdapter.MyDelegationLevel() {
            @Override
            public void editItem(Answer answer) {
                openDiaLogAnswer(Gravity.CENTER, 2, answer);
            }

            @Override
            public void deleteItem(Answer answer) {
                alertDialogtoDelete(answer);
            }
        });
    }
    // hộp thoại để thống báo có xóa dữ liệu hay không
    private void alertDialogtoDelete(Answer answer) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage("Bạn có muốn xóa không?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Có",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        daoAnswer.deleteDataToFire(answer, question.getId());
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
    private void getDataFromFirebase() {
        daoAnswer.getDataFromFirebase(question.getId(), answerAdapter);
    }

    private int getSelectedSpinner(Spinner spinner, String word) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(word)) {
                return i;
            }
        }
        return 0;
    }
    // hộp thoại sửa dữ liệu Question
    public void openDialogEditQuestion(int center, Question question) {
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
        for (Topic topic : questionInterface.daoTopic.getTopicList()) {
            listTopic.add(topic.getNameTopic());
        }
        for (TypeQuestion typeQuestion : questionInterface.daoTypeQuestion.getTypeQuestionList()) {
            listTypeQuestion.add(typeQuestion.getTypeQuestionName());
        }
        spnTopic.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.listoptionitem, R.id.tvOptionItem, listTopic));
        spnTypeQuestion.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.listoptionitem, R.id.tvOptionItem, listTypeQuestion));
        if (question != null) {
            spnTopic.setSelection(getSelectedSpinner(spnTopic, question.getNameTopic()));
            spnTypeQuestion.setSelection(getSelectedSpinner(spnTopic, question.getNameTypeQuestion()));
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
                daoQuestion.editDataToFireBase(question, edtTitle, edtCorrectAnswer,tvTitle,tvCorrectAnswer);
            }
        });
        dialog.show();
    }
    // hộp thoại để thêm và sửa Answer
    public void openDiaLogAnswer(int center, int choice, Answer answer) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addeditanswer);
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
        for (Question question4 : daoQuestion.getQuestionList()) {
            if (question.getId() == question4.getId()) {
                question2 = question4;
                break;
            }
        }
        EditText edtAnswer = dialog.findViewById(R.id.edtAnswer);
        EditText edtQuestion = dialog.findViewById(R.id.edtTitleQuestion);
        edtQuestion.setText(question.getTitle());
        TextView tvThemSua = dialog.findViewById(R.id.tvThemSua);
        Button btnPickImageTopic = dialog.findViewById(R.id.btnPickImageTopic);
        LinearLayout linearLayout = dialog.findViewById(R.id.lnpickimage);
        if (daoAnswer.getAnswerList().size() > 0) {
            idAnswer = questionInterface.daoTopic.getTopicList().get(questionInterface.daoTopic.getTopicList().size() - 1).getId() + 1;
        }
        if (question.getNameTypeQuestion().equalsIgnoreCase("Image")) {
        } else {
            linearLayout.setVisibility(View.GONE);
        }
        imgAnswer = dialog.findViewById(R.id.imgAnswer);
        btnPickImageTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionInterface.openFileChoose();
                if (questionInterface.uri!= null) {
                    imgAnswer.setImageURI(questionInterface.uri);
                }
            }
        });
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (choice == 2)
        {
            btnYes.setText("Sửa");
            tvThemSua.setText("Sửa dữ liệu");
            edtAnswer.setText(answer.getAnswerQuestion());
            if (answer.getUrlImage().isEmpty()) {
            } else {
                Picasso.get().load(answer.getUrlImage()).resize(100, 100).into(imgAnswer);
            }
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Answer answer1 = new Answer();
                    answer1.setId(answer.getId());
                    answer1.setAnswerQuestion(edtAnswer.getText().toString());
                    if (answer.getUrlImage().isEmpty()) {
                    } else {
                        answer1.setUrlImage(answer.getUrlImage());
                    }
                    if (answer.getAnswerQuestion().equalsIgnoreCase(answer1.getAnswerQuestion())) {
                        edtAnswer.setError("Trùng dữ liệu");
                        edtAnswer.requestFocus();
                    } else {
                        daoAnswer.setContext(getContext());
                        daoAnswer.editDataToFireBase(answer1, edtAnswer, question.getId());
                    }
                    if (questionInterface.uri!= null) {
                        daoImageStorage.setmImgURL(questionInterface.uri);
                        daoImageStorage.uploadFileImageToAnswer(2, imgAnswer, "Question" + question.getId() + "Answer" + answer1.getId(), answer1, question.getId());
                    }
                }
            });
        } else if (choice == 1) {
            btnYes.setText("Thêm");
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    {
                        Answer answer1 = new Answer();
                        answer1.setId(idAnswer);
                        answer1.setAnswerQuestion(edtAnswer.getText().toString());
                        answer1.setUrlImage("");
                        daoAnswer.setContext(getContext());
                        daoAnswer.addDataToFireBase(answer1, edtAnswer, question.getId());
                        if (questionInterface.uri!=null) {
                            daoImageStorage.setmImgURL(questionInterface.uri);
                            daoImageStorage.uploadFileImageToAnswer(1, imgAnswer, "Question" + question.getId() + "Answer" + answer1.getId(), answer1, question.getId());
                        }
                    }
                }
            });
        }
        dialog.show();
    }
    // hàm onClick cho các view theo id
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flEdit:
                openDialogEditQuestion(Gravity.CENTER, question);
                break;
            case R.id.flAdd:
                openDiaLogAnswer(Gravity.CENTER, 1, null);
                break;
        }
    }
}