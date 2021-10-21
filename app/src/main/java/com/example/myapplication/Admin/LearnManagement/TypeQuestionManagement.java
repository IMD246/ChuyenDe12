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
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Admin.LearnManagement.DAO.DAOTypeQuestion;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class TypeQuestionManagement extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TypeQuestionAdapter typeQuestionAdapter;
    private DAOTypeQuestion daoTypeQuestion;
    private SearchView searchView;
    private ImageView imgAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_question_management);
        initUI();
        getDataFromFireBase();
    }

    private void initUI() {
        daoTypeQuestion = new DAOTypeQuestion(this);
        searchView = findViewById(R.id.svTypeQuestion);
        recyclerView = findViewById(R.id.rcvTypeQuestion);
        typeQuestionAdapter = new TypeQuestionAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        typeQuestionAdapter.setTypeQuestionList(daoTypeQuestion.getTypeQuestionList());
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
    private void getDataFromFireBase() {
        daoTypeQuestion.getDataFromRealTimeToList(typeQuestionAdapter);
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
        EditText edtTypeQuestion = dialog.findViewById(R.id.edtLevel);
        edtTypeQuestion.setHint("TypeQuestion");
        TextView textView = dialog.findViewById(R.id.tvThemSua);
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
            textView.setText("Sửa dữ liệu");
            btnYes.setText("Sửa");
            edtTypeQuestion.setText(typeQuestion.getTypeQuestionName());
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    daoTypeQuestion.editDataToFireBase(typeQuestion,edtTypeQuestion);
                }
            });
        }
        else if (choice == 1)
        {
            btnYes.setText("Thêm");
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    daoTypeQuestion.addDataToFireBase(edtTypeQuestion);
                }
            });
        }
        dialog.show();
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
                        daoTypeQuestion.deleteDataToFire(typeQuestion);
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
}