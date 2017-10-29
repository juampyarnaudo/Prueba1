package com.example.juanpablo.prueba1.activity;
//importan las librerias
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.adapter.StockListAdapter;
import com.example.juanpablo.prueba1.entity.Stock;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//que es extends AppCompactActivity???
public class StockActivity extends AppCompatActivity {
// declaran las variables
    private FloatingActionButton fab;
    private SearchView svSearch;
    private ListView lvStock;
    private ProgressBar pbLoad;
    private StockListAdapter stockListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        lvStock = (ListView) findViewById(R.id.lvStock);
        svSearch = (SearchView) findViewById(R.id.svSearch);
        pbLoad = (ProgressBar) findViewById(R.id.pbLoad);

        setStock(this);

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

    private void setStock(final Context context) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("stock");
        final List<Stock> stocks = new ArrayList<>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                stocks.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Stock stock = snapshot.getValue(Stock.class);
                    stocks.add(stock);
                }
                stockListAdapter = new StockListAdapter(context, stocks);

                lvStock.setAdapter(stockListAdapter);
                lvStock.setTextFilterEnabled(true);

                pbLoad.setVisibility(View.GONE);

                setupSearchView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Mapa error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
