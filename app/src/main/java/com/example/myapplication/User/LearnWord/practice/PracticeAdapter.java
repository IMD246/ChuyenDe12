package com.example.myapplication.User.LearnWord.practice;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.ArrayList;

public class PracticeAdapter extends RecyclerView.Adapter<PracticeAdapter.ViewHolder> {

    Context context;
    ArrayList<String> listAnswer;
    ArrayList<Boolean> listResult;
    int resultPos;


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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvAnswer.setText(listAnswer.get(position));
        if (listAnswer.size() > 3) {
            holder.layoutAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.getAdapterPosition() == resultPos) {
                        holder.layoutAnswer.setBackgroundColor(Color.GREEN);
                        listResult.add(true);
//                        int curentProgress = holder.progressBar.getProgress();
//                        holder.progressBar.setProgress(curentProgress+10);
                    } else {
                        holder.layoutAnswer.setBackgroundColor(Color.RED);
                        listResult.add(false);
                    }
                    holder.layoutAnswer.setEnabled(false);
                    notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return listAnswer.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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


}
