package com.example.cateringservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import  java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class Login extends AppCompatActivity {

    EditText maill, passs;
    TextView registerr;
    Button Loggin;
    String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        maill = (EditText) findViewById(R.id.Email);
        passs = (EditText) findViewById(R.id.password);


        Loggin = (Button) findViewById(R.id.loginBtn);
        registerr = (TextView) findViewById(R.id.createText);

        Loggin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String Email = maill.getText().toString().trim();
                String Password = passs.getText().toString().trim();

                //constraints for the fields

                if (TextUtils.isEmpty(Password)) {
                    passs.setError("Password Required!");
                    return;
                }


                //Email Address constraints
                String arr[] = Email.split("@");
                if (arr.length != 2) {
                    maill.setError("Invalid Email Address!");
                    return;
                } else if (arr[0].length() == 0 || arr[1].length() == 0) {
                    maill.setError("Invalid Email Address!");
                    return;
                }

                String abc[] = Email.split("@");
                answer = Password + "" + abc[0];

                Query checkUser = FirebaseDatabase.getInstance().getReference("users").child(answer);

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            Loggin.setTextColor(Color.parseColor("#FF0000"));

                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Wrong password and email or user not registered !!", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


            }
        });
        registerr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerr.setTextColor(Color.parseColor("#FF0000"));

                Intent intent = new Intent(getApplicationContext(), Register.class);

                startActivity(intent);
            }
        });
    }
}


