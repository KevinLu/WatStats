package com.kevinlu.watstats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kevinlu.watstats.data.AccountBalance;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AccountBalanceAdapter extends RecyclerView.Adapter<AccountBalanceAdapter.AccountBalanceViewHolder> {

    private List<AccountBalance> accountBalances;

    AccountBalanceAdapter(List<AccountBalance> accountBalances) {
        this.accountBalances = accountBalances;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getItemCount() {
        return accountBalances.size();
    }

    @NotNull
    public AccountBalanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountBalanceViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.balance_item, parent, false
                )
        );
    }

    class AccountBalanceViewHolder extends RecyclerView.ViewHolder {
        private ImageView accountIcon;
        private TextView accountTitle, accountBalance, accountBalanceChange;

        AccountBalanceViewHolder(@NonNull View itemView) {
            super(itemView);
            accountIcon = itemView.findViewById(R.id.account_icon);
            accountTitle = itemView.findViewById(R.id.account_title);
            accountBalance = itemView.findViewById(R.id.account_balance);
            accountBalanceChange = itemView.findViewById(R.id.account_change);
        }

        void setAccountBalanceData(AccountBalance accountBalanceData) {
            accountIcon.setImageResource(accountBalanceData.getAccountIcon());
            accountTitle.setText(accountBalanceData.getAccountTitle());
            accountBalance.setText(accountBalanceData.getAccountBalance());
            accountBalanceChange.setText(accountBalanceData.getAccountBalanceChange());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AccountBalanceAdapter.AccountBalanceViewHolder holder, int position) {
        holder.setAccountBalanceData(accountBalances.get(position));
    }
}
