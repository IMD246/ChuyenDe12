package com.example.EnglishBeginner.fragment.LearnWord.practice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.R;

import java.util.ArrayList;

public class PracticeAdapter extends RecyclerView.Adapter<PracticeAdapter.ViewHolder> {

    Context context;
    ArrayList<String> listAnswer;
    ArrayList<Boolean> listResult;
    int resultPos;
    Boolean hadChoose = false;

    submitEvent submitEventTemp;

    public void setSubmitEventTemp(submitEvent submitEventTemp) {
        this.submitEventTemp = submitEventTemp;
    }

    public PracticeAdapter(Context context, ArrayList<String> listAnswer, int resultPos, ArrayList<Boolean> listResult) {
        this.context = context;
        this.listAnswer = listAnswer;
        this.resultPos = resultPos;
        this.listResult = listResult;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View ref = LayoutInflater.from(context).inflate(R.layout.practice_answeritem, parent, false);

        return new ViewHolder(ref);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvAnswer.setText(listAnswer.get(position));
        if (hadChoose) {
            if (holder.getAdapterPosition() == resultPos) {
                holder.layoutAnswer.setBackgroundColor(Color.GREEN);
                submitEventTemp.delayAndPushDataWhenSubmit();
            }
        }
        if (listAnswer.size() > 3) {
            holder.layoutAnswer.setOnClickListener(v -> {
                if (holder.getAdapterPosition() == resultPos) {
                    holder.layoutAnswer.setBackgroundColor(Color.GREEN);
                    listResult.add(true);
                } else {
                    holder.layoutAnswer.setBackgroundColor(Color.RED);
                    listResult.add(false);
                }
                hadChoose = true;
                holder.layoutAnswer.setEnabled(false);
                notifyDataSetChanged();
            });
        }

    }

    @Override
    public int getItemCount() {
        return listAnswer.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //khai báo các phần từ
        TextView tvAnswer;
        LinearLayout layoutAnswer;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAnswer = itemView.findViewById(R.id.tv_item_listanswer_practice);
            layoutAnswer = itemView.findViewById(R.id.layout_answer_item_list);
            progressBar = itemView.findViewById(R.id.practice_progress_bar);
        }
    }

    interface submitEvent {
        void delayAndPushDataWhenSubmit();
    }
}
