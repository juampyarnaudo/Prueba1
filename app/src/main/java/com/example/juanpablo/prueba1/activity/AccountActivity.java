package com.example.juanpablo.prueba1.activity;

//Se importan librerias
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AccountActivity extends AppCompatActivity {
// se declaran como privada las variables
    private EditText etUser, etPassword, etName, etLastName, etDni, etPhone, etAddress;
    private Button btnCreate, btnCancel, btnBrowse;
    private ImageView ivPhoto;

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private String TAG = "AccountActivity";

    private static int RESULT_LOAD_IMG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
// se declaran las variables y castean.
        btnCreate= (Button) findViewById(R.id.btnCreate);
        btnCancel= (Button) findViewById(R.id.btnCancel);
        btnBrowse= (Button) findViewById(R.id.btnBrowse);
        etUser= (EditText) findViewById(R.id.etUser);
        etPassword= (EditText) findViewById(R.id.etPassword);
        etName= (EditText) findViewById(R.id.etName);
        etLastName= (EditText) findViewById(R.id.etLastName);
        etDni= (EditText) findViewById(R.id.etDni);
        etPhone= (EditText) findViewById(R.id.etPhone);
        etAddress = (EditText) findViewById(R.id.etAddress);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });
// el boton Crear llama la funcion createUser.
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUser.getText().length()>6 && etUser.getText().toString().contains("@")
                        && etPassword.getText().length()>5){
                    if(isInputRight(etName.getText().toString()) && isInputRight(etLastName.getText().toString())) {
                        createUser();
                    } else {
                        Toast.makeText(getBaseContext(),"Nombre y/o Apellidos incompletos",Toast.LENGTH_LONG).show();
                    }
                }else   {
                    Toast.makeText(getBaseContext(),"Usuario o Contraseña incorrecto",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

            }
        });
    }
// la funcion createUser controla que los campos esten bien llenados y crea el usuario, en su defecto tirra error.

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ivPhoto.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Algo malo paso", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "No tienes seleccionada ninguna foto",Toast.LENGTH_LONG).show();
        }
    }


    private void createUser(){
        mAuth.createUserWithEmailAndPassword(etUser.getText().toString(),etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(getBaseContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            //Si es correcto, ingresa a la base de datos el UserID, usuario, pass, nombre, apellido, direccion, dni, telefono, imagen.
                            Toast.makeText(getBaseContext(), "Correcto", Toast.LENGTH_LONG).show();
                            String userId = task.getResult().getUser().getUid();
                            User user = User.getInstance();
                            user.setUserId(userId);
                            user.setUser(etUser.getText().toString());
                            user.setPassword(etPassword.getText().toString());
                            user.setName(etName.getText().toString());
                            user.setLastName(etLastName.getText().toString());
                            user.setAddress(etAddress.getText().toString());
                            try{
                                user.setDni(Long.parseLong(etDni.getText().toString()));
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                            try{
                                user.setPhone(Long.parseLong(etPhone.getText().toString()));
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                            uploadImage(userId);
                            user.setImage("users/" + userId + "/profile.jpg");

                            // en la base de datos de firebase, muestra despues de User, el registro guardado.
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users").child(userId);
                            myRef.setValue(user);

                            closeActivity();
                        }
                    }
                });
    }

    private void uploadImage(String uid){
        StorageReference storageRef = storage.getReference();
        StorageReference mountainsRef = storageRef.child("users/" + uid + "/profile.jpg");
        ivPhoto.setDrawingCacheEnabled(true);
        ivPhoto.buildDrawingCache();
        Bitmap bitmap = ivPhoto.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);

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

    private void closeActivity() {
        Intent intent = new Intent(this, StockActivity.class);
        startActivity(intent);
        this.finish();
    }

    private boolean isInputRight(String input){
        if(input != null && !"".equals(input)){
            return true;
        }
        return false;
    }
}

