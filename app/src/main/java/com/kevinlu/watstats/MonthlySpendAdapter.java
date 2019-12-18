package com.kevinlu.watstats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.kevinlu.watstats.models.MonthlySpending;

import java.util.List;

public class MonthlySpendAdapter extends PagerAdapter {

    private List<MonthlySpending> monthlySpendings;
    private LayoutInflater layoutInflater;
    private Context context;

    public MonthlySpendAdapter(List<MonthlySpending> monthlySpendings, Context context) {
        this.monthlySpendings = monthlySpendings;
        this.context = context;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return monthlySpendings.size();
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
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.monthlyspend_item, container, false);

        TextView monthlySpendAmount;

        monthlySpendAmount = view.findViewById(R.id.monthly_spend_amount);

        monthlySpendAmount.setText(monthlySpendings.get(position).getAmountSpent());

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}