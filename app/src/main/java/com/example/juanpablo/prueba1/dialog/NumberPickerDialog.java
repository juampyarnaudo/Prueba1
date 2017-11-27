package com.example.juanpablo.prueba1.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.activity.StockActivity;
import com.example.juanpablo.prueba1.entity.Element;
import com.example.juanpablo.prueba1.entity.NewBuy;
import com.example.juanpablo.prueba1.entity.Stock;

public class NumberPickerDialog extends DialogFragment {

    private Stock stock;
    private int stockAmount = 0 ;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_number_picker, null);
        NumberPicker np = view.findViewById(R.id.numberPicker);
        np.setMaxValue(stock.getCount());
        np.setMinValue(0);
        TextView tvPrice = view.findViewById(R.id.tvPrice);
        tvPrice.setText("$" + Double.toString(stock.getPrice()));
        final TextView tvTotal = view.findViewById(R.id.tvTotal);
        tvTotal.setText("Total $" + stock.getPrice() * np.getValue());


        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                stockAmount = newVal;
                tvTotal.setText("Total $" + Double.toString(stock.getPrice() * stockAmount));
            }
        });

        builder.setTitle(stock.getName());
        builder.setView(view);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(stockAmount != 0){
                    NewBuy newBuy = NewBuy.getInstance();
                    Element element = new Element(stockAmount, stock.getPrice(), stock.getName());
                    newBuy.getElements().add(element);
                    double totalActual = newBuy.getTotal();
                    newBuy.setTotal(totalActual + stock.getPrice() * stockAmount);
                    StockActivity activity = (StockActivity) getActivity();
                    activity.enabledButton();
                    Toast.makeText(getContext(), "Se agregaron " + stockAmount + " " + stock.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    public void setStockItem(Stock stock) {
        this.stock = stock;
    }
}
