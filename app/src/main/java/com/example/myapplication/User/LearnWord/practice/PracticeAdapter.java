package com.example.myapplication.User.LearnWord.practice;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    ArrayList<Boolean>listResult;
    int resultPos;


    public PracticeAdapter(Context context, ArrayList<String> listAnswer,int resultPos,ArrayList<Boolean>listResult) {
        this.context = context;
        this.listAnswer = listAnswer;
        this.resultPos = resultPos;
        this.listResult=listResult;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View ref = LayoutInflater.from(context).inflate(R.layout.practice_answeritem,parent,false);

        return new ViewHolder(ref);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.answerButton.setText(  listAnswer.get(position).toString());

        if(listAnswer.size()>3){
            holder.answerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(holder.getAdapterPosition()==resultPos){
                        holder.answerButton.setBackgroundColor(Color.GREEN);
                        listResult.add(true);
                    }
                    else {
                        holder.answerButton.setBackgroundColor(Color.RED);
                        listResult.add(false);
                    }
                    holder.answerButton.setEnabled(false);
                    notifyDataSetChanged();

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return listAnswer.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        Button answerButton;

        public ViewHolder(View itemView) {
            super(itemView);
            this.answerButton =itemView.findViewById(R.id.practice_btnAnswer);
        }
    }


}
