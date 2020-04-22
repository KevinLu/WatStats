package com.kevinlu.watstats;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.francochen.watcard.WatCardClient;
import com.francochen.watcard.model.balance.BalanceType;
import com.francochen.watcard.model.balance.Balances;
import com.francochen.watcard.model.transaction.Transaction;
import com.francochen.watcard.model.transaction.TransactionRequest;
import com.francochen.watcard.model.transaction.Transactions;
import com.jaychang.srv.SimpleRecyclerView;
import com.jaychang.srv.decoration.SectionHeaderProvider;
import com.jaychang.srv.decoration.SimpleSectionHeaderProvider;
import com.kevinlu.watstats.data.AccountBalance;
import com.kevinlu.watstats.data.Balance;
import com.kevinlu.watstats.data.Date;
import com.kevinlu.watstats.data.Store;
import com.kevinlu.watstats.util.Conversions;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;

public class HomeFragment extends Fragment {

    private TextView totalBalance;
    private SimpleRecyclerView simpleRecyclerView;
    private AccountBalanceAdapter accountBalanceAdapter;
    private List<AccountBalance> accountBalanceList;
    private List<com.kevinlu.watstats.data.Transaction> transactionList;
    private WatCardClient.Builder builder = new WatCardClient.Builder();
    private WatCardClient client;
    private String user;
    private String pass;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Date defaultDate = new Date(0, "");

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

        assert view != null;
        totalBalance = view.findViewById(R.id.total_balance);

        accountBalanceList = new ArrayList<>();

        accountBalanceAdapter = new AccountBalanceAdapter(accountBalanceList);

        ViewPager2 accountBalances = view.findViewById(R.id.account_balances);
        accountBalances.setAdapter(accountBalanceAdapter);
        accountBalances.setOffscreenPageLimit(3);
        accountBalances.setPageTransformer((page, position) -> {
            float myOffset = position * -(Conversions.dpToPx(Objects.requireNonNull(getContext()), 40)
                    + Conversions.dpToPx(getContext(), 205));
            page.setTranslationX(myOffset);
        });

        /*accountBalances.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position < accountBalanceList.size() - 1) {
                    return;
                }
                accountBalances.setCurrentItem(position - 1);
            }
        });*/

        // This login info is 100% working as confirmed by login screen.
        // No need to check validity.
        assert activity != null;
        Bundle bundle = activity.getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getString("user");
            pass = bundle.getString("pass");
        }

        builder.account(user);
        builder.pin(pass);
        client = builder.build();

        getBalances();

        simpleRecyclerView = view.findViewById(R.id.account_transactions);
        //simpleRecyclerView.addItemDecoration(new DividerItemDecoration(activity, RecyclerView.VERTICAL));
        this.addRecyclerHeaders(true);
        this.bindData();
    }

    private void getBalances() {
        compositeDisposable.add(client.getBalances()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Balances>() {
                    @Override
                    public void onSuccess(Balances balances) {
                        setAccountBalances(balances);
                        //TODO: change float to BigDecimal
                        float sum = 0;
                        for (BalanceType balanceType : BalanceType.values()) {
                            sum += balances.get(balanceType).getBalance().floatValue();
                        }
                        String total = "$ " + Conversions.roundUp(sum, 2);
                        setTotalBalance(total);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Watcard:getBalances()", e.toString());
                    }
                }));
    }

    //Adds the headers to the recycler view
    private void addRecyclerHeaders(boolean blank) {
        FragmentActivity activity = getActivity();
        SectionHeaderProvider<com.kevinlu.watstats.data.Transaction> header = new SimpleSectionHeaderProvider<com.kevinlu.watstats.data.Transaction>() {
            @NonNull
            @Override
            public View getSectionHeaderView(@NonNull com.kevinlu.watstats.data.Transaction transaction, int i) {
                View view = LayoutInflater.from(activity).inflate(R.layout.header_blank, ((ViewGroup) getView()), false);
                if (!blank) {
                    view = LayoutInflater.from(activity).inflate(R.layout.header_transactions, ((ViewGroup) getView()), false);
                    TextView textView = view.findViewById(R.id.header_name);
                    textView.setText(transaction.getDate());
                }
                return view;
            }

            //Checks if next transaction is listed under same heading as current transaction
            @Override
            public boolean isSameSection(@NonNull com.kevinlu.watstats.data.Transaction transaction, @NonNull com.kevinlu.watstats.data.Transaction nextTransaction) {
                return transaction.getDateID() == nextTransaction.getDateID();
            }

            //For sticky headers
            @Override
            public boolean isSticky() {
                return true;
            }
        };
        simpleRecyclerView.setSectionHeader(header);
    }

    //function adds each transaction to recycler view
    private void bindData() {
        //Initialize the list of transactions
        transactionList = new ArrayList<>();

        // Transactions from 1 week ago til now, but we'll just return a few rows for recent transactions
        long RECENT_LENGTH_MS = 604800000L; // Let user decide this?
        java.util.Date dateTo = new java.util.Date();
        java.util.Date dateFrom = new java.util.Date(dateTo.getTime() - RECENT_LENGTH_MS);

        int NUM_RECENT_TRANSACTIONS = 5;
        compositeDisposable.add(client.getTransactions(new TransactionRequest(dateFrom, dateTo, NUM_RECENT_TRANSACTIONS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Transactions>() {
                    @Override
                    public void onSuccess(Transactions transactions) {
                        addRecentTransactions(transactions);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Watcard:getData()", e.toString());
                    }
                }));
    }

    private void addRecentTransactions(Transactions transactions) {
        Locale locale = new Locale(Locale.ENGLISH.getLanguage(), Locale.CANADA.getCountry());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        com.kevinlu.watstats.data.Transaction t;
        for (Transaction transaction : transactions.get()) {
            int TERMINAL_SUBSTRING_START = 8;
            String terminal = transaction.getTerminal().substring(TERMINAL_SUBSTRING_START);
            String location = "?";
            int color = R.color.aluminum;
            Store store = Store.matchStore(transaction.getTerminal());

            if (store != null) {
                terminal = store.getName();
                location = store.getLocation();
                color = store.getColor();
            }

            String amount = transaction.getAmount().toString();
            String balanceType = Objects.requireNonNull(Balance.matchBalance(transaction.getBalanceType().getId())).getName();
            String dateTime = dateFormat.format(transaction.getDate());

            //Get list of unique dates (for headings) and initialize here
            t = new com.kevinlu.watstats.data.Transaction(terminal, color, location, amount, balanceType, dateTime, defaultDate);
            transactionList.add(t);
        }

        //Sort transactions in order
        Collections.sort(transactionList, (transaction, nextTransaction) -> transaction.getDateID() - nextTransaction.getDateID());

        List<RecyclerViewAdapter> cells = new ArrayList<>();

        //Iterates through list of transactions
        for (com.kevinlu.watstats.data.Transaction transaction : transactionList) {
            RecyclerViewAdapter cell = new RecyclerViewAdapter(transaction);
            cell.setOnCellClickListener(o -> ((MainActivity) Objects.requireNonNull(getActivity())).goTransactions());
            cells.add(cell);
        }
        simpleRecyclerView.addCells(cells);
    }

    private void setTotalBalance(String total) {
        totalBalance.setText(total);
    }

    private void setAccountBalances(Balances balances) {
        for (BalanceType balanceType : BalanceType.values()) {
            if (balances.get(balanceType).getBalance().compareTo(BigDecimal.ZERO) != 0) {
                Balance balance = Balance.matchBalance(balanceType.getId());
                assert balance != null;
                accountBalanceList.add(new AccountBalance(
                                Objects.requireNonNull(balance.getIcon()),
                                Objects.requireNonNull(balance.getNewlineName()),
                                "$ " + balances.get(balanceType).getBalance().toString(),
                                "$ 0"));
            }
        }
        accountBalanceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
