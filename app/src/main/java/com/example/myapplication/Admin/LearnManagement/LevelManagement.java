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

import com.example.myapplication.Admin.LearnManagement.Adapter.LevelAdapter;
import com.example.myapplication.Admin.LearnManagement.DAO.DAOQuestion;
import com.example.myapplication.Admin.LearnManagement.DTO.Level;
import com.example.myapplication.Admin.LearnManagement.DAO.DAOLevel;
import com.example.myapplication.R;

import java.util.List;

public class LevelManagement extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LevelAdapter levelAdapter;
    private List<Level> list;
    private SearchView searchView;
    private DAOLevel daoLevel;
    private ImageView imgAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_management);
        initUI();
        setDataListFromRealTimeFireBase();
    }
    private void initUI() {
        daoLevel = new DAOLevel(this);
        searchView = findViewById(R.id.svLevel);
        recyclerView = findViewById(R.id.rcvLevel);
        levelAdapter = new LevelAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(recyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        levelAdapter.setListLevel(daoLevel.getLevelList());
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
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                levelAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
    private void setDataListFromRealTimeFireBase()
    {
        daoLevel.getDataFromRealTimeToList(levelAdapter);
    }
    // tạo hộp thoại để thêm và sửa dữ liệu
    private void openDialog(int center, int choice, Level level) {
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
        EditText edtLevel = dialog.findViewById(R.id.edtLevel);
        TextView tvThemSua = dialog.findViewById(R.id.tvThemSua);
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        edtLevel.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (Gravity.CENTER == center)
        {
            dialog.setCancelable(true);
        }
        else
        {
            dialog.setCancelable(false);
        }
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (choice == 2)
        {
            btnYes.setText("Sửa");
            tvThemSua.setText("Sửa dữ liệu");
            edtLevel.setText(String.valueOf(level.getNameLevel()));
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    daoLevel.editDataToFireBase(level,edtLevel);
                }
            });
        }
        else if (choice == 1)
        {
            btnYes.setText("Thêm");
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    daoLevel.addDataToFireBase(edtLevel);
                }
            });
        }
        dialog.show();
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
                        daoLevel.deleteDataToFire(level);
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