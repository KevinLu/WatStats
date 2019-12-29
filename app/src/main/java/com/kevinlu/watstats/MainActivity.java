package com.kevinlu.watstats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.francochen.watcard.WatCardClient;
import com.francochen.watcard.model.balance.BalanceType;
import com.francochen.watcard.model.balance.Balances;
import com.kevinlu.watstats.data.AccountBalance;
import com.kevinlu.watstats.data.MonthlySpending;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;

public class MainActivity extends AppCompatActivity {

    private TextView totalBalance;
    private ViewPager accountBalances;
    private ViewPager monthlySpendings;
    private AccountBalanceAdapter accountBalanceAdapter;
    private List<AccountBalance> accountBalanceList;
    private List<MonthlySpending> monthlySpendingList;
    private WatCardClient.Builder builder = new WatCardClient.Builder();
    private WatCardClient client;
    private String user;
    private String pass;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalBalance = findViewById(R.id.total_balance);

        accountBalanceList = new ArrayList<>();

        accountBalanceAdapter = new AccountBalanceAdapter(accountBalanceList, this);

        accountBalances = findViewById(R.id.account_balances);
        accountBalances.setAdapter(accountBalanceAdapter);
        accountBalances.setPadding(dpToPx(36), dpToPx(36), dpToPx(205), 0);

        accountBalances.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position < accountBalanceList.size() - 1) {
                    return;
                }
                accountBalances.setCurrentItem(position - 1, true);
            }
        });

        monthlySpendingList = new ArrayList<>();

        MonthlySpendAdapter monthlySpendAdapter = new MonthlySpendAdapter(monthlySpendingList, this);

        monthlySpendings = findViewById(R.id.monthly_spend_pager);
        monthlySpendings.setAdapter(monthlySpendAdapter);
        monthlySpendings.setPadding(dpToPx(18), 0, dpToPx(226), 0);

        monthlySpendings.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position < monthlySpendingList.size() - 1) {
                    return;
                }
                monthlySpendings.setCurrentItem(position - 1, true);
            }
        });

        // This login info is 100% working as confirmed by login screen.
        // No need to check validity.
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getString("user");
            pass = bundle.getString("pass");
        }

        builder.account(user);
        builder.pin(pass);
        client = builder.build();

        getBalances();
    }

    public void getBalances() {
        compositeDisposable.add(client.getBalances()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Balances>() {
                    @Override
                    public void onSuccess(Balances balances) {
                        setAccountBalances(
                                balances.get(BalanceType.RESIDENCE_PLAN).getBalance().toString(),
                                balances.get(BalanceType.FLEXIBLE_2).getBalance().toString(),
                                balances.get(BalanceType.TRANSFER_MP).getBalance().toString()
                        );
                        //TODO: change float to BigDecimal
                        float sum = 0;
                        for (BalanceType balanceType : BalanceType.values()) {
                            sum += Float.valueOf(balances.get(balanceType).getBalance().toString());
                        }
                        String total = "$ " + sum;
                        setTotalBalance(total);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Watcard:getBalances()", e.toString());
                    }
                }));
    }

    public void setTotalBalance(String total) {
        totalBalance.setText(total);
    }

    public void setAccountBalances(String mealplanAmount, String flexAmount, String transferAmount) {
        accountBalanceList.add(new AccountBalance(R.drawable.ic_mealplan, "Meal\nPlan",
                mealplanAmount, "$ 0"));
        accountBalanceList.add(new AccountBalance(R.drawable.ic_flexdollar, "Flex\nDollars",
                flexAmount, "$ 0"));
        accountBalanceList.add(new AccountBalance(R.drawable.ic_transfer, "Transfer Meal Plan",
                transferAmount, "$ 0"));
        accountBalanceAdapter.notifyDataSetChanged();
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
