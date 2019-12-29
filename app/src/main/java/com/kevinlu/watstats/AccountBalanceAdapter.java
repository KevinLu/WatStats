package com.kevinlu.watstats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.kevinlu.watstats.data.AccountBalance;

import java.util.List;

public class AccountBalanceAdapter extends PagerAdapter {

    private final List<AccountBalance> accountBalances;
    private final Context context;

    AccountBalanceAdapter(List<AccountBalance> accountBalances, Context context) {
        this.accountBalances = accountBalances;
        this.context = context;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return accountBalances.size();
    }

    /**
     * Determines whether a page View is associated with a specific key object
     * as returned by {@link #instantiateItem(ViewGroup, int)}. This method is
     * required for a PagerAdapter to function properly.
     *
     * @param view   Page View to check for association with <code>object</code>
     * @param object Object to check for association with <code>view</code>
     * @return true if <code>view</code> is associated with the key object <code>object</code>
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.balance_item, container, false);

        ImageView accountIcon;
        TextView accountTitle, accountBalance, accountBalanceChange;

        accountIcon = view.findViewById(R.id.account_icon);
        accountTitle = view.findViewById(R.id.account_title);
        accountBalance = view.findViewById(R.id.account_balance);
        accountBalanceChange = view.findViewById(R.id.account_change);

        accountIcon.setImageResource(accountBalances.get(position).getAccountIcon());
        accountTitle.setText(accountBalances.get(position).getAccountTitle());
        accountBalance.setText(accountBalances.get(position).getAccountBalance());
        accountBalanceChange.setText(accountBalances.get(position).getAccountBalanceChange());

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
