package com.example.juanpablo.prueba1.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
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
import com.example.juanpablo.prueba1.entity.Stock;
import com.example.juanpablo.prueba1.entity.User;
import com.example.juanpablo.prueba1.util.LocationUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class OrderActivity extends AppCompatActivity {

    public static final String ACTION_CODE = "ACTION";
    public static final String CLEAR_CODE = "CLEAR";
    public static final String BUY_CODE = "BUY";
    public static final int NOTIFICATION_ID = 1;

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

    private LocationUtil locationUtil;

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

        List<Element> elements = NewBuy.getInstance().getElements();

        locationUtil = new LocationUtil(this);
        locationUtil.getLocation();

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
                        locationUtil.getLocation();
                        break;
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

                NewBuy.getInstance().setUserId(User.getInstance().getUserId());

                if(radioGroup.getCheckedRadioButtonId() == R.id.rbDelivery){
                    if("".equals(etAdress.getText().toString())){
                        Toast.makeText(getBaseContext(), "Agregar Direccion valida", Toast.LENGTH_LONG).show();
                    } else {
                        NewBuy.getInstance().setAddress(etAdress.getText().toString());
                        NewBuy.getInstance().setDelivery(true);
                        recordBuy();
                        launchNotification();
                        finishAndResult(BUY_CODE);
                    }

                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbLocal){
                    NewBuy.getInstance().setDelivery(false);
                    NewBuy.getInstance().setAddress("");
                    NewBuy.getInstance().setLocation("");
                    recordBuy();
                    launchNotification();
                    finishAndResult(BUY_CODE);
                }

            }
        });
    }

    private void finishAndResult(String action){
        NewBuy.setInstance(null);
        Intent i = getIntent();
        i.putExtra(ACTION_CODE, action);
        setResult(RESULT_OK, i);

        if(!CLEAR_CODE.equals(action)){
            Intent intent = new Intent(this, CongratsActivity.class);
            startActivity(intent);
        }
        finish();
    }

    private void launchNotification() {
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] pattern = new long[]{1000,500,1000};

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Compra registrada exitosamente")
                .setContentText("¡Gracias por confiar en nosotros!")
                .setSound(defaultSound)
                .setVibrate(pattern)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.beer_icon)
                .setLargeIcon((((BitmapDrawable)getResources()
                        .getDrawable(R.drawable.logo)).getBitmap()))
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0));

        Notification.InboxStyle inboxStyle = new Notification.InboxStyle(builder)
                .setBigContentTitle("Compra registrada exitosamente")
                .setSummaryText("Total de la compra $" + NewBuy.getInstance().getTotal());

        for(Element element : NewBuy.getInstance().getElements()){
            inboxStyle.addLine(element.getStockId() + " -> " + element.getAmount());
        }

        NotificationManager notifManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        notifManager.notify(NOTIFICATION_ID, inboxStyle.build());
    }

    private void recordBuy(){
        mDatabase = FirebaseDatabase.getInstance();
        String ref = User.getInstance().getUserId() + NewBuy.getInstance().getDate()
                .replace(" ", ""). replace("/","")
                .replace(":","");
        myRef = mDatabase.getReference("buys/" + ref);

        decreaseSotck();

        myRef.setValue((Buy)NewBuy.getInstance());
    }

    private void decreaseSotck() {
        for (final Element element : NewBuy.getInstance().getElements()){
            DatabaseReference stockRef = FirebaseDatabase.getInstance().getReference("stock/"+element.getStockId());
            stockRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Stock stock = dataSnapshot.getValue(Stock.class);
                    if(element.getAmount() <= stock.getCount()){
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("stock/" + stock.getName());
                        int newCOunt = stock.getCount() - element.getAmount();
                        reference.child("count").setValue(newCOunt);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
