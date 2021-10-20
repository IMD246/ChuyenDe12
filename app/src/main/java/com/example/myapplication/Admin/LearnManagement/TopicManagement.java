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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.DEFAULTVALUE;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class TopicManagement extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TopicAdapter topicAdapter;
    private List<Topic> list;
    private List<Level> listLevel;
    private AutoCompleteTextView autoCompleteTextView;
    private String level;
    private SearchView searchView;
    private ImageView imgAdd;
    private int index;
    private Level level1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_management);
        searchView = findViewById(R.id.svTopic);
        list = setData();
        listLevel = setDataListLevel();
        autoCompleteTextView = findViewById(R.id.atcTopic_Level);
        autoCompleteTextView.setAdapter(new LevelSpinnerAdapter(this,R.layout.listoptionitem,R.id.tvOptionItem,listLevel));
        recyclerView = findViewById(R.id.rcvTopic);
        topicAdapter = new TopicAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(recyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        topicAdapter.setTopicList(list);
        recyclerView.setAdapter(topicAdapter);
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
                topicAdapter.getFilter().filter(newText);
                return false;
            }
        });
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                level = autoCompleteTextView.getText().toString();
                topicAdapter.setTopicListDependOnLevel(level);
            }
        });
    }
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

        if (Gravity.CENTER == center)
        {
            dialog.setCancelable(true);
        }
        else
        {
            dialog.setCancelable(false);
        }
        EditText edtTopic = dialog.findViewById(R.id.edtTopic);
        final String st;
        ImageView imgTopic = dialog.findViewById(R.id.imgaddeditTopic);
        AutoCompleteTextView autoCompleteTextView1 = dialog.findViewById(R.id.atcaddeditTopic_Level);
        autoCompleteTextView1.setAdapter(new LevelSpinnerAdapter(this,R.layout.listoptionitem,R.id.tvOptionItem,listLevel));
        if (topic!=null)
        {
            for (int i=0; i<list.size();i++)
            {
                if (topic.getId() == list.get(i).getId())
                {
                    index = i;
                    break;
                }
            }
        }
        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (autoCompleteTextView1.getText().toString()!=null)
                {
                    for (Level level2 : listLevel)
                    {
                        if (level2.getNameLevel() == Integer.parseInt(autoCompleteTextView1.getText().toString().trim()))
                        {
                            level1 = level2;
                            break;
                        }
                    }
                }
            }
        });
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
            edtTopic.setText(topic.getNameTopic());
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editDataList(topic,edtTopic.getText().toString());
                    Toast.makeText(TopicManagement.this, "Sửa", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if (choice == 1)
        {
            btnYes.setText("Thêm");
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (level1==null)
                    {
                        level1 = new Level(1,1);
                    }
                    Topic topic = new Topic(3,level1.getId(),level1.getNameLevel(),edtTopic.getText().toString(),"");
                    addDataList(topic);
                    Toast.makeText(TopicManagement.this, "Thêm", Toast.LENGTH_SHORT).show();
                }
            });
        }
        dialog.show();
    }
    private void editDataList(Topic topic,String nameTopic)
    {
        int j=0;
        for (int i=0;i<list.size();i++)
        {
            if (topic.getNameTopic() == list.get(i).getNameTopic())
            {
                j=i;
                break;
            }
        }
        list.get(j).setNameTopic(nameTopic);
        topicAdapter.setTopicList(list);
    }
    private void deleteDataList(Topic topic)
    {
        list.remove(topic);
        topicAdapter.setTopicList(list);
    }
    private void addDataList(Topic topic)
    {
        list.add(topic);
        topicAdapter.setTopicList(list);
    }
    // Xây dựng một Hộp thoại thông báo
    public void alertDialog(Topic topic) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Bạn có muốn xóa không?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Có",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteDataList(topic);
                        Toast.makeText(TopicManagement.this, "Xóa", Toast.LENGTH_SHORT).show();
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
    private List<Topic> setData() {
        List<Topic>list = new ArrayList<>();
        Topic topic = new Topic(1,1,1,"Cơ bản","");
        Topic topic1 = new Topic(2,1,1,"Cơ bản 1","");
        list.add(topic);
        list.add(topic1);
        return list;
    }
    private List<Level>setDataListLevel()
    {
        List<Level>list = new ArrayList<>();
        Level level = new Level(1,1);
        Level level1 = new Level(2,2);
        list.add(level);
        list.add(level1);
        return list;
    }
}