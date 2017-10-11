package com.example.juanpablo.prueba1.activity;
//importan las librerias
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.adapter.StockListAdapter;
import com.example.juanpablo.prueba1.entity.Stock;

import java.util.ArrayList;
import java.util.List;
//que es extends AppCompactActivity???
public class StockActivity extends AppCompatActivity {
// declaran las variables
    private FloatingActionButton fab;
    private SearchView svSearch;
    private ListView lvStock;
    private StockListAdapter stockListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        lvStock = (ListView) findViewById(R.id.lvStock);
        try{
            svSearch = (SearchView) findViewById(R.id.svSearch);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        List<Stock> lista = new ArrayList<>();
        Stock stock = new Stock("fernet",160,23);
        lista.add(stock);
        stock = new Stock("Smirnoff",120,21);
        lista.add(stock);
        stock = new Stock("Absolut",610,10);
        lista.add(stock);
        stock = new Stock("Quilmes",35,64);
        lista.add(stock);
        stock = new Stock("Isenbeck",29,54);
        lista.add(stock);
        stock = new Stock("Wisky",400,5);
        lista.add(stock);
        stock = new Stock("Coca Cola 3lt",60,150);
        lista.add(stock);
        stockListAdapter = new StockListAdapter(this,lista);

        lvStock.setAdapter(stockListAdapter);
        lvStock.setTextFilterEnabled(true);

        setupSearchView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.history:
                Intent intent2 = new Intent(this, HistoryActivity.class);
                startActivity(intent2);
                break;
            case R.id.logout:
                finish();
                break;
        }
        return true;
    }

    private void setupSearchView() {
        svSearch.setIconifiedByDefault(false);
        svSearch.setQueryHint("Search Here");
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                stockListAdapter.getFilter().filter(newText);
                return true;
            }
        });
        svSearch.setSubmitButtonEnabled(true);
    }
}
