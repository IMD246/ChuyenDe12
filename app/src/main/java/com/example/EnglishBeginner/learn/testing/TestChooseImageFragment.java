package com.example.EnglishBeginner.learn.testing;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.Adapter.TestChooseImageItem_Adapter;
import com.example.EnglishBeginner.DTO.Answer;
import com.example.EnglishBeginner.DTO.Question;
import com.example.EnglishBeginner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TestChooseImageFragment extends Fragment implements TextToSpeech.OnInitListener{
    //khai báo
    private View myView;

    private ImageView imgSpeak;
    private TextView tvQuestion;
    private RecyclerView recyclerViewAnswer;
    private List<Answer> answerList;
    private DatabaseReference databaseReference;
    private TestChooseImageItem_Adapter testChooseImageItem_adapter;
    private TestEnglishActivity testEnglishActivity;
    private Question question;
    private GridLayoutManager gridLayoutManager;
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
        answerList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("listquestion");
        imgSpeak = myView.findViewById(R.id.img_listen);
        tvQuestion = myView.findViewById(R.id.tv_question);
        testEnglishActivity = (TestEnglishActivity) getActivity();
        recyclerViewAnswer = myView.findViewById(R.id.recycle_view_button_answer);
        testChooseImageItem_adapter = new TestChooseImageItem_Adapter(getContext(), recyclerViewAnswer);
        gridLayoutManager = new GridLayoutManager(getContext(),2);
        imgSpeak.setOnClickListener(v -> texttoSpeak());
    }
    private void getDataAnswer(int idQuestion)
    {
        databaseReference.child(idQuestion+"/listanswer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (answerList != null)
                {
                    answerList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Answer answer = dataSnapshot.getValue(Answer.class);
                    answerList.add(answer);
                }
                testChooseImageItem_adapter.setAnswerList(answerList);
                recyclerViewAnswer.setLayoutManager(gridLayoutManager);
                recyclerViewAnswer.setAdapter(testChooseImageItem_adapter);
                testChooseImageItem_adapter.notifyDataSetChanged();
                testChooseImageItem_adapter.setInterface_learn(answer -> {
                    if (answer!=null)
                    {
                        testEnglishActivity.answer = answer.trim();
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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