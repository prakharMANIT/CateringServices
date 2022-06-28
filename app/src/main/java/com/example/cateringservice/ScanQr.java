package com.example.cateringservice;

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

public class ScanQr extends AppCompatActivity {


    CodeScanner codeScanner;
    CodeScannerView scanView;
    TextView resultData;
    String answer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        //Current Date
        Format f = new SimpleDateFormat("MM/dd/yyyy");
        final String currDate = f.format(new Date());
        //Initialize and Assign variable
        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.ScanQR);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ScanQR :


                        return true;
                    case R.id.GenerateQr :
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);

                        return true;
                    case R.id.Database :

                        startActivity(new Intent(getApplicationContext(),Database.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


        //begin
        scanView=findViewById(R.id.scannerView);
        codeScanner=new CodeScanner(this,scanView);
        resultData=findViewById(R.id.resultsOfQr);
        //

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override

            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        answer=result.getText();
                        answer=answer.trim();

                        Query checkUser=FirebaseDatabase.getInstance().getReference("users").child(answer);
                        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {

                                    String begDate=snapshot.child("beginDate").getValue().toString();
                                    String endDatee = snapshot.child("endDate").getValue().toString();
                                   try{ SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                                    Date datee1 = sdf.parse(endDatee);
                                    Date dateee2 = sdf.parse(currDate);
                                       Date dateee3 = sdf.parse(begDate);
                                    if(dateee2.after(datee1))
                                    {
                                        resultData.setText("Entry Denied - Event Over !!");
                                        return;
                                    }
                                   else if(dateee3.after(dateee2))
                                   {
                                       resultData.setText("Entry Denied - Event yet to begin !!");
                                       return;
                                   }
                                   }catch (ParseException e)
                                   {
                                       e.printStackTrace();
                                   }
                                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");


                                    LocalDate date1 = LocalDate.parse(begDate, dtf);
                                    LocalDate date2 = LocalDate.parse(currDate, dtf);
                                    LocalDate date3 = LocalDate.parse(endDatee, dtf);
                                    long daysBetweenBegCurr = ChronoUnit.DAYS.between(date1, date2);
                                    long daysBetweenBegEnd=ChronoUnit.DAYS.between(date1, date3);



                                        if(Integer.parseInt(snapshot.child(daysBetweenBegCurr+"").getValue().toString().trim())>0)
                                        {

                                            String x=""+(Integer.parseInt(snapshot.child(daysBetweenBegCurr+"").getValue().toString().trim())-1);
                                            resultData.setText("Entry permitted , Number of Plates left for today - "+x);
                                            DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users");
                                            Map<String,Object> updatedValues=new HashMap<>();
                                            updatedValues.put("/"+answer+"/"+daysBetweenBegCurr,x);
                                            reference.updateChildren(updatedValues);

                                            //Sending the details to new Activity
                                            Intent intent = new Intent(getApplicationContext(), Entry.class);
                                            //passing details


                                            intent.putExtra("begD", begDate);
                                            intent.putExtra("endD",endDatee);
                                            intent.putExtra("mob",snapshot.child("contactNo").getValue().toString().trim());
                                            intent.putExtra("mail", snapshot.child("emailAddress").getValue().toString().trim());
                                            intent.putExtra("t", x);
                                            intent.putExtra("event", snapshot.child("eventType").getValue().toString().trim());
                                            // start the Intent
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            resultData.setText("Entry Denied , No plates left for today !!");
                                        }

                                }
                                else
                                {
                                    resultData.setText("Entry Denied , No registered Guest Found !!");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });
            }
        });
        scanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultData.setText("Scan the Code!!");
                codeScanner.startPreview();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        requestForCamera();

    }

    private void requestForCamera() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(ScanQr.this, "Camera permission is Required !!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
            token.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
}


