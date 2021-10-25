package com.example.myapplication.Admin.LearnManagement.DAO;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.Admin.LearnManagement.DTO.Topic;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    public void uploadFileImageTopic(ImageView imageView , String name, Topic topic)
    {
        if (mImgURL !=null)
        {
        StorageReference fileReference = storageReference.child(name+" "+topic.getId());
            fileReference.putFile(mImgURL).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                    {
                        imageView.setImageURI(null);
                        mImgURL =  task.getResult();
                        topic.setUrlImage(mImgURL.toString());
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listtopic");
                        databaseReference.child(String.valueOf(topic.getId()))
                                .setValue(topic).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isComplete())
                                        {
                                            Toast.makeText(context, "Thêm ảnh thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            });
        }
    }
}
