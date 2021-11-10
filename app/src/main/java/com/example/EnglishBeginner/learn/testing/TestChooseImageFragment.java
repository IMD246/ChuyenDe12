package com.example.EnglishBeginner.learn.testing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.EnglishBeginner.R;

public class TestChooseImageFragment extends Fragment {
    //khai báo
    private View myView;

    private ImageView imgExit, imgSpeak;
    private TextView tvQuestion;
    private RecyclerView recyclerViewAnswer;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_test_choose_image, container, false);
        setControl();
        return myView;
    }
    //Ánh xạ, khởi tạo,...
    private void setControl() {
        //ánh xạ các view

        imgSpeak = myView.findViewById(R.id.img_listen);
        tvQuestion = myView.findViewById(R.id.tv_question);
        recyclerViewAnswer = myView.findViewById(R.id.recycle_view_button_answer);
    }
}