package com.example.EnglishBeginner.Admin.Adapter;

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

import com.example.EnglishBeginner.Admin.DTO.Question;
import com.example.EnglishBeginner.Admin.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.R;

import java.util.ArrayList;
import java.util.List;

public class LearnQuestionAdapter extends RecyclerView.Adapter<LearnQuestionAdapter.LearnQuestionViewHolder> implements Filterable {

    private Context context;
    private List<Question> questionList;
    private List<Question> questionListOld;

    private MyDelegationLevel myDelegationLevel;

    public void setMyDelegationLevel(MyDelegationLevel myDelegationLevel) {
        this.myDelegationLevel = myDelegationLevel;
    }

    public LearnQuestionAdapter(Context context) {
        this.context = context;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
        questionListOld = questionList;
        notifyDataSetChanged();
    }
    // tìm kiếm dữ liệu dựa vào các options của menu
    public void setListDependOnTopicAndTypeQuestion(@NonNull String topic, @NonNull String typeQuestion) {
        if (questionList.size() == 0) {
            questionList = questionListOld;
        }
        if (topic.equalsIgnoreCase(DEFAULTVALUE.TOPIC) && typeQuestion.equalsIgnoreCase(DEFAULTVALUE.TYPEQUESTION)) {
            questionList = questionListOld;
        } else {
            List<Question> list = new ArrayList<>();
            for (Question question : questionListOld) {
                if (question.getNameTopic().equalsIgnoreCase(topic) && question.getNameTypeQuestion().equalsIgnoreCase(typeQuestion)) {
                    list.add(question);
                } else if (question.getNameTypeQuestion().equalsIgnoreCase(typeQuestion) && topic.equalsIgnoreCase(DEFAULTVALUE.TOPIC)) {
                    list.add(question);
                } else if (typeQuestion.equalsIgnoreCase(DEFAULTVALUE.TYPEQUESTION) && question.getNameTopic().equalsIgnoreCase(topic)) {
                    list.add(question);
                }
            }
            questionList = list;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LearnQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.learnquestionitem, parent, false);
        return new LearnQuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LearnQuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        if (question == null) {
            return;
        }
        holder.tvTitle.setText("Câu hỏi: " + question.getTitle());
        holder.tvNameTopic.setText("Chủ đề: " + question.getNameTopic());
        holder.tvTypeQuestion.setText("Loại câu hỏi: " + question.getNameTypeQuestion());
        if (question.getTypeWord().equalsIgnoreCase(null) && question.getWord().equalsIgnoreCase(null)) {
            holder.tvWord.setText("Từ vựng: " + DEFAULTVALUE.DEFAULTVALUE + " (" + DEFAULTVALUE.DEFAULTVALUE + ")");
        } else {
            holder.tvWord.setText("Từ vựng: " + question.getWord() + " (" + question.getTypeWord() + ")");
        }
        if (question.getExample().equalsIgnoreCase(null)) {
            holder.tvExample.setText("Ví dụ: " + DEFAULTVALUE.DEFAULTVALUE);
        } else {
            holder.tvExample.setText("Ví dụ: " + question.getExample());
        }
        if (question.getGrammar().equalsIgnoreCase(null)) {
            holder.tvGrammar.setText("Ngữ pháp: " + DEFAULTVALUE.DEFAULTVALUE);
        } else {
            holder.tvGrammar.setText("Ngữ pháp: " + question.getGrammar());
        }
        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDelegationLevel != null) {
                    switch (v.getId()) {
                        case R.id.imgEdit_LearnQuestion:
                            myDelegationLevel.editItem(question);
                            break;
                    }
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        if (questionList != null) {
            return questionList.size();
        }
        return 0;
    }
    // hàm tìm kiếm gần đúng với từng keyword mà người dùng nhập vào
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty() || strSearch.length() == 0) {
                    if (questionList.size() == 0) {
                        questionList = questionListOld;
                    }
                } else {
                    List<Question> list = new ArrayList<>();
                    for (Question question : questionList) {
                        if (question.getTitle().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(question);
                        }
                    }
                    questionList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = questionList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                questionList = (List<Question>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class LearnQuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle, tvNameTopic, tvTypeQuestion, tvExample, tvWord, tvGrammar;
        private ImageView imgEdit;
        View.OnClickListener onClickListener;

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        public LearnQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameTopic = itemView.findViewById(R.id.tvTopic_LearnQuestion);
            tvTitle = itemView.findViewById(R.id.tvTitleLearnQuestion);
            tvTypeQuestion = itemView.findViewById(R.id.tvTypeQuestion_LearnQuestion);
            tvExample = itemView.findViewById(R.id.tvExampleLearnQuestion);
            tvWord = itemView.findViewById(R.id.tvWordLearnQuestion);
            tvGrammar = itemView.findViewById(R.id.tvGrammar);
            imgEdit = itemView.findViewById(R.id.imgEdit_LearnQuestion);
            imgEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClick(v);
        }
    }

    public interface MyDelegationLevel {
        public void editItem(Question question);
    }
}
