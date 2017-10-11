package com.example.juanpablo.prueba1;
//Importan las librerias

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.juanpablo.prueba1.activity.AccountActivity;
import com.example.juanpablo.prueba1.activity.StockActivity;
import com.example.juanpablo.prueba1.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
//Este es el HOME

    //Se declaran las variables EditText y botones
    private EditText etUser, etPassword;
    private Button btnCreate, btnLogin;
    //Se linkea con Firebase Autenticación.
    private FirebaseAuth mAuth;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        etUser = (EditText) findViewById(R.id.etUser);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
            });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
            });

    }
    //Esta es la funcion Crear Usuario, inicializa la vista AccountActivity para poder crearlo al usuario.
    private void createUser() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
//        Intent intent = new Intent(getBaseContext(), StockActivity.class);
//        startActivity(intent);
    }
    //Login de usuario.
    private void loginUser(){
        mAuth.signInWithEmailAndPassword(etUser.getText().toString(),etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        //Si el logeo es incorrecto, muestra un cuadro diciendo "Login incorrecto"
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(getBaseContext(), "Login incorrecto", Toast.LENGTH_SHORT).show();
                        }
                        //Si el logeo es Correcto, muestra un cuadro diciendo "Login Correcto" e inicializa la siguiente vista ( StockActivity )
                        else {
                            Toast.makeText(getBaseContext(), "Login correcto", Toast.LENGTH_SHORT).show();
                            launchStockActivity(task.getResult().getUser().getUid());
                        }

                    }
                });

    }
    private void launchStockActivity(String userId){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(userId);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                User.setUser(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Intent intent = new Intent(getBaseContext(), StockActivity.class);
        startActivity(intent);
        etUser.setText("");
        etPassword.setText("");
    }
}
