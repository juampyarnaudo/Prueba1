package com.example.juanpablo.prueba1.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.juanpablo.prueba1.R;
import com.example.juanpablo.prueba1.activity.StockActivity;
import com.example.juanpablo.prueba1.entity.Element;
import com.example.juanpablo.prueba1.entity.NewBuy;
import com.example.juanpablo.prueba1.entity.Stock;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class NumberPickerDialog extends DialogFragment {

    private TextView tvTotal;
    private TextView tvPrice;
    private NumberPicker numberPicker;
    private ImageView ivPhoto;

    private Stock stock;
    private int stockAmount = 0 ;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_number_picker, null);

        tvPrice = view.findViewById(R.id.tvPrice);
        tvTotal = view.findViewById(R.id.tvTotal);
        numberPicker = view.findViewById(R.id.numberPicker);
        ivPhoto = view.findViewById(R.id.ivPhoto);

        numberPicker.setMaxValue(stock.getCount());
        numberPicker.setMinValue(0);
        tvPrice.setText("$" + Double.toString(stock.getPrice()));
        tvTotal.setText("Total $" + stock.getPrice() * numberPicker.getValue());
        Glide.with(getActivity())
                .load(stock.getImageUrl())
                .into(ivPhoto);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
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
                    activity.modifyStockElement(stock, stockAmount);
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
