package com.mycompany.accountmanager;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

public class MainActivity extends AppCompatActivity {
    Intent intent = new Intent();

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String s = intent.getStringExtra("test");
        String senderNum = intent.getStringExtra("senderNum");
//        Toast.makeText(this, ""+senderNum.trim().equals("01079634363"), Toast.LENGTH_LONG).show();
//        if(senderNum.trim().equals("15991111"))
//            sendSMS(s);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.saveButton);
        final Switch exec = (Switch) findViewById(R.id.exec);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
//                shareKakao();
            }
        });

        exec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED &&
                            checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(MainActivity.this, "문자 전달 기능이 실행됩니다.", Toast.LENGTH_LONG).show();
                    }else{
                        switchPermission();
                        exec.setChecked(false);
                    }
                }
            }
        });
    }

    public void shareKakao() {
        Log.i("shareKakaotalk", "Execute");
        try {
            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(this);
            final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

            /*메시지 추가*/
            kakaoBuilder.addText("카카오링크 테스트");

            /*메시지 발송*/
            kakaoLink.sendMessage(kakaoBuilder, this);
        } catch (KakaoParameterException e) {
            Log.e("shareKakaotalk Error", e.toString());
        }
    }

    public void switchPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_DENIED &&
                    checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {

                addPermission(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, 1);

            } else if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
                addPermission(new String[]{Manifest.permission.RECEIVE_SMS}, 1);
            }


        }
    }

    public void processSMSIntent(Intent intent){
        if (intent != null){
            String sender = intent.getStringExtra("sender");
            String contents =  intent.getStringExtra("contents");
            String receivedDate = intent.getStringExtra("receivedDate");
            Toast.makeText(this, contents, Toast.LENGTH_SHORT).show();

        }
    }
    public void addPermission(final String[] permissions, final int n) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("권한이 필요합니다.")
                .setMessage("이 기능을 사용하기 위해서는 단말기의 \"SMS접근\" 권한이 필요합니다. 계속하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermissions(permissions, n);
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "기능을 취소했습니다.", Toast.LENGTH_SHORT).show();

                    }
                }).create().show();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode){
//            case 1:
//                for (int i=0; i < permissions.length; i++){
//                    String permission = permissions[i];
//                    int grantResult = grantResults[i];
////                    if(permission.equals(Manifest.permissio))
//                }
//        }
//    }

    public void sendSMS(String smsText){
        String smsNum = "01093643474";
        sendSms(smsNum, smsText);

    }

    public void sendSms(String smsNumber, String smsText){
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent);

    }
}
