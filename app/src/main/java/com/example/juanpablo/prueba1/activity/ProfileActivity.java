package com.example.juanpablo.prueba1.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUser;
    private EditText etPassword;
    private EditText etName;
    private EditText etLastName;
    private EditText etDni;
    private EditText etPhone;
    private EditText etAddress;
    private ImageView ivPhoto;
    private Button btnBrowse;
    private Button btnUpdate;
    private Button btnCancel;
    private Switch stUpdatePass;

    private User user;

    private StorageReference storageRef;
    private FirebaseStorage storage;
    private DatabaseReference mDatabase;
    private FirebaseUser fireUser;

    private static int RESULT_LOAD_IMG = 1;
    private static String TAG = "ProfileActivity";

    private boolean isPhotoChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        tvUser= (TextView) findViewById(R.id.etUser);
        etPassword= (EditText) findViewById(R.id.etPassword);
        etName= (EditText) findViewById(R.id.etName);
        etLastName= (EditText) findViewById(R.id.etLastName);
        etDni= (EditText) findViewById(R.id.etDni);
        etPhone= (EditText) findViewById(R.id.etPhone);
        etAddress= (EditText) findViewById(R.id.etAddress);
        ivPhoto= (ImageView) findViewById(R.id.ivProfile);
        btnBrowse= (Button) findViewById(R.id.btnBrowse);
        btnUpdate= (Button) findViewById(R.id.btnUpdate);
        btnCancel= (Button) findViewById(R.id.btnCancel);
        stUpdatePass= (Switch) findViewById(R.id.stUpdatePass);

        user = User.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child(user.getImage());
        mDatabase = FirebaseDatabase.getInstance().getReference();

        tvUser.setText(user.getUser());
        etName.setText(user.getName());
        etLastName.setText(user.getLastName());
        etDni.setText(Long.toString(user.getDni()));
        etPhone.setText(Long.toString(user.getPhone()));
        etAddress.setText(user.getAddress());

        loadImage();

        setButtonAction();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ivPhoto.setImageBitmap(cropBitmap(selectedImage));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Algo malo paso", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "No tienes seleccionada ninguna foto",Toast.LENGTH_LONG).show();
        }
    }

    private void loadImage() {
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getBaseContext())
                        .load(uri.toString())
                        .into(ivPhoto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getBaseContext(), "Error al cargar imagen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setButtonAction() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPhotoChanged = true;
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Map<String, Object> map = new HashMap<>();
                    map.put("users/" + user.getUserId() + "/name", etName.getText().toString());
                    map.put("users/" + user.getUserId() + "/lastName", etLastName.getText().toString());
                    map.put("users/" + user.getUserId() + "/dni", Long.parseLong(etDni.getText().toString()));
                    map.put("users/" + user.getUserId() + "/phone", Long.parseLong(etPhone.getText().toString()));
                    map.put("users/" + user.getUserId() + "/address", etAddress.getText().toString());
                    if (stUpdatePass.isChecked() && !"".equals(etPassword.getText())) {
                        map.put("users/" + user.getUserId() + "/password", etPassword.getText().toString());
                        fireUser = FirebaseAuth.getInstance().getCurrentUser();
                        fireUser.updatePassword(etPassword.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User password updated.");
                                        } else {
                                            Toast.makeText(getBaseContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                    if (isPhotoChanged) {
                        uploadImage(user.getUserId());
                    }

                    mDatabase.updateChildren(map);
                    Toast.makeText(getBaseContext(), "Actualizacion Exitosa", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void uploadImage(String uid){
//        StorageReference mountainsRef = storageRef;
        ivPhoto.setDrawingCacheEnabled(true);
        ivPhoto.buildDrawingCache();
        Bitmap bitmap = ivPhoto.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }

    private Bitmap cropBitmap(Bitmap srcBmp){
        Bitmap dstBmp;
        if (srcBmp.getWidth() >= srcBmp.getHeight()){

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    srcBmp.getWidth()/2 - srcBmp.getHeight()/2,
                    0,
                    srcBmp.getHeight(),
                    srcBmp.getHeight()
            );

        }else{

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    0,
                    srcBmp.getHeight()/2 - srcBmp.getWidth()/2,
                    srcBmp.getWidth(),
                    srcBmp.getWidth()
            );
        }
        return dstBmp;
    }
}
