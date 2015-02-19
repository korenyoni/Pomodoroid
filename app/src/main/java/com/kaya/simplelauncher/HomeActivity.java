package com.kaya.simplelauncher;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends Activity implements OnClickListener{

    private static final int ADMIN_INTENT = 15;
    private static final String description = "Sample Administrator description";

    private ComponentName mComponentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponentName = new ComponentName(this, MyAdminReceiver.class);
        setContentView(R.layout.activity_home);
        Button btnEnableAdmin = (Button) findViewById(R.id.btnEnable);
        btnEnableAdmin.setOnClickListener(this);
        changeRingerMode(this, false);

    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.btnEnable)
        {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,description);
            startActivityForResult(intent, ADMIN_INTENT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADMIN_INTENT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Registered As Admin", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Failed to register as Admin", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void changeRingerMode(Context context, boolean on) {

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (on)
        {
            audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
        else
        {
            audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }

    }
    public void showApps(View v){
        Intent i = new Intent(this, AppsListActivity.class);
        startActivity(i);
    }

    @Override
    public void onDestroy()
    {
        changeRingerMode(this, true);
        super.onDestroy();
    }

}