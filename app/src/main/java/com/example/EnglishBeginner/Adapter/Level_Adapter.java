package com.example.EnglishBeginner.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.DTO.Level;
import com.example.EnglishBeginner.DTO.Topic;
import com.example.EnglishBeginner.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Level_Adapter extends RecyclerView.Adapter<Level_Adapter.LearnViewHolder> {
    //khai báo các trường dữ liệu
    public List<Level> levelArrayList;
    public Interface_Learn interface_learn;
    private Context context;

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

    //khởi tạo view holder
    @NonNull
    @Override
    public LearnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_level, parent, false);
        return new LearnViewHolder(view);
    }

    //xử lí giao diện, action cho viewholder
    @Override
    public void onBindViewHolder(@NonNull LearnViewHolder holder, int position) {
        Level level = levelArrayList.get(position);
        if (level == null) {
            return;
        }
        if (level.getUrlImage().trim().isEmpty() || level.getUrlImage().trim().length() == 0) {
        } else {
            Picasso.get().load(level.getUrlImage()).resize(100, 100).into(holder.imgLesson);
        }
        if (level.getListTopic().size() > 0) {
            holder.rcvLevelTopicItem.getRecycledViewPool().setMaxRecycledViews(1,0);
            setTopicItemRecycler(holder.rcvLevelTopicItem, level.getListTopic());
        }
        //xử lí khi click item learn:
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (interface_learn!=null)
//                {
//                    interface_learn.createAlertDialog(level);
//                }

            }
        });
    }

    private void onSelectedItemMenu(PopupMenu popupMenu) {

    }

    //trả về số phần tử của list
    @Override
    public int getItemCount() {
        if (levelArrayList != null) {
            return levelArrayList.size();
        }
        return 0;
    }
    //class ViewHodler
    public class LearnViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgLesson;
        private RecyclerView rcvLevelTopicItem;
        private LinearLayout layout;

        public LearnViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLesson = itemView.findViewById(R.id.imgLevel);
            rcvLevelTopicItem = itemView.findViewById(R.id.rcvLevelTopicItem);
            layout = itemView.findViewById(R.id.layout_btn_lesson);
        }
    }

    private void setTopicItemRecycler(RecyclerView recycler, List<Topic> topicList) {
        recycler.setHasFixedSize(true);
        recycler.setNestedScrollingEnabled(false);
        Topic_Adapter topic_adapter = new Topic_Adapter(context);
        topic_adapter.setTopicList(topicList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        topic_adapter.setTopicList(topicList);
        recycler.setLayoutManager(gridLayoutManager);
        recycler.setAdapter(topic_adapter);
        topic_adapter.setInterface_learn(new Topic_Adapter.Interface_Learn() {
            @Override
            public void onClickItemLearn(Topic topic) {

            }

            @Override
            public void onClickItemPopup(String string) {

            }

            @Override
            public void createAlertDialog(Topic topic) {
                interface_learn.createAlertDialog(topic);
            }
        });
    }

    public interface Interface_Learn {
        public void onClickItemLearn(Level level);

        public void onClickItemPopup(String string);

        public void createAlertDialog(Topic topic);
    }
}
