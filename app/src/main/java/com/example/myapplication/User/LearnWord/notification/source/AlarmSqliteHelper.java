package com.example.myapplication.User.LearnWord.notification.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.User.LearnWord.history.HistoryItem;

import java.util.ArrayList;

public class AlarmSqliteHelper extends SQLiteOpenHelper {

    Context context;
    final static String DATABASE_NAME = "alarm.db";
    public static int DATABASE_VERSION = 1;

    String table_name = "day";
    String table_name2 = "pickertime";
    String column_id = "id";
    String column_day = "day";
    String column_status = "status";
    String column_picker = "picker";


    public AlarmSqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + table_name +
                " (" + column_id + " TEXT, " +
                column_day + " TEXT, " +
                column_status + " INTEGER ); ";

        String query2 = "CREATE TABLE " + table_name2 +
                " (" + column_picker + " TEXT ); ";

        db.execSQL(query);
        db.execSQL(query2);


        Log.e("TAG", "onCreate: create success");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(db);
    }
    public void addDay(DayItem dayItem) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(column_id, dayItem.getId());
            cv.put(column_day, dayItem.getDay());
            cv.put(column_status, dayItem.getStatus());
            long result = db.insert(table_name, null, cv);
            if (result == -1) {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Successfully", Toast.LENGTH_SHORT).show();
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void addDefaultFirstTime( ) {
        SQLiteDatabase db=this.getWritableDatabase();
        //list of day in the week
        ArrayList<DayItem> list = new ArrayList<DayItem>();
        list.add(new DayItem(2, "Monday", 0));
        list.add(new DayItem(3, "Tuesday", 0));
        list.add(new DayItem(4, "Wednesdays", 0));
        list.add(new DayItem(5, "Thursday", 0));
        list.add(new DayItem(6, "Friday", 0));
        list.add(new DayItem(7, "Saturday", 0));
        list.add(new DayItem(1, "Sunday", 0));
        //add default day
        for (DayItem dayItem : list) {
            ContentValues cv = new ContentValues();
            cv.put(column_id, dayItem.getId()+"");
            cv.put(column_day, dayItem.getDay());
            cv.put(column_status, dayItem.getStatus());
            db.insert(table_name, null, cv);

        }
        //add default time
        ContentValues cv2 = new ContentValues();
        cv2.put(column_picker,"bạn chưa chọn mốc thời gian nào");

        db.insert(table_name2, null, cv2);

    }
    public void fetchData(ArrayList<DayItem> list, TextView txt_time) {
        list.clear();
        try {
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            //add a column if it not exist
            Cursor cursor = sqLiteDatabase.rawQuery("select * from " + table_name, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {

                    DayItem addItem = new DayItem(Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1).toString(),
                            cursor.getInt(2));
                        list.add(addItem);
                }
                Cursor cursor2 = sqLiteDatabase.rawQuery("select * from " + table_name2, null, null);

                if (cursor2!=null) {
                    while (cursor2.moveToNext()) {
                    txt_time.setText(cursor2.getString(0));}
                }

                cursor.close();
                cursor2.close();

                sqLiteDatabase.close();

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void editDay(DayItem dayItem){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String queue = "UPDATE "+table_name+" SET status = "+dayItem.getStatus()
                + " WHERE "+column_id + " = " +  dayItem.getId() ;
        sqLiteDatabase.execSQL(queue);
        sqLiteDatabase.close();
    }
    public void editTime(String time){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String queue = "UPDATE "+table_name2+" SET "+column_picker+" = "+time;
        sqLiteDatabase.execSQL(queue);
        sqLiteDatabase.close();
    }
}
