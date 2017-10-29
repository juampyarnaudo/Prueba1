package com.example.juanpablo.prueba1.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.entity.Stock;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class StockListAdapter extends ArrayAdapter<Stock> {
    private List<Stock> objects;
    private List<Stock> objectsFiltered;
    private List<String> urlImages = new ArrayList<>();
    private StockFilter filter;
    private TextView tvName;
    private TextView tvPrice;
    private TextView tvCount;
    private ImageView ivStock;
    private Context context;

    private StorageReference storageRef;
    private FirebaseStorage storage;

    public StockListAdapter(Context context, List<Stock> objects) {
        super(context, R.layout.adapter_stock_list, objects);
        this.context = context;
        this.objects = objects;
        this.objectsFiltered = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (null == convertView) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_stock_list, parent, false);
        }

        tvName = (TextView) convertView.findViewById(R.id.tvName);
        tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
        tvCount = (TextView) convertView.findViewById(R.id.tvCount);
        ivStock = (ImageView) convertView.findViewById(R.id.ivStock);

        tvName.setText(objectsFiltered.get(position).getName());
        tvPrice.setText(Double.toString(objectsFiltered.get(position).getPrice()));
        tvCount.setText(Integer.toString(objectsFiltered.get(position).getCount()));
        ivStock.setImageResource(R.drawable.perfil);

        String s = "https://firebasestorage.googleapis.com/v0/b/softnow-97a6e.appspot.com/o/stock%2FBagio%20-%20Pera.jpg?alt=media&token=80904f88-89eb-4228-93c2-f0aceeaa9139";

        Glide.with(context)
                .load(s)
                .into(ivStock);

//        storage = FirebaseStorage.getInstance();
//        storageRef = storage.getReference().child(objectsFiltered.get(position).getImage() + ".jpg");
//
//        loadImage(position);

        return convertView;
    }

    @Override
    public int getCount() {
        return objectsFiltered.size();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new StockFilter();
        }
        return filter;
    }

    private void loadImage(int position) {
        if(position < urlImages.size() || urlImages.size() == 0) {
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    urlImages.add(uri.toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(context, "Error al cargar imagen", Toast.LENGTH_SHORT).show();
                }
            });
        }

        try {
            Glide.with(context)
                    .load(urlImages.get(position))
                    .into(ivStock);
        } catch (IndexOutOfBoundsException e) {}
    }

    class StockFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String query = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            final List<Stock> originalList = objects;
            final List<Stock> filteredList = new ArrayList<>(originalList.size());

            for(int i = 0; i<originalList.size(); i++){
                String stockName = originalList.get(i).getName();
                if(stockName.toLowerCase().contains(query)) {
                    filteredList.add(originalList.get(i));
                }
            }
            result.values = filteredList;
            result.count = filteredList.size();

            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            objectsFiltered = (List<Stock>) results.values;

            notifyDataSetInvalidated();
        }
    }
}