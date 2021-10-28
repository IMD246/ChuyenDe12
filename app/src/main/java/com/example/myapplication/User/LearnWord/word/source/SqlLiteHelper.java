package com.example.myapplication.User.LearnWord.word.source;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.User.DTO.Word;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class SqlLiteHelper extends SQLiteOpenHelper {
    Context context;
    String dbName;
    String dbPath;
    private static final int DATABASE_VERSION = 1;

    public SqlLiteHelper(Context context, String dbName, int version) {
        super(context, dbName, null, version);
        this.context = context;
        this.dbName = dbName;

        SQLiteDatabase database = this.getReadableDatabase();
        String filePath = database.getPath();
        database.close();
        this.dbPath =filePath;

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new SQLException("Can't downgrade database from version " +
                oldVersion + " to " + newVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.disableWriteAheadLogging();

    }

    public void checkDb() {
        SQLiteDatabase checkDb = null;
        String filePath = dbPath;

        try {
            checkDb = SQLiteDatabase.openDatabase(filePath, null, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (checkDb != null) {
            Toast.makeText(context, "Databsr alreay exsitr", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "coppying", Toast.LENGTH_SHORT).show();

            CopyDatabase();
        }
    }

    private void CopyDatabase() {
        this.getReadableDatabase();
        try {


            InputStream ios = context.getAssets().open(dbName);

            OutputStream os = new FileOutputStream(dbPath);


            byte[] buffer = new byte[1024];

            int len;
            while ((len = ios.read(buffer)) > 0) {
                os.write(buffer, 0, len);

            }
            os.flush();
            ios.close();
            os.close();
            Toast.makeText(context, "Databsr alreay exsitr", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Copy db", "CopyDatabase: coppied ");
    }

    public void OpenDatabase() {
        String filePath = dbPath ;
        SQLiteDatabase.openDatabase(filePath, null, 0);
    }


    public void fetchData(ArrayList<Word> listOfWord, int page)    {

        listOfWord.clear();
        int startPosition = page * 30;
        int endPosition = startPosition + 30 - 1;
        if (page == 0) {
            startPosition = page + 100;
            endPosition = page + 38 - 1;
        }


        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        //add a column if it not exist

        Cursor cursor = sqLiteDatabase.rawQuery("select * from av LIMIT "
                + startPosition + "," + endPosition, null, null);

        if(cursor != null){


        while (cursor.moveToNext()) {

            Word addItem = new Word(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1).toString(), cursor.getString(2).toString(),cursor.getString(3).toString(),
                    cursor.getString(4).toString()
                   );
            listOfWord.add(addItem);
        } }
        cursor.close();
        sqLiteDatabase.close();
    }
    public void fetchRandomWord(String word,ArrayList<String> listItem){

        listItem.clear();
        char firstLetter = word.charAt(0);

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        //add a column if it not exist

        Cursor cursor = sqLiteDatabase.rawQuery("  select * from av where word like '"+firstLetter+"%' ORDER BY RANDOM() LIMIT 3", null, null);

        if(cursor != null){


            while (cursor.moveToNext()) {

                String item = cursor.getString(1);
                listItem.add(item);
            } }
        cursor.close();
        sqLiteDatabase.close();


    }
}
