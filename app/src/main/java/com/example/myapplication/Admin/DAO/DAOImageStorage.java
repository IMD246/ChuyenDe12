package com.example.myapplication.Admin.DAO;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.Admin.DTO.Answer;
import com.example.myapplication.Admin.DTO.Level;
import com.example.myapplication.Admin.DTO.Question;
import com.example.myapplication.Admin.DTO.Topic;
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
                        databaseReference.child(topic.getId()+"/urlImage").
                                setValue(topic.getUrlImage()).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listlevel");
                        databaseReference.child(String.valueOf(topic.getIdLevel()) + "/listtopic").child(topic.getId()+"/urlImage").
                                setValue(topic.getUrlImage()).
                                addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Thêm Ảnh cho Topic của Level thành công", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }
    // hàm upload ảnh cho Level
    public void uploadFileImageLevel(int choice,ImageView imageView , String name, Level level)
    {
        if (mImgURL !=null)
        {
            StorageReference fileReference = storageReference.child(name+" "+level.getId());
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
                        if (choice == 1) {
                            imageView.setImageURI(null);
                        }
                        mImgURL =  task.getResult();
                        level.setUrlImage(mImgURL.toString());
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listlevel");
                        databaseReference.child(level.getId()+"/urlImage").
                                setValue(level.getUrlImage()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete())
                                {
                                    Toast.makeText(context, "thêm ảnh thành công", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }
    }
    // hàm upload ảnh cho answer
    public void uploadFileImageToAnswer(int i, ImageView imgAnswer, String s, Answer answer1, int idQuestion, Question question) {
        if (mImgURL !=null)
        {
            StorageReference fileReference = storageReference.child(s);
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
                        if (i == 1) {
                            imgAnswer.setImageURI(null);
                        }
                        mImgURL =  task.getResult();
                        answer1.setUrlImage(mImgURL.toString());
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listquestion");
                        databaseReference.child(idQuestion+"/listanswer").child(String.valueOf(answer1.getId())).setValue(answer1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    Toast.makeText(context, "Thêm ảnh thành công", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(context, "Thêm ảnh không thành công", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }
        if (mImgURL !=null)
        {
            StorageReference fileReference = storageReference.child(s);
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
                        mImgURL = task.getResult();
                        answer1.setUrlImage(mImgURL.toString());
                        DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("listtopic/"+question.getIdTopic()+"/listquestion");
                        databaseReference.child(idQuestion + "/listanswer").child(String.valueOf(answer1.getId())).setValue(answer1).
                                addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
//                                    Toast.makeText(context, "Thêm Ảnh Answer cho Question trong Topic thành công", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}
