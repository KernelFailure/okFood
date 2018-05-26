package com.example.leonp.okfood.UserAccount.Account.CreateNewPost;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.leonp.okfood.R;
import com.example.leonp.okfood.UserAccount.Account.Models.Post;
import com.example.leonp.okfood.UserAccount.Account.Utils.UniversalImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ThreadLocalRandom;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by leonp on 4/22/2018.
 */

public class CreatePostActivity extends AppCompatActivity {
    private static final String TAG = "CreatePostActivity";

    // Constants
    private static final int CAMERA_REQUEST_CODE = 111;
    private static final int MEMORY_PICK_REQUEST_CODE = 222;

    // vars
    private Bitmap mPostBitmap;
    private String mImagePath;
    private String post_id;

    // widgets
    private ImageView ivFolder;
    private ImageView ivCamera;
    private FrameLayout mFrameLayout;
    private CircleImageView ivPostImage;
    private TextView tvSubmitPost;
    private EditText etPostTitle, etPostDescription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpost);

        ivFolder = (ImageView) findViewById(R.id.ivFolder);
        ivCamera = (ImageView) findViewById(R.id.ivCamera);
        mFrameLayout = (FrameLayout) findViewById(R.id.container);
        ivPostImage = (CircleImageView) findViewById(R.id.ivPostImage);
        tvSubmitPost = (TextView) findViewById(R.id.tvSubmitPost);
        etPostTitle = (EditText) findViewById(R.id.etPostTitle);
        etPostDescription = (EditText) findViewById(R.id.etPostDescription);

        init();


    }

    private void init() {
        Log.d(TAG, "init: Starting");

        setCameraIconListener();
        setFolderIconListener();
        setSubmitButtonListener();


    }

    private void setSubmitButtonListener() {
        Log.d(TAG, "setSubmitButtonListener: Starting");

        tvSubmitPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Attempting to submit post");

                if (!etPostTitle.getText().toString().equals("")
                        && !etPostDescription.getText().toString().equals("")
                        && mPostBitmap != null) {

                    post_id = FirebaseDatabase.getInstance().getReference().push().getKey();

                    Log.d(TAG, "onClick: Streaming bytes now");

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                            .child("post_images/")
                            .child(post_id);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    mPostBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] bytes = stream.toByteArray();


                    UploadTask uploadTask = storageReference.putBytes(bytes);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUri = taskSnapshot.getDownloadUrl();
                            mImagePath = downloadUri.toString();

                            // make sure all required fields aren't empty and upload content to firebase database
                            if (!etPostTitle.equals("") || !etPostDescription.equals("")) {
                                // create fork in DB for posts.  Ordered by post_ids
                                Log.d(TAG, "onClick: posting to DB now");
                                Log.d(TAG, "onSuccess: Image path is: " + mImagePath);

                                // TODO prepare for upload failures (try/catch or upload task)

                                Post post = new Post();

                                post.setTitle(etPostTitle.getText().toString());
                                post.setPost_description(etPostDescription.getText().toString());
                                post.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                post.setPost_id(post_id);
                                post.setImage_path(mImagePath);

                                int randomNum = ThreadLocalRandom.current().nextInt(4, 326 + 1);
                                post.setNumberOfComments(randomNum + " Comments");

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                reference.child("posts/")
                                        .child(post_id + "/")
                                        .setValue(post);

                                resetFields();
                                Toast.makeText(CreatePostActivity.this, "Post Created", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(CreatePostActivity.this, "Fill Out All Fields", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: Upload Failed");
                            Toast.makeText(CreatePostActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(CreatePostActivity.this, "Fill Out All Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setCameraIconListener() {
        Log.d(TAG, "setCameraIconListener: Starting");
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Trying to navigate to camera");
                if (cameraPermissionGranted()) {
                    Log.d(TAG, "onClick: Permissions were already granted");
                    goToCamera();
                } else {
                    Log.d(TAG, "onClick: Permissions were NOT already granted\nAsking for those now");
                    requestPermissions();
                }
            }
        });
    }

    private void setFolderIconListener() {
        Log.d(TAG, "setFolderIconListener: Starting");
        // Uses simplified way of picking pictures from memory

        ivFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, MEMORY_PICK_REQUEST_CODE);
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: Trying to set bitmap from resulst to post image");
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ivPostImage.setImageBitmap(bitmap);
            mPostBitmap = bitmap;

            Log.d(TAG, "onActivityResult: Bitmap To String Image Path: " + mImagePath);
        } else if (requestCode == MEMORY_PICK_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: Trying to set pic from memory to post image");
            Uri selectedImageUri = data.getData();
            ivPostImage.setImageURI(selectedImageUri);
            try {
                mPostBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                Log.e(TAG, "onActivityResult: IOException: " + e.getMessage());
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        goToCamera();
    }

    private void requestPermissions() {
        Log.d(TAG, "requestPermissions: Starting");
        requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
    }

    private boolean cameraPermissionGranted() {
        Log.d(TAG, "cameraPermissionGranted: Starting");
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
           return false;
        }
        return true;
    }

    private void goToCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    private void resetFields() {
        Log.d(TAG, "resetFields: Starting");

        etPostTitle.setText("");
        etPostDescription.setText("");
        ivPostImage.setImageResource(R.drawable.ic_upload);
    }

    private void pickFromMemoryTwo() {
        // More complex/custom way of picking from memory
        ivFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating to gallery fragment ");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                GalleryFragment fragment = new GalleryFragment();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
                getSupportFragmentManager().popBackStack();
            }
        });
    }

}
