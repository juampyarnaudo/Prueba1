package com.example.juanpablo.prueba1.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.adapter.HistoryListAdapter;
import com.example.juanpablo.prueba1.entity.Buy;
import com.example.juanpablo.prueba1.entity.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ListView lvHistory;
    private HistoryListAdapter historyListAdapter;

    private List<Buy> buys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setBuy(this);

        lvHistory = (ListView) findViewById(R.id.lvHistory);
    }

    public void setBuy(final Context context) {
        User user = User.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query = database.getReference("buys").orderByChild("userId").equalTo(user.getUserId());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                buys = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Buy buy = snapshot.getValue(Buy.class);
                    buys.add(buy);
                }
                historyListAdapter = new HistoryListAdapter(getBaseContext(), buys);
                lvHistory.setAdapter(historyListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Mapa error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
