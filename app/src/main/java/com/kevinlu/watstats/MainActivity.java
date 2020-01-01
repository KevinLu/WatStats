package com.kevinlu.watstats;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.francochen.watcard.WatCardClient;
import com.francochen.watcard.model.balance.BalanceType;
import com.francochen.watcard.model.balance.Balances;
import com.francochen.watcard.model.transaction.Transaction;
import com.francochen.watcard.model.transaction.TransactionRequest;
import com.francochen.watcard.model.transaction.Transactions;
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
    MonthlySpendAdapter monthlySpendAdapter;
    private List<AccountBalance> accountBalanceList;
    private List<MonthlySpending> monthlySpendingList;
    private WatCardClient.Builder builder = new WatCardClient.Builder();
    private WatCardClient client;
    private String user;
    private String pass;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        monthlySpendAdapter = new MonthlySpendAdapter(monthlySpendingList, this);

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
        // Currently the default is the last 3 months of spending.
        // In the future, it's possible to add a settings page to change this.
        getMonthlySpending(3);
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
                        String total = "$ " + roundUp(sum, 2);
                        setTotalBalance(total);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Watcard:getBalances()", e.toString());
                    }
                }));
    }

    /**
     * Gets the amount of money spent however many {@code monthsAgo}.
     *
     * @param monthsAgo an integer, how many months ago of spending to show.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getMonthlySpending(int monthsAgo) {
        LocalDate now = LocalDate.now();
        for (int i = 0; i < monthsAgo; i++) {
            YearMonth month = YearMonth.from(now.minusMonths(i));
            LocalDate from = month.atDay(1);
            LocalDate to = month.atEndOfMonth();
            setMonthlySpendingList(from, to);
        }
    }

    /**
     * Populates the monthlySpendingList with the amount of money in the specified time period.
     * Date period should be from the start of a month to the end of a month.
     *
     * @param from a LocalDate that sets the date from query
     * @param to   a LocalDate that sets the date to query
     */
    public void setMonthlySpendingList(LocalDate from, LocalDate to) {
        compositeDisposable.add(client.getTransactions(new TransactionRequest(from, to))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Transactions>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(Transactions transactions) {
                        float monthlySpending = 0;
                        for (Transaction transaction : transactions.get()) {
                            monthlySpending += transaction.getAmount().floatValue();
                        }
                        String totalMonthlySpending = "$ " + roundUp(monthlySpending, 2);
                        setMonthlySpending(totalMonthlySpending, from.getMonth().getDisplayName(TextStyle.FULL, Locale.CANADA));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Watcard:getMonthlySpend", e.toString());
                    }
                }));
    }

    /**
     * Adds a MonthlySpending object to the monthlySpendingList.
     *
     * @param monthlySpending a String, the amount of money spent in that month.
     * @param month           a String, the month spending occurred.
     */
    public void setMonthlySpending(String monthlySpending, String month) {
        monthlySpendingList.add(new MonthlySpending(month, monthlySpending));
        monthlySpendAdapter.notifyDataSetChanged();
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

    public float roundUp(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
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
