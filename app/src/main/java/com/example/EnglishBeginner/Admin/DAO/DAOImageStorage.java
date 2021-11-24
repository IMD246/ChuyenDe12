package com.example.EnglishBeginner.Admin.DAO;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.EnglishBeginner.Admin.DTO.Answer;
import com.example.EnglishBeginner.Admin.DTO.Level;
import com.example.EnglishBeginner.Admin.DTO.Question;
import com.example.EnglishBeginner.Admin.DTO.Topic;
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
    public void uploadFileImageTopic(int choice,ImageView imageView, String name, Topic topic) {
        if (mImgURL != null) {
            StorageReference fileReference = storageReference.child(name+""+topic.getId());
            fileReference.putFile(mImgURL).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        if (choice == 1) {
                            imageView.setImageURI(null);
                        }
                        mImgURL = task.getResult();
                        topic.setUrlImage(mImgURL.toString());
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listtopic");
                        databaseReference.child(topic.getId() + "/urlImage").
                                setValue(topic.getUrlImage()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    Toast.makeText(context, "cập nhật ảnh thành công", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
            mImgURL = null;
        }
    }

    // hàm upload ảnh cho Level
    public void uploadFileImageLevel(int choice, ImageView imageView, String name, Level level) {
        if (mImgURL != null) {
            StorageReference fileReference = storageReference.child(name+""+level.getId());
            fileReference.putFile(mImgURL).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        if (choice == 1) {
                            imageView.setImageURI(null);
                        }
                        mImgURL = task.getResult();
                        level.setUrlImage(mImgURL.toString());
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listlevel");
                        databaseReference.child(level.getId() + "/urlImage").
                                setValue(level.getUrlImage()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    Toast.makeText(context, "Cập nhật ảnh thành công", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
            mImgURL = null;
        }
    }
    // hàm upload ảnh cho answer
    public void uploadFileImageToAnswer(int i, ImageView imgAnswer, String s, Answer answer1, int idQuestion) {
        if (mImgURL != null) {
            StorageReference fileReference = storageReference.child(s);
            fileReference.putFile(mImgURL).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (i == 1) {
                        imgAnswer.setImageURI(null);
                    }
                    mImgURL = task.getResult();
                    answer1.setUrlImage(mImgURL.toString());
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listquestion");
                    databaseReference.child(idQuestion + "/listanswer").child(answer1.getId()+"/urlImage").setValue(answer1.getUrlImage()).addOnCompleteListener(task1 -> {
                        if (task1.isComplete()) {
                            Toast.makeText(context, "Cập nhật ảnh thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            mImgURL = null;
        }
    }
    // hàm upload ảnh cho answer
    public void uploadFileImageToQuestion(String s, Question question,ImageView imgQuestion ,int choice) {
        if (mImgURL != null) {
            StorageReference fileReference = storageReference.child(s);
            fileReference.putFile(mImgURL).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    mImgURL = task.getResult();
                    if (choice == 1)
                    {
                        imgQuestion.setImageURI(null);
                    }
                    question.setUrlImage(mImgURL.toString());
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listquestion/"+question.getId()+"/urlImage");
                    databaseReference.setValue(question.getUrlImage()).addOnCompleteListener(task1 -> {
                        if (task1.isComplete()) {
                            Toast.makeText(context, "Cập nhật ảnh thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            mImgURL = null;
        }
    }
}
