package com.kaya.simplelauncher;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SmsListener extends BroadcastReceiver{

    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;

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
            }else{
                Toast.makeText(context.getApplicationContext(), "Not Registered as admin", Toast.LENGTH_SHORT).show();
            }
        }
    }
}