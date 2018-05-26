package com.example.leonp.okfood.UserAccount.Account.MainActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonp.okfood.R;
import com.example.leonp.okfood.UserAccount.Account.CreateNewPost.CreatePostActivity;
import com.example.leonp.okfood.UserAccount.Account.LoginActivity;
import com.example.leonp.okfood.UserAccount.Account.Models.User;
import com.example.leonp.okfood.UserAccount.Account.Utils.UniversalImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by leonp on 4/22/2018.
 */

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    // constants
    private static final int CAMERA_REQUEST_CODE = 333;
    private static final int MEMORY_PICK_REQUEST_CODE = 444;

    // widgets
    private TextView tvCreateNewPost;
    private ImageView ivEditusername;
    private ImageView ivEditEmail;
    private CircleImageView ivProfileImage;
    private TextView tvChangePicture;
    private TextView tvUsername, tvEmail;
    private ImageView ivSettings;

    // vars
    private Context mContext;
    private Bitmap mProfileBitmap;
    private Boolean UsernameAlreadyExists;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mContext = getContext();

        tvCreateNewPost = (TextView) view.findViewById(R.id.tvCreateNewPost);
        ivEditusername = (ImageView) view.findViewById(R.id.ivEditUsername); 
        ivEditEmail = (ImageView) view.findViewById(R.id.ivEditEmail);
        ivProfileImage = (CircleImageView) view.findViewById(R.id.ivProfileImage);
        tvChangePicture = (TextView) view.findViewById(R.id.tvChangePicture);
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        ivSettings = (ImageView) view.findViewById(R.id.ivSettings);

        init();

        return view;
    }

    private void init() {
        Log.d(TAG, "init: Starting: " + tvCreateNewPost);

        getContentFromDatabase();

        setupCreateNewPostListener();
        setupEditUsernameListener();
        setupEditEmailListener();
        setupChangePictureListener();
//        setupPickFromMemoryListener();
//        setupTakePhotoListener();
        setSettingsListener();
    }


    private void setSettingsListener() {
        Log.d(TAG, "setSettingsListener: Starting");
        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Clicked settings");

                final CharSequence options[] = new CharSequence[] {"Edit Profile", "Connect Social Media", "Sign Out"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Profile Settings");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        String temp = options[which].toString();
                        switch (temp) {
                            case "Edit Profile":
                                Toast.makeText(mContext, "Attempted Edit Profile", Toast.LENGTH_SHORT).show();
                                break;
                            case "Connect Social Media":
                                Toast.makeText(mContext, "Attempted Connect Social Media", Toast.LENGTH_SHORT).show();
                                break;
                            case "Sign Out":
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                getActivity().finish();
                                getActivity().getSupportFragmentManager().popBackStack();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
    }

    private void getContentFromDatabase() {

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "getContentFromDatabase: Starting with ID: " + userID);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = reference.orderByChild("user_id").equalTo(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: Found potential user match");
                    User user = snapshot.getValue(User.class);
                    tvUsername.setText(user.getUsername());
                    //tvEmail.setText(user.getEmail());
                    tvEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    Log.d(TAG, "onDataChange: Email and username: " + user.getUsername() + ":" + user.getEmail());
                    try {
                        UniversalImageLoader.setImage(user.getProfile_image_path(), ivProfileImage);
                    } catch (NullPointerException e) {
                        Log.e(TAG, "onDataChange: NullPointerException:" + e.getMessage());
                    } catch (Exception e) {
                        Log.e(TAG, "onDataChange: Exception" + e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    private void setupPickFromMemoryListener() {
//        Log.d(TAG, "setupTakePhotoListener: Starting");
//
//        tvPickFromMemory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(intent, MEMORY_PICK_REQUEST_CODE);
//            }
//        });
//    }
//
//    private void setupTakePhotoListener() {
//        Log.d(TAG, "setupPickFromMemoryListener: Starting");
//
//        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: Trying to navigate to camera");
//                if (cameraPermissionGranted()) {
//                    Log.d(TAG, "onClick: Permissions were already granted");
//                    goToCamera();
//                } else {
//                    Log.d(TAG, "onClick: Permissions were NOT already granted\nAsking for those now");
//                    requestPermissions();
//                }
//            }
//        });
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            Log.d(TAG, "onActivityResult: Attempting to store picture taken");
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ivProfileImage.setImageBitmap(bitmap);
            mProfileBitmap = bitmap;
            uploadImage();
            ivProfileImage.setVisibility(View.VISIBLE);
//            tvPickFromMemory.setVisibility(View.GONE);
//            tvTakePhoto.setVisibility(View.GONE);
        } else if (requestCode == MEMORY_PICK_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            Log.d(TAG, "onActivityResult: Attempting to store picture from memory");
            Uri selectedImageUri = data.getData();
            ivProfileImage.setImageURI(selectedImageUri);
            try {
                mProfileBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                uploadImage();
            } catch (IOException e) {
                Log.e(TAG, "onActivityResult: IOException: " + e.getMessage());
            }

            ivProfileImage.setVisibility(View.VISIBLE);
//            tvPickFromMemory.setVisibility(View.GONE);
//            tvTakePhoto.setVisibility(View.GONE);
        }
    }

    private void uploadImage() {
        Log.d(TAG, "uploadImage: Starting");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mProfileBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();

        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("user_images/").child(userID);

        UploadTask uploadTask = storageReference.putBytes(bytes);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess: Upload successfull");
                Toast.makeText(mContext, "Photo Uploaded Successfully", Toast.LENGTH_SHORT).show();
                Uri downloadUri = taskSnapshot.getDownloadUrl();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                        .child("users");
                reference.child(userID).child("profile_image_path").setValue(downloadUri.toString());

            }
        });

    }

    private void setupChangePictureListener() {
        Log.d(TAG, "setupChangePictureListener: Starting");
        
        tvChangePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Attempting to change profile picture");

                final CharSequence options[] = new CharSequence[] {"Take New Picture", "Pick from Memory"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Change Profile Picture");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String temp = options[which].toString();
                        switch (temp) {
                            case "Take New Picture":
                                Log.d(TAG, "onClick: Clicked to take new picture");
                                if (cameraPermissionGranted()) {
                                    Log.d(TAG, "onClick: Permissions were already granted");
                                    goToCamera();
                                } else {
                                    Log.d(TAG, "onClick: Permissions were NOT already granted\nAsking for those now");
                                    requestPermissions();
                                }
                                break;
                            case "Pick from Memory":
                                Log.d(TAG, "onClick: Clicked to pick from memory");
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent, MEMORY_PICK_REQUEST_CODE);
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
        
    }
    
    private void setupCreateNewPostListener() {
        Log.d(TAG, "setupCreateNewPostListener: Starting");

        tvCreateNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CreatePostActivity.class);
                startActivity(intent);
            }
        });
    }

    private void usernameAlreadyExists(final String newUsername) {
        Log.d(TAG, "usernameAlreadyExists: Starting with: " + newUsername);

        UsernameAlreadyExists = false;

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final Query query = reference.child("users")
                .orderByChild("username")
                .equalTo(newUsername);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.exists()) {
                        Log.d(TAG, "onDataChange: Username " + snapshot.getValue(User.class).getUsername() + " already exitst");
                        Toast.makeText(mContext, "That username already exists", Toast.LENGTH_SHORT).show();
                        UsernameAlreadyExists = true;
                    } else {
                        Log.d(TAG, "onDataChange: Username didn't already exist.  Adding: " + newUsername);
                        UsernameAlreadyExists = false;

                    }
                }

                if (!UsernameAlreadyExists) {

                    Log.d(TAG, "uploadNewUsername: Username didn't already exist. Adding: " + newUsername);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    reference.child("users").child(userID).child("username").setValue(newUsername);
                    getContentFromDatabase();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    
    
    private void setupEditUsernameListener() {
        Log.d(TAG, "setupEditUsernameListener: Starting");

        ivEditusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Attempting to change username");
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                String username = tvUsername.getText().toString();
                final EditText edittext = new EditText(getActivity());
                edittext.setHint(username);
                alert.setTitle("Change Username");
                alert.setMessage("Enter Your New Username");
                

                alert.setView(edittext);

                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value
                        //Editable YouEditTextValue = edittext.getText();
                        //OR
                        String newUsername = edittext.getText().toString();
                        usernameAlreadyExists(newUsername);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();

            }
        });
    }

    private void getNewEmailAndVerify() {
        Log.d(TAG, "getNewEmailAndVerify: Starting at email: " + FirebaseAuth.getInstance().getCurrentUser().getEmail());

        AlertDialog.Builder inputEmailAlert = new AlertDialog.Builder(getActivity());
        inputEmailAlert.setTitle("Change Email");
        inputEmailAlert.setMessage("Enter New Email");
        final EditText editText = new EditText(getActivity());
        editText.setHint(tvEmail.getText().toString());
        inputEmailAlert.setView(editText);
        inputEmailAlert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // upload new email and verify
                Log.d(TAG, "onClick: clicked to save new email");
                final String newEmail = editText.getText().toString();
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.updateEmail(newEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Update Email Successful");
                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: Email verification sent successfully");
                                Log.d(TAG, "onClick: Sending email verification to: " + newEmail);

                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                getActivity().finish();
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: Failed to update email: " + e.getMessage());
                    }
                });
            }
        });
        inputEmailAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "onClick: Clicked to cancel new email input");
            }
        });

        inputEmailAlert.show();
    }
    
    private void setupEditEmailListener() {
        Log.d(TAG, "setupEditEmailListener: Starting");
        // TODO: pop up dialogue to edit email and make sure that that new email also gets verified
        
        ivEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Edit Email clicked");
                final AlertDialog.Builder notifyAlert = new AlertDialog.Builder(getActivity());
                notifyAlert.setTitle("Change Email");
                notifyAlert.setMessage("Changing Email will sign you out and require you to re-authenticate your email");

                notifyAlert.setPositiveButton("Sounds Good", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "onClick: Sounds Good clicked.");
                        getNewEmailAndVerify();
                    }
                });
                notifyAlert.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "onClick: Clicked to cancel Notify Alert");
                    }
                });

                notifyAlert.show();
            }
        });
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

        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void goToCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }
}
