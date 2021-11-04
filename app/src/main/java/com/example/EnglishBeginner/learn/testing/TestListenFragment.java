package com.example.EnglishBeginner.learn.testing;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.main_interface.UserInterfaceActivity;

public class TestListenFragment extends Fragment {
    //khai báo
    private View myView;

    private Button btnPass, btnSubmit;
    private ImageView imgExit, imgSpeak;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewAnswer, recyclerViewDisplay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_test_listen, container, false);
        setControl();
        setEvent();
        return myView;
    }

    private void setEvent() {
        //Sử kiện nút trở lại
        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserInterfaceActivity.class);
                getContext().startActivity(intent);
            }
        });
    }

    //Ánh xạ, khởi gán
    private void setControl() {
        //ánh xạ các view
        btnPass = myView.findViewById(R.id.btn_pass_test);
        btnSubmit = myView.findViewById(R.id.btn_continute_test);
        imgExit = myView.findViewById(R.id.img_exit_test);
        imgSpeak = myView.findViewById(R.id.img_listen);
        recyclerViewAnswer = myView.findViewById(R.id.recycle_view_button_answer);
        recyclerViewDisplay = myView.findViewById(R.id.recycle_view_show_answer);
        progressBar = myView.findViewById(R.id.test_progress_bar);
    }
}