package com.example.EnglishBeginner.Admin.LearnManagement;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.Admin.Adapter.LevelSpinnerAdapter;
import com.example.EnglishBeginner.Admin.Adapter.TopicAdapter;
import com.example.EnglishBeginner.Admin.DAO.DAOImageStorage;
import com.example.EnglishBeginner.Admin.DAO.DAOLevel;
import com.example.EnglishBeginner.Admin.DAO.DAOTopic;
import com.example.EnglishBeginner.Admin.DTO.Level;
import com.example.EnglishBeginner.Admin.DTO.Topic;
import com.example.EnglishBeginner.R;

import java.util.ArrayList;
import java.util.List;

public class TopicManagement extends AppCompatActivity {

    private TopicAdapter topicAdapter;
    private DAOTopic daoTopic;
    private DAOLevel daoLevel;
    private DAOImageStorage daoImageStorage;
    private AutoCompleteTextView autoCompleteTextView;
    private String level;
    private ImageView imgTopic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_management);
        initUI();
        getDataFirebase();
    }

    private void getDataFirebase() {
        daoTopic.getDataFromRealTimeFirebase(topicAdapter);
    }

    private void initUI() {
        daoImageStorage = new DAOImageStorage(this);
        daoLevel = new DAOLevel(this);
        daoLevel.getDataFromRealTimeToList(null);
        daoTopic = new DAOTopic(this);
        SearchView searchView = findViewById(R.id.svTopic);
        autoCompleteTextView = findViewById(R.id.atcTopic_Level);
        autoCompleteTextView.setAdapter(new LevelSpinnerAdapter(this,R.layout.listoptionitem,R.id.tvOptionItem,daoLevel.getLevelList()));
        RecyclerView recyclerView = findViewById(R.id.rcvTopic);
        topicAdapter = new TopicAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        topicAdapter.setTopicList(daoTopic.getTopicList());
        recyclerView.setAdapter(topicAdapter);
        recyclerView.scrollToPosition(topicAdapter.getItemCount() -1);
        topicAdapter.setMyDelegationLevel(new TopicAdapter.MyDelegationLevel() {
            @Override
            public void editItem(Topic topic) {
                openDialog(Gravity.CENTER,2, topic);
            }

            @Override
            public void deleteItem(Topic topic) {
                alertDialog(topic);
            }
        });
        ImageView imgAdd = findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(v -> openDialog(Gravity.CENTER,1,null));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                topicAdapter.getFilter().filter(newText);
                return false;
            }
        });
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            level = autoCompleteTextView.getText().toString();
            topicAdapter.setTopicListDependOnLevel(level);
        });
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
            imgTopic.setImageURI(daoImageStorage.getmImgURL());
        }
    }
    private int getSelectedSpinner(Spinner spinner , String word)
    {
        for (int i=0; i< spinner.getCount();i++)
        {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(word))
            {
                return i;
            }
        }
        return 0;
    }
    @SuppressLint("SetTextI18n")
    public void openDialog(int center, int choice, Topic topic) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addedittopic);

        Window window = dialog.getWindow();
        if (window==null)
        {return;}
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = center;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(Gravity.CENTER == center);
        EditText edtTopic = dialog.findViewById(R.id.edtTopic);
        Button btnPickImageTopic = dialog.findViewById(R.id.btnPickImageTopic);
        imgTopic = dialog.findViewById(R.id.imgaddeditTopic);
        Spinner spnTopic = dialog.findViewById(R.id.spnTopic_Level);
        btnPickImageTopic.setOnClickListener(v -> openFileChoose());
        List<String> list = new ArrayList<>();
        for (Level level : daoLevel.getLevelList())
        {
            list.add(String.valueOf(level.getNameLevel()));
        }
        spnTopic.setAdapter(new ArrayAdapter<>(this, R.layout.listoptionitem, R.id.tvOptionItem, list));
        if (topic!=null)
        {
            spnTopic.setSelection(getSelectedSpinner(spnTopic,String.valueOf(topic.getLevel())));
        }
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(v -> dialog.dismiss());
        if (choice == 2)
        {
            btnYes.setText("Sửa");
            assert topic != null;
            edtTopic.setText(topic.getNameTopic());
            if (topic.getUrlImage().isEmpty()) {
            }
            else
            {
                Glide.with(getApplicationContext()).load(topic.getUrlImage()).into(imgTopic);
            }
            btnYes.setOnClickListener(v -> {
                Topic topic1 = new Topic();
                topic1.setId(topic.getId());
                topic1.setNameTopic(edtTopic.getText().toString());
                topic1.setLevel(Integer.parseInt(spnTopic.getSelectedItem().toString()));
                topic1.setUrlImage(topic.getUrlImage());
                for (Level level : daoLevel.getLevelList())
                {
                    if (level.getNameLevel() == topic1.getLevel())
                    {
                        topic1.setIdLevel(level.getId());
                        break;
                    }
                }
                if (!(topic.getNameTopic().equalsIgnoreCase(topic1.getNameTopic())&&topic.getIdLevel() == topic1.getIdLevel()))
                {
                    daoTopic.editDataToFireBase(topic1, edtTopic);
                }
                daoImageStorage.uploadFileImageTopic(choice,imgTopic,"Topic ",topic1);
            });
        }
        else if (choice == 1)
        {
            btnYes.setText("Thêm");
            btnYes.setOnClickListener(v -> {
                    Topic topic12 = new Topic();
                    topic12.setId(daoTopic.getTopicList().get(daoTopic.getTopicList().size()-1).getId()+1);
                    topic12.setNameTopic(edtTopic.getText().toString());
                    topic12.setLevel(Integer.parseInt(spnTopic.getSelectedItem().toString()));
                    topic12.setUrlImage("");
                    for (Level level : daoLevel.getLevelList())
                    {
                        if (level.getNameLevel() == topic12.getLevel())
                        {
                            topic12.setIdLevel(level.getId());
                            break;
                        }
                    }
                    daoTopic.addDataToFireBase(topic12,edtTopic);
                    daoImageStorage.uploadFileImageTopic(choice,imgTopic,"Topic", topic12);
            });
        }
        dialog.show();
    }
    // Xây dựng một Hộp thoại thông báo
    public void alertDialog(Topic topic) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Bạn có muốn xóa không?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Có",
                (dialog, id) -> daoTopic.deleteDataToFire(topic));

        builder1.setNegativeButton(
                "Không",
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}