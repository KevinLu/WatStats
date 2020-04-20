package com.kevinlu.watstats;

import android.graphics.drawable.Drawable;
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
import androidx.recyclerview.widget.RecyclerView;

import com.francochen.watcard.WatCardClient;
import com.francochen.watcard.model.transaction.TransactionRequest;
import com.francochen.watcard.model.transaction.Transactions;
import com.jaychang.srv.SimpleRecyclerView;
import com.jaychang.srv.decoration.DividerItemDecoration;
import com.jaychang.srv.decoration.SectionHeaderProvider;
import com.jaychang.srv.decoration.SimpleSectionHeaderProvider;
import com.kevinlu.watstats.data.Balance;
import com.kevinlu.watstats.data.Date;
import com.kevinlu.watstats.data.Store;
import com.kevinlu.watstats.util.Conversions;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;

public class TransactionsFragment extends Fragment {

    private final int TERMINAL_SUBSTRING_START = 8;

    private TextView monthlySpendAmount;
    private TextView monthlySpendText;
    private SimpleRecyclerView simpleRecyclerView;
    private List<com.kevinlu.watstats.data.Transaction> transactionList;
    private WatCardClient.Builder builder = new WatCardClient.Builder();
    private WatCardClient client;
    private String user;
    private String pass;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transactions, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        FragmentActivity activity = getActivity();

        assert view != null;
        simpleRecyclerView = view.findViewById(R.id.account_transactions);
        monthlySpendAmount = view.findViewById(R.id.monthly_spend_amount);
        monthlySpendText = view.findViewById(R.id.monthly_spend_text);

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

        //simpleRecyclerView.addItemDecoration(new DividerItemDecoration(activity, RecyclerView.VERTICAL));
        this.addRecyclerHeaders(false);
        this.bindData();
    }


    //Adds the headers to the recycler view
    private void addRecyclerHeaders(boolean blank) {
        FragmentActivity activity = getActivity();
        SectionHeaderProvider<com.kevinlu.watstats.data.Transaction> header = new SimpleSectionHeaderProvider<com.kevinlu.watstats.data.Transaction>() {
            @NonNull
            @Override
            public View getSectionHeaderView(@NonNull com.kevinlu.watstats.data.Transaction transaction, int i) {
                View view = LayoutInflater.from(activity).inflate(R.layout.header_blank, ((ViewGroup)getView()), false);
                if (!blank) {
                    view = LayoutInflater.from(activity).inflate(R.layout.header_transactions, ((ViewGroup)getView()), false);
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

        // Transactions from 3 months ago til now, but we'll just return a few rows for recent transactions
        long THREE_MONTHS_MS = 7884000000L; // Let user decide this?
        java.util.Date dateTo = new java.util.Date();
        java.util.Date dateFrom = new java.util.Date(dateTo.getTime() - THREE_MONTHS_MS);

        //int NUM_TRANSACTIONS = 30;
        compositeDisposable.add(client.getTransactions(new TransactionRequest(dateFrom, dateTo))
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
        final int currentMonth = new java.util.Date().getMonth();
        float currentMonthSpending = 0;
        Locale locale = new Locale(Locale.ENGLISH.getLanguage(), Locale.CANADA.getCountry());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
        com.kevinlu.watstats.data.Transaction t;
        String date = "";
        int i = 0;

        for (com.francochen.watcard.model.transaction.Transaction transaction : transactions.get()) {
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
            String time = timeFormat.format(transaction.getDate());
            String newDate = dateFormat.format(transaction.getDate());

            if (!date.equals(newDate)) { i++; }
            date = newDate;

            //Get list of unique dates (for headings) and initialize here
            t = new com.kevinlu.watstats.data.Transaction(terminal, color, location, amount, balanceType, time, new Date(i, date));
            transactionList.add(t);

            if (transaction.getDate().getMonth() == currentMonth) {
                currentMonthSpending += transaction.getAmount().floatValue();
            }
        }

        //Sort transactions in order
        Collections.sort(transactionList, (transaction, nextTransaction) -> transaction.getDateID() - nextTransaction.getDateID());

        List<RecyclerViewAdapter> cells = new ArrayList<>();

        //Iterates through list of transactions
        for (com.kevinlu.watstats.data.Transaction transaction : transactionList) {
            RecyclerViewAdapter cell = new RecyclerViewAdapter(transaction);
            cells.add(cell);
        }
        simpleRecyclerView.addCells(cells);
        setMonthlySpendText("Total spent in " + getMonthForInt(currentMonth));
        setMonthlySpendAmount("$ " + Conversions.roundUp(currentMonthSpending, 2));
    }

    private void setMonthlySpendAmount(String total) {
        monthlySpendAmount.setText(total);
    }

    private void setMonthlySpendText(String text) {
        monthlySpendText.setText(text);
    }

    private String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }
}
