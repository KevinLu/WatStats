package com.kevinlu.watstats;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.francochen.watcard.WatCardAPI;
import com.francochen.watcard.WatCardService;
import com.francochen.watcard.model.RequestVerificationToken;
import com.francochen.watcard.model.balance.BalanceType;
import com.francochen.watcard.model.balance.Balances;
import com.kevinlu.watstats.models.AccountBalance;
import com.kevinlu.watstats.models.MonthlySpending;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private TextView totalBalance;
    private ViewPager accountBalances;
    private ViewPager monthlySpendings;
    private AccountBalanceAdapter accountBalanceAdapter;
    private MonthlySpendAdapter monthlySpendAdapter;
    private List<AccountBalance> accountBalanceList;
    private List<MonthlySpending> monthlySpendingList;
    private WatCardAPI api = new WatCardAPI();
    private WatCardService service = api.createService();
    private String user;
    private String pass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        FragmentActivity activity = getActivity();

        totalBalance = view.findViewById(R.id.total_balance);

        accountBalanceList = new ArrayList<>();

        accountBalanceAdapter = new AccountBalanceAdapter(accountBalanceList, activity);

        accountBalances = view.findViewById(R.id.account_balances);
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

        monthlySpendAdapter = new MonthlySpendAdapter(monthlySpendingList, activity);

        monthlySpendings = view.findViewById(R.id.monthly_spend_pager);
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

        Bundle bundle = activity.getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getString("user");
            pass = bundle.getString("pass");
        }

        login(user, pass);
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

                setAccountBalances(
                        response.body().get(BalanceType.RESIDENCE_PLAN).getBalance(),
                        response.body().get(BalanceType.FLEXIBLE_2).getBalance(),
                        response.body().get(BalanceType.TRANSFER_MP).getBalance()
                );

                float sum = 0;
                for (BalanceType balanceType : BalanceType.values()) {
                    sum += Float.valueOf(response.body().get(balanceType).getBalance().
                            substring(1).replace(",", ""));
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
