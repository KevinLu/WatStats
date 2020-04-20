package com.kevinlu.watstats;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;
import com.kevinlu.watstats.data.Transaction;

import org.jetbrains.annotations.NotNull;

public class RecyclerViewAdapter extends SimpleCell<Transaction, RecyclerViewAdapter.ViewHolder>{

    RecyclerViewAdapter(@NonNull Transaction item) {
        super(item);
    }

    protected int getLayoutRes() {
        return R.layout.transaction_item;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, @NotNull View cellView) {
        return new ViewHolder(cellView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Context context, Object o) {
        viewHolder.storeLocation.setText(getItem().getLocation());
        viewHolder.terminal.setText(getItem().getTerminal());
        viewHolder.date.setText(getItem().getDateTime());
        viewHolder.amount.setText(getItem().getAmount());
        viewHolder.balanceType.setText(getItem().getBalanceType());
        ImageViewCompat.setImageTintList(viewHolder.image, ColorStateList.valueOf(ContextCompat.getColor(context, getItem().getColor())));
    }

    class ViewHolder extends SimpleViewHolder{
        ImageView image;
        TextView storeLocation;
        TextView terminal;
        TextView date;
        TextView amount;
        TextView balanceType;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.store_location_icon_bg);
            storeLocation = itemView.findViewById(R.id.store_location_icon_text);
            terminal = itemView.findViewById(R.id.terminal);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amount);
            balanceType = itemView.findViewById(R.id.balance_type);
        }
    }
}