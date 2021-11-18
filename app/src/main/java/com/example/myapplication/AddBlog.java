package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddBlog extends AppCompatActivity {
    EditText edt_title,edt_description;
    ImageView img_thumnail;
    Button btn_add,btn_cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_blog);
        setControl();
        setEvent();
    }

    private void setEvent() {
        img_thumnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChoose();
            }
        });
    }

    private void setControl() {
        edt_title = findViewById(R.id.edt_Title);
        edt_description = findViewById(R.id.edt_description);
        img_thumnail = findViewById(R.id.img_thumnailBlog);
        btn_add = findViewById(R.id.btn_PostBlog);
        btn_cancel = findViewById(R.id.btn_cancelPostBlog);

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
//            daoImageStorage.setmImgURL(data.getData());
            img_thumnail.setImageURI(data.getData());

        }
    }

}
