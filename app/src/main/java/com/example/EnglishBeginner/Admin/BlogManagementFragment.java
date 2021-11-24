package com.example.EnglishBeginner.Admin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
 import android.view.ViewGroup;
 import androidx.appcompat.widget.SearchView;
import com.example.EnglishBeginner.Admin.Adapter.BlogAdapter;
import com.example.EnglishBeginner.Admin.DTO.Blog;
import com.example.EnglishBeginner.R;

import java.util.ArrayList;
import java.util.List;

public class BlogManagementFragment extends Fragment {

    View v ;
    BlogAdapter blogAdapter;
    public BlogManagementFragment() {

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_blogmanagement, container, false);
        initUI();
        return v;
    }

//
//    private void getDataFirebase() {
//        daoTopic.getDataFromRealTimeFirebase(topicAdapter);
//    }
//
    private void initUI() {
        SearchView searchView = v.findViewById(R.id.svBlog);
        RecyclerView recyclerView = v.findViewById(R.id.rcvBlog);
//        daoImageStorage = new DAOImageStorage(this);
//        daoLevel = new DAOLevel(this);
//        daoLevel.getDataFromRealTimeToList(null);
//        daoTopic = new DAOTopic(this);

//        autoCompleteTextView = findViewById(R.id.atcTopic_Level);
//        autoCompleteTextView.setAdapter(new LevelSpinnerAdapter(this,R.layout.listoptionitem,R.id.tvOptionItem,daoLevel.getLevelList()));

        List<Blog> listBlog = new ArrayList<>();
//        Blog blog = new Blog("1","12:12:12:12","nghia","",1,2,3,"new");
//        Blog blog2 = new Blog("1","12:12:12:12","nghia","",1,2,3,"new");
//        Blog blog3 = new Blog("1","12:12:12:12","nghia","",1,2,3,"new");
//            listBlog.add(blog2);
//            listBlog.add(blog3);
//            listBlog.add(blog);
         blogAdapter = new BlogAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
//        blogAdapter.setBlogList(daoTopic.getTopicList());
        blogAdapter.setBlogList(listBlog);
        recyclerView.setAdapter(blogAdapter);
//        recyclerView.scrollToPosition(topicAdapter.getItemCount() -1);
//        topicAdapter.setMyDelegationLevel(new TopicAdapter.MyDelegationLevel() {
//            @Override
//            public void editItem(Topic topic) {
//                openDialog(Gravity.CENTER,2, topic);
//            }
//
//            @Override
//            public void deleteItem(Topic topic) {
//                alertDialog(topic);
//            }
//        });
//        ImageView imgAdd = findViewById(R.id.imgAdd);
//        imgAdd.setOnClickListener(v -> openDialog(Gravity.CENTER,1,null));
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                topicAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
//            level = autoCompleteTextView.getText().toString();
//            topicAdapter.setTopicListDependOnLevel(level);
//        });
    }

//    public void openFileChoose()
//    {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, 100);
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100 && resultCode == RESULT_OK && data !=null && data.getData()!=null)
//        {
//            daoImageStorage.setmImgURL(data.getData());
//            imgTopic.setImageURI(daoImageStorage.getmImgURL());
//        }
//    }
//    private int getSelectedSpinner(Spinner spinner , String word)
//    {
//        for (int i=0; i< spinner.getCount();i++)
//        {
//            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(word))
//            {
//                return i;
//            }
//        }
//        return 0;
//    }
//    @SuppressLint("SetTextI18n")
//    public void openDialog(int center, int choice, Topic topic) {
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.addedittopic);
//
//        Window window = dialog.getWindow();
//        if (window==null)
//        {return;}
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        WindowManager.LayoutParams windowAttributes = window.getAttributes();
//        windowAttributes.gravity = center;
//        window.setAttributes(windowAttributes);
//
//        dialog.setCancelable(Gravity.CENTER == center);
//        EditText edtTopic = dialog.findViewById(R.id.edtTopic);
//        Button btnPickImageTopic = dialog.findViewById(R.id.btnPickImageTopic);
//        imgTopic = dialog.findViewById(R.id.imgaddeditTopic);
//        Spinner spnTopic = dialog.findViewById(R.id.spnTopic_Level);
//        btnPickImageTopic.setOnClickListener(v -> openFileChoose());
//        List<String> list = new ArrayList<>();
//        for (Level level : daoLevel.getLevelList())
//        {
//            list.add(String.valueOf(level.getNameLevel()));
//        }
//        spnTopic.setAdapter(new ArrayAdapter<>(this, R.layout.listoptionitem, R.id.tvOptionItem, list));
//        if (topic!=null)
//        {
//            spnTopic.setSelection(getSelectedSpinner(spnTopic,String.valueOf(topic.getLevel())));
//        }
//        Button btnYes = dialog.findViewById(R.id.btnYes);
//        Button btnNo = dialog.findViewById(R.id.btnNo);
//        btnNo.setOnClickListener(v -> dialog.dismiss());
//        if (choice == 2)
//        {
//            btnYes.setText("Sửa");
//            assert topic != null;
//            edtTopic.setText(topic.getNameTopic());
//            if (topic.getUrlImage().isEmpty()) {
//            }
//            else
//            {
//                Glide.with(getApplicationContext()).load(topic.getUrlImage()).into(imgTopic);
//            }
//            btnYes.setOnClickListener(v -> {
//                Topic topic1 = new Topic();
//                topic1.setId(topic.getId());
//                topic1.setNameTopic(edtTopic.getText().toString());
//                topic1.setLevel(Integer.parseInt(spnTopic.getSelectedItem().toString()));
//                topic1.setUrlImage(topic.getUrlImage());
//                for (Level level : daoLevel.getLevelList())
//                {
//                    if (level.getNameLevel() == topic1.getLevel())
//                    {
//                        topic1.setIdLevel(level.getId());
//                        break;
//                    }
//                }
//                if (!(topic.getNameTopic().equalsIgnoreCase(topic1.getNameTopic())&&topic.getIdLevel() == topic1.getIdLevel()))
//                {
//                    daoTopic.editDataToFireBase(topic1, edtTopic);
//                }
//                daoImageStorage.uploadFileImageTopic(choice,imgTopic,"Topic ",topic1);
//            });
//        }
//        else if (choice == 1)
//        {
//            btnYes.setText("Thêm");
//            btnYes.setOnClickListener(v -> {
//                Topic topic12 = new Topic();
//                topic12.setId(daoTopic.getTopicList().get(daoTopic.getTopicList().size()-1).getId()+1);
//                topic12.setNameTopic(edtTopic.getText().toString());
//                topic12.setLevel(Integer.parseInt(spnTopic.getSelectedItem().toString()));
//                topic12.setUrlImage("");
//                for (Level level : daoLevel.getLevelList())
//                {
//                    if (level.getNameLevel() == topic12.getLevel())
//                    {
//                        topic12.setIdLevel(level.getId());
//                        break;
//                    }
//                }
//                daoTopic.addDataToFireBase(topic12,edtTopic);
//                daoImageStorage.uploadFileImageTopic(choice,imgTopic,"Topic", topic12);
//            });
//        }
//        dialog.show();
//    }
//    // Xây dựng một Hộp thoại thông báo
//    public void alertDialog(Topic topic) {
//        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//        builder1.setMessage("Bạn có muốn xóa không?");
//        builder1.setCancelable(true);
//        builder1.setPositiveButton(
//                "Có",
//                (dialog, id) -> daoTopic.deleteDataToFire(topic));
//
//        builder1.setNegativeButton(
//                "Không",
//                (dialog, id) -> dialog.cancel());
//
//        AlertDialog alert11 = builder1.create();
//        alert11.show();
//    }
}