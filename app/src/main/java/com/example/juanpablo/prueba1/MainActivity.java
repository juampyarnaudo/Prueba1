package com.example.juanpablo.prueba1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.juanpablo.prueba1.activity.AccountActivity;
import com.example.juanpablo.prueba1.activity.StockActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private EditText etUser, etPassword;
    private Button btnCreate, btnLogin;
    private FirebaseAuth mAuth;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    private void createUser() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }
    private void loginUser(){
        mAuth.signInWithEmailAndPassword(etUser.getText().toString(),etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(getBaseContext(), "Login incorrecto", Toast.LENGTH_SHORT).show();
                        }   else {
                            Toast.makeText(getBaseContext(), "Login correcto", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getBaseContext(), StockActivity.class);
                            startActivity(intent);
                        }

                    }
                });

    }
}
