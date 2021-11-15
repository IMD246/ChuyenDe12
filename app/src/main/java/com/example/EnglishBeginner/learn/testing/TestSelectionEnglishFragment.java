package com.example.EnglishBeginner.learn.testing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.R;

public class TestSelectionEnglishFragment extends Fragment {
    //khai báo
    private View myView;

    private ImageView imgSpeak;
    private TextView tvQuestion;
    private RecyclerView recyclerViewAnswer, recyclerViewDisplay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_test_selection_english, container, false);
        setControl();
        setEvent();
        return myView;
    }

    //xử lí màn hình learn
    private void setEvent() {

    }

    //Ánh xạ, khởi tạo,...
    private void setControl() {
        //ánh xạ các view
        imgSpeak = myView.findViewById(R.id.img_listen);
        tvQuestion = myView.findViewById(R.id.tv_question);
        recyclerViewAnswer = myView.findViewById(R.id.recycle_view_button_answer);
        recyclerViewDisplay = myView.findViewById(R.id.recycle_view_show_answer);
    }
}