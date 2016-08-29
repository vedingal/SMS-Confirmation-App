package com.venndingal.smsconfirmation;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    EditText et_mobileNo, et_verificationCode;
    Button btn_sendVerificationCode, btn_verifyCode;
    String verificationCode;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // for hiding title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        c = this;

        //Get permission to send SMS.
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);

        et_mobileNo = (EditText) findViewById(R.id.et_mobileNo);
        et_verificationCode = (EditText) findViewById(R.id.et_verificationCode);
        btn_sendVerificationCode = (Button) findViewById(R.id.btn_sendVerificationCode);
        btn_verifyCode = (Button) findViewById(R.id.btn_verifyCode);

        et_verificationCode.setVisibility(View.INVISIBLE);
        btn_verifyCode.setVisibility(View.INVISIBLE);

        btn_sendVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Assign 6 digit random number to variable 'verificationCode'
                Random rnd = new Random();
                int n = 100000 + rnd.nextInt(900000);
                verificationCode = Integer.toString(n);

                String str_mobileNo = et_mobileNo.getText().toString();
                if (!et_mobileNo.getText().toString().equals("")){
                    et_mobileNo.setError(null);

                    //Used to send SMS
                    SmsManager sm = SmsManager.getDefault();
                    sm.sendTextMessage(str_mobileNo, null, "SMSConfimation App: Your Verification Code is " + verificationCode, null, null);

                    et_verificationCode.setVisibility(View.VISIBLE);
                    btn_verifyCode.setVisibility(View.VISIBLE);
                    btn_verifyCode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String str_verificationCode = et_verificationCode.getText().toString();

                            if (!str_verificationCode.equals("")){

                                et_verificationCode.setError(null);

                                if (str_verificationCode.equals(verificationCode)){
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(c, "Verification successful!", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }else{
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            et_verificationCode.setError("Did not match code sent.");
                                        }
                                    });
                                }

                            }else{
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        et_verificationCode.setError("Can not be empty.");
                                    }
                                });
                            }
                        }
                    });

                }else{
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            et_mobileNo.setError("Can not be empty.");
                        }
                    });
                }

            }
        });


    }
}
