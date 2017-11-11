package com.example.juanpablo.prueba1.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.entity.Buy;
import com.example.juanpablo.prueba1.entity.Element;

import java.util.List;

public class HistoryListAdapter extends ArrayAdapter<Buy> {

    private TextView tvDate;
    private TextView tvTotal;
    private ListView lvInsideHistory;

    private List<Buy> buys;
    private Context context;

    public HistoryListAdapter(Context context, List<Buy> buys) {
        super(context, R.layout.adapter_history_list, buys);
        this.buys = buys;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_history_list, parent, false);
        }

        tvDate = convertView.findViewById(R.id.tvDate);
        tvTotal = convertView.findViewById(R.id.tvTotal);
        lvInsideHistory = convertView.findViewById(R.id.lvInsideHistory);

        tvDate.setText(buys.get(position).getDate());
        tvTotal.setText(Double.toString(buys.get(position).getTotal()));

        ListAdapter adapter = new HistoryInsideListAdapter(context, buys.get(position).getElements());
        lvInsideHistory.setAdapter(adapter);

        lvInsideHistory.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,100 * buys.get(position).getElements().size()));

        return convertView;
    }
}
