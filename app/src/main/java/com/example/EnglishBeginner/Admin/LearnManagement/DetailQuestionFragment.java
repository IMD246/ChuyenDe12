package com.example.EnglishBeginner.Admin.LearnManagement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
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
import com.example.EnglishBeginner.Admin.DTO.Word;
import com.example.EnglishBeginner.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailQuestionFragment extends Fragment implements View.OnClickListener {

    private View view;
    private DAOAnswer daoAnswer;
    private AnswerAdapter answerAdapter;
    private QuestionInterface questionInterface;
    private DAOQuestion daoQuestion;
    private DAOImageStorage daoImageStorage;
    private ImageView imgAnswer;
    private Question question;
    private TextView tvTitle, tvCorrectAnswer;
    private int idAnswer = 1;
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // ánh xạ cho các view và set adapter
    @SuppressLint("SetTextI18n")
    private void initUI() {
        questionInterface = (QuestionInterface) getActivity();
        tvTitle = view.findViewById(R.id.tvTitleQuestionDetail);
        tvCorrectAnswer = view.findViewById(R.id.tvCorrectAnswerQuestion);
        tvTitle.setText("Câu hỏi: " + question.getTitle());
        tvCorrectAnswer.setText("Câu Trả Lời Chính xác: " + question.getCorrectAnswer());
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
        if (getContext()!=null) {
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
                if (getActivity()!=null) {
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
        dialog.setCancelable(Gravity.CENTER == center);
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
        TextView tvThemSua = dialog.findViewById(R.id.tvThemSua);
        Spinner spnTopic = dialog.findViewById(R.id.spnQuestion_Topic);
        Spinner spnTypeQuestion = dialog.findViewById(R.id.spnQuestion_TypeQuestion);
        List<String> listTopic = new ArrayList<>();
        List<String>listTypeQuestion = Arrays.asList(getResources().getStringArray(R.array.typeQuestion));
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        btnYes.setText("Sửa");
        tvThemSua.setText("Sửa dữ liệu");
        for (Topic topic : questionInterface.daoTopic.getTopicList()) {
            listTopic.add(topic.getNameTopic());
        }
        spnTopic.setAdapter(new ArrayAdapter<>(getContext(), R.layout.listoptionitem, R.id.tvOptionItem, listTopic));
        spnTypeQuestion.setAdapter(new ArrayAdapter<>(getContext(), R.layout.listoptionitem, R.id.tvOptionItem, listTypeQuestion));
        if (question != null) {
            spnTopic.setSelection(getSelectedSpinner(spnTopic, question.getNameTopic()));
            spnTypeQuestion.setSelection(getSelectedSpinner(spnTopic, question.getNameTypeQuestion()));
            svTitleQuestion.setText(question.getTitle());
            edtCorrectAnswer.setText(question.getCorrectAnswer());
        }
        svTitleQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edtCorrectAnswer.setText("");
            }
        });
        btnNo.setOnClickListener(v -> dialog.dismiss());
        btnYes.setOnClickListener(v -> {
            Question question1 = new Question();
            assert question != null;
            question1.setId(question.getId());
            question1.setTitle(svTitleQuestion.getText().toString());
            question1.setCorrectAnswer(edtCorrectAnswer.getText().toString());
            question1.setNameTopic(spnTopic.getSelectedItem().toString());
            question1.setNameTypeQuestion(spnTypeQuestion.getSelectedItem().toString());
            for (Topic topic : questionInterface.daoTopic.getTopicList()) {
                if (question1.getNameTopic().equalsIgnoreCase(topic.getNameTopic())) {
                    question1.setIdTopic(topic.getId());
                    break;
                }
            }
            daoQuestion.setContext(getContext());
            databaseReference.orderByChild("word").equalTo(question1.getTitle()).
                    addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Word word = dataSnapshot.getValue(Word.class);
                            question1.setWord(word.getWord());
                            question1.setTypeWord(word.getTypeWord());
                            question1.setWordMeaning(word.getMeaning());
                        }
                    }
                    else
                    {
                        question1.setWord("");
                        question1.setTypeWord("");
                        question1.setWordMeaning("");
                    }
                    daoQuestion.editDataToFireBase(question1, svTitleQuestion, edtCorrectAnswer, tvTitle, tvCorrectAnswer);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data !=null && data.getData()!=null)
        {
            daoImageStorage.setmImgURL(data.getData());
            imgAnswer.setImageURI(daoImageStorage.getmImgURL());
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
        if (!(question.getNameTypeQuestion().equalsIgnoreCase(DEFAULTVALUE.IMAGE))) {
            linearLayout.setVisibility(View.GONE);
        }
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
                if (getContext()!=null) {
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
                daoImageStorage.uploadFileImageToAnswer(2, imgAnswer, "Question"+question.getId()+"Answer" + answer1.getId(), answer1, question.getId());
            });
        } else if (choice == 1) {
            btnYes.setText("Thêm");
            btnYes.setOnClickListener(v -> {
                {
                    Answer answer1 = new Answer();
                    if (question.getNameTypeQuestion().equalsIgnoreCase(DEFAULTVALUE.IMAGE)) {
                        if (daoAnswer.getAnswerList().size()<4)
                        {
                            if (daoAnswer.getAnswerList().size() > 0) {
                                idAnswer = daoAnswer.getAnswerList().get(daoAnswer.getAnswerList().size() - 1).getId() + 1;
                            }
                            answer1.setId(idAnswer);
                            answer1.setAnswerQuestion(edtAnswer.getText().toString());
                            answer1.setUrlImage("");
                            daoAnswer.setContext(getContext());
                            daoAnswer.addDataAnswerToFirebaseQuestion(answer1, edtAnswer, question.getId());
                            daoImageStorage.uploadFileImageToAnswer(1, imgAnswer, "Question" + question.getId() + "Answer" + answer1.getId(), answer1, question.getId());
                        }
                        else
                        {
                            DEFAULTVALUE.alertDialogMessage("Thông báo","Chỉ được thêm 4 câu hỏi ảnh",getContext());
                        }
                    }
                    else
                    {
                        if (daoAnswer.getAnswerList().size() > 0) {
                            idAnswer = daoAnswer.getAnswerList().get(daoAnswer.getAnswerList().size() - 1).getId() + 1;
                        }
                        answer1.setId(idAnswer);
                        answer1.setAnswerQuestion(edtAnswer.getText().toString());
                        answer1.setUrlImage("");
                        daoAnswer.setContext(getContext());
                        daoAnswer.addDataAnswerToFirebaseQuestion(answer1, edtAnswer, question.getId());
                        daoImageStorage.uploadFileImageToAnswer(1, imgAnswer, "Question" + question.getId() + "Answer" + answer1.getId(), answer1, question.getId());
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