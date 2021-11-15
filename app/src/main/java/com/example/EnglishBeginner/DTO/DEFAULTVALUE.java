package com.example.EnglishBeginner.DTO;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

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
    final public static String IMAGE = "Image";
    final public static String WRITE = "Write";
    final public static String READ = "Read";
    final public static String LISTEN = "Listen";
    final public static String TEST = "Test";
    final public static String LEARN = "Learn";
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
                (dialog, id) -> {
                    dialog.cancel();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
