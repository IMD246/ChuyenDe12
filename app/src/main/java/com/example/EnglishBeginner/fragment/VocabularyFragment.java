package com.example.EnglishBeginner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.fragment.LearnWord.WordManagement.IetlsManagement;
import com.example.EnglishBeginner.fragment.LearnWord.WordManagement.ToeicManagement;
import com.example.EnglishBeginner.fragment.LearnWord.vocubulary.VocubularyAapter;
import com.example.EnglishBeginner.fragment.LearnWord.vocubulary.VocubularyItem;

import java.util.ArrayList;

public class VocabularyFragment extends Fragment {
    private View myView;
    private RecyclerView danhsach;
    ArrayList<VocubularyItem> vocabularyItems = new ArrayList<>();
    ImageView toeic_image, ielts_image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_vocabulary, container, false);
        AddItem();
        setControl();
        setEvent();
        return myView;
    }

    private void setEvent() {
        ielts_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), IetlsManagement.class);
                startActivity(intent);
            }
        });

        toeic_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ToeicManagement.class);
                startActivity(intent);
            }
        });
    }

    private void AddItem() {
        VocubularyItem item1 = new VocubularyItem("Từ đã lưu", R.drawable.save);
        VocubularyItem item2 = new VocubularyItem("lịch sử tra từ", R.drawable.history);
        VocubularyItem item3 = new VocubularyItem("tra từ điển", R.drawable.word);
        VocubularyItem item4 = new VocubularyItem("Luyện tập", R.drawable.practice);
        VocubularyItem item5 = new VocubularyItem("cài đặt lich học", R.drawable.com_facebook_button_icon);
        vocabularyItems.add(item1);
        vocabularyItems.add(item2);
        vocabularyItems.add(item3);
        vocabularyItems.add(item4);
        vocabularyItems.add(item5);
    }

    private void setControl() {
        danhsach = (RecyclerView) myView.findViewById(R.id.vocubulary_recycleItem);
        VocubularyAapter adapter = new VocubularyAapter(getContext(), vocabularyItems);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);// Tạo layout manager
        danhsach.setItemAnimator(new DefaultItemAnimator());// Gán hiệu ứng cho Recyclerview
        danhsach.setLayoutManager(layoutManager);// Gán layout manager cho recyclerview
        danhsach.setAdapter(adapter);//gán adapter cho Recyclerview.


        toeic_image = myView.findViewById(R.id.img_toeic);
        ielts_image = myView.findViewById(R.id.img_ielts);
    }
}
