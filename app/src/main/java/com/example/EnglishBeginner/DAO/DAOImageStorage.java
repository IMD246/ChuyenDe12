package com.example.EnglishBeginner.DAO;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.EnglishBeginner.DTO.Answer;
import com.example.EnglishBeginner.DTO.Blog;
import com.example.EnglishBeginner.DTO.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class DAOImageStorage {
    private StorageReference storageReference;
    private Uri mImgURL;
    private Context context;

    public Uri getmImgURL() {
        return mImgURL;
    }

    public void setmImgURL(Uri mImgURL) {
        this.mImgURL = mImgURL;
    }

    public DAOImageStorage(Context context) {
        this.context = context;
        storageReference = FirebaseStorage.getInstance().getReference("images");
    }

    // hàm upload ảnh cho topic
    public void uploadImageBlog(ImageView imageView, String name, Blog blog) {
        if (mImgURL != null) {
            StorageReference fileReference = storageReference.child(name + " " + blog.getId());
            fileReference.putFile(mImgURL).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    imageView.setImageURI(null);
                    mImgURL = task.getResult();
                    blog.setUrlImage(mImgURL.toString());
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listblog");
                    databaseReference.child(blog.getId() + "/urlImage").
                            setValue(blog.getUrlImage()).addOnCompleteListener(task1 -> {
                                if (task1.isComplete()) {
                                    Toast.makeText(context, "Thêm ảnh thành công", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        }
        mImgURL = null;
    }
    public void uploadFileImageUser(String name, String uid, User user) {
        if (mImgURL != null) {
            StorageReference fileReference = storageReference.child(name + " " + uid);
            fileReference.putFile(mImgURL).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    mImgURL = task.getResult();
                    user.setImageUser(mImgURL.toString());
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                    databaseReference.child(uid + "/imageUser").
                            setValue(user.getImageUser()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()) {
                                Toast.makeText(context, "Đã cập nhật ảnh", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }
}
