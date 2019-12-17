package com.kevinlu.watstats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.List;
import com.kevinlu.watstats.models.AccountBalance;

public class MainActivity extends AppCompatActivity {

    ViewPager accountBalances;
    AccountBalanceAdapter accountBalanceAdapter;
    List<AccountBalance> accountBalanceList;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

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

        accountBalances = findViewById(R.id.accountBalances);
        accountBalances.setAdapter(accountBalanceAdapter);
        accountBalances.setPadding(dpToPx(36), dpToPx(36), dpToPx(205), 0);

        Integer[] colors_temp =
                {getResources().getColor(R.color.mealplanColor),
                getResources().getColor(R.color.flexColor),
                getResources().getColor(R.color.transferColor)};

        colors = colors_temp;

        accountBalances.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (accountBalanceAdapter.getCount() - 1)
                        && position < (colors.length - 1)) {
                    accountBalances.setBackgroundColor(
                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                } else {
                    accountBalances.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}











