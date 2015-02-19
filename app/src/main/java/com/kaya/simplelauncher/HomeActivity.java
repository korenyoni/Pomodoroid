package com.kaya.simplelauncher;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

public class HomeActivity extends Activity implements OnClickListener{

    private static final int ADMIN_INTENT = 15;
    private static final String description = "Sample Administrator description";

    private ComponentName mComponentName;
    private DevicePolicyManager mDevicePolicyManager;

    //
    private CountDownTimer countDownTimer;
    private CountDownTimer breakCountDownTimer;
    private boolean timerHasStarted = false;
    private Button startB;
    private Button settingsB;
    public TextView text;
    public TextView status;
    public static long  study = 1;
    private final long startTime = study * 60 * 1000;
    private final long interval = 1 * 1000;
    private final long startBreakTime = 100 * 1000;
    private static final long SEC_PER_MIN = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDevicePolicyManager = (DevicePolicyManager)getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this, MyAdminReceiver.class);
        setContentView(R.layout.activity_home);
        Button btnEnableAdmin = (Button) findViewById(R.id.btnEnable);
        Button btnDisableAdmin = (Button) findViewById(R.id.btnDisable);
        startB = (Button) this.findViewById(R.id.button);
        btnEnableAdmin.setOnClickListener(this);
        btnDisableAdmin.setOnClickListener(this);
        changeRingerMode(this, false);

        text = (TextView) this.findViewById(R.id.timer);
        status = (TextView) this.findViewById(R.id.status);
        countDownTimer = new MyCountDownTimer(startTime, interval);
        breakCountDownTimer = new BreakCountDownTimer(startBreakTime, interval);
        text.setText(String.valueOf(startTime / 1000 / SEC_PER_MIN) + ":" + String.valueOf(startTime / 1000 % 60));
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
            if (!timerHasStarted) {
                countDownTimer.start();
                status.setText("STUDY!");
                timerHasStarted = true;
                startB.setText("STOP");
            } else {
                countDownTimer.cancel();
                breakCountDownTimer.cancel();
                status.setText("");
                timerHasStarted = false;
                startB.setText("RESTART");
            }
        }
        if (v.getId() == R.id.btnDisable)
        {
            mDevicePolicyManager.removeActiveAdmin(mComponentName);
            Toast.makeText(getApplicationContext(), "Admin registration removed", Toast.LENGTH_SHORT).show();
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

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);


        Log.d("Focus debug", "Focus changed !");

        if(!hasFocus) {
            Log.d("Focus debug", "Lost focus !");

            boolean isAdmin = mDevicePolicyManager.isAdminActive(mComponentName);
            if (isAdmin) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                mDevicePolicyManager.lockNow();
                getFragmentManager().popBackStack();

            }
        }
    }

    @Override
    public void onDestroy()
    {
        changeRingerMode(this, true);
        mDevicePolicyManager.removeActiveAdmin(mComponentName);
        Toast.makeText(getApplicationContext(), "Admin registration removed", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);

        }
        @Override
        public void onFinish() {

            text.setText("Break time!");
            //startB.setText("BREAK");
            countDownTimer.cancel();
            status.setText("BREAK!");
            breakCountDownTimer.start();
        }
        @Override
        public void onTick(long millisUntilFinished) {
            String minutes = "";
            if(millisUntilFinished / 1000 % SEC_PER_MIN < 10){
                minutes = "0" + String.valueOf(millisUntilFinished / 1000 % SEC_PER_MIN);
                text.setText("" + millisUntilFinished / 1000 / SEC_PER_MIN + ":" + minutes );
            }
            else {
                text.setText("" + millisUntilFinished / 1000 / SEC_PER_MIN + ":" + String.valueOf(millisUntilFinished / 1000 % SEC_PER_MIN) );
            }


        }


    }

    public class BreakCountDownTimer extends CountDownTimer{

        public BreakCountDownTimer(long startBreakTime, long interval) {
            super(startBreakTime, interval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String minutes = "";
            if(millisUntilFinished / 1000 % SEC_PER_MIN < 10){
                minutes = "0" + String.valueOf(millisUntilFinished / 1000 % SEC_PER_MIN);
                text.setText("" + millisUntilFinished / 1000 / SEC_PER_MIN + ":" + minutes );
            }
            else {
                text.setText("" + millisUntilFinished / 1000 / SEC_PER_MIN + ":" + String.valueOf(millisUntilFinished / 1000 % SEC_PER_MIN) );
            }
        }

        @Override
        public void onFinish() {
            text.setText("Study");
            status.setText("STUDY!");
            breakCountDownTimer.cancel();
            countDownTimer.start();
        }
    }




}

