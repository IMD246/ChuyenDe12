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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.learn.testing.source.UserChooseEnglishAdapter;
import com.example.EnglishBeginner.learn.testing.source.UserHadChooseEnglishAdapter;

import java.util.ArrayList;

public class TestSelectionEnglishFragment extends Fragment {
    //khai báo
    private View myView;

    private ImageView imgSpeak;
    private TextView tvQuestion;
    private RecyclerView rv_listForUserToChoose, rv_listThatUserHadChoose;
    private ArrayList<String> listForUserToChoose;
    private ArrayList<String> listUserHadChoose;
    UserChooseEnglishAdapter userChooseEnglishAdapter;
    UserHadChooseEnglishAdapter UserHadChooseEnglishAdapter;

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
    private void NotifyData(){
        userChooseEnglishAdapter.notifyDataSetChanged();
        UserHadChooseEnglishAdapter.notifyDataSetChanged();
    }
    private void setAdapter(){

        listUserHadChoose = new ArrayList<>();
        listForUserToChoose = new ArrayList<>();
        String temp = "i do you not khow are ?";
        changeStringToArrayList(temp,listForUserToChoose);

        userChooseEnglishAdapter = new UserChooseEnglishAdapter(getContext(), listForUserToChoose, listUserHadChoose);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);// Tạo layout manager
        layoutManager.canScrollHorizontally();
        layoutManager.canScrollVertically();
        rv_listForUserToChoose.setLayoutManager(layoutManager);// Gán layout manager cho recyclerview
        rv_listForUserToChoose.setAdapter(userChooseEnglishAdapter);//gán adapter cho Recyclerview.
        //set adapter for list user had choose
        userChooseEnglishAdapter.setNotifyData(new UserChooseEnglishAdapter.notifyData() {
            @Override
            public void notifyDataChange() {
                NotifyData();
            }
        });
        UserHadChooseEnglishAdapter = new UserHadChooseEnglishAdapter(getContext(), listUserHadChoose, listForUserToChoose);
        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(getContext(), 3);// Tạo layout manager
        layoutManager2.canScrollHorizontally();
        layoutManager2.canScrollVertically();
        rv_listThatUserHadChoose.setLayoutManager(layoutManager2);// Gán layout manager cho recyclerview
        rv_listThatUserHadChoose.setAdapter(UserHadChooseEnglishAdapter);//gán adapter cho Recyclerview.
        UserHadChooseEnglishAdapter.setNotifyData(new UserHadChooseEnglishAdapter.notifyData() {
            @Override
            public void notifyDataChange() {
                NotifyData();
            }
        });
    }
    //Ánh xạ, khởi tạo,...
    private void setControl() {
        //ánh xạ các view
        imgSpeak = myView.findViewById(R.id.img_listen);
        tvQuestion = myView.findViewById(R.id.tv_question);
        rv_listForUserToChoose = myView.findViewById(R.id.recycle_view_button_answer);
        rv_listThatUserHadChoose = myView.findViewById(R.id.recycle_view_show_answer);
        setAdapter();

    }
    private void changeStringToArrayList(String text,ArrayList<String> listResult){
        String [] temp = text.split(" ");
        for (String item: temp) {
            listResult.add(item);
        }
    }
}