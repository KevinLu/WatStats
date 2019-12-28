package com.kevinlu.watstats;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.francochen.watcard.WatCardClient;
import com.francochen.watcard.authentication.AuthenticationException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;


public class LoginActivity extends AppCompatActivity {

    private AnimationDrawable anim;

    private EditText pass, user;

    private WatCardClient.Builder builder = new WatCardClient.Builder();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(android.R.id.content).setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        setContentView(R.layout.activity_login);
        RelativeLayout container = findViewById(R.id.container);
        anim = (AnimationDrawable) container.getBackground();
        anim.setEnterFadeDuration(1000);
        anim.setExitFadeDuration(2000);

        user = findViewById(R.id.loginUser);
        pass = findViewById(R.id.loginPass);
        Button btnLogin = findViewById(R.id.loginButton);

        btnLogin.setOnClickListener(view -> {
            // Enter the WatCard info provided by user.
            builder.account(user.getText().toString());
            builder.pin(pass.getText().toString());
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
                            Intent launchHome = new Intent(LoginActivity.this, MainActivity.class);
                            //TODO: change passing String to passing Client
                            launchHome.putExtra("user", user.getText().toString());
                            launchHome.putExtra("pass", pass.getText().toString());
                            startActivity(launchHome);
                        }

                        /**
                         * Called once if the deferred computation 'throws' an exception.
                         *
                         * @param e the exception, not null.
                         */
                        @Override
                        public void onError(Throwable e) {
                            Log.e("Watcard:login", e.toString());

                            if (user.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
                                AlertDialog.Builder blankAlert = new AlertDialog.Builder(LoginActivity.this);
                                blankAlert.setMessage("Please enter your login details.");
                                blankAlert.setTitle("Empty fields");
                                blankAlert.setPositiveButton("OK", null);
                                blankAlert.setCancelable(true);
                                blankAlert.create().show();
                            } else if (e.getClass() == AuthenticationException.class) {
                                AlertDialog.Builder authAlert = new AlertDialog.Builder(LoginActivity.this);
                                authAlert.setTitle("Incorrect login details.");
                                authAlert.setMessage("Please check your login details and re-enter them.");
                                authAlert.setPositiveButton("OK", null);
                                authAlert.setCancelable(true);
                                authAlert.create().show();
                            } else {
                                AlertDialog.Builder networkAlert = new AlertDialog.Builder(LoginActivity.this);
                                networkAlert.setTitle("Network error.");
                                networkAlert.setMessage("A network error has occurred. Please check that you are connected to the internet.");
                                networkAlert.setPositiveButton("OK", null);
                                networkAlert.setCancelable(true);
                                networkAlert.create().show();
                            }
                        }
                    })
            );
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (anim != null && !anim.isRunning()) anim.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (anim != null && anim.isRunning()) anim.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
