package com.example.EnglishBeginner.learn.learning;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.DTO.Question;
import com.example.EnglishBeginner.R;

import java.util.Locale;

public class LearningEnglishFragment extends Fragment implements TextToSpeech.OnInitListener{
    //khai báo
    private View myView;
    private TextView tvLesson, tvVocabulary, tvMean, tvExample, tvExampleMeans,tvGrammar,tvTypeWord;
    private ImageView imgSpeak, imgLesson;
    private Question question;
    private TextToSpeech textToSpeech;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_learning_english, container, false);
        setControl();
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            question = (Question) bundle.get("question");
            if (!(question.getUrlImage().trim().isEmpty()))
            {
                Glide.with(getContext()).load(question.getUrlImage()).into(imgLesson);
            }
            tvLesson.setText(question.getWord());
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
        textToSpeech = new TextToSpeech(getContext(),this);
        imgLesson = myView.findViewById(R.id.img_word_lesson);
        imgSpeak = myView.findViewById(R.id.img_btn_Listen);
        tvTypeWord = myView.findViewById(R.id.tvTypeWord);
        tvLesson = myView.findViewById(R.id.tv_lesson);
        tvVocabulary = myView.findViewById(R.id.tv_word_lesson);
        tvMean = myView.findViewById(R.id.tv_mean_lesson);
        tvExample = myView.findViewById(R.id.tv_example_word);
        tvExampleMeans = myView.findViewById(R.id.tv_example_mean_word);
        tvGrammar = myView.findViewById(R.id.tv_grammar);
        imgSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texttoSpeak();
            }
        });
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
    private void texttoSpeak() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(question.getWord(), TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else {
            textToSpeech.speak(question.getWord(), TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            textToSpeech.setSpeechRate(1.0f);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("error", "This Language is not supported");
            } else {
                texttoSpeak();
            }
        } else {
            Log.e("error", "Failed to Initialize");
        }
    }
}