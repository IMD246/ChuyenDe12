package com.example.EnglishBeginner.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.DTO.Answer;
import com.example.EnglishBeginner.R;

import java.util.List;

public class TestChooseImageItem_Adapter extends RecyclerView.Adapter<TestChooseImageItem_Adapter.ChooseImageViewHolder> {
    //khai báo các trường dữ liệu
    public List<Answer> answerList;
    public interface_Test interface_learn;
    private final Context context;
    public static int previousPosition = 0;
    private RecyclerView recyclerView;

    //hàm constructor
    public TestChooseImageItem_Adapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
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
            if (holder.getAdapterPosition() == previousPosition){
                v = recyclerView.findViewHolderForAdapterPosition(previousPosition).itemView;
                holder.linearLayout = (LinearLayout) v.findViewById(R.id.layout_btn_lesson);
                holder.linearLayout.setSelected(true);
                previousPosition = holder.getAdapterPosition();
            }else {
                v = recyclerView.findViewHolderForAdapterPosition(previousPosition).itemView;
                holder.linearLayout = (LinearLayout) v.findViewById(R.id.layout_btn_lesson);
                holder.linearLayout.setSelected(false);

                v = recyclerView.findViewHolderForAdapterPosition(holder.getAdapterPosition()).itemView;
                holder.linearLayout = (LinearLayout) v.findViewById(R.id.layout_btn_lesson);
                holder.linearLayout.setSelected(true);
                previousPosition = holder.getAdapterPosition();
            }
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
        private LinearLayout linearLayout;
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
