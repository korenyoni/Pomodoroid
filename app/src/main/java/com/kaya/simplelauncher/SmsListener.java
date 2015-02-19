package com.kaya.simplelauncher;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsListener extends BroadcastReceiver{

    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;
    private static final String MESSAGE = "Automated Message.";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // TODO Auto-generated method stub

        mDevicePolicyManager = (DevicePolicyManager)context.getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(context, MyAdminReceiver.class);

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
        {
            boolean isAdmin = mDevicePolicyManager.isAdminActive(mComponentName);
            if (isAdmin) {
                mDevicePolicyManager.lockNow();
                Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                SmsMessage[] msgs = null;
                String msg_from;
                if (bundle != null){
                    //---retrieve the SMS message received---
                    try{
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for(int i=0; i<msgs.length; i++){
                            msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                            msg_from = msgs[i].getOriginatingAddress();
                            sendSMS(msg_from, MESSAGE);
                        }
                    }catch(Exception e){
//                            Log.d("Exception caught",e.getMessage());
                    }
                }
            }else{
                Toast.makeText(context.getApplicationContext(), "Not Registered as admin", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void sendSMS(String phoneNumber, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
}