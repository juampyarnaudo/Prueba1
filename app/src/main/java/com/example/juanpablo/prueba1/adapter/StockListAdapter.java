package com.example.juanpablo.prueba1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.entity.Stock;

import java.util.ArrayList;
import java.util.List;

public class StockListAdapter extends ArrayAdapter<Stock> {
    private List<Stock> objects;
    private List<Stock> objectsFiltered;
    private StockFilter filter;
    private TextView tvName;
    private TextView tvPrice;
    private TextView tvCount;
    private ImageView ivStock;

    public StockListAdapter(Context context, List<Stock> objects) {
        super(context, R.layout.adapter_stock_list, objects);
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