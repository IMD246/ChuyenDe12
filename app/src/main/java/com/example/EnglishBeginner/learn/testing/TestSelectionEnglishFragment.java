package com.example.EnglishBeginner.learn.testing;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.Adapter.UserChooseEnglishAdapter;
import com.example.EnglishBeginner.Adapter.UserHadChooseEnglishAdapter;
import com.example.EnglishBeginner.DAO.DAOAnswer;
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

public class TestSelectionEnglishFragment extends Fragment implements TextToSpeech.OnInitListener{
    //khai báo
    private View myView;

    private ImageView imgSpeak;
    private TextView tvQuestion;
    private RecyclerView rv_listForUserToChoose, rv_listThatUserHadChoose;
    private ArrayList<String> listUserHadChoose;
    private UserChooseEnglishAdapter userChooseEnglishAdapter;
    private UserHadChooseEnglishAdapter userHadChooseEnglishAdapter;
    private Question question;
    private DAOAnswer daoAnswer;
    private TestEnglishActivity testEnglishActivity;
    private DatabaseReference databaseReference;
    protected static final int RESULT_SPEECH = 1;
    private TextToSpeech textToSpeech;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_test_selection_english, container, false);
        setControl();
        Bundle bundle = getArguments();
        if (bundle != null) {
            question = (Question) bundle.get("question");
            tvQuestion.setText(question.getTitle());
            getDataAnswer(question.getId());
        }
        setEvent();
        setAdapter();
        return myView;
    }

    private void setEvent() {
        imgSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                texttoSpeak();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void NotifyData(int choice) {
        userHadChooseEnglishAdapter.notifyDataSetChanged();
        userChooseEnglishAdapter.notifyDataSetChanged();
        testEnglishActivity.answer = "";
        if (choice == 1) {
            for (String answerList : userHadChooseEnglishAdapter.getListUserHadChoose()) {
                testEnglishActivity.answer += answerList + " ";
            }
        }
    }

    private void setAdapter() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);// Tạo layout manager
        layoutManager.canScrollHorizontally();
        layoutManager.canScrollVertically();
        rv_listForUserToChoose.setLayoutManager(layoutManager);// Gán layout manager cho recyclerview
        //set adapter for list user had choose
        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(getContext(), 3);// Tạo layout manager
        layoutManager2.canScrollHorizontally();
        layoutManager2.canScrollVertically();
        rv_listThatUserHadChoose.setLayoutManager(layoutManager2);// Gán layout manager cho recyclerview
    }

    //Ánh xạ, khởi tạo,...
    private void setControl() {
        textToSpeech = new TextToSpeech(getContext(), this);
        testEnglishActivity = (TestEnglishActivity) getActivity();
        daoAnswer = new DAOAnswer(testEnglishActivity.getBaseContext());
        listUserHadChoose = new ArrayList<>();
        //ánh xạ các view
        imgSpeak = myView.findViewById(R.id.img_Listen);
        tvQuestion = myView.findViewById(R.id.tv_question);
        rv_listForUserToChoose = myView.findViewById(R.id.recycle_view_button_answer);
        rv_listThatUserHadChoose = myView.findViewById(R.id.recycle_view_show_answer);
    }

    private void getDataAnswer(int idQuestion) {
        databaseReference = FirebaseDatabase.getInstance().getReference("listquestion");
        databaseReference.child(idQuestion + "/listanswer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String answerString = "";
                List<Answer> list = new ArrayList<>();
                ArrayList<String> arrayList = new ArrayList<>();
                if (list != null) {
                    list.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Answer answer = dataSnapshot.getValue(Answer.class);
                    list.add(answer);
                }
                for (Answer temp : list) {
                    answerString = answerString + temp.getAnswerQuestion() + " ";
                }
                String[] temp = answerString.trim().split(" ");
                for (String item : temp) {
                    arrayList.add(item);
                }
                if (userChooseEnglishAdapter != null) {
                    userChooseEnglishAdapter.notifyDataSetChanged();
                }
                userChooseEnglishAdapter = new UserChooseEnglishAdapter(getContext(), listUserHadChoose, arrayList);
                userHadChooseEnglishAdapter = new UserHadChooseEnglishAdapter(getContext(), listUserHadChoose, arrayList);
                //gán adapter cho Recyclerview.
                rv_listForUserToChoose.setAdapter(userChooseEnglishAdapter);
                rv_listThatUserHadChoose.setAdapter(userHadChooseEnglishAdapter);//gán adapter cho Recyclerview.
                userHadChooseEnglishAdapter.setNotifyData(new UserHadChooseEnglishAdapter.notifyData() {
                    @Override
                    public void notifyDataChange() {
                        NotifyData(2);
                    }
                });
                userChooseEnglishAdapter.setNotifyData(new UserChooseEnglishAdapter.notifyData() {
                    @Override
                    public void notifyDataChange() {
                        NotifyData(1);
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