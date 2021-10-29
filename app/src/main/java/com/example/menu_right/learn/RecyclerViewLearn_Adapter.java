package com.example.menu_right.learn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu_right.Login.DEFAULTVALUE;
import com.example.menu_right.R;

import java.util.ArrayList;

public class RecyclerViewLearn_Adapter extends RecyclerView.Adapter<RecyclerViewLearn_Adapter.LearnViewHolder> {
    //khai báo các trường dữ liệu
    public ArrayList<Learn> learnArrayList;
    public Interface_Learn interface_learn;
    private Context context;

    //hàm constructor
    public RecyclerViewLearn_Adapter(Context context, ArrayList<Learn> learnArrayList) {
        this.context = context;
        this.learnArrayList = learnArrayList;
    }


    public void setInterface_learn(Interface_Learn interface_learn) {
        this.interface_learn = interface_learn;
    }

    //khởi tạo view holder
    @NonNull
    @Override
    public LearnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_recycleview_learn, parent, false);
        return new LearnViewHolder(view);
    }

    //xử lí giao diện, action cho viewholder
    @Override
    public void onBindViewHolder(@NonNull LearnViewHolder holder, int position) {
        Learn learn = learnArrayList.get(position);
        if (learn == null) {
            return;
        }
        holder.imgLesson.setImageResource(learn.getImage());
        //xử lí khi click item learn:
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hiện thị các lựa chọn khi ấn vào nút bài học
                PopupMenu popupMenu = new PopupMenu(context, holder.imgLesson);
                popupMenu.getMenuInflater().inflate(R.menu.menu_button_lesson, popupMenu.getMenu());
                onSelectedItemMenu(popupMenu);
                popupMenu.show();
            }
        });
    }

    private void onSelectedItemMenu(PopupMenu popupMenu){
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.item_menu_learn:
                    interface_learn.onClickItemPopup(DEFAULTVALUE.LEARNING_SCREEN);
                    break;
                case R.id.item_menu_test:
                    interface_learn.onClickItemPopup(DEFAULTVALUE.TEST_SCREEN);
                    break;
            }
            return true;
        });
    }
    //trả về số phần tử của list
    @Override
    public int getItemCount() {
        if (learnArrayList != null) {
            return learnArrayList.size();
        }
        return 0;
    }

    //class ViewHodler
    public class LearnViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgLesson;
        private LinearLayout layout;

        public LearnViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLesson = itemView.findViewById(R.id.img_btn_lesson);
            layout = itemView.findViewById(R.id.layout_btn_lesson);
        }
    }

    public interface Interface_Learn {
        public void onClickItemLearn(Learn learn);
        public void onClickItemPopup(String string);
    }
}
