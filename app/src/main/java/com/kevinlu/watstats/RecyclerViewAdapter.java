package com.kevinlu.watstats;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;
import com.kevinlu.watstats.data.Transaction;

public class RecyclerViewAdapter extends SimpleCell<Transaction, RecyclerViewAdapter.ViewHolder>{

    public RecyclerViewAdapter(@NonNull Transaction item) {
        super(item);
    }

    protected int getLayoutRes() {
        return R.layout.transaction_item;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
        return new ViewHolder(cellView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Context context, Object o) {
        viewHolder.terminal.setText(getItem().getTerminal());
        viewHolder.date.setText(getItem().getDateTime());
        viewHolder.amount.setText(getItem().getAmount());
        viewHolder.balanceType.setText(getItem().getBalanceType());
        viewHolder.image.setImageResource(getItem().getImage());
    }

    public class ViewHolder extends SimpleViewHolder{
        ImageView image;
        TextView terminal;
        TextView date;
        TextView amount;
        TextView balanceType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            terminal = itemView.findViewById(R.id.terminal);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amount);
            balanceType = itemView.findViewById(R.id.balance_type);
        }
    }
}