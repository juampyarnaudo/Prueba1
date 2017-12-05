package com.example.juanpablo.prueba1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.entity.Stock;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class StockListAdapter extends ArrayAdapter<Stock> {
    private List<Stock> objects;
    private List<Stock> objectsFiltered;
    private StockFilter filter;
    private TextView tvName;
    private TextView tvPrice;
    private TextView tvHeader;
    private TextView tvCount;
    private ImageView ivStock;
    private LinearLayout llHeader;
    private Context context;

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
        tvHeader = (TextView) convertView.findViewById(R.id.tvHeader);
        ivStock = (ImageView) convertView.findViewById(R.id.ivStock);
        llHeader = (LinearLayout) convertView.findViewById(R.id.llHeader);

        tvName.setText(objectsFiltered.get(position).getName());
        tvPrice.setText("$" + objectsFiltered.get(position).getPrice());
        tvCount.setText("Unidades: " + objectsFiltered.get(position).getCount());
        tvHeader.setText(objectsFiltered.get(position).getCategory());
        ivStock.setImageResource(R.drawable.perfil);

        Glide.with(context)
                .load(objectsFiltered.get(position).getImageUrl())
                .apply(bitmapTransform(new RoundedCornersTransformation(30, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(ivStock);

        if(position > 0) {
            if(objectsFiltered.get(position - 1).getCategory().equals(objectsFiltered.get(position).getCategory())) {
                llHeader.setVisibility(View.GONE);
            } else {
                llHeader.setVisibility(View.VISIBLE);
            }
        } else {
            llHeader.setVisibility(View.VISIBLE);
        }

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