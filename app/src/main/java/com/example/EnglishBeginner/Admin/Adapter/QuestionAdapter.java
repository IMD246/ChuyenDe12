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

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> implements Filterable {

    private Context context;
    private List<Question> questionList;
    private List<Question> questionListOld;
    private String typeTopicOld = DEFAULTVALUE.ALL;
    private String typeQuestionOld = DEFAULTVALUE.ALL;
    private String keyWord = "";
    private MyDelegationLevel myDelegationLevel;

    public void setMyDelegationLevel(MyDelegationLevel myDelegationLevel) {
        this.myDelegationLevel = myDelegationLevel;
    }

    public QuestionAdapter(Context context) {
        this.context = context;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
        questionListOld = questionList;
        notifyDataSetChanged();
    }
    public void setListDependOnTopicAndTypeQuestion(@NonNull String topic, @NonNull String typeQuestion) {
        typeTopicOld = topic;
        typeQuestionOld = typeQuestion;
        if (questionList.size() == 0) {
            questionList = questionListOld;
        }
        if ((topic.equalsIgnoreCase(DEFAULTVALUE.ALL) &&
                typeQuestion.equalsIgnoreCase(DEFAULTVALUE.ALL)) && keyWord.isEmpty()) {
            questionList = questionListOld;
        } else {
            List<Question> list = new ArrayList<>();
            for (Question question : questionListOld) {
                if (!keyWord.isEmpty()) {
                    if (question.getNameTopic().equalsIgnoreCase(topic)
                            && question.getNameTypeQuestion().equalsIgnoreCase(typeQuestion) &&
                            question.getTitle().toLowerCase().contains(keyWord.toLowerCase())) {
                        list.add(question);
                    } else if (question.getNameTypeQuestion().equalsIgnoreCase(typeQuestion)
                            && topic.equalsIgnoreCase(DEFAULTVALUE.ALL) &&
                            question.getTitle().toLowerCase().contains(keyWord.toLowerCase())) {
                        list.add(question);
                    } else if (typeQuestion.equalsIgnoreCase(DEFAULTVALUE.ALL) &&
                            question.getNameTopic().equalsIgnoreCase(topic) &&
                            question.getTitle().toLowerCase().contains(keyWord.toLowerCase())) {
                        list.add(question);
                    }
                } else {
                    if (question.getNameTopic().equalsIgnoreCase(topic) && question.getNameTypeQuestion().equalsIgnoreCase(typeQuestion)) {
                        list.add(question);
                    } else if (question.getNameTypeQuestion().equalsIgnoreCase(typeQuestion) && topic.equalsIgnoreCase(DEFAULTVALUE.ALL)) {
                        list.add(question);
                    } else if (typeQuestion.equalsIgnoreCase(DEFAULTVALUE.ALL) && question.getNameTopic().equalsIgnoreCase(topic)) {
                        list.add(question);
                    }
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
        if (question == null) {
            return;
        }
        holder.tvTitle.setText("Câu hỏi: " + String.valueOf(question.getTitle()));
        holder.tvNameTopic.setText("Chủ đề: " + question.getNameTopic());
        holder.tvTypeQuestion.setText("Loại câu hỏi: " + question.getNameTypeQuestion());

        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDelegationLevel != null) {
                    switch (v.getId()) {
                        case R.id.imgEdit_Question:
                            myDelegationLevel.editItem(question);
                            break;
                        case R.id.imgDelete_Question:
                            myDelegationLevel.deleteItem(question);
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                keyWord = constraint.toString();
                if (strSearch.isEmpty() && typeQuestionOld.equalsIgnoreCase(DEFAULTVALUE.ALL)
                        && typeTopicOld.equalsIgnoreCase(DEFAULTVALUE.ALL)) {
                    questionList = questionListOld;
                } else {
                    List<Question> list = new ArrayList<>();
                    for (Question question : questionList) {
                        if (!(strSearch.isEmpty())) {
                            if (typeQuestionOld.equalsIgnoreCase(DEFAULTVALUE.ALL) && typeTopicOld.equalsIgnoreCase(DEFAULTVALUE.ALL)) {
                                if (question.getTitle().toLowerCase().contains(strSearch.toLowerCase())) {
                                    list.add(question);
                                }
                            } else if (typeTopicOld.equalsIgnoreCase(DEFAULTVALUE.ALL) && !(typeTopicOld.equalsIgnoreCase(DEFAULTVALUE.ALL))) {
                                if (question.getTitle().toLowerCase().contains(strSearch.toLowerCase())
                                        && question.getNameTopic().equalsIgnoreCase(typeTopicOld)) {
                                    list.add(question);
                                }
                            } else {
                                if (question.getTitle().toLowerCase().contains(strSearch.toLowerCase())
                                        && question.getNameTypeQuestion().equalsIgnoreCase(typeQuestionOld)) {
                                    list.add(question);
                                }
                            }
                        } else {
                            if (typeTopicOld.equalsIgnoreCase(DEFAULTVALUE.ALL) && !(typeTopicOld.equalsIgnoreCase(DEFAULTVALUE.ALL))) {
                                if (question.getNameTopic().equalsIgnoreCase(typeTopicOld)) {
                                    list.add(question);
                                }
                            } else {
                                if (question.getNameTypeQuestion().equalsIgnoreCase(typeQuestionOld)) {
                                    list.add(question);
                                }
                            }
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

    public static class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle, tvNameTopic, tvTypeQuestion;
        private ImageView imgDelete, imgEdit;
        View.OnClickListener onClickListener;

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameTopic = itemView.findViewById(R.id.tvTopic_Question);
            tvTitle = itemView.findViewById(R.id.tvQuestion);
            tvTypeQuestion = itemView.findViewById(R.id.tvTypeQuestion_Question);
            imgDelete = itemView.findViewById(R.id.imgDelete_Question);
            imgDelete.setOnClickListener(this);
            imgEdit = itemView.findViewById(R.id.imgEdit_Question);
            imgEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClick(v);
        }
    }

    public interface MyDelegationLevel {
        public void editItem(Question question);

        public void deleteItem(Question question);
    }
}
