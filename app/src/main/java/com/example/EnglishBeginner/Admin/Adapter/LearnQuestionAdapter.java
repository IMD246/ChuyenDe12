package com.example.EnglishBeginner.Admin.Adapter;

import android.annotation.SuppressLint;
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

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.Admin.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.Admin.DTO.Question;
import com.example.EnglishBeginner.R;

import java.util.ArrayList;
import java.util.List;

public class LearnQuestionAdapter extends RecyclerView.Adapter<LearnQuestionAdapter.LearnQuestionViewHolder> implements Filterable {

    private List<Question> questionList;
    private List<Question> questionListOld;
    private MyDelegationLevel myDelegationLevel;
    private String typeTopicOld = DEFAULTVALUE.ALL;
    private String typeQuestionOld = DEFAULTVALUE.ALL;
    private String keyWord = "";
    private final Context context;

    public void setMyDelegationLevel(MyDelegationLevel myDelegationLevel) {
        this.myDelegationLevel = myDelegationLevel;
    }

    public LearnQuestionAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
        questionListOld = questionList;
        notifyDataSetChanged();
    }

    // Tìm kiếm dữ liệu dựa vào các options của menu
    @SuppressLint("NotifyDataSetChanged")
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
    public LearnQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.learnquestionitem, parent, false);
        return new LearnQuestionViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LearnQuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        if (question == null) {
            return;
        }
        holder.tvTitle.setText("Câu hỏi: " + question.getTitle());
        holder.tvNameTopic.setText("Chủ đề: " + question.getNameTopic());
        holder.tvTypeQuestion.setText("Loại câu hỏi: " + question.getNameTypeQuestion());
        if (question.getWord().isEmpty()) {
            holder.tvWord.setText("Từ vựng: " + DEFAULTVALUE.DEFAULTVALUE + " (" + DEFAULTVALUE.DEFAULTVALUE + ")");
        } else {
            holder.tvWord.setText("Từ vựng: " + question.getWord() + " (" + question.getTypeWord() + ")");
        }
        holder.tvWordMeaning.setText("Nghĩa của từ: " + question.getWordMeaning());
        holder.tvExample.setText("Ví dụ: " + question.getExample());
        holder.tvExampleMeaning.setText("Nghĩa ví dụ: " + question.getExampleMeaning());
        holder.tvGrammar.setText("Ngữ pháp: " + question.getGrammar());
        if (question.getUrlImage().trim().length() > 0 || !(question.getUrlImage().isEmpty())) {
            Glide.with(context).load(question.getUrlImage()).into(holder.imgLearnQuestion);
        }
        holder.onClickListener = v -> {
            if (myDelegationLevel != null) {
                if (v.getId() == R.id.imgEdit_LearnQuestion) {
                    myDelegationLevel.editItem(question);
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
            @SuppressWarnings("unchecked")
            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                questionList = (List<Question>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class LearnQuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvTitle;
        private final TextView tvNameTopic;
        private final TextView tvTypeQuestion;
        private final TextView tvExample;
        private final TextView tvWord;
        private final TextView tvGrammar;
        private final TextView tvWordMeaning;
        private final TextView tvExampleMeaning;
        private final ImageView imgLearnQuestion;
        private View.OnClickListener onClickListener;

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
            tvWordMeaning = itemView.findViewById(R.id.tvWordMeaning);
            tvExampleMeaning = itemView.findViewById(R.id.tvExampleMeaning);
            imgLearnQuestion = itemView.findViewById(R.id.imgLearnQuestion);
            ImageView imgEdit = itemView.findViewById(R.id.imgEdit_LearnQuestion);
            imgEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClick(v);
        }
    }

    public interface MyDelegationLevel {
        void editItem(Question question);
    }
}
