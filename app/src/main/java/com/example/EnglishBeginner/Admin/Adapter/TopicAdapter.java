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

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.Admin.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.Admin.DTO.Topic;
import com.example.EnglishBeginner.R;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> implements Filterable {

    private Context context;
    private List<Topic> topicList;
    private List<Topic> topicListOld;
    private String level = DEFAULTVALUE.ALL;
    private String keyWord = "";

    private MyDelegationLevel myDelegationLevel;

    public void setMyDelegationLevel(MyDelegationLevel myDelegationLevel) {
        this.myDelegationLevel = myDelegationLevel;
    }

    public TopicAdapter(Context context) {
        this.context = context;
    }

    public void setTopicList(List<Topic> topicList) {
        this.topicList = topicList;
        topicListOld = topicList;
        notifyDataSetChanged();
    }

    public void setTopicListDependOnLevel(@NonNull String level) {
        this.level = level;
        if (topicList.size() == 0) {
            topicList = topicListOld;
        }
        if (level.equalsIgnoreCase(DEFAULTVALUE.ALL) && keyWord.isEmpty()) {
            topicList = topicListOld;
        } else {
            List<Topic> list = new ArrayList<>();
            for (Topic topic : topicListOld) {
                if (keyWord.isEmpty()) {
                    if (!level.equalsIgnoreCase(DEFAULTVALUE.ALL))
                    {
                        if (topic.getLevel() == Integer.parseInt(level)) {
                            list.add(topic);
                        }
                    }
                } else {
                    if (level.equalsIgnoreCase(DEFAULTVALUE.ALL)) {
                        if (topic.getNameTopic().toLowerCase().contains(keyWord.toLowerCase())){
                            list.add(topic);
                        }
                    }
                    else
                    {
                        if (topic.getNameTopic().toLowerCase().contains(keyWord.toLowerCase())
                                && topic.getLevel() == Integer.parseInt(level)) {
                            list.add(topic);
                        }
                    }
                }
            }
            topicList = list;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topicitem, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        Topic topic = topicList.get(position);
        if (topic == null) {
            return;
        }
        if (topic.getUrlImage().isEmpty()) {
        } else {
            Glide.with(context).load(topic.getUrlImage()).into(holder.imgTopic);
        }
        holder.tvLevel.setText("Level: " + String.valueOf(topic.getLevel()));
        holder.tvNameTopic.setText("Topic: " + topic.getNameTopic());

        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDelegationLevel != null) {
                    switch (v.getId()) {
                        case R.id.imgEdit_Topic:
                            myDelegationLevel.editItem(topic);
                            break;
                        case R.id.imgDelete_Topic:
                            myDelegationLevel.deleteItem(topic);
                            break;
                    }
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        if (topicList != null) {
            return topicList.size();
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
                if (strSearch.isEmpty() && level.equalsIgnoreCase(DEFAULTVALUE.ALL)) {
                    topicList = topicListOld;
                } else {
                    List<Topic> list = new ArrayList<>();
                    for (Topic topic : topicListOld) {
                        if (keyWord.isEmpty()) {
                            if (!level.equalsIgnoreCase(DEFAULTVALUE.ALL))
                            {
                                if (topic.getLevel() == Integer.parseInt(level)) {
                                    list.add(topic);
                                }
                            }
                        } else {
                            if (level.equalsIgnoreCase(DEFAULTVALUE.ALL)) {
                                if (topic.getNameTopic().toLowerCase().contains(keyWord.toLowerCase())){
                                    list.add(topic);
                                }
                            }
                            else
                            {
                                if (topic.getNameTopic().toLowerCase().contains(keyWord.toLowerCase())
                                        && topic.getLevel() == Integer.parseInt(level)) {
                                    list.add(topic);
                                }
                            }
                        }
                    }
                    topicList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = topicList;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                topicList = (List<Topic>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvLevel, tvNameTopic;
        private ImageView imgDelete, imgEdit, imgTopic;
        View.OnClickListener onClickListener;

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTopic = itemView.findViewById(R.id.imgvTopic);
            tvNameTopic = itemView.findViewById(R.id.tvTopic);
            tvLevel = itemView.findViewById(R.id.tvTopic_Level);
            imgDelete = itemView.findViewById(R.id.imgDelete_Topic);
            imgDelete.setOnClickListener(this);
            imgEdit = itemView.findViewById(R.id.imgEdit_Topic);
            imgEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClick(v);
        }
    }

    public interface MyDelegationLevel {
        public void editItem(Topic topic);

        public void deleteItem(Topic topic);
    }
}
