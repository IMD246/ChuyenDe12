package com.example.myapplication.Admin.LearnManagement.DAO;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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
    public void uploadFile(ImageView imageView , String name, int id)
    {
        if (mImgURL !=null)
        {
        StorageReference fileReference = storageReference.child(name+" "+id);
            fileReference.putFile(mImgURL).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageView.setImageURI(null);
                    Toast.makeText(context, "Upload file successful", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
