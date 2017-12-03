package com.example.juanpablo.prueba1.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.entity.Stock;

public class CongratsActivity extends AppCompatActivity {

    private Button btnBackHome;
    private TextView tv2;

    private static final String DELIVERY = "¡Tu orden esta siendo procesada! Tendras noticias nuestras muy " +
            "pronto con el tiempo aproximado de la entrega y el costo de la misma.\n ¡¡A brindar se ha dicho!!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congrats);

        btnBackHome = (Button) findViewById(R.id.btnBackHome);
        tv2 = (TextView) findViewById(R.id.tv2);

        boolean isDelivery = getIntent().getBooleanExtra("isDelivery", false);

        if(isDelivery) {
            tv2.setText(DELIVERY);
        }

        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
