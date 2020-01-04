package com.kevinlu.watstats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    final Fragment home = new HomeFragment();
    final Fragment transactions = new TransactionsFragment();
    final FragmentManager manager = getSupportFragmentManager();
    Fragment selectedFragment = home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        manager.beginTransaction().add(R.id.fragment_container, transactions, "2").hide(transactions).commit();
        manager.beginTransaction().add(R.id.fragment_container, home, "1").commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        manager.beginTransaction().hide(selectedFragment).show(home).commit();
                        selectedFragment = home;
                        return true;
                    case R.id.nav_transactions:
                        manager.beginTransaction().hide(selectedFragment).show(transactions).commit();
                        selectedFragment = transactions;
                        return true;
                }
                return false;
            };
}
