package com.example.juanpablo.prueba1.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.adapter.HistoryInsideListAdapter;
import com.example.juanpablo.prueba1.entity.Buy;
import com.example.juanpablo.prueba1.entity.Element;
import com.example.juanpablo.prueba1.entity.NewBuy;
import com.example.juanpablo.prueba1.entity.User;
import com.example.juanpablo.prueba1.util.LocationUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OrderActivity extends AppCompatActivity {

    public static final String ACTION_CODE = "ACTION";
    public static final String CLEAR_CODE = "CLEAR";
    public static final String BUY_CODE = "BUY";
    private ListView lvElements;
    private TextView tvDate;
    private TextView tvTotal;
    private Button btnClear;
    private Button btnAccept;
    private Button btnCancel;
    private RadioGroup radioGroup;
    private RadioButton rbLocal;
    private RadioButton rbDelivery;
    private EditText etAdress;

    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;

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
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rbLocal = (RadioButton) findViewById(R.id.rbLocal);
        rbLocal = (RadioButton) findViewById(R.id.rbLocal);
        etAdress = (EditText) findViewById(R.id.etAdress);

        mDatabase = FirebaseDatabase.getInstance();

        List<Element> elements = NewBuy.getInstance().getElements();

        lvElements.setAdapter(new HistoryInsideListAdapter(this, elements));
        lvElements.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,110 * elements.size()));

        tvDate.setText(NewBuy.getInstance().getDate());
        tvTotal.setText("Total a Pagar $" + NewBuy.getInstance().getTotal());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbLocal:
                        etAdress.setVisibility(View.GONE);
                        break;
                    case R.id.rbDelivery:
                        etAdress.setVisibility(View.VISIBLE);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndResult(CLEAR_CODE);
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ref = User.getInstance().getUserId() + NewBuy.getInstance().getDate()
                        .replace(" ", ""). replace("/","")
                        .replace(":","");
                myRef = mDatabase.getReference("buys/" + ref);
                NewBuy.getInstance().setUserId(User.getInstance().getUserId());
                myRef.setValue((Buy)NewBuy.getInstance());
                Toast.makeText(getBaseContext(), "Compra registrada exitosamente", Toast.LENGTH_LONG).show();
                finishAndResult(BUY_CODE);
            }
        });
        LocationUtil locationUtil = new LocationUtil(this);
        String location = locationUtil.getLocation();
    }

    private void finishAndResult(String action){
        NewBuy.setInstance(null);
        Intent i = getIntent();
        i.putExtra(ACTION_CODE, action);
        setResult(RESULT_OK, i);
        finish();
    }
}
