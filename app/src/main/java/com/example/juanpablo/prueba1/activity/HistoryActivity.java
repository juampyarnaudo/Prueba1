package com.example.juanpablo.prueba1.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.entity.Buy;
import com.example.juanpablo.prueba1.entity.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setBuy(this);
    }

    public static void setBuy(final Context context) {
        User user = User.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query = database.getReference("buys").orderByChild("userId").equalTo(user.getUserId());
        final Map<String,Buy> buyMap = new HashMap<>();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                buyMap.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Buy buy = snapshot.getValue(Buy.class);
                    buyMap.put(snapshot.getKey(), buy);
                }
                Toast.makeText(context, buyMap.size()+"", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Mapa error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
