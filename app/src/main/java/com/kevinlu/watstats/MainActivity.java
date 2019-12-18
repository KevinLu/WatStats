package com.kevinlu.watstats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.List;
import com.kevinlu.watstats.models.AccountBalance;
import com.kevinlu.watstats.models.MonthlySpending;

public class MainActivity extends AppCompatActivity {

    ViewPager accountBalances;
    ViewPager monthlySpendings;
    AccountBalanceAdapter accountBalanceAdapter;
    MonthlySpendAdapter monthlySpendAdapter;
    List<AccountBalance> accountBalanceList;
    List<MonthlySpending> monthlySpendingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accountBalanceList = new ArrayList<>();
        accountBalanceList.add(new AccountBalance(R.drawable.ic_mealplan, "Meal\nPlan",
                "$ 823.49", "$ 395.21"));
        accountBalanceList.add(new AccountBalance(R.drawable.ic_flexdollar, "Flex\nDollars",
                "$ 823.49", "$ 395.21"));
        accountBalanceList.add(new AccountBalance(R.drawable.ic_transfer, "Transfer Meal Plan",
                "$ 823.49", "$ 395.21"));

        accountBalanceAdapter = new AccountBalanceAdapter(accountBalanceList, this);

        accountBalances = findViewById(R.id.account_balances);
        accountBalances.setAdapter(accountBalanceAdapter);
        accountBalances.setPadding(dpToPx(36), dpToPx(36), dpToPx(205), 0);

        accountBalances.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position < accountBalanceList.size() - 1) {
                    return;
                }
                accountBalances.setCurrentItem(position - 1, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        monthlySpendingList = new ArrayList<>();
        monthlySpendingList.add(new MonthlySpending("December", "$ 254.98"));
        monthlySpendingList.add(new MonthlySpending("November", "$ 392.11"));
        monthlySpendingList.add(new MonthlySpending("September", "$ 153.50"));

        monthlySpendAdapter = new MonthlySpendAdapter(monthlySpendingList, this);

        monthlySpendings = findViewById(R.id.monthly_spend_pager);
        monthlySpendings.setAdapter(monthlySpendAdapter);
        monthlySpendings.setPadding(dpToPx(36), dpToPx(36), dpToPx(205), 0);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}











