package com.example.juanpablo.prueba1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMessage = (TextView) findViewById(R.id.firebaseText);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        DatabaseReference myRef2 = database.getReference("Referencia 2");
        DatabaseReference myRef3 = database.getReference("Referencia 3");
        List<String> list = new ArrayList<>();
        list.add("Juampy");
        list.add("Maxi");
        list.add("Paula");
        list.add("Berni");


        Map map = new HashMap();
        map.put("hola", list);
        map.put("hola1", "Lista");
        map.put("hola2", list);

        myRef2.setValue(list);
        myRef3.setValue(map);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d("MAIN ACTIVITY", "Value is: " + value);
                tvMessage.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("MAIN ACTIVITY", "Failed to read value.", error.toException());
            }
        });
    }
}
