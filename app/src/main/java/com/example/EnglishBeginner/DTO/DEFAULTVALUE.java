package com.example.EnglishBeginner.DTO;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.Adapter.DAOImageLevel_Adapter;
import com.example.EnglishBeginner.R;

import java.util.ArrayList;

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

    public static void alertDialogTopic(String level, String title, ArrayList<Integer> arrayList, Context context) {
        Dialog dialog = new Dialog(context);
        RecyclerView rscView = dialog.findViewById(R.id.rsc_level_topic);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 5);
        rscView.setLayoutManager(gridLayoutManager);
        DAOImageLevel_Adapter daoImageLevel_adapter = new DAOImageLevel_Adapter(context);
        daoImageLevel_adapter.setIntegerList(arrayList);
        rscView.setAdapter(daoImageLevel_adapter);


        TextView tvLevel = dialog.findViewById(R.id.tv_level);
        TextView tvTitle = dialog.findViewById(R.id.tv_title);

        tvLevel.setText(level);
        tvTitle.setText(title);

        dialog.setContentView(R.layout.layout_popup_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
