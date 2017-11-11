package com.example.juanpablo.prueba1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.entity.Element;

import java.util.List;

public class HistoryInsideListAdapter extends ArrayAdapter<Element>{

    private TextView tvStock, tvPrice, tvAmount;

    private List<Element> elements;

    public HistoryInsideListAdapter(Context context, List<Element> elements) {
        super(context, R.layout.adapter_inside_history_list, elements);
        this.elements = elements;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (null == convertView) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_inside_history_list, parent, false);
        }

        tvStock = convertView.findViewById(R.id.tvStock);
        tvPrice = convertView.findViewById(R.id.tvPrice);
        tvAmount = convertView.findViewById(R.id.tvAmount);

        tvStock.setText(elements.get(position).getStockId());
        tvPrice.setText(Double.toString(elements.get(position).getPrice()));
        tvAmount.setText(Integer.toString(elements.get(position).getAmount()));

        return convertView;
    }
}
