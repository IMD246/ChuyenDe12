package com.example.EnglishBeginner.Admin.LearnManagement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.Admin.Adapter.AnswerAdapter;
import com.example.EnglishBeginner.Admin.DAO.DAOAnswer;
import com.example.EnglishBeginner.Admin.DAO.DAOImageStorage;
import com.example.EnglishBeginner.Admin.DAO.DAOQuestion;
import com.example.EnglishBeginner.Admin.DTO.Answer;
import com.example.EnglishBeginner.Admin.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.Admin.DTO.Question;
import com.example.EnglishBeginner.Admin.DTO.Topic;
import com.example.EnglishBeginner.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailQuestionFragment extends Fragment implements View.OnClickListener {

    private View view;
    private DAOAnswer daoAnswer;
    private AnswerAdapter answerAdapter;
    private QuestionInterface questionInterface;
    private DAOQuestion daoQuestion;
    private DAOImageStorage daoImageStorage;
    private ImageView imgAnswer, imgQuestion;
    private int openChooseFile = 0;
    private Question question;
    private TextView tvTitle, tvCorrectAnswer, tvWord, tvMeaningWord, tvExample, tvExampleMeaning, tvGrammar;
    private int idAnswer = 1;

    public DetailQuestionFragment() {
    }

    @SuppressLint({"SetTextI18n", "UseRequireInsteadOfGet"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_question_fragment, container, false);
        Bundle bundleReceiver = getArguments();
        initUI();
        if (bundleReceiver != null) {
            question = (Question) bundleReceiver.get("object_question");
            tvTitle.setText("Câu hỏi: " + question.getTitle());
            tvCorrectAnswer.setText("Câu Trả Lời Chính xác: " + question.getCorrectAnswer());
            tvWord.setText("Từ vựng: "+question.getWord() +" ("+ question.getTypeWord()+")");
            tvMeaningWord.setText("Nghĩa từ: " + question.getWordMeaning());
            tvExample.setText("Ví dụ: " + question.getExample());
            tvExampleMeaning.setText("Nghĩa ví dụ: " + question.getExampleMeaning());
            tvCorrectAnswer.setText("Câu Trả Lời Chính xác: " + question.getCorrectAnswer());
            tvGrammar.setText("Ngữ pháp: "+question.getGrammar());
        }
        getDataFromFirebase();
        return view;
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // ánh xạ cho các view và set adapter
    @SuppressLint("SetTextI18n")
    private void initUI() {
        questionInterface = (QuestionInterface) getActivity();
        tvTitle = view.findViewById(R.id.tvTitleQuestionDetail);
        tvExample = view.findViewById(R.id.tvExample);
        tvExampleMeaning = view.findViewById(R.id.tvExampleMeaning);
        tvWord = view.findViewById(R.id.tvWord);
        tvMeaningWord = view.findViewById(R.id.tvMeaningWord);
        tvGrammar = view.findViewById(R.id.tvGrammar);
        tvCorrectAnswer = view.findViewById(R.id.tvCorrectAnswerQuestion);
        answerAdapter = new AnswerAdapter(getContext());
        daoAnswer = new DAOAnswer(getContext());
        daoImageStorage = new DAOImageStorage(getContext());
        daoQuestion = new DAOQuestion(getContext());
        RecyclerView rcvAnswer = view.findViewById(R.id.rcvAnswer);
        FloatingActionButton flAdd = view.findViewById(R.id.flAdd);
        flAdd.setOnClickListener(this);
        FloatingActionButton flEdit = view.findViewById(R.id.flEdit);
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
        if (getContext() != null) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());

            builder1.setMessage("Bạn có muốn xóa không?");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "Có",
                    (dialog, id) -> daoAnswer.deleteDataToFire(answer, question.getId()));

            builder1.setNegativeButton(
                    "Không",
                    (dialog, id) -> dialog.cancel());

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    private void getDataFromFirebase() {
        daoAnswer.getDataFromFirebase(question.getId(), answerAdapter);
        daoAnswer.getAQuestionFromFirebase(question.getId());
    }

    private int getSelectedSpinner(Spinner spinner, String word) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(word)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() == null) {
            return;
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((view, keyCode, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                    return true;
                }
            }
            return false;
        });
    }

    // hộp thoại sửa dữ liệu Question
    @SuppressLint("SetTextI18n")
    public void openDialogEditQuestion(int center, Question question) {
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
        dialog.setCancelable(Gravity.CENTER == center);
        EditText edtTitleQuestion = dialog.findViewById(R.id.svTitleQuestion);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        EditText edtCorrectAnswer = dialog.findViewById(R.id.edtCorrectAnswer);
        Spinner spnTopic = dialog.findViewById(R.id.spnQuestion_Topic);
        List<String> listTopic = new ArrayList<>();
        EditText edtWord = dialog.findViewById(R.id.edtWordItem);
        EditText edtMeaning = dialog.findViewById(R.id.edtMeaningItem);
        Spinner spnTypeWord = dialog.findViewById(R.id.spnTypeWordItem);
        Spinner spnCategoryWord = dialog.findViewById(R.id.spnCategoryWord);
        EditText edtExample = dialog.findViewById(R.id.edtExample);
        EditText edtGrammar = dialog.findViewById(R.id.edtGrammar);
        EditText edtExampleMeaning = dialog.findViewById(R.id.edtExampleMeaning);
        Button btnPickImage = dialog.findViewById(R.id.btnPickImageQuestion);
        imgQuestion = dialog.findViewById(R.id.imgEditQuestion);
        btnPickImage.setOnClickListener(v -> {
            openFileChoose();
            openChooseFile = 1;
        });
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        btnYes.setText("Sửa");
        tvTitle.setText("Sửa dữ liệu");
        for (Topic topic : questionInterface.daoTopic.getTopicList()) {
            listTopic.add(topic.getNameTopic());
        }
        spnTopic.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,listTopic));
        if (question != null) {
            edtWord.setText(question.getWord());
            edtMeaning.setText(question.getWordMeaning());
            edtExample.setText(question.getExample());
            edtExampleMeaning.setText(question.getExampleMeaning());
            spnTopic.setSelection(getSelectedSpinner(spnTopic, question.getNameTopic()));
            edtTitleQuestion.setText(question.getTitle());
            edtGrammar.setText(question.getGrammar());
            edtCorrectAnswer.setText(question.getCorrectAnswer());
            spnTypeWord.setSelection(getSelectedSpinner(spnTypeWord, question.getTypeWord()));
            spnCategoryWord.setSelection(getSelectedSpinner(spnCategoryWord, question.getCategoryWord()));
            if (question.getUrlImage().trim().isEmpty()
                    || question.getUrlImage().trim().length()==0
            )
            { }
            else
            {
                Glide.with(questionInterface.getBaseContext()).load(question.getUrlImage()).into(imgQuestion);
            }
        }
        btnNo.setOnClickListener(v -> dialog.dismiss());
        btnYes.setOnClickListener(v -> {
            Question question1 = new Question();
            assert question != null;
            question1.setId(question.getId());
            question1.setTitle(edtTitleQuestion.getText().toString());
            question1.setCorrectAnswer(edtCorrectAnswer.getText().toString());
            question1.setGrammar(edtGrammar.getText().toString());
            question1.setExample(edtExample.getText().toString());
            question1.setExampleMeaning(edtExampleMeaning.getText().toString());
            question1.setTypeWord(spnTypeWord.getSelectedItem().toString());
            question1.setCategoryWord(spnCategoryWord.getSelectedItem().toString());
            question1.setWordMeaning(edtMeaning.getText().toString());
            question1.setWord(edtWord.getText().toString());
            question1.setNameTopic(spnTopic.getSelectedItem().toString());
            for (Topic topic : questionInterface.daoTopic.getTopicList()) {
                if (question1.getNameTopic().equalsIgnoreCase(topic.getNameTopic())) {
                    question1.setIdTopic(topic.getId());
                    break;
                }
            }
            daoQuestion.editDataToFireBase(question1, edtTitleQuestion, edtWord, edtCorrectAnswer, tvTitle, tvCorrectAnswer);
            daoImageStorage.uploadFileImageToQuestion("Question " + question.getId(), question1, imgQuestion, 2);
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            daoImageStorage.setmImgURL(data.getData());
            if (openChooseFile == 1) {
                imgQuestion.setImageURI(daoImageStorage.getmImgURL());
            } else {
                imgAnswer.setImageURI(daoImageStorage.getmImgURL());
            }
        }
    }

    // hàm mở file chọn ảnh trong thiết bị
    public void openFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    // hộp thoại để thêm và sửa Answer
    @SuppressLint("SetTextI18n")
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
        dialog.setCancelable(Gravity.CENTER == center);
        EditText edtAnswer = dialog.findViewById(R.id.edtAnswer);
        EditText edtQuestion = dialog.findViewById(R.id.edtTitleQuestion);
        edtQuestion.setText(question.getTitle());
        TextView tvThemSua = dialog.findViewById(R.id.tvTitle);
        Button btnPickImageTopic = dialog.findViewById(R.id.btnPickImageTopic);
        imgAnswer = dialog.findViewById(R.id.imgAnswer);
        btnPickImageTopic.setOnClickListener(v -> openFileChoose());
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(v -> dialog.dismiss());
        if (choice == 2) {
            btnYes.setText("Sửa");
            tvThemSua.setText("Sửa dữ liệu");
            edtAnswer.setText(answer.getAnswerQuestion());
            if (answer.getUrlImage().isEmpty()) {
            } else {
                if (getContext() != null) {
                    Glide.with(getContext()).load(answer.getUrlImage()).into(imgAnswer);
                }
            }
            btnYes.setOnClickListener(v -> {
                Answer answer1 = new Answer();
                answer1.setId(answer.getId());
                answer1.setAnswerQuestion(edtAnswer.getText().toString());
                if (!(answer.getUrlImage().isEmpty() || answer.getUrlImage().trim().length() == 0)) {
                    answer1.setUrlImage(answer.getUrlImage());
                }
                if (answer.getAnswerQuestion().equalsIgnoreCase(answer1.getAnswerQuestion())) {
                    edtAnswer.setError("Trùng dữ liệu");
                    edtAnswer.requestFocus();
                } else {
                    daoAnswer.setContext(getContext());
                    daoAnswer.editDataToFireBase(answer1, edtAnswer, question.getId());
                }
                daoImageStorage.uploadFileImageToAnswer(2, imgAnswer, "Question" + question.getId() + "Answer" + answer1.getId(), answer1, question.getId());
            });
        } else if (choice == 1) {
            btnYes.setText("Thêm");
            btnYes.setOnClickListener(v -> {
                {
                    Answer answer1 = new Answer();
                    if (daoAnswer.getAnswerList().size() < 4) {
                        if (daoAnswer.getAnswerList().size() > 0) {
                            idAnswer = daoAnswer.getAnswerList().get(daoAnswer.getAnswerList().size() - 1).getId() + 1;
                        }
                        answer1.setId(idAnswer);
                        answer1.setAnswerQuestion(edtAnswer.getText().toString());
                        answer1.setUrlImage("");
                        daoAnswer.setContext(getContext());
                        daoAnswer.addDataAnswerToFirebaseQuestion(answer1, edtAnswer, question.getId());
                        daoImageStorage.uploadFileImageToAnswer(1, imgAnswer, "Question" + question.getId() + "Answer" + answer1.getId(), answer1, question.getId());
                    } else {
                        DEFAULTVALUE.alertDialogMessage("Thông báo", "Chỉ được thêm 4 câu hỏi", getContext());
                    }
                }
            });
        }
        dialog.show();
    }

    // hàm onClick cho các view theo id
    @SuppressLint("NonConstantResourceId")
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