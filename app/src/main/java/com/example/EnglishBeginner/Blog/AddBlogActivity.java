package com.example.EnglishBeginner.Blog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.EnglishBeginner.DAO.DAOBlog;
import com.example.EnglishBeginner.DAO.DAOImageStorage;
import com.example.EnglishBeginner.DTO.Blog;
import com.example.EnglishBeginner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class AddBlogActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText edt_title,edt_description;
    private ImageView img_thumnail;
    private Button btn_add,btn_cancel;
    private FirebaseUser user;
    private Boolean [] booleans = new Boolean[3];
    private DAOBlog daoBlog;
    private DAOImageStorage daoImageStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_blog);
        setControl();
        setEvent();
    }
    private void setEvent() {
        img_thumnail.setOnClickListener(this);
        edt_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_description.getText().toString().trim().isEmpty())
                {
                    edt_description.setError("Hãy nhập vào gì đó");
                    edt_description.requestFocus();
                    booleans[1] = false;
                }
                else
                {
                    booleans[1] = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_title.getText().toString().trim().isEmpty())
                {
                    edt_title.setError("Hãy nhập vào gì đó");
                    edt_title.requestFocus();
                    booleans[0] = false;
                }
                else
                {
                    booleans[0] = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void setControl() {
        Arrays.fill(booleans,true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        daoImageStorage = new DAOImageStorage(this);
        daoBlog = new DAOBlog(this);
        edt_title = findViewById(R.id.edt_Title);
        edt_description = findViewById(R.id.edt_description);
        img_thumnail = findViewById(R.id.img_thumnailBlog);
        btn_cancel = findViewById(R.id.btn_cancelPostBlog);
        btn_add = findViewById(R.id.btn_PostBlog);
        btn_add.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }
    public void openFileChoose()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data !=null && data.getData()!=null)
        {
            daoImageStorage.setmImgURL(data.getData());
            img_thumnail.setImageURI(daoImageStorage.getmImgURL());
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.img_thumnailBlog: openFileChoose();
            break;
            case R.id.btn_PostBlog: addBlog();
                break;
            case R.id.btn_cancelPostBlog: this.finish();
                break;
        }
    }
    private void addBlog() {
        String title = edt_title.getText().toString().trim();
        String content = edt_description.getText().toString().trim();
        if (booleans[0] && booleans [1])
        {
            Blog blog = new Blog();
            if (daoBlog.getBlogList().size()>0)
            {
                blog.setId(daoBlog.getBlogList().get(daoBlog.getBlogList().size()-1).getId());
            }
            else
            {
                 blog.setId(1);
            }
            blog.setNameUser(user.getEmail());
            blog.setDayOfPost(getDateTime());
            blog.setIdUser(user.getUid());
            blog.setTitle(title);
            blog.setContent(content);
            daoBlog.addBlog(blog);
            daoImageStorage.uploadImageBlog(img_thumnail,"Blog",blog);
        }
    }
    private String getDateTime()
    {
        SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("HH:mm:ss a, dd/MM/yyyy");
        String currentDateTime = simpleDateFormat.format(Calendar.getInstance().getTime());
        return currentDateTime;
    }
}