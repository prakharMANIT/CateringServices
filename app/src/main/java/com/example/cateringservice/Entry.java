package com.example.cateringservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Entry extends AppCompatActivity {

    TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        t=(TextView)findViewById(R.id.textViewDetails);
        final Intent inte = getIntent();
        String x= inte.getStringExtra("t");        String eve = inte.getStringExtra("event");
        String beg=inte.getStringExtra("begD");
        String end = inte.getStringExtra("endD");
        String phoneN=inte.getStringExtra("mob");
        String mail = inte.getStringExtra("mail");
        t.setText("Event - "+eve+"\n"+"Begin date - "+beg+"\n"+"End date - "+end+"\n"+"Phone no. - "+phoneN+"\n"+"Email - "+mail+"\n\n"+"Plates Left for today - "+x);
    }
}
