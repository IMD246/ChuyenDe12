package com.example.EnglishBeginner.learn.testing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.EnglishBeginner.Adapter.TestChooseImageItem_Adapter;
import com.example.EnglishBeginner.DAO.DAOAnswer;
import com.example.EnglishBeginner.DTO.Question;
import com.example.EnglishBeginner.R;

public class TestChooseImageFragment extends Fragment {
    //khai báo
    private View myView;

    private ImageView imgExit, imgSpeak;
    private TextView tvQuestion;
    private RecyclerView recyclerViewAnswer;
    private ProgressBar progressBar;
    private DAOAnswer daoAnswer;
    private TestChooseImageItem_Adapter testChooseImageItem_adapter;
    private TestEnglishActivity testEnglishActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_test_choose_image, container, false);
        setControl();
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            Question question = (Question) bundle.get("question");
            getDataAnswer(question.getId());
         }
        return myView;
    }
    //Ánh xạ, khởi tạo,...
    private void setControl() {
        //ánh xạ các view
        daoAnswer = new DAOAnswer(getContext());
        testEnglishActivity = (TestEnglishActivity) getActivity();
        testChooseImageItem_adapter = new TestChooseImageItem_Adapter(testEnglishActivity.getApplicationContext());
        testChooseImageItem_adapter.setAnswerList(daoAnswer.getAnswerList());
        imgSpeak = myView.findViewById(R.id.img_listen);
        tvQuestion = myView.findViewById(R.id.tv_question);
        recyclerViewAnswer = myView.findViewById(R.id.recycle_view_button_answer);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(testEnglishActivity.getApplicationContext(),4);
        recyclerViewAnswer.setLayoutManager(gridLayoutManager);
        recyclerViewAnswer.setAdapter(testChooseImageItem_adapter);
    }
    private void getDataAnswer(int idQuestion)
    {
        daoAnswer.getDataFromFirebase(idQuestion,testChooseImageItem_adapter);
    }
}