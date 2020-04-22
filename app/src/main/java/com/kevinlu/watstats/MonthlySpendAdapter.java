package com.kevinlu.watstats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kevinlu.watstats.data.MonthlySpending;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MonthlySpendAdapter extends RecyclerView.Adapter<MonthlySpendAdapter.MonthlySpendViewHolder> {

    private List<MonthlySpending> monthlySpendings;
    private RelativeLayout monthlySpendCard;

    MonthlySpendAdapter(List<MonthlySpending> monthlySpendings) {
        this.monthlySpendings = monthlySpendings;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getItemCount() {
        return monthlySpendings.size();
    }

    @NotNull
    public MonthlySpendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MonthlySpendViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.monthlyspend_item, parent, false
                )
        );
    }

    class MonthlySpendViewHolder extends RecyclerView.ViewHolder {
        private TextView monthlySpendMonth, monthlySpendAmount;

        MonthlySpendViewHolder(@NonNull View itemView) {
            super(itemView);
            monthlySpendCard = itemView.findViewById(R.id.monthly_spend_card);
            monthlySpendMonth = itemView.findViewById(R.id.monthly_spend_month);
            monthlySpendAmount = itemView.findViewById(R.id.monthly_spend_amount);
        }

        void setMonthlySpendData(MonthlySpending monthlySpendData) {
            monthlySpendMonth.setText(monthlySpendData.getMonth());
            monthlySpendAmount.setText(monthlySpendData.getAmountSpent());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MonthlySpendAdapter.MonthlySpendViewHolder holder, int position) {
        holder.setMonthlySpendData(monthlySpendings.get(position));
    }

    public void highlightItem() {
        monthlySpendCard.setBackgroundResource(R.drawable.monthlyspend_card_bg_active);
    }

}
