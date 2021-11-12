package com.example.EnglishBeginner.learn.testing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.EnglishBeginner.Adapter.TestChooseImageItem_Adapter;
import com.example.EnglishBeginner.DAO.DAOAnswer;
import com.example.EnglishBeginner.DTO.Answer;
import com.example.EnglishBeginner.DTO.Question;
import com.example.EnglishBeginner.R;

import java.util.Locale;

public class TestChooseImageFragment extends Fragment implements TextToSpeech.OnInitListener{
    //khai báo
    private View myView;

    private ImageView imgExit, imgSpeak;
    private TextView tvQuestion;
    private RecyclerView recyclerViewAnswer;
    private ProgressBar progressBar;
    private DAOAnswer daoAnswer;
    private TestChooseImageItem_Adapter testChooseImageItem_adapter;
    private TestEnglishActivity testEnglishActivity;
    private Question question;
    private TextToSpeech textToSpeech;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_test_choose_image, container, false);
        Bundle bundle = getArguments();
        setControl();
        if (bundle!=null)
        {
            question = (Question) bundle.get("question");
            getDataAnswer(question.getId());
            tvQuestion.setText(question.getTitle());
        }
        return myView;
    }
    //Ánh xạ, khởi tạo,...
    private void setControl() {
        //ánh xạ các view
        textToSpeech = new TextToSpeech(getContext(),this);
        daoAnswer = new DAOAnswer(getContext());
        testEnglishActivity = (TestEnglishActivity) getActivity();
        testChooseImageItem_adapter = new TestChooseImageItem_Adapter(testEnglishActivity.getApplicationContext());
        testChooseImageItem_adapter.setAnswerList(daoAnswer.getAnswerList());
        imgSpeak = myView.findViewById(R.id.img_listen);
        tvQuestion = myView.findViewById(R.id.tv_question);
        recyclerViewAnswer = myView.findViewById(R.id.recycle_view_button_answer);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(testEnglishActivity.getApplicationContext(),2);
        recyclerViewAnswer.setLayoutManager(gridLayoutManager);
        recyclerViewAnswer.setAdapter(testChooseImageItem_adapter);
        testChooseImageItem_adapter.setInterface_learn(new TestChooseImageItem_Adapter.interface_Test() {
            @Override
            public void onClickItemLearn(Answer answer) {
                if (answer!=null)
                {
                    testEnglishActivity.answer = answer.getAnswerQuestion();
                }
            }
        });
        imgSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texttoSpeak();
            }
        });
    }
    private void getDataAnswer(int idQuestion)
    {
        daoAnswer.getDataFromFirebase(idQuestion,testChooseImageItem_adapter);
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