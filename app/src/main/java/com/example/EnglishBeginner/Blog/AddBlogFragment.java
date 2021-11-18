package com.example.EnglishBeginner.Blog;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.EnglishBeginner.R;

public class AddBlogFragment extends Fragment {

    private View view;
    private EditText edt_title,edt_description;
    private ImageView img_thumnail;
    private Button btn_add,btn_cancel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_blog, container, false);
        setControl();
        setEvent();
        return view;
    }
    private void setEvent() {
        img_thumnail.setOnClickListener(view -> openFileChoose());
    }

    private void setControl() {
        edt_title = view.findViewById(R.id.edt_Title);
        edt_description = view.findViewById(R.id.edt_description);
        img_thumnail = view.findViewById(R.id.img_thumnailBlog);
        btn_add = view.findViewById(R.id.btn_PostBlog);
        btn_cancel = view.findViewById(R.id.btn_cancelPostBlog);
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
        if (requestCode == 100 && resultCode == getActivity().RESULT_OK && data !=null && data.getData()!=null)
        {
//            daoImageStorage.setmImgURL(data.getData());
            img_thumnail.setImageURI(data.getData());
        }
    }
}