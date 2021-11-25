package com.example.EnglishBeginner.DTO;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class DEFAULTVALUE {
    final public static String USER = "User";
    final public static String ALL = "All";
    final public static String TOEIC = "Toeic";
    final public static String IETLS = "Ietls";
    final public static String IMAGE = "Image";
    final public static String WRITE = "Write";
    final public static String READ = "Read";
    final public static String LISTEN = "Listen";
    final public static String MOSTFAVORITE = "MostFavorite";
    final public static String YOURFAVORITE = "YourFavorite";
    final public static String NEW = "New";
    final public static String TEST = "Test";
    final public static String LEARN = "Learn";
    final public static String MALE = "Male";
    final public static String FEMALE = "Female";
    final public static String OTHER = "Other";

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
