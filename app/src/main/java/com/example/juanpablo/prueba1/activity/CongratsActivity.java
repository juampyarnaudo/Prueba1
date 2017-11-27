package com.example.juanpablo.prueba1.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.entity.Stock;

public class CongratsActivity extends AppCompatActivity {

    private Button btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congrats);

        btnBackHome = (Button) findViewById(R.id.btnBackHome);

        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
