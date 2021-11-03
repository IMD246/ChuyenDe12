package com.example.LearnEnglish.learn.learning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.LearnEnglish.R;
import com.example.LearnEnglish.main_interface.UserInterfaceActivity;

public class LearningEnglishFragment extends Fragment {
    //khai báo
    private View myView;

    private Button btnBack, btnContinue;
    private ProgressBar progressBar;
    private TextView tvLesson, tvVocabulary, tvMean, tvExample, tvExampleMeans;
    private ImageView imgSpeak, imgLesson;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_learning_english, container, false);
        setControl();
        setEvent();
        return myView;
    }

    //xử lí màn hình learn
    private void setEvent() {


        //Sử kiện nút trở lại
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserInterfaceActivity.class);
                getContext().startActivity(intent);
            }
        });
    }

    //Ánh xạ, khởi gán
    private void setControl() {
        btnBack = myView.findViewById(R.id.btn_back_learn);
        btnContinue = myView.findViewById(R.id.btn_continute_learn);
        progressBar = myView.findViewById(R.id.learn_progress_bar);
        imgLesson = myView.findViewById(R.id.img_word_lesson);
        imgSpeak = myView.findViewById(R.id.img_btn_Listen);
        tvLesson = myView.findViewById(R.id.tv_lesson);
        tvVocabulary = myView.findViewById(R.id.tv_word_lesson);
        tvMean = myView.findViewById(R.id.tv_mean_lesson);
        tvExample = myView.findViewById(R.id.tv_example_word);
        tvExampleMeans = myView.findViewById(R.id.tv_example_mean_word);
    }
}