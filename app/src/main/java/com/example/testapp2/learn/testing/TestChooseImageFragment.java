package com.example.testapp2.learn.testing;

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

import com.example.testapp2.R;

public class TestChooseImageFragment extends Fragment {
    //khai báo
    private View myView;

    private Button btnPass, btnSubmit;
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
        btnPass = myView.findViewById(R.id.btn_pass_test);
        btnSubmit = myView.findViewById(R.id.btn_continute_test);
        imgExit = myView.findViewById(R.id.img_exit_test);
        imgSpeak = myView.findViewById(R.id.img_listen);
        tvQuestion = myView.findViewById(R.id.tv_question);
        recyclerViewAnswer = myView.findViewById(R.id.recycle_view_button_answer);
        progressBar = myView.findViewById(R.id.test_progress_bar);
    }
}