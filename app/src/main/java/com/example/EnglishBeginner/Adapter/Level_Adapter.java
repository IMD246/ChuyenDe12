package com.example.EnglishBeginner.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.DTO.Level;
import com.example.EnglishBeginner.DTO.Topic;
import com.example.EnglishBeginner.R;

import java.util.ArrayList;
import java.util.List;

public class Level_Adapter extends RecyclerView.Adapter<Level_Adapter.LearnViewHolder> {
    //khai báo các trường dữ liệu
    private List<Level> levelArrayList;
    private List<Topic> topicList;
    private Interface_Learn interface_learn;
    private String uid;
    private final Context context;

    public void setUid(String uid) {
        this.uid = uid;
    }

    //hàm constructor
    public Level_Adapter(Context context) {
        this.context = context;
    }

    public void setLevelArrayList(List<Level> levelArrayList) {
        this.levelArrayList = levelArrayList;
    }

    public void setInterface_learn(Interface_Learn interface_learn) {
        this.interface_learn = interface_learn;
    }

    public void setTopicList(List<Topic> topicList) {
        this.topicList = topicList;
    }

    //khởi tạo view holder
    @NonNull
    @Override
    public LearnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_level, parent, false);
        return new LearnViewHolder(view);
    }

    //xử lí giao diện, action cho viewHolder
    @Override
    public void onBindViewHolder(@NonNull LearnViewHolder holder, int position) {
        Level level = levelArrayList.get(position);
        if (level == null) {
            return;
        }
        holder.tvTitleLevel.setText(String.valueOf(level.getNameLevel()));
        if (!(level.getUrlImage().trim().isEmpty())) {
            Glide.with(context).load(level.getUrlImage()).into(holder.imgLesson);
        }
        List<Topic> listTopicNew = new ArrayList<>();
        if (topicList.size()>0)
        {
            for (int i=0;i<topicList.size();i++)
            {
                if (level.getId() == topicList.get(i).getIdLevel())
                {
                    listTopicNew.add(topicList.get(i));
                }
            }
        }
        setTopicItemRecycler(holder.rcvLevelTopicItem, listTopicNew,position);
        //xử lí khi click item learn:
        holder.layout.setOnClickListener(v -> {
            if (interface_learn!=null)
            {
                interface_learn.onClickItemLearn(level);
            }
        });
    }
    //trả về số phần tử của list
    @Override
    public int getItemCount() {
        if (levelArrayList != null) {
            return levelArrayList.size();
        }
        return 0;
    }
    //class ViewHolder
    public static class LearnViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgLesson;
        private final RecyclerView rcvLevelTopicItem;
        private final LinearLayout layout;
        private final TextView tvTitleLevel;

        public LearnViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLesson = itemView.findViewById(R.id.imgLevel);
            rcvLevelTopicItem = itemView.findViewById(R.id.rcvLevelTopicItem);
            layout = itemView.findViewById(R.id.layout_btn_lesson);
            tvTitleLevel = itemView.findViewById(R.id.tvTitleLevel);
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private void setTopicItemRecycler(RecyclerView recycler, List<Topic> topicList, int count) {
        Topic_Adapter topic_adapter = new Topic_Adapter(context);
        topic_adapter.setTopicList(topicList);
        if (count == 0)
        {
            topic_adapter.setUid(uid);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context.getApplicationContext(), 2);
        recycler.setLayoutManager(gridLayoutManager);
        recycler.setAdapter(topic_adapter);
        recycler.setHasFixedSize(true);
        recycler.setNestedScrollingEnabled(false);
        topic_adapter.notifyDataSetChanged();
        topic_adapter.setInterface_learn(topic -> interface_learn.createAlertDialog(topic));
    }

    public interface Interface_Learn {
        void onClickItemLearn(Level level);
        void createAlertDialog(Topic topic);
    }
}
