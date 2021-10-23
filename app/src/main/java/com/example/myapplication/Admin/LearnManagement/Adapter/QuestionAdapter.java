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

import com.example.myapplication.Admin.LearnManagement.DTO.Question;
import com.example.myapplication.DEFAULTVALUE;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> implements Filterable {
    private Context context;
    private List<Question> questionList;
    private List<Question> questionListOld;
    private Mydelegation mydelegation;

    public QuestionAdapter(Context context) {
        this.context = context;
    }

    public void setMydelegation(Mydelegation mydelegation) {
        this.mydelegation = mydelegation;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
        questionListOld = questionList;
    }
    public void setListDependOnTopicAndTypeQuestion(@NonNull String topic , String typeQuestion)
    {
        if (topic.equalsIgnoreCase(DEFAULTVALUE.TOPIC) && typeQuestion.equalsIgnoreCase(DEFAULTVALUE.TYPEQUESTION))
        {
            questionList = questionListOld;
        }
        else
        {
            List<Question> list = new ArrayList<>();
            for (Question question : questionList)
            {
                if (question.getNameTopic().equalsIgnoreCase(topic) || question.getNameTypeQuestion().equalsIgnoreCase(typeQuestion))
                {
                    list.add(question);
                }
            }
            questionList = list;
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.questionitem, parent, false);
        return new QuestionViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        if (question == null)
        {return;}
        holder.tvTopicQuestion.setText("Chủ đề : " + question.getNameTopic());
        holder.tvTypeQuestion.setText("Loại câu hỏi : " + question.getNameTypeQuestion());
        holder.tvQuestion.setText("Câu hỏi: " + question.getTitle());
        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mydelegation != null) {
                    switch (v.getId()) {
                        case R.id.imgDelete_Question:
                            mydelegation.deleteItem(question);
                            break;
                        case R.id.imgEdit_Question:
                            mydelegation.editItem(question);
                            break;
                    }
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String s = constraint.toString();
                if (s.isEmpty() || s.length() == 0)
                {
                    questionList = questionListOld;
                }
                else
                {
                    List<Question> list = new ArrayList<>();
                    for (Question question : questionList)
                    {
                        if (question.getTitle().toLowerCase().contains(s.toLowerCase()))
                        {
                            list.add(question);
                        }
                    }
                    questionList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values =  questionList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                questionList = (List<Question>)results.values;
                notifyDataSetChanged();
            }
        };
    }
    public class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvQuestion, tvTopicQuestion, tvTypeQuestion;
        ImageView imgDeleteQuestion, imgEditQuestion;
        View.OnClickListener onClickListener;

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvTopicQuestion = itemView.findViewById(R.id.tvTopic_Question);
            tvTypeQuestion = itemView.findViewById(R.id.tvTypeQuestion_Question);
            imgEditQuestion = itemView.findViewById(R.id.imgEdit_Question);
            imgEditQuestion.setOnClickListener(this);
            imgDeleteQuestion = itemView.findViewById(R.id.imgDelete_Question);
            imgDeleteQuestion.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClick(v);
        }
    }

    public interface Mydelegation {
        public void deleteItem(Question question);
        public void editItem(Question question);
    }
}
