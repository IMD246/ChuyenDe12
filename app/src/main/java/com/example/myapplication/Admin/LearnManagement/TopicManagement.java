package com.example.myapplication.Admin.LearnManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class TopicManagement extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TopicAdapter topicAdapter;
    private List<Topic> list;
    private List<Level> listLevel;
    private AutoCompleteTextView autoCompleteTextView;
    private String level;
    private Uri mImgURL;
    private SearchView searchView;
    private ImageView imgAdd;
    private static final int PICK_IMAGE_REQUEST = 1;
    private int index;
    private ImageView imgTopic;
    private Level level1;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_management);
        storageReference = FirebaseStorage.getInstance().getReference("images");
        databaseReference = FirebaseDatabase.getInstance().getReference("images");
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
    private void uploadFile(String topicName)
    {
        if (mImgURL!=null)
        {
            StorageReference fileReference = storageReference.child(topicName);
            fileReference.putFile(mImgURL).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgTopic.setImageURI(null);
                    Toast.makeText(TopicManagement.this, "Upload file successful", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            Toast.makeText(this, "Không có file nào được chọn", Toast.LENGTH_SHORT).show();
        }
    }
    private void openFileChoose()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData()!=null)
        {
            mImgURL = data.getData();
            Picasso.get().load(mImgURL).into(imgTopic);
            imgTopic.setImageURI(mImgURL);
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
        Button btnPickImageTopic = dialog.findViewById(R.id.btnPickImageTopic);
        imgTopic = dialog.findViewById(R.id.imgaddeditTopic);
        Spinner spnTopic = dialog.findViewById(R.id.spnTopic_Level);
        btnPickImageTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoose();
            }
        });
        List<String> list = new ArrayList<>();
        for (Level level : listLevel)
        {
            list.add(String.valueOf(level.getNameLevel()));
        }
        spnTopic.setAdapter(new ArrayAdapter<String>(this,R.layout.listoptionitem,R.id.tvOptionItem,list));
        if (topic!=null)
        {
            spnTopic.setSelection(getSelectedSpinner(spnTopic,String.valueOf(topic.getLevel())));
        }
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
            if (topic.getUrlImageTopic().length() > 0) {
                Picasso.get().load(topic.getUrlImageTopic()).into(imgTopic);
            }
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editDataList(topic,edtTopic.getText().toString());
                    uploadFile("Topic " + topic.getId());
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
                    if (mImgURL != null) {
                        Topic topic = new Topic(3, level1.getId(), level1.getNameLevel(), edtTopic.getText().toString(),mImgURL.toString());
                        uploadFile("Topic " + topic.getId());
                        addDataList(topic);
                    }
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
        list.get(j).setUrlImageTopic(mImgURL.toString());
        topicAdapter.setTopicList(list);
    }
    private void deleteDataList(Topic topic)
    {
        list.remove(topic);
        topicAdapter.setTopicList(list);
    }
    private void addDataList(Topic topic)
    {
        Toast.makeText(this, ""+mImgURL.toString(), Toast.LENGTH_SHORT).show();
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
        Topic topic1 = new Topic(2,2,2,"Cơ bản 1","");
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