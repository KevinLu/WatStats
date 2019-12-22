package com.kevinlu.watstats;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    RelativeLayout container;
    AnimationDrawable anim;

    //LoginHelper loginHelper;
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
        anim.setEnterFadeDuration(100);
        anim.setExitFadeDuration(1000);

        //loginHelper = new LoginHelper(MainActivity.this);

        lblHeader = findViewById(R.id.appHeader);

        edtEmail = findViewById(R.id.loginUser);
        edtPassword = findViewById(R.id.loginPass);
        btnLogin = findViewById(R.id.loginButton);

        btnLogin.setOnClickListener(view -> {
            if (edtPassword.getText().toString().equals("") && edtEmail.getText().toString().equals("")) {
                // Show error...
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
        if (anim != null && !anim.isRunning())
            anim.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (anim != null && anim.isRunning())
            anim.stop();
    }
}
