package com.reality.escape.one_one;

/**
 * Created by krish on 2/15/2017.
 */

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class UploadPicture extends AppCompatActivity {

    private ImageView imageView;
    private Button uploadButton;
    private static final int SELECT_PICTURE = 100;
    private DatabaseReference myRef;

    // creating an instance of Firebase Storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    public String profilePicId;
    //creating a storage reference. Replace the below URL with your Firebase storage URL.
    StorageReference storageRef = storage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        profilePicId=getIntent().getStringExtra("name");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(profilePicId);



        //getting the reference of the views
        imageView = (ImageView) findViewById(R.id.imageview);
        uploadButton = (Button) findViewById(R.id.uploadbutton);
        onImageViewClick(); // for selecting an Image from gallery.
        onUploadButtonClick(); // for uploading the image to Firebase Storage.
    }

    protected  void onImageViewClick(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"),SELECT_PICTURE );
            }
        });

    }
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode==RESULT_OK){
            if(requestCode==SELECT_PICTURE){
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    Log.i("IMAGE PATH TAG", "Image Path : " + path);
                    // Set the image in ImageView
                    imageView.setImageURI(selectedImageUri);

                }
            }
        }
    }
    private String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    protected void onUploadButtonClick(){

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creating a reference to the full path of the file. myfileRef now points
                // gs://fir-demo-d7354.appspot.com/myuploadedfile.jpg
                StorageReference myfileRef = storageRef.child(profilePicId+".jpg");
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = myfileRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(UploadPicture.this, "TASK FAILED", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(UploadPicture.this, "TASK SUCCEEDED", Toast.LENGTH_SHORT).show();
                        Uri downloadUrl =taskSnapshot.getDownloadUrl();
                        String DOWNLOAD_URL = downloadUrl.getPath();
                        Log.v("DOWNLOAD URL", DOWNLOAD_URL);
                        myRef.child("profilepic").setValue(DOWNLOAD_URL);
                        Intent intent=new Intent(UploadPicture.this,MainActivity.class);
                        intent.putExtra("download_link",DOWNLOAD_URL);
                        intent.putExtra("sender",profilePicId);
                        startActivity(intent);

                    }
                });
            }
        });

    }
}