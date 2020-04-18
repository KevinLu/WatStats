package com.kevinlu.watstats;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    final Fragment home = new HomeFragment();
    final Fragment transactions = new TransactionsFragment();
    final FragmentManager manager = getSupportFragmentManager();
    Fragment selectedFragment = home;
    private BottomNavigationView bottomNav;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        manager.beginTransaction().add(R.id.fragment_container, transactions, "2").hide(transactions).commit();
        manager.beginTransaction().add(R.id.fragment_container, home, "1").commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        goHome();
                        return true;
                    case R.id.nav_transactions:
                        goTransactions();
                        return true;
                }
                return false;
            };

    public void goTransactions() {
        manager.beginTransaction().hide(selectedFragment).show(transactions).commit();
        selectedFragment = transactions;
        changeSelectedMenuItem(R.id.nav_transactions);
    }

    public void goHome() {
        manager.beginTransaction().hide(selectedFragment).show(home).commit();
        selectedFragment = home;
        changeSelectedMenuItem(R.id.nav_home);
    }

    private void changeSelectedMenuItem(int itemId) {
        for (int i = 0; i < bottomNav.getMenu().size(); i++) {
            MenuItem menuItem = bottomNav.getMenu().getItem(i);
            boolean isSelected = menuItem.getItemId() == itemId;
            menuItem.setChecked(isSelected);
        }
    }
}
