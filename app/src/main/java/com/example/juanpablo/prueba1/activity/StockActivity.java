package com.example.juanpablo.prueba1.activity;
//importan las librerias
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.juanpablo.prueba1.MainActivity;
import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.adapter.StockListAdapter;
import com.example.juanpablo.prueba1.dialog.NumberPickerDialog;
import com.example.juanpablo.prueba1.entity.NewBuy;
import com.example.juanpablo.prueba1.entity.Stock;
import com.example.juanpablo.prueba1.util.LocationUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//que es extends AppCompactActivity???
public class StockActivity extends AppCompatActivity {
    public static final int BUY_CAR_CODE = 1;
    // declaran las variables
    private FloatingActionButton fab;
    private SearchView searchView;
    private ListView lvStock;
    private ProgressBar pbLoad;
    private StockListAdapter stockListAdapter;

    private List<Stock> stocks;
    private List<Integer> stocksChangedIndex = new ArrayList<>();
    private List<Integer> stocksChangedValues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        LocationUtil locationUtil = new LocationUtil(this);
        locationUtil.checkPermission();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        lvStock = (ListView) findViewById(R.id.lvStock);
        pbLoad = (ProgressBar) findViewById(R.id.pbLoad);

        setStock(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double total = NewBuy.getInstance().getTotal();
                Snackbar.make(view, "Total a pagar $" + total, Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(R.color.snackbar_action))
                        .setAction("Comprar", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getBaseContext(), OrderActivity.class);
                                startActivityForResult(intent, BUY_CAR_CODE);
                            }
                        })
                        .show();
            }
        });

        enabledButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
            setupSearchView();
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.invalidateOptionsMenu();
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
            case R.id.contact:
                Intent intent3 = new Intent(this, ContactActivity.class);
                startActivity(intent3);
                break;
            case R.id.exit:
                finish();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent4 = new Intent(this, MainActivity.class);
                startActivity(intent4);
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == BUY_CAR_CODE) {
                enabledButton();
            }
        }
    }

    private void setupSearchView() {
        if (searchView != null) {
            searchView.setQueryHint("Buscar...");
            searchView.setIconifiedByDefault(true);
            searchView.setSubmitButtonEnabled(false);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        }
    }

    private void setStock(final Context context) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("stock");
        stocks = new ArrayList<>();
        stockListAdapter = new StockListAdapter(context, stocks);
        lvStock.setAdapter(stockListAdapter);

        ref.orderByChild("price").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Stock stock = snapshot.getValue(Stock.class);
                    addStock(stock);
                }

                lvStock.setTextFilterEnabled(true);

                pbLoad.setVisibility(View.GONE);

                setupSearchView();
                setListeners();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Mapa error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setListeners() {
        lvStock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NumberPickerDialog dialog = new NumberPickerDialog();
                if(stocks.get(position).getCount() != 0) {
                    dialog.setStockItem(stocks.get(position));
                    dialog.show(getFragmentManager(),"Tag");
                } else {
                    Toast.makeText(getBaseContext(), "Stock insuficiente", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void enabledButton(){
        if(NewBuy.getInstance().getTotal() != 0){
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.INVISIBLE);
            if(stocksChangedIndex.size() > 0) {
                restoreModifiedStockElements();
            }
        }
    }

    public void modifyStockElement(Stock stock, int newCount) {
        int indexOf = stocks.indexOf(stock);
        int oldCount = stocks.get(indexOf).getCount();
        stocksChangedIndex.add(indexOf);
        stocksChangedValues.add(oldCount);
        stocks.get(indexOf).setCount(oldCount - newCount);
        stockListAdapter.notifyDataSetChanged();
    }

    private void restoreModifiedStockElements() {
        for(int i = 0; i < stocksChangedIndex.size(); i++) {
            Integer index = stocksChangedIndex.get(i);
            Integer count = stocksChangedValues.get(i);
            stocks.get(index).setCount(count);
        }
        stocksChangedIndex = new ArrayList<>();
        stocksChangedValues = new ArrayList<>();
        stockListAdapter.notifyDataSetChanged();
    }

    private void addStock(Stock stock) {
        if (!stocks.contains(stock)) {
            stocks.add(stock);
            stockListAdapter.notifyDataSetChanged();
        } else {
            int indexOf = stocks.indexOf(stock);
            stocks.get(indexOf).setCount(stock.getCount());
        }
    }
}
