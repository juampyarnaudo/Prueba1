package com.example.juanpablo.prueba1.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.adapter.HistoryInsideListAdapter;
import com.example.juanpablo.prueba1.entity.Element;
import com.example.juanpablo.prueba1.entity.NewBuy;

import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private ListView lvElements;
    private TextView tvDate;
    private TextView tvTotal;
    private Button btnClear;
    private Button btnAccept;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        lvElements = (ListView) findViewById(R.id.lvElements);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        List<Element> elements = NewBuy.getInstance().getElements();

        lvElements.setAdapter(new HistoryInsideListAdapter(this, elements));
        lvElements.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,110 * elements.size()));

        tvDate.setText(NewBuy.getInstance().getDate());
        tvTotal.setText("Total a Pagar $" + NewBuy.getInstance().getTotal());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewBuy.setInstance(null);
                finish();
            }
        });
    }
}
