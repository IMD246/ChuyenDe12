package com.example.EnglishBeginner.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.DTO.Answer;
import com.example.EnglishBeginner.DTO.Level;
import com.example.EnglishBeginner.DTO.Topic;
import com.example.EnglishBeginner.R;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

public class TestChooseImageItem_Adapter extends RecyclerView.Adapter<TestChooseImageItem_Adapter.ChooseImageViewHolder> {
    //khai báo các trường dữ liệu
    public List<Answer> answerList;
    public interface_Test interface_learn;
    private final Context context;
    private boolean check;

    //hàm constructor
    public TestChooseImageItem_Adapter(Context context) {
        this.context = context;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }

    public void setInterface_learn(interface_Test interface_learn) {
        this.interface_learn = interface_learn;
    }

    //khởi tạo view holder
    @NonNull
    @Override
    public ChooseImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_test_choose_image, parent, false);
        return new ChooseImageViewHolder(view);
    }

    //xử lí giao diện, action cho viewHolder
    @Override
    public void onBindViewHolder(@NonNull ChooseImageViewHolder holder, int position) {
        Answer answer = answerList.get(position);
        if (answer == null) {
            return;
        }
        if (answer.getUrlImage().trim().isEmpty() || answer.getUrlImage().trim().length() == 0) {
        } else {
            Glide.with(context).load(answer.getUrlImage()).into(holder.imgAnswer);
        }
        //xử lí khi click item learn:
        holder.linearLayout.setOnClickListener(v -> {
            if (interface_learn!=null)
            {
                interface_learn.onClickItemLearn(answer);
            }
            check = true;
            holder.linearLayout.setBackgroundColor(Color.RED);

            answer.setCheck(true);
        });
    }
    //trả về số phần tử của list
    @Override
    public int getItemCount() {
        if (answerList != null) {
            return answerList.size();
        }
        return 0;
    }
    //class ViewHolder
    public static class ChooseImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgAnswer;
        private final LinearLayout linearLayout;
        public ChooseImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnswer = itemView.findViewById(R.id.img_answer_choose_img);
            linearLayout = itemView.findViewById(R.id.layout_btn_lesson);
        }
    }
    public interface interface_Test {
        void onClickItemLearn(Answer answer);
    }
}
