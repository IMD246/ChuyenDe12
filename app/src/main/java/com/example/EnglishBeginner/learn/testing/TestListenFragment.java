package com.example.EnglishBeginner.learn.testing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.EnglishBeginner.DTO.Question;
import com.example.EnglishBeginner.R;

import java.util.ArrayList;
import java.util.Locale;

public class TestListenFragment extends Fragment implements TextToSpeech.OnInitListener {
    //khai báo
    private View myView;
    protected static final int RESULT_SPEECH = 1;
    private TextToSpeech textToSpeech;
    private Question question;
    private TestEnglishActivity testEnglishActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_test_listen, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            question = (Question) bundle.get("question");
        }
        setControl();
        return myView;
    }

    //Ánh xạ, khởi gán
    private void setControl() {
        //ánh xạ các view
        testEnglishActivity = (TestEnglishActivity) getActivity();
        textToSpeech = new TextToSpeech(getContext(), this);
        ImageView imgSpeak = myView.findViewById(R.id.img_Listen_Learn);
        ImageView imgMic = myView.findViewById(R.id.imgMic);
        imgSpeak.setOnClickListener(v -> texttoSpeak());
        imgMic.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
            try {
                startActivityForResult(intent, RESULT_SPEECH);
                testEnglishActivity.answer = null;
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        });
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

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @SuppressLint("ObsoleteSdkInt")
    private void texttoSpeak() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(question.getTitle(), TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeech.speak(question.getTitle(), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_SPEECH) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                testEnglishActivity.answer = text.get(0);
            }
        }
    }
}