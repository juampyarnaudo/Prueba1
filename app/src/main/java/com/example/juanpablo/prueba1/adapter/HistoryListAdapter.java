package com.example.juanpablo.prueba1.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.entity.Buy;
import com.example.juanpablo.prueba1.entity.Element;

import java.util.List;

public class HistoryListAdapter extends ArrayAdapter<Buy> {

    private TextView tvDate;
    private TextView tvTotal;
    private TextView tvDelivery;
    private LinearLayout llElements;
    private LinearLayout llHeader;

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
        tvDelivery = convertView.findViewById(R.id.tvDelivery);
        llElements = convertView.findViewById(R.id.llElements);
        llHeader = convertView.findViewById(R.id.llHeader);

        if (buys.get(position).isClosed()){
            llHeader.setBackgroundResource(R.color.colorPrimary);
        }

        if(buys.get(position).isDelivery()) {
            tvDelivery.setText("Delivery");
        } else {
            tvDelivery.setText("Reserva");
        }

        tvDate.setText(buys.get(position).getDate());
        tvTotal.setText("$" + buys.get(position).getTotal());

        setLlElements(llElements, buys.get(position).getElements());

        return convertView;
    }

    private void setLlElements(LinearLayout layout, List<Element> llElements) {
        TextView tvStock;
        TextView tvPrice;
        TextView tvAmount;

        layout.removeAllViews();

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(Element element : llElements) {
            View view = inflater.inflate(R.layout.adapter_inside_history_list, null);
            tvStock = view.findViewById(R.id.tvStock);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvAmount = view.findViewById(R.id.tvAmount);

            tvStock.setText(element.getStockId());
            tvPrice.setText("$" + element.getPrice());
            tvAmount.setText(Integer.toString(element.getAmount()));

            layout.addView(view);
        }
    }
}
