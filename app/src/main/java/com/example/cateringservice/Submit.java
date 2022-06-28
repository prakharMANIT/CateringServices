package com.example.cateringservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.net.Uri;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.OutputStream;
import java.io.WriteAbortedException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Submit extends AppCompatActivity {


    ImageView qrCode;
    Button buttonEmail,buttonMessage;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;
    OutputStream outputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        qrCode=(ImageView)findViewById(R.id.imageView5);
        buttonEmail=(Button)findViewById(R.id.buttonEmail);
        buttonMessage=(Button)findViewById(R.id.buttonMessage);

        final Intent inte = getIntent();
        String data = inte.getStringExtra("given_str");
        String phoneN=inte.getStringExtra("PhoneNo");
        final String addr=inte.getStringExtra("email_add");
        qrgEncoder=new QRGEncoder(data,null, QRGContents.Type.TEXT,700);

        try{
            bitmap=qrgEncoder.encodeAsBitmap();
            qrCode.setImageBitmap(bitmap);

        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }


        File dir=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/Demo/");
        dir.mkdir();
        final File file=new File(dir,"qr"+".jpg");
        try {
            outputStream = new FileOutputStream(file);
        }catch (FileNotFoundException f)
        {
            f.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        try {
            outputStream.flush();
        }catch (IOException g)
        {
            g.printStackTrace();
        }
        try {
            outputStream.close();

        }catch (IOException j)
        {
            j.printStackTrace();
        }
        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent=new Intent(Intent.ACTION_SEND);
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                emailIntent.setType("image/*");
                emailIntent.putExtra(Intent.EXTRA_EMAIL,addr);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Invitation from Catering Services !!!");
                emailIntent.putExtra(Intent.EXTRA_TEXT,"You are most welcome for the Party. Please bring the QRCode with you!!");
                emailIntent.putExtra(Intent.EXTRA_STREAM,Uri.parse(file.getAbsolutePath()+"/Demo/qr.jpg"));
                startActivity(Intent.createChooser(emailIntent,"send Email ....."));
            }
        });


    }

}
