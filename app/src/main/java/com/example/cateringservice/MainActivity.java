package com.example.cateringservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.os.Bundle;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText editTextEventType,editTextBeginDate,editTextEndDate,editTextContactNo,editTextEmail;
    Button buttonSubmit;
    EditText noOfTimes;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

        //this app start with Register.java not with MainActivity.java
        //changed with the AndroidManifest.xml

        //only sending of qr code through email and message not working
        // that is Submit.java not working.

    Member m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize and Assign variable
        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.GenerateQr);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ScanQR :

                        startActivity(new Intent(getApplicationContext(),ScanQr.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.GenerateQr :


                        return true;
                    case R.id.Database :

                        startActivity(new Intent(getApplicationContext(),Database.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


        //Edit Text and Button

        editTextBeginDate=(EditText) findViewById(R.id.password);
        editTextContactNo=(EditText) findViewById(R.id.editText4);
        editTextEmail=(EditText) findViewById(R.id.editText5);
        editTextEndDate=(EditText) findViewById(R.id.phone);
        editTextEventType=(EditText) findViewById(R.id.fullName);
        noOfTimes=(EditText)findViewById(R.id.noOfTimesADay);



        //Submit button OnClick
        buttonSubmit=(Button)findViewById(R.id.buttonEmail);

        //on click listner on submit button

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                rootNode=FirebaseDatabase.getInstance();
                reference=rootNode.getReference("users");

                String eventTyp=editTextEventType.getText().toString().trim();
                String beginDatee=editTextBeginDate.getText().toString().trim();
                String endDatee=editTextEndDate.getText().toString().trim();
                String contactNum=editTextContactNo.getText().toString().trim();
                String emailAdd=editTextEmail.getText().toString().trim();
                String times=noOfTimes.getText().toString().trim();

                //constraints for the fields
                if(TextUtils.isEmpty(eventTyp))
                {
                    editTextEventType.setError("EventType Required!");
                    return;
                }
                if(TextUtils.isEmpty(beginDatee))
                {
                    editTextBeginDate.setError("Beginning Date Required!");
                    return;
                }
                if(TextUtils.isEmpty(endDatee))
                {
                    editTextEndDate.setError("Ending Date Required!");
                    return;
                }
                if(TextUtils.isEmpty(contactNum))
                {
                    editTextContactNo.setError("Contact Number Required!");
                    return;
                }
                if(TextUtils.isEmpty(emailAdd))
                {
                    editTextEmail.setError("Email Address Required!");
                    return;
                }
                if(TextUtils.isEmpty(times))
                {
                    noOfTimes.setError("Serving Frequency per day Required!");
                    return;
                }

                //Email Address constraints
                String arr[]=emailAdd.split("@");
                if(arr.length!=2)
                {
                    editTextEmail.setError("Invalid Email Address!");
                    return;
                }
                else if(arr[0].length()==0 || arr[1].length()==0 )
                {
                    editTextEmail.setError("Invalid Email Address!");
                    return;
                }
                //Dates constraints
                String arr1[]=beginDatee.split("/");
                String arr2[]=endDatee.split("/");
                if(arr1.length!=3)
                {
                    editTextBeginDate.setError("Enter valid Date format!!!(mm/dd/yyyy)");
                    return;
                }
                else if(arr1[0].length()!=2 || arr1[1].length()!=2 || arr1[2].length()!=4)
                {
                    editTextBeginDate.setError("Enter valid Date format!!!(mm/dd/yyyy)");
                    return;
                }
                if(arr2.length!=3)
                {
                    editTextEndDate.setError("Enter valid Date format!!!(mm/dd/yyyy)");
                    return;
                }
                else if(arr2[0].length()!=2 || arr2[1].length()!=2 || arr2[2].length()!=4)
                {
                    editTextEndDate.setError("Enter valid Date format!!!(mm/dd/yyyy)");
                    return;
                }
                //constraints over
                int noOfTime=Integer.parseInt(noOfTimes.getText().toString().trim());


                //using member
                String abc[]=emailAdd.split("@");
                m=new Member(beginDatee,endDatee,eventTyp,contactNum,emailAdd,noOfTime);
                reference.child(contactNum+""+abc[0]).setValue(m);
                //IMP
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");


                LocalDate date1 = LocalDate.parse(m.beginDate, dtf);
                LocalDate date2 = LocalDate.parse(m.endDate, dtf);
                long daysBetween = ChronoUnit.DAYS.between(date1, date2);
                //IMP ENDS
                for(long i=0;i<=daysBetween;i++)
                    reference.child(contactNum+""+abc[0]).child(""+i).setValue(noOfTime+"");


                String str=editTextContactNo.getText().toString()+abc[0];


                //chaning color of button submit

                buttonSubmit.setTextColor(Color.parseColor("#FF0000"));


                // Create the Intent object of this class Context() to Second_activity class
                Intent intent = new Intent(getApplicationContext(), Submit.class);
                //passing str


                intent.putExtra("given_str", str);
                intent.putExtra("PhoneNo",contactNum);
                intent.putExtra("email_add",emailAdd);
                // start the Intent
                startActivity(intent);


            }
        });

    }
}
