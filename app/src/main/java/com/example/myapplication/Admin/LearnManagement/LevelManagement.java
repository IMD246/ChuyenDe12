package com.example.myapplication.Admin.LearnManagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class LevelManagement extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LevelAdapter levelAdapter;
    private List<Level>list;
    private ImageView imgAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_management);
        list = setData();
        recyclerView = findViewById(R.id.rcvLevel);
        levelAdapter = new LevelAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(recyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        levelAdapter.setListLevel(list);
        recyclerView.setAdapter(levelAdapter);
        levelAdapter.setMyDelegationLevel(new LevelAdapter.MyDelegationLevel() {
            @Override
            public void editItem(Level level) {
                openDialog(Gravity.CENTER,2, level);
            }

            @Override
            public void deleteItem(Level level) {
                alertDialog(level);
            }
        });
        imgAdd = findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(Gravity.CENTER,1,null);
            }
        });
    }

    private void openDialog(int center,int choice,Level level) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addeditlevel);

        Window window = dialog.getWindow();
        if (window==null)
        {return;}
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = center;
        window.setAttributes(windowAttributes);

        if (Gravity.CENTER == center)
        {
            dialog.setCancelable(true);
        }
        else
        {
            dialog.setCancelable(false);
        }
        EditText edtLevel = dialog.findViewById(R.id.edtLevel);
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        edtLevel.setInputType(InputType.TYPE_CLASS_NUMBER);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (choice == 2)
        {
            btnYes.setText("Sửa");
            edtLevel.setText(String.valueOf(level.getNameLevel()));
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editDataList(level,Integer.parseInt(edtLevel.getText().toString()));
                    Toast.makeText(LevelManagement.this, "Sửa", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if (choice == 1)
        {
            btnYes.setText("Thêm");
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Level level = new Level(3,Integer.parseInt(edtLevel.getText().toString()));
                    addDataList(level);
                    levelAdapter.notifyDataSetChanged();
                    Toast.makeText(LevelManagement.this, "Thêm", Toast.LENGTH_SHORT).show();
                }
            });
        }
        dialog.show();
    }
    private void editDataList(Level level,int nameLevel)
    {
        int j=0;
        for (int i=0;i<list.size();i++)
        {
            if (level.getNameLevel() == list.get(i).getNameLevel())
            {
                j=i;
                break;
            }
        }
        list.get(j).setNameLevel(nameLevel);
        levelAdapter.setListLevel(list);
        levelAdapter.notifyDataSetChanged();
    }
    private void deleteDataList(Level level)
    {
        list.remove(level);
        levelAdapter.setListLevel(list);
        levelAdapter.notifyDataSetChanged();
    }
    private void addDataList(Level level)
    {
        list.add(level);
        levelAdapter.setListLevel(list);
        levelAdapter.notifyDataSetChanged();
    }
    // Xây dựng một Hộp thoại thông báo
    public void alertDialog(Level level) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Bạn có muốn xóa không?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Có",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteDataList(level);
                        Toast.makeText(LevelManagement.this, "Xóa", Toast.LENGTH_SHORT).show();
                    }
                });

        builder1.setNegativeButton(
                "Không",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    private List<Level> setData() {
        List<Level>list = new ArrayList<>();
        Level level = new Level(1,1);
        Level level1 = new Level(2,2);
        list.add(level);
        list.add(level1);
        return list;
    }
}