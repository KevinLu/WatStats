package com.kevinlu.watstats;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.francochen.watcard.WatCardClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;

public class SplashActivity extends AppCompatActivity {

    private WatCardClient.Builder builder = new WatCardClient.Builder();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences loginPreferences = getSharedPreferences("login", MODE_PRIVATE);
        boolean saveLogin = loginPreferences.getBoolean("saveLogin", false);

        if (saveLogin) {
            String user = loginPreferences.getString("user", "");
            String pass = loginPreferences.getString("pass", "");
            login(user, pass);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    /**
     * Call this method to login and launch MainActivity.
     *
     * @param user a String, the Watcard number
     * @param pass a String, the Watcard PIN
     */
    private void login(String user, String pass) {
        // Enter the WatCard info provided by user.
        builder.account(user);
        builder.pin(pass);
        builder.build();
        // Cache the client.
        WatCardClient client = builder.build();

        compositeDisposable.add(client.hasValidCredentials()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    /**
                     * Called once the deferred computation completes normally.
                     */
                    @Override
                    public void onComplete() {
                        Intent launchHome = new Intent(SplashActivity.this, MainActivity.class);
                        //TODO: change passing String to passing Client
                        launchHome.putExtra("user", user);
                        launchHome.putExtra("pass", pass);
                        startActivity(launchHome);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Watcard:splashactivity", e.toString());
                    }
                })
        );
    }
}
