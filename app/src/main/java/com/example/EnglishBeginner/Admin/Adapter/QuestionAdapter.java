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

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> implements Filterable {

    private List<Question> questionList;
    private List<Question> questionListOld;
    private String typeTopicOld = DEFAULTVALUE.ALL;
    private String keyWord = "";
    private MyDelegationLevel myDelegationLevel;
    private final Context context;

    public void setMyDelegationLevel(MyDelegationLevel myDelegationLevel) {
        this.myDelegationLevel = myDelegationLevel;
    }

    public QuestionAdapter(Context context) {
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
    public void setListDependOnTopic(@NonNull String topic) {
        typeTopicOld = topic;
        if (questionList.size() == 0) {
            questionList = questionListOld;
        }
        if ((topic.equalsIgnoreCase(DEFAULTVALUE.ALL) && keyWord.isEmpty())) {
            questionList = questionListOld;
        } else {
            List<Question> list = new ArrayList<>();
            for (Question question : questionListOld) {
                if (!keyWord.isEmpty()) {
                    if (topic.equalsIgnoreCase(DEFAULTVALUE.ALL)) {
                        if (question.getTitle().toLowerCase().contains(keyWord.toLowerCase())) {
                            list.add(question);
                        }
                    } else {
                        if (question.getNameTopic().equalsIgnoreCase(topic)
                                && question.getTitle().toLowerCase().contains(keyWord.toLowerCase())) {
                            list.add(question);
                        }
                    }
                } else {
                    if (topic.equalsIgnoreCase(DEFAULTVALUE.ALL)) {
                        if (question.getTitle().toLowerCase().contains(keyWord.toLowerCase())) {
                            list.add(question);
                        }
                    } else {
                        if (question.getNameTopic().equalsIgnoreCase(topic)) {
                            list.add(question);
                        }
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
        View view = LayoutInflater.from(context).inflate(R.layout.questionitem, parent, false);
        return new QuestionViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        if (question == null) {
            return;
        }
        holder.tvTitle.setText("Câu hỏi: " + question.getTitle());
        holder.tvNameTopic.setText("Chủ đề: " + question.getNameTopic());
        holder.tvWordQuestion.setText("Từ vựng: " + question.getWord());
        if (!(question.getUrlImage().trim().isEmpty())) {
            Glide.with(context).load(question.getUrlImage()).into(holder.imgQuestion);
        }
        holder.onClickListener = v -> {
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
                if (strSearch.isEmpty() && typeTopicOld.equalsIgnoreCase(DEFAULTVALUE.ALL)) {
                    questionList = questionListOld;
                } else {
                    List<Question> list = new ArrayList<>();
                    for (Question question : questionList) {
                        if (!strSearch.isEmpty()) {
                            if (typeTopicOld.equalsIgnoreCase(DEFAULTVALUE.ALL)) {
                                if (question.getTitle().toLowerCase().contains(keyWord.toLowerCase()) || question.getWord().toLowerCase().contains(keyWord.toLowerCase())) {
                                    list.add(question);
                                }
                            } else {
                                if (question.getNameTopic().equalsIgnoreCase(typeTopicOld)
                                        && (question.getTitle().toLowerCase().contains(keyWord.toLowerCase()) || question.getWord().toLowerCase().contains(keyWord.toLowerCase()))) {
                                    list.add(question);
                                }
                            }
                        } else {
                            if (typeTopicOld.equalsIgnoreCase(DEFAULTVALUE.ALL)) {
                                if (question.getTitle().toLowerCase().contains(keyWord.toLowerCase()) || question.getWord().toLowerCase().contains(keyWord.toLowerCase()))
                                {
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

    public static class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvTitle;
        private final TextView tvNameTopic,tvWordQuestion;
        private final ImageView imgQuestion;
        View.OnClickListener onClickListener;

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameTopic = itemView.findViewById(R.id.tvTopic_Question);
            tvTitle = itemView.findViewById(R.id.tvQuestion);
            tvWordQuestion = itemView.findViewById(R.id.tvWordQuestionItem);
            imgQuestion = itemView.findViewById(R.id.imgQuestion);
            ImageView imgDelete = itemView.findViewById(R.id.imgDelete_Question);
            imgDelete.setOnClickListener(this);
            ImageView imgEdit = itemView.findViewById(R.id.imgEdit_Question);
            imgEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClick(v);
        }
    }

    public interface MyDelegationLevel {
        void editItem(Question question);

        void deleteItem(Question question);
    }
}
