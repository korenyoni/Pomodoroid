package com.kaya.simplelauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;


public class IncomingCallReceiver extends BroadcastReceiver
{

    Context mContext;


    @Override
    public void onReceive(Context mContext, Intent intent)
    {
        AudioManager audio = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        try
        {

            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            System.out.println("Hello");


            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING))
            {
                audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                Toast.makeText(mContext, "Phone Is Ringing", Toast.LENGTH_LONG).show();
            }

            if(state.equals(TelephonyManager.EXTRA_STATE_IDLE) || state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
            {
                audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                Toast.makeText(mContext, "Not Ringing", Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e)
        {
            //your custom message
        }

    }

}