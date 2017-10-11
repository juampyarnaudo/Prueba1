package com.example.juanpablo.prueba1.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.entity.Buy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }

    public static void getBuy(final Context context) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference ref = database.getReference("buys/vLtvmiN1UZgSdnw2J0DUc7skor72201710090702");
        DatabaseReference ref = database.getReference("buys");
        final Map<String,Buy> buyMap = new HashMap<>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Buy buy = snapshot.getValue(Buy.class);
                    buyMap.put(snapshot.getKey(), buy);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Mapa error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
