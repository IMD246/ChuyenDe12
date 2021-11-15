package com.example.EnglishBeginner.learn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.learn.testing.TestEnglishActivity;

import pl.droidsonroids.gif.GifImageView;

public class FinishEnglishFragment extends Fragment {
    //khai báo
    private View myView;
    private TestEnglishActivity testEnglishActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_finish_english, container, false);
        setControl();
        return myView;
    }
    //Ánh xạ, khởi tạo,...
    private void setControl() {
        testEnglishActivity = (TestEnglishActivity) getActivity();
        TextView textView = myView.findViewById(R.id.titleFinishResult);
        GifImageView gifImageView = myView.findViewById(R.id.givFinishResult);
        if (testEnglishActivity.checkFinishResult == false)
        {
            textView.setText("Hãy cố gắng lần sau");
            gifImageView.setImageResource(R.drawable.failure);
        }
    }
}