package com.example.menu_right.learn;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu_right.R;

import java.util.ArrayList;

public class LearnFragment extends Fragment {

    private View myView;

    //KHAI BÁO THÀNH PHẦN TRONG RECYCLERVIEW LEARN
    RecyclerView learnRecyclerView;
    RecyclerViewLearn_Adapter learnRecyclerView_adapter;
    ArrayList<Learn> learnArrayList = new ArrayList<Learn>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_learn, container, false);
        setControl();
        setEvent();
        return myView;
    }

    //xử lí adapter, tạo sự kiện cho item
    private void setEvent() {
        learnRecyclerView_adapter = new RecyclerViewLearn_Adapter(getContext(), addData(), new RecyclerViewLearn_Adapter.Interface_Learn() {
            @Override
            public void onClickItemLearn(Learn learn) {
                onClickGoToLearningEnglish();
            }
        });
        learnRecyclerView.setAdapter(learnRecyclerView_adapter);
    }

    //Ánh xạ, xử lí view của list
    private void setControl() {
        learnRecyclerView = myView.findViewById(R.id.recycle_view_learn);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(learnRecyclerView.VERTICAL);
        learnRecyclerView.setLayoutManager(linearLayoutManager);
    }


    //khởi tạo dữ liệu mặc định cho List learn
    private ArrayList<Learn> addData() {
        learnArrayList.add(new Learn("bài học 1"));
        learnArrayList.add(new Learn("bài học 2"));
        learnArrayList.add(new Learn("bài học 3"));
        return learnArrayList;
    }

    //hàm chuyển sang màn hình học tiếng Anh
    private void onClickGoToLearningEnglish() {
        Intent intent = new Intent(getContext(), LearningEnglishActivity.class);
//        Bundle bundle = new Bundle();
        //Truyền dữ liệu: thêm tham số lưu trữ dữ liệu mở câu lệnh này ra
//        bundle.putSerializable("obj_User", user);
//        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }
}
