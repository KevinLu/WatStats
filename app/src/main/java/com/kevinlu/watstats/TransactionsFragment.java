package com.kevinlu.watstats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.jaychang.srv.SimpleRecyclerView;
import com.jaychang.srv.decoration.DividerItemDecoration;
import com.jaychang.srv.decoration.SectionHeaderProvider;
import com.jaychang.srv.decoration.SimpleSectionHeaderProvider;
import com.kevinlu.watstats.models.Date;
import com.kevinlu.watstats.models.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionsFragment extends Fragment {

    SimpleRecyclerView simpleRecyclerView;

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

        simpleRecyclerView = view.findViewById(R.id.account_transactions);
        simpleRecyclerView.addItemDecoration(new DividerItemDecoration(activity, RecyclerView.VERTICAL));
        this.addRecyclerHeaders();
        this.bindData();
    }

    //Adds the headers to the recycler view
    private void addRecyclerHeaders() {
        FragmentActivity activity = getActivity();
        SectionHeaderProvider<Transaction> header = new SimpleSectionHeaderProvider<Transaction>() {
            @NonNull
            @Override
            public View getSectionHeaderView(@NonNull Transaction transaction, int i) {
                View view = LayoutInflater.from(activity).inflate(R.layout.header_transactions, null, false);
                TextView textView = view.findViewById(R.id.header_name);
                textView.setText(transaction.getDate());
                return view;
            }

            //Checks if next transaction is listed under same heading as current transaction
            @Override
            public boolean isSameSection(@NonNull Transaction transaction, @NonNull Transaction nextTransaction) {
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
        List<Transaction> transaction_list = getData();

        //Sort transactions in order
        Collections.sort(transaction_list, (transaction, nextTransaction) -> transaction.getDateID() - nextTransaction.getDateID());

        List<RecyclerViewAdapter> cells = new ArrayList<>();

        //Iterates through list of transactions
        for (Transaction transaction : transaction_list) {
            RecyclerViewAdapter cell = new RecyclerViewAdapter(transaction);
            cells.add(cell);
        }

        simpleRecyclerView.addCells(cells);
    }

    //function to get list of transactions (currently hardcoded)
    private ArrayList<Transaction> getData() {

        //List of transaction
        ArrayList<Transaction> transaction_list = new ArrayList<>();

        //Get list of unique dates (for headings) and initialize here
        Date today = new Date(0, "Today");
        Date yesterday = new Date(1, "Yesterday");
        Date december23 = new Date(2, "23 December");

        //Get list of transactions and add transaction_list
        Transaction t = new Transaction("Mudies", R.drawable.ic_mealplan, "-$5.30",
                "12/25/2019 5:00 PM", today);
        transaction_list.add(t);

        t = new Transaction("CMH", R.drawable.ic_mealplan, "-$12.30",
                "12/25/2019 3:20 PM", today);
        transaction_list.add(t);

        t = new Transaction("V1 Laundry", R.drawable.ic_flexdollar, "-$1.00",
                "12/24/2019 10:00 PM", yesterday);
        transaction_list.add(t);

        t = new Transaction("SLC", R.drawable.ic_mealplan, "-$11.50",
                "12/24/2019 1:00 PM", yesterday);
        transaction_list.add(t);

        t = new Transaction("WatCard", R.drawable.ic_transfer, "+$50.00",
                "12/24/2019 12:00 PM", yesterday);
        transaction_list.add(t);

        t = new Transaction("WStore", R.drawable.ic_flexdollar, "-$2.50",
                "12/23/2019 10:00 PM", december23);
        transaction_list.add(t);

        t = new Transaction("The Market", R.drawable.ic_mealplan, "-$20.20",
                "12/23/2019 4:20 AM", december23);
        transaction_list.add(t);

        return transaction_list;
    }
}
