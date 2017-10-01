package com.example.juanpablo.prueba1.activity;
//Se importan librerias
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountActivity extends AppCompatActivity {
// se declaran como privada las variables
    private EditText etUser, etPassword, etName, etLastName, etDni, etPhone, etAddress;
    private Button btnCreate, btnCancel, btnBrowse;

    private FirebaseAuth mAuth;
    private String TAG = "AccountActivity";

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

        mAuth = FirebaseAuth.getInstance();

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

                createUser();
            }
        });
    }
// la funcion createUser controla que los campos esten bien llenados y crea el usuario, en su defecto tirra error.
    private void createUser(){
        mAuth.createUserWithEmailAndPassword(etUser.getText().toString(),etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
                        } else{
                            //Si es correcto, ingresa a la base de datos el UserID, usuario, pass, nombre, apellido, direccion, dni, telefono, imagen.
                            Toast.makeText(getBaseContext(),"Correcto", Toast.LENGTH_LONG).show();
                            String userId = task.getResult().getUser().getUid();
                            User user = User.getInstance();
                            user.setUserId(userId);
                            user.setUser(etUser.getText().toString());
                            user.setPassword(etPassword.getText().toString());
                            user.setName(etName.getText().toString());
                            user.setLastName(etLastName.getText().toString());
                            user.setAddress(etAddress.getText().toString());
                            user.setDni(Long.parseLong(etDni.getText().toString()));
                            user.setPhone(Long.parseLong(etPhone.getText().toString()));
                            user.setImage("");

                            // en la base de datos de firebase, muestra despues de User, el registro guardado.
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users").child(userId);
                            myRef.setValue(user);

                            closeActivity();
                        }
                    }
                });
    }

    private void closeActivity() {
        this.finish();
    }
}

