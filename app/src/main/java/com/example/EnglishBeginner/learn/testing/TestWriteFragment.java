package com.example.EnglishBeginner.learn.testing;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.EnglishBeginner.DTO.Question;
import com.example.EnglishBeginner.R;

import java.util.Locale;

public class TestWriteFragment extends Fragment implements TextToSpeech.OnInitListener{
    //khai báo
    private View myView;

    private ImageView imgSpeak;
    private TextView tvQuestion;
    private EditText edtAnswer;
    private TestEnglishActivity testEnglishActivity;
    private String answer = null;
    private Question question;
    private TextToSpeech textToSpeech;
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
        textToSpeech = new TextToSpeech(getContext(),this);
        testEnglishActivity = (TestEnglishActivity) getActivity();
        imgSpeak = myView.findViewById(R.id.img_btn_Listen);
        tvQuestion = myView.findViewById(R.id.tv_question);
        tvQuestion.setText(question.getTitle());
        edtAnswer = myView.findViewById(R.id.edt_answer);
        edtAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                answer = edtAnswer.getText().toString();
                if (answer!=null || answer.trim().length()>0)
                {
                    testEnglishActivity.answer = answer;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        imgSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texttoSpeak();
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
    private void texttoSpeak() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(question.getTitle(), TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else {
            textToSpeech.speak(question.getTitle(), TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}