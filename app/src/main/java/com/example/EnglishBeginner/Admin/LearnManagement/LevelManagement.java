package com.example.EnglishBeginner.Admin.LearnManagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.EnglishBeginner.Admin.Adapter.LevelAdapter;
import com.example.EnglishBeginner.Admin.DAO.DAOImageStorage;
import com.example.EnglishBeginner.Admin.DTO.Level;
import com.example.EnglishBeginner.Admin.DAO.DAOLevel;
import com.example.EnglishBeginner.R;
import com.squareup.picasso.Picasso;

public class LevelManagement extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LevelAdapter levelAdapter;
    private SearchView searchView;
    private DAOLevel daoLevel;
    private ImageView imgAdd,imgLevel;
    private DAOImageStorage daoImageStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_management);
        initUI();
        setDataListFromRealTimeFireBase();
    }
    private void initUI() {
        daoImageStorage = new DAOImageStorage(this);
        daoLevel = new DAOLevel(this);
        searchView = findViewById(R.id.svLevel);
        recyclerView = findViewById(R.id.rcvLevel);
        levelAdapter = new LevelAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
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
    public void openFileChoose()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data !=null && data.getData()!=null)
        {
            daoImageStorage.setmImgURL(data.getData());
            imgLevel.setImageURI(daoImageStorage.getmImgURL());
        }
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

        if (Gravity.CENTER == center)
        {
            dialog.setCancelable(true);
        }
        else
        {
            dialog.setCancelable(false);
        }
        EditText edtLevel = dialog.findViewById(R.id.edtLevel);
        edtLevel.setInputType(InputType.TYPE_CLASS_NUMBER);
        TextView tvThemSua = dialog.findViewById(R.id.tvThemSua);
        imgLevel = dialog.findViewById(R.id.imgaddeditLevel);
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        Button btnPickImage = dialog.findViewById(R.id.btnPickImageLevel);
        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoose();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (choice == 2)
        {
            if (level.getUrlImage().isEmpty())
            { }
            else
            {
                Picasso.get().load(level.getUrlImage()).resize(100, 100).into(imgLevel);
            }
            btnYes.setText("Sửa");
            tvThemSua.setText("Sửa dữ liệu");
            edtLevel.setText(String.valueOf(level.getNameLevel()));
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Level level2 = new Level();
                    level2.setId(level.getId());
                    if (edtLevel.getText().toString().isEmpty())
                    {
                        level2.setNameLevel(0);
                    }
                    else {
                        level2.setNameLevel(Integer.parseInt(edtLevel.getText().toString()));
                    }
                    level2.setUrlImage(level.getUrlImage());
                    if (level.getNameLevel() != level2.getNameLevel()) {
                        daoLevel.editDataToFireBase(level2, edtLevel);
                    }
                    daoImageStorage.uploadFileImageLevel(choice,imgLevel,"Level",level2);
                }
            });
        }
        else if (choice == 1)
        {
            btnYes.setText("Thêm");
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Level level1 = new Level();
                    int i=1;
                    if (daoLevel.getLevelList().size()>0)
                    {
                        i = daoLevel.getLevelList().get(daoLevel.getLevelList().size()-1).getId()+1;
                    }
                    level1.setId(i);
                    if (edtLevel.getText().toString().isEmpty())
                    {
                        level1.setNameLevel(0);
                    }
                    else {
                        level1.setNameLevel(Integer.parseInt(edtLevel.getText().toString()));
                    }
                    daoLevel.addDataToFireBase(level1,edtLevel);
                    daoImageStorage.uploadFileImageLevel(choice,imgLevel,"Level",level1);
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