package com.example.EnglishBeginner.learn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.Adapter.TestChooseImageItem_Adapter;
import com.example.EnglishBeginner.DAO.DAOAnswer;
import com.example.EnglishBeginner.DTO.Answer;
import com.example.EnglishBeginner.DTO.Question;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.learn.testing.TestEnglishActivity;

public class FinishEnglishFragment extends Fragment {
    //khai báo
    private View myView;
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
    }
}