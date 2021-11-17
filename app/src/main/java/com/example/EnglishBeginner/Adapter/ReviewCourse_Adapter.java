package com.example.EnglishBeginner.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.DTO.Level;
import com.example.EnglishBeginner.DTO.ReviewCourse;
import com.example.EnglishBeginner.DTO.Topic;
import com.example.EnglishBeginner.R;

import java.util.ArrayList;
import java.util.List;

public class ReviewCourse_Adapter extends RecyclerView.Adapter<ReviewCourse_Adapter.LearnViewHolder> {
    //khai báo các trường dữ liệu
    private List<ReviewCourse> reviewCourseList;
    private final Context context;
    private Interface_Course interface_course;

    //hàm constructor
    public ReviewCourse_Adapter(Context context) {
        this.context = context;
    }

    public void setLevelArrayList(List<ReviewCourse> levelArrayList) {
        this.reviewCourseList = levelArrayList;
    }

    public void setInterface_course(Interface_Course interface_course) {
        this.interface_course = interface_course;
    }

    //khởi tạo view holder
    @NonNull
    @Override
    public LearnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_review_course, parent, false);
        return new LearnViewHolder(view);
    }

    //xử lí giao diện, action cho viewHolder
    @Override
    public void onBindViewHolder(@NonNull LearnViewHolder holder, int position) {
        ReviewCourse reviewCourse = reviewCourseList.get(position);
        if (reviewCourse == null) {
            return;
        }
        if (reviewCourse.isCheck()){
            holder.layoutCorrect.setVisibility(View.GONE);
        }else{
            holder.linearlayout.setBackgroundColor(Color.parseColor("#ffdfe0"));
            holder.tvAnswer.setTextColor(Color.parseColor("#ea2b2b"));
            holder.tvtitle.setTextColor(Color.parseColor("#ea2b2b"));
            holder.tvCorrectAnswer.setTextColor(Color.parseColor("#ea2b2b"));
        }
        if (reviewCourse.getTypeQuestion().equalsIgnoreCase(DEFAULTVALUE.LISTEN)){
            holder.layoutAnswer.setVisibility(View.GONE);
            holder.layoutCorrect.setVisibility(View.GONE);
        }else {
            holder.btnSpeak.setVisibility(View.GONE);
        }
        holder.tvtitle.setText(reviewCourse.getQuestion()+"");
        holder.tvAnswer.setText(reviewCourse.getUserAnswer());
        holder.tvCorrectAnswer.setText(reviewCourse.getCorrectAnswer());
        holder.btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interface_course.onClickItemCourse(reviewCourse.getCorrectAnswer());
            }
        });
        //xử lí khi click item learn:
        holder.cardView.setOnClickListener(v -> {

        });
    }
    //trả về số phần tử của list
    @Override
    public int getItemCount() {
        if (reviewCourseList != null) {
            return reviewCourseList.size();
        }
        return 0;
    }
    //class ViewHolder
    public static class LearnViewHolder extends RecyclerView.ViewHolder {
        private TextView tvtitle, tvAnswer, tvCorrectAnswer;
        private LinearLayout btnSpeak;
        private CardView cardView;
        private LinearLayout linearlayout, layoutCorrect, layoutAnswer;

        public LearnViewHolder(@NonNull View itemView) {
            super(itemView);
            tvtitle = itemView.findViewById(R.id.tv_title_item);
            tvAnswer = itemView.findViewById(R.id.tv_answer);
            tvCorrectAnswer = itemView.findViewById(R.id.tv_correct_item);
            btnSpeak = itemView.findViewById(R.id.img_Listen_Learn);
            cardView = itemView.findViewById(R.id.layout_item_review);
            linearlayout = itemView.findViewById(R.id.linearlayout);
            layoutCorrect = itemView.findViewById(R.id.layout_correct);
            layoutAnswer = itemView.findViewById(R.id.layout_answer);
        }
    }
    public interface Interface_Course {
        void onClickItemCourse(String corectAnswer);
    }
}
