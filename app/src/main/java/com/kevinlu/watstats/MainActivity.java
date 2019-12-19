package com.kevinlu.watstats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.francochen.watcard.WatCardAPI;
import com.francochen.watcard.WatCardService;
import com.francochen.watcard.model.RequestVerificationToken;
import com.francochen.watcard.model.balance.BalanceType;
import com.francochen.watcard.model.balance.Balances;
import com.google.android.material.tabs.TabLayout;
import com.kevinlu.watstats.models.AccountBalance;
import com.kevinlu.watstats.models.MonthlySpending;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView totalBalance;
    ViewPager accountBalances;
    ViewPager monthlySpendings;
    AccountBalanceAdapter accountBalanceAdapter;
    MonthlySpendAdapter monthlySpendAdapter;
    List<AccountBalance> accountBalanceList;
    List<MonthlySpending> monthlySpendingList;
    WatCardAPI api = new WatCardAPI();
    WatCardService service = api.createService();

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
        monthlySpendingList.add(new MonthlySpending("October", "$ 153.50"));

        monthlySpendAdapter = new MonthlySpendAdapter(monthlySpendingList, this);

        monthlySpendings = findViewById(R.id.monthly_spend_pager);
        monthlySpendings.setAdapter(monthlySpendAdapter);
        monthlySpendings.setPadding(dpToPx(18), 0, dpToPx(226), 0);

//        monthlySpendings.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        login("user", "pass");
    }

    public void login(String id, String pin) {
        service.getRequestVerificationToken().enqueue(new Callback<RequestVerificationToken>() {
            @Override
            public void onResponse(Call<RequestVerificationToken> call, Response<RequestVerificationToken> response) {
                RequestVerificationToken token = response.body();
                authenticate(token, id, pin);
            }

            @Override
            public void onFailure(Call<RequestVerificationToken> call, Throwable t) {
                Log.e("Watcard", t.toString());
            }
        });
    }

    public void authenticate(RequestVerificationToken token, String id, String pin) {
        service.authenticate(id, pin, 0, token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("Watcard:login", response.toString());
                getBalances();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Watcard:authfail", t.toString());
            }
        });
    }

    public void getBalances() {
        service.getBalances().enqueue(new Callback<Balances>() {
            @Override
            public void onResponse(Call<Balances> call, Response<Balances> response) {
                Log.d("Watcard:balances", response.toString());
                String RESIDENCE_PLAN = response.body().get(BalanceType.RESIDENCE_PLAN).getBalance();
                String SUPER_SAVER_MP = response.body().get(BalanceType.SUPER_SAVER_MP).getBalance();
                String SAVER_MP = response.body().get(BalanceType.SAVER_MP).getBalance();
                String CASUAL_MP = response.body().get(BalanceType.CASUAL_MP).getBalance();
                String FLEXIBLE_1 = response.body().get(BalanceType.FLEXIBLE_1).getBalance();
                String FLEXIBLE_2 = response.body().get(BalanceType.FLEXIBLE_2).getBalance();
                String TRANSFER_MP = response.body().get(BalanceType.TRANSFER_MP).getBalance();
                String DONS_MEAL_ALLOW = response.body().get(BalanceType.DONS_MEAL_ALLOW).getBalance();
                String DONS_FLEX = response.body().get(BalanceType.DONS_FLEX).getBalance();
                String UNALLOCATED = response.body().get(BalanceType.UNALLOCATED).getBalance();
                String DEPT_CHARGE = response.body().get(BalanceType.DEPT_CHARGE).getBalance();
                String OVERDRAFT = response.body().get(BalanceType.OVERDRAFT).getBalance();

                setAccountBalances(
                        RESIDENCE_PLAN,
                        FLEXIBLE_2,
                        TRANSFER_MP
                );

                float sum = 0;
                for (BalanceType balanceType : BalanceType.values()) {
                    sum += Float.valueOf(response.body().get(balanceType).getBalance().
                            substring(1));
                }
                String total = "$ " + sum;
                setTotalBalance(total);
            }

            @Override
            public void onFailure(Call<Balances> call, Throwable t) {
                Log.e("Watcard:balancesfail", t.toString());
            }
        });
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
}











