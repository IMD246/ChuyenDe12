package com.example.englishbeginner.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishbeginner.DTO.Topic;
import com.example.englishbeginner.R;
import com.example.englishbeginner.learn.LearningEnglishActivity;
import com.example.englishbeginner.learn.TestSelectionEnglishActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Topic_Adapter extends RecyclerView.Adapter<Topic_Adapter.TopicViewHolder> {
    //khai báo các trường dữ liệu
    public List<Topic> topicList;
    public Interface_Learn interface_learn;
    private Context context;

    //hàm constructor
    public Topic_Adapter(Context context) {
        this.context = context;
    }

    public void setTopicList(List<Topic> topicList) {
        this.topicList = topicList;
        notifyDataSetChanged();
    }

    public void setInterface_learn(Interface_Learn interface_learn) {
        this.interface_learn = interface_learn;
    }

    //khởi tạo view holder
    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_topic, parent, false);
        return new TopicViewHolder(view);
    }
    //xử lí giao diện, action cho viewholder
    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        Topic topic = topicList.get(position);
        if (topic == null) {
            return;
        }
        if (topic.getUrlImage().trim().isEmpty()||topic.getUrlImage().trim().length()==0)
        { }
        else {
            Picasso.get().load(topic.getUrlImage()).resize(100,100).into(holder.imgTopic);
        }
        holder.tvNameTopic.setText(topic.getNameTopic());
        //xử lí khi click item learn:
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hiện thị các lựa chọn khi ấn vào nút bài học
                PopupMenu popupMenu = new PopupMenu(context, holder.imgTopic);
                popupMenu.getMenuInflater().inflate(R.menu.menu_button_lesson, popupMenu.getMenu());
                onSelectedItemMenu(popupMenu);
                popupMenu.show();
            }
        });
    }

    private void onSelectedItemMenu(PopupMenu popupMenu){
        popupMenu.setOnMenuItemClickListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.item_menu_learn:
//                    interface_learn.onClickItemPopup(DEFAULTVALUE.LEARNING_SCREEN);
                    intent = new Intent(context, LearningEnglishActivity.class);
                    context.startActivity(intent);
                    break;
                case R.id.item_menu_test:
//                    interface_learn.onClickItemPopup(DEFAULTVALUE.TEST_SCREEN);
                    intent = new Intent(context, TestSelectionEnglishActivity.class);
                    context.startActivity(intent);
                    break;
            }
            return true;
        });
    }
    //trả về số phần tử của list
    @Override
    public int getItemCount() {
        if (topicList != null) {
            return topicList.size();
        }
        return 0;
    }
    //class ViewHodler
    public class TopicViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgTopic;
        private TextView tvNameTopic;
        private LinearLayout layout;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTopic = itemView.findViewById(R.id.imgTopic);
            tvNameTopic = itemView.findViewById(R.id.tvNameTopic);
            layout = itemView.findViewById(R.id.layout_btn_lesson);
        }
    }

    public interface Interface_Learn {
        public void onClickItemLearn(Topic topic);
        public void onClickItemPopup(String string);
    }
}
