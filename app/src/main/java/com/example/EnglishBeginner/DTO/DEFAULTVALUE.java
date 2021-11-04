package com.example.EnglishBeginner.DTO;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.Adapter.ImageLevel_Adapter;
import com.example.EnglishBeginner.R;

import java.util.ArrayList;
import java.util.List;

public class DEFAULTVALUE {
    final public static String ADMIN = "Admin";
    final public static String USER = "User";
    final public static String LEVEL = "Cấp độ";
    final public static String TYPEQUESTION = "Loại câu hỏi";
    final public static String TOPIC = "Chủ đề";
    final public static String ANSWER = "Câu trả lời";
    final public static String LEARNTOPIC = "Học";
    final public static String QUESTION = "Câu hỏi";
    final public static String ALL = "All";
    final public static String LEVELLABEL = "Level";
    final public static String DEFAULTVALUE = "Default";
    final public static String NOUNS = "Nouns";
    final public static String VERBS = "Verbs";
    final public static String ADJECTIVE = "Adjective";
    final public static String ADVERB = "Adverb";
    final public static String TOEIC = "Toeic";
    final public static String IETLS = "Ietls";
    final public static String TYPEWORD = "Loại từ vựng";
    public static String LEARNING_SCREEN = "LearningEnglishActivity";
    public static String TEST_SCREEN = "TestEnglishActivity";
    public static String MALE = "Male";
    public static String FEMALE = "Female";
    public static String OTHER = "Other";

    public static void alertDialogMessage(String title, String msg, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setNegativeButton(
                "Đóng",
                (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void alertDialogTopic(String level, String title, List<Integer> list, Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_popup_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        ImageView imglevel1 = dialog.findViewById(R.id.level1);
        ImageView imglevel2 = dialog.findViewById(R.id.level2);
        TextView tvLevel = dialog.findViewById(R.id.tv_level_topic);
        TextView tvTitle = dialog.findViewById(R.id.tv_title_topic);
        TextView tvLevel1 = dialog.findViewById(R.id.tv_level1);
        TextView tvLevel2 = dialog.findViewById(R.id.tv_level2);

        tvLevel.setText(level);
        tvTitle.setText(title);

        List<Integer> integerList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++){
            if (list.get(i) == 0){
                integerList.add(R.drawable.level_blur);
            }else {
                integerList.add(R.drawable.level);

            }
        }
        if (list.get(0) == 1){
            tvLevel1.setText("1");
        }else{
            tvLevel1.setText("");
        }
        if (list.get(1) == 1){
            tvLevel2.setText("2");
        }
        else{
            tvLevel2.setText("");
        }
        imglevel1.setImageResource(integerList.get(0));
        imglevel2.setImageResource(integerList.get(1));



        dialog.show();
    }
}
