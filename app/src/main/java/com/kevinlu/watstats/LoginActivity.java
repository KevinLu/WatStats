package com.kevinlu.watstats;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    RelativeLayout container;
    AnimationDrawable anim;

    TextView lblHeader;

    EditText edtPassword, edtEmail;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(android.R.id.content).setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        setContentView(R.layout.activity_login);
        container = findViewById(R.id.container);
        anim = (AnimationDrawable) container.getBackground();
        anim.setEnterFadeDuration(1000);
        anim.setExitFadeDuration(2000);

        lblHeader = findViewById(R.id.appHeader);

        edtEmail = findViewById(R.id.loginUser);
        edtPassword = findViewById(R.id.loginPass);
        btnLogin = findViewById(R.id.loginButton);

        btnLogin.setOnClickListener(view -> {
            if (edtPassword.getText().toString().equals("") || edtEmail.getText().toString().equals("")) {
                AlertDialog.Builder blankAlert  = new AlertDialog.Builder(this);

                blankAlert.setMessage("Please enter your login details.");
                blankAlert.setTitle("Empty fields");
                blankAlert.setPositiveButton("OK", null);
                blankAlert.setCancelable(true);
                blankAlert.create().show();
            } else {
                Intent launchHome = new Intent(LoginActivity.this, MainActivity.class);
                launchHome.putExtra("user", edtEmail.getText().toString());
                launchHome.putExtra("pass", edtPassword.getText().toString());
                startActivity(launchHome);
            }
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
}
