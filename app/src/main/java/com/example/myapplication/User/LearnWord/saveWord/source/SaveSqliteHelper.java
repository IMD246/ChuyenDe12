package com.example.myapplication.User.LearnWord.saveWord.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


import com.example.myapplication.User.DTO.Word;

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
                column_pronounce + " TEXT); ";
                db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+table_name);
        onCreate(db);
    }

    public void addSaveWord(Word word){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(column_id, word.getId());
        cv.put(column_word, word.getWord());
        cv.put(column_html, word.getHtmlText());
        cv.put(column_description, word.getDescription());
        cv.put(column_pronounce, word.getPronounce());

        long result = db.insert(table_name,null,cv);
        if(result== -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Successfully", Toast.LENGTH_SHORT).show();
        }
    }
    public void fetchData(ArrayList<Word> list){
        list.clear();


        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        //add a column if it not exist

        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+table_name, null, null);

        if(cursor != null){


            while (cursor.moveToNext()) {
                boolean checked = false;

                Word addItem = new Word(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1).toString(),
                        cursor.getString(2).toString()
                        ,cursor.getString(3).toString(),
                        cursor.getString(4).toString()
                );
                for (Word w:list
                     ) {
                    if( w.getId() == addItem.getId()){
                        checked = true;
                    }
                }
                if(checked==true){
                }
                else{
                    list.add(addItem);
                }
            } }
        cursor.close();
        sqLiteDatabase.close();
    }
    public void removeSaveWord(Word word){
        SQLiteDatabase db = this.getWritableDatabase();

        int i= db.delete("save_word","id = '" + word.getId() +"'",null);

        Log.e("","delete data" + i);
    }


}
