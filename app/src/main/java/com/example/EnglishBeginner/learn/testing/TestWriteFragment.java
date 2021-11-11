package com.example.EnglishBeginner.learn.testing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.EnglishBeginner.DTO.Question;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.main_interface.UserInterfaceActivity;

public class TestWriteFragment extends Fragment{
    //khai báo
    private View myView;

    private ImageView imgSpeak;
    private TextView tvQuestion;
    private EditText edtAnswer;
    private TestEnglishActivity testEnglishActivity;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewAnswer, recyclerViewDisplay;
    private String answer = null;
    private Question question;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_test_write, container, false);
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            question =(Question) bundle.get("question");
        }
        setControl();
        return myView;
    }
    private void setControl() {
        //ánh xạ các view
        testEnglishActivity = (TestEnglishActivity) getActivity();
        imgSpeak = myView.findViewById(R.id.img_listen);
        tvQuestion = myView.findViewById(R.id.tv_question);
        tvQuestion.setText(question.getTitle());
        edtAnswer = myView.findViewById(R.id.edt_answer);
        edtAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                answer = edtAnswer.getText().toString();
                if (answer!=null || answer.trim().length()>0)
                {
                    testEnglishActivity.answer = answer;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}