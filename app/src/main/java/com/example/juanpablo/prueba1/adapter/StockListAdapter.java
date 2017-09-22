package com.example.juanpablo.prueba1.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.juanpablo.prueba1.R;

import java.util.List;

public class StockListAdapter extends ArrayAdapter<String> {
    private List<String> objects;
    private TextView tvName;
    private TextView tvPosition;

    public StockListAdapter(Context context, List<String> objects) {
        super(context, R.layout.adapter_stock_list, objects);
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position,View convertView, ViewGroup parent) {

        if (null == convertView) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_stock_list, parent, false);
        }

        tvName = (TextView) convertView.findViewById(R.id.tvName);
        tvPosition= (TextView) convertView.findViewById(R.id.tvPosition);

        tvName.setText(objects.get(position));
        tvPosition.setText(Integer.toString(position));

        return convertView;
    }
}
