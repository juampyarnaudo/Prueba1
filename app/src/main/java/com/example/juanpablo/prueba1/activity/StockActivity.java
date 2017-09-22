package com.example.juanpablo.prueba1.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.adapter.StockListAdapter;

import java.util.ArrayList;
import java.util.List;

public class StockActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private ListView lvStock;
    private StockListAdapter stockListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        lvStock = (ListView) findViewById(R.id.lvStock);

        List<String> lista = new ArrayList<>();
        lista.add("fernet");
        lista.add("vodka");
        lista.add("gaseosas");
        lista.add("jugos");
        stockListAdapter = new StockListAdapter(this,lista);

        lvStock.setAdapter(stockListAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
