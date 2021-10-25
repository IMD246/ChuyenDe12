package com.example.myapplication.Admin.LearnManagement.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Admin.LearnManagement.DTO.Answer;
import com.example.myapplication.Admin.LearnManagement.DTO.Topic;
import com.example.myapplication.DEFAULTVALUE;
import com.example.myapplication.R;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>{

    private Context context;
    private List<Answer> answerList;
    private StorageReference firebaseStorage;

    private MyDelegationLevel myDelegationLevel;

    public void setMyDelegationLevel(MyDelegationLevel myDelegationLevel) {
        this.myDelegationLevel = myDelegationLevel;
    }

    public AnswerAdapter(Context context) {
        this.context = context;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answeritem,parent,false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        Answer answer = answerList.get(position);
        if (answer==null)
        {return;}
            if (answer.getUrlImage().isEmpty()) {
                holder.imgAnswer.setVisibility(View.GONE);
            }
            else
            {
                holder.imgAnswer.setVisibility(View.VISIBLE);
                Picasso.get().load(answer.getUrlImage()).resize(100, 100).into(holder.imgAnswer);
            }
        holder.tvAnswerQuestion.setText("Câu trả lời: "+answer.getAnswerQuestion());
        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDelegationLevel != null)
                {
                    switch (v.getId())
                    {
                        case R.id.imgEdit_Answer:myDelegationLevel.editItem(answer);
                            break;
                        case R.id.imgDelete_Answer:myDelegationLevel.deleteItem(answer);
                            break;
                    }
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        if (answerList !=null)
        {
            return answerList.size();
        }
        return 0;
    }
    public static class AnswerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvAnswerQuestion;
        private ImageView imgDelete,imgEdit,imgAnswer;
        View.OnClickListener onClickListener;

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnswer = itemView.findViewById(R.id.imgAnswer);
            tvAnswerQuestion = itemView.findViewById(R.id.tvAnswer);
            imgDelete = itemView.findViewById(R.id.imgDelete_Answer);
            imgDelete.setOnClickListener(this);
            imgEdit = itemView.findViewById(R.id.imgEdit_Answer);
            imgEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClick(v);
        }
    }
    public interface MyDelegationLevel
    {
        public void editItem(Answer answer);
        public void deleteItem(Answer answer);
    }
}
