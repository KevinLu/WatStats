package com.kevinlu.watstats;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
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

    @BindView(R.id.input_watcard)
    EditText _watcardText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.about)
    TextView _aboutLink;
    @BindView(R.id.privacy_policy)
    TextView _privacyLink;

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

        String watcard = _watcardText.getText().toString();
        String password = _passwordText.getText().toString();

        webDriver = findViewById(R.id.webview_test);
        webDriver.getSettings().setJavaScriptEnabled(true);
        webDriver.getSettings().setDomStorageEnabled(true);
        webDriver.getSettings().setAppCacheEnabled(false);
        webDriver.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webLogin(watcard, password);

        // TODO: Implement Watcard login here

        new android.os.Handler().postDelayed (
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                    }
                }, 3000);
    }

    /**
     * webLogin(String id, String pwd) consumes a string, id, and a
     * string, pwd and logs in user. Outputs invalid id otherwise
     * webLogin: String String -> String
     * Examples:
     * (check-expect (webLogin("69" "420")) "Incorrect Login Details")
     * @param id
     * @param pwd
     */
    public void webLogin(String id, String pwd) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        webDriver.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {

                Toast.makeText(getApplicationContext(),
                        "WebView Error",
                        Toast.LENGTH_SHORT).show();

                super.onReceivedError(view, request, error);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                switch (url) {
                    case "https://watcard.uwaterloo.ca/OneWeb/Account/LogOn#first":
                        webDriver.loadUrl("javascript:(function(){" +
                                String.format("document.getElementById('Account').value='%s';", id) +
                                String.format("document.getElementById('Password').value='%s';", pwd) +
                                "document.forms[1].submit();" +
                                "})()");
                        break;
                    case "https://watcard.uwaterloo.ca/OneWeb/Account/LogOn":
                        _loginButton.setEnabled(true);

                        Toast.makeText(getBaseContext(), "Incorrect login details", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        break;
                    case "https://watcard.uwaterloo.ca/OneWeb/Account/Personal":
                        Toast.makeText(getBaseContext(), "Logged in", Toast.LENGTH_LONG).show();
                        _loginButton.setEnabled(true);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        progressDialog.dismiss();
                        break;
                }
            }
        });
        webDriver.loadUrl("https://watcard.uwaterloo.ca/OneWeb/Account/LogOn#first");
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        //finish();
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
