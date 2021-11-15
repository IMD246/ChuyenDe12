package com.example.EnglishBeginner.fragment.LearnWord.saveWord.source;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.EnglishBeginner.DTO.Word;

import java.util.ArrayList;


public class SaveSqliteHelper extends SQLiteOpenHelper {

    Context context;
    final static String  DATABASE_NAME = "SaveWord.db";
    public static int DATABASE_VERSION =1;

    String table_name = "save_word";
    String column_id = "id";
    String column_word = "word";
    String column_html = "html";
    String column_description = "description";
    String column_pronounce = "pronounce";
    String column_meaning = "meaning";

    public SaveSqliteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+table_name+
                " (" + column_id + " TEXT, "+
                column_word + " TEXT, "+
                column_html + " TEXT, "+
                column_description + " TEXT, "+
                column_pronounce + " TEXT, "+
                column_meaning + " TEXT); ";
                db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+table_name);
        onCreate(db);
    }
    public void alertDialogConfirm(SQLiteDatabase db, ContentValues cv,Word word){
        if(checkTheSaveWord(word)){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("tu bi trung\nban co muon them khong");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "đồng ý",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            long result = db.insert(table_name,null,cv);
                            if(result== -1){
                                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(context, "Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            builder1.setNegativeButton(
                    "không",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        else{
            long result = db.insert(table_name,null,cv);
            if(result== -1){
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "Successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addSaveWordByButton(Word word){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(column_id, word.getId());
        cv.put(column_word, word.getWord());
//        TranslateText translateText = new TranslateText();
//        translateText.TranslateText(word.getWord());
        cv.put(column_meaning,word.getDescription()) ;
        alertDialogConfirm(db,cv,word);


    }
    public void addSaveWordByButtonFirebase(Word word){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(column_id, word.getId());
        cv.put(column_word, word.getWord());
//        TranslateText translateText = new TranslateText();
//        translateText.TranslateText(word.getWord());
        cv.put(column_meaning,word.getMeaning()) ;



        alertDialogConfirm(db,cv,word);

    }
    public void addSaveWordByFloating(Word word){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(column_id, word.getId());
        cv.put(column_word, word.getWord());
        if(word.getMeaning()!=null){
            cv.put(column_meaning, word.getMeaning());

        }else{
            cv.put(column_meaning,"not found");

        }

        alertDialogConfirm(db,cv,word);


    }
    public boolean checkTheSaveWord(Word word){
        ArrayList<Word> list = new ArrayList<>();
        fetchData(list);
        boolean checkIfEqual = false;
        for (Word word1:list) {
            if(word1.getWord().equals(word.getWord())){
                checkIfEqual = true;
                break;
            }

        }

       return checkIfEqual;

    }
    public void fetchData(ArrayList<Word> list){
        list.clear();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        //add a column if it not exist

        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+table_name, null, null);

        if(cursor != null){
            while (cursor.moveToNext()) {
                boolean checked = false;
                try{
                    Word addItem = new Word( Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1).toString(),cursor.getString(5).toString()
                    );
//
                    list.add(addItem);
                }catch (Exception ex){
                    ex.printStackTrace();
                }


            } }

        cursor.close();

    }
    public void removeSaveWord(Word word){
        SQLiteDatabase db = this.getWritableDatabase();

        int i= db.delete("save_word",column_word+" = '" + word.getWord() +"'",null);

        Log.e("","delete data" + i);
    }


}
