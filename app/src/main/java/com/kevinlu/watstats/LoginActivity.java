package com.kevinlu.watstats;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.BindView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private WebView webDriver;

    @BindView(R.id.input_watcard) EditText _watcardText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.about) TextView _aboutLink;
    @BindView(R.id.privacy_policy) TextView _privacyLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(v -> login());

        _aboutLink.setOnClickListener(v -> {
            // Display the info about the app
            Toast.makeText(getBaseContext(), "App info goes here", Toast.LENGTH_SHORT).show();
        });

        _privacyLink.setOnClickListener(v -> {
            // Display the info about the app
            Toast.makeText(getBaseContext(), "Privacy policy goes here", Toast.LENGTH_SHORT).show();
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        String watcard = _watcardText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement Watcard login here

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String watcard = _watcardText.getText().toString();
        String password = _passwordText.getText().toString();

        if (watcard.isEmpty()) {
            _watcardText.setError("Enter your Watcard number");
            valid = false;
        } else {
            _watcardText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("Enter your Watcard password");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
