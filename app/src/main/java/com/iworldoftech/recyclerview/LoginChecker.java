package com.iworldoftech.recyclerview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginChecker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_checker);
    }

    public void checker(View view){
        EditText usernameField = (EditText) findViewById(R.id.username);
        EditText passwordField = (EditText) findViewById(R.id.password);

        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if(username.equals("admin") && password.equals("admin")) {
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
        } else {
            LayoutInflater inflater = getLayoutInflater();
            View view2 = inflater.inflate(R.layout.alert_viewer, null);
            final TextView edit = (TextView) view2.findViewById(R.id.alertTextContent);

            edit.setText("Incorrect Password");

            AlertDialog.Builder builder = new AlertDialog.Builder(LoginChecker.this);
            builder.setMessage("System");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setView(view2);
            builder.setPositiveButton("OK", (dialog, which) -> {} );
            builder.setCancelable(false);
            builder.create().show();
        }

//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
    }
}