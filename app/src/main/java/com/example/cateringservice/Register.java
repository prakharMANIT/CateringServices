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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class Register extends AppCompatActivity {


    EditText name,mail,pass,phone;
    TextView login;
    Button register;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    Member m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name=(EditText)findViewById(R.id.fullName);
        mail=(EditText)findViewById(R.id.Email);
        pass=(EditText)findViewById(R.id.password);
        phone=(EditText)findViewById(R.id.phone);

        register=(Button)findViewById(R.id.registerBtn);
        login=(TextView)findViewById(R.id.createText);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rootNode=FirebaseDatabase.getInstance();
                reference=rootNode.getReference("users");

                String FullName=name.getText().toString().trim();
                String Email=mail.getText().toString().trim();
                String Password=pass.getText().toString().trim();
                String Mobile=phone.getText().toString().trim();


                //constraints for the fields
                if(TextUtils.isEmpty(FullName))
                {
                    name.setError("Full Name Required!");
                    return;
                }
                if(TextUtils.isEmpty(Password))
                {
                    pass.setError("Password Required!");
                    return;
                }
                if(TextUtils.isEmpty(Mobile))
                {
                    phone.setError("Phone no. Required!");
                    return;
                }

                //Email Address constraints
                String arr[]=Email.split("@");
                if(arr.length!=2)
                {
                    mail.setError("Invalid Email Address!");
                    return;
                }
                else if(arr[0].length()==0 || arr[1].length()==0 )
                {
                    mail.setError("Invalid Email Address!");
                    return;
                }

                //using member
                String abc[]=Email.split("@");
                m=new Member(Password,Mobile,FullName,Email," ",909);
                reference.child(Password+""+abc[0]).setValue(m);

                register.setTextColor(Color.parseColor("#FF0000"));

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login.setTextColor(Color.parseColor("#FF0000"));

                Intent intent = new Intent(getApplicationContext(), Login.class);

                startActivity(intent);
            }
        });

    }
}
