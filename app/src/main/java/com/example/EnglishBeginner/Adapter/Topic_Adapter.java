package com.example.EnglishBeginner.Adapter;

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

import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.DTO.Topic;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.learn.learning.LearningEnglishFragment;
import com.example.EnglishBeginner.learn.testing.TestSelectionEnglishFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    //xử lí giao diện, action cho viewholder
    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        Topic topic = topicList.get(position);
        if (topic == null) {
            return;
        }
        if (topic.getUrlImage().trim().isEmpty() || topic.getUrlImage().trim().length() == 0) {
        } else {
            Picasso.get().load(topic.getUrlImage()).resize(100, 100).into(holder.imgTopic);
        }
        holder.tvNameTopic.setText(topic.getNameTopic());
        //xử lí khi click item learn:
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> listProcess = new ArrayList<>();
                listProcess.add(1);
                listProcess.add(1);
                DEFAULTVALUE.alertDialogTopic("Cấp 1", "Hãy cố gắng vượt qua thử thách này để chứng tỏ khả năng của bản thân!",listProcess, context);
            }
        });
    }

    private void onSelectedItemMenu(PopupMenu popupMenu) {
        popupMenu.setOnMenuItemClickListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.item_menu_learn:
                    intent = new Intent(context, LearningEnglishFragment.class);
                    context.startActivity(intent);
                    break;
                case R.id.item_menu_test:
                    intent = new Intent(context, TestSelectionEnglishFragment.class);
                    context.startActivity(intent);
                    break;
            }
            return true;
        });
    }

    //trả về số phần tử của list
    @Override
    public int getItemCount() {
        return topicList.size();
    }

    //class ViewHodler
    public class TopicViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgTopic;
        private TextView tvNameTopic;
        private LinearLayout layout;

        public TopicViewHolder(View itemView) {
            super(itemView);
            imgTopic = itemView.findViewById(R.id.imgTopic);
            tvNameTopic = itemView.findViewById(R.id.tvNameTopic);
            layout = itemView.findViewById(R.id.layout_btn_lesson);
        }
    }

    public interface Interface_Learn {
        public void onClickItemLearn(Topic topic);

        public void onClickItemPopup(String string);

        public void createAlertDialog(Topic topic, Context context);
    }
}
