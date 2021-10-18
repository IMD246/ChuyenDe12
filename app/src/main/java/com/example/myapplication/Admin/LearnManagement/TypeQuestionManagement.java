package com.example.myapplication.Admin.LearnManagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

public class TypeQuestionManagement extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TypeQuestionAdapter typeQuestionAdapter;
    private List<TypeQuestion> list;
    SearchView searchView;
    private ImageView imgAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_question_management);
        searchView = findViewById(R.id.svTypeQuestion);
        list = setData();
        recyclerView = findViewById(R.id.rcvTypeQuestion);
        typeQuestionAdapter = new TypeQuestionAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(recyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        typeQuestionAdapter.setTypeQuestionList(list);
        recyclerView.setAdapter(typeQuestionAdapter);
        typeQuestionAdapter.setMyDelegationLevel(new TypeQuestionAdapter.MyDelegationLevel() {
            @Override
            public void editItem(TypeQuestion typeQuestion) {
                openDialog(Gravity.CENTER,2, typeQuestion);
            }

            @Override
            public void deleteItem(TypeQuestion typeQuestion) {
                alertDialog(typeQuestion);
            }
        });
        imgAdd = findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(Gravity.CENTER,1,null);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                typeQuestionAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
    private void openDialog(int center, int choice, TypeQuestion typeQuestion) {
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
        edtLevel.setHint("TypeQuestion");
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (choice == 2)
        {
            btnYes.setText("Sửa");
            edtLevel.setText(typeQuestion.getTypeQuestionName());
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editDataList(typeQuestion,edtLevel.getText().toString());
                    Toast.makeText(TypeQuestionManagement.this, "Sửa", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if (choice == 1)
        {
            btnYes.setText("Thêm");
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TypeQuestion typeQuestion1 = new TypeQuestion(3,edtLevel.getText().toString());
                    addDataList(typeQuestion1);
                    Toast.makeText(TypeQuestionManagement.this, "Thêm", Toast.LENGTH_SHORT).show();
                }
            });
        }
        dialog.show();
    }
    private void editDataList(TypeQuestion typeQuestion,String nameLevel)
    {
        int j=0;
        for (int i=0;i<list.size();i++)
        {
            if (typeQuestion.getTypeQuestionName() == list.get(i).getTypeQuestionName())
            {
                j=i;
                break;
            }
        }
        list.get(j).setTypeQuestionName(nameLevel);
        typeQuestionAdapter.setTypeQuestionList(list);
        typeQuestionAdapter.notifyDataSetChanged();
    }
    private void deleteDataList(TypeQuestion typeQuestion)
    {
        list.remove(typeQuestion);
        typeQuestionAdapter.setTypeQuestionList(list);
        typeQuestionAdapter.notifyDataSetChanged();
    }
    private void addDataList(TypeQuestion typeQuestion)
    {
        list.add(typeQuestion);
        typeQuestionAdapter.setTypeQuestionList(list);
        typeQuestionAdapter.notifyDataSetChanged();
    }
    // Xây dựng một Hộp thoại thông báo
    public void alertDialog(TypeQuestion typeQuestion) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Bạn có muốn xóa không?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Có",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteDataList(typeQuestion);
                        Toast.makeText(TypeQuestionManagement.this, "Xóa", Toast.LENGTH_SHORT).show();
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
    private List<TypeQuestion> setData() {
        List<TypeQuestion>list = new ArrayList<>();
        TypeQuestion typeQuestion1 = new TypeQuestion(1,"Viết");
        TypeQuestion typeQuestion2 = new TypeQuestion(2,"Đọc");
        list.add(typeQuestion1);
        list.add(typeQuestion2);
        return list;
    }
}