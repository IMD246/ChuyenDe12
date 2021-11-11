package com.example.EnglishBeginner.learn.learning;

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

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.DTO.Question;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.main_interface.UserInterfaceActivity;

public class LearningEnglishFragment extends Fragment {
    //khai báo
    private View myView;
    private TextView tvLesson, tvVocabulary, tvMean, tvExample, tvExampleMeans,tvGrammar,tvTypeWord;
    private ImageView imgSpeak, imgLesson;
    private Question question;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_learning_english, container, false);
        setControl();
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            question = (Question) bundle.get("question");
            if (question.getUrlImage().isEmpty()||question.getUrlImage().trim().length()==0)
            {
                imgLesson.setVisibility(View.GONE);
            }
            else
            {
                Glide.with(getContext()).load(question.getUrlImage()).into(imgLesson);
                imgLesson.setVisibility(View.VISIBLE);
            }
            tvLesson.setText(question.getTitle());
            tvVocabulary.setText(question.getWord());
            tvExample.setText(question.getExample());
            tvTypeWord.setText(question.getTypeWord()+":");
            tvGrammar.setText(question.getGrammar());
            tvExampleMeans.setText(question.getExampleMeaning());
            tvMean.setText(question.getWordMeaning());
        }
        return myView;
    }
    //Ánh xạ, khởi gán
    private void setControl() {
        imgLesson = myView.findViewById(R.id.img_word_lesson);
        imgSpeak = myView.findViewById(R.id.img_btn_Listen);
        tvTypeWord = myView.findViewById(R.id.tvTypeWord);
        tvLesson = myView.findViewById(R.id.tv_lesson);
        tvVocabulary = myView.findViewById(R.id.tv_word_lesson);
        tvMean = myView.findViewById(R.id.tv_mean_lesson);
        tvExample = myView.findViewById(R.id.tv_example_word);
        tvExampleMeans = myView.findViewById(R.id.tv_example_mean_word);
        tvGrammar = myView.findViewById(R.id.tv_grammar);
    }
}