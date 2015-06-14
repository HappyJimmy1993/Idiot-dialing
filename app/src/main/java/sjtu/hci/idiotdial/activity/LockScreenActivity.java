package sjtu.hci.idiotdial.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

import sjtu.hci.idiotdial.R;
import sjtu.hci.idiotdial.manager.AudioManager;
import sjtu.hci.idiotdial.service.LockScreenService;

/**
 * Created by Tian on 2015/5/30.
 */
public class LockScreenActivity extends Activity {

    private static final String TAG = "LockScreenActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.e(TAG, "On Created!");
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lockscreen);
        startService(new Intent(this, LockScreenService.class));
        TextView pressToSay = (TextView) findViewById(R.id.pressToSay);
        Log.e(TAG, "2333");
        pressToSay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startSayName();
                        break;
                    case MotionEvent.ACTION_UP:
                        stopSayName();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        stopSayName();
                        break;
                }
                return true;
            }
        });
    }

    public void unlock(View view){
        vibrate();
        finish();
    }

    public void startSayName(){
        Log.e(TAG, "Start Say Name");
        AudioManager.getInstance().startRecord(getApplicationContext());
    }

    public void stopSayName(){
        Log.e(TAG, "Stop Say Name");
        String name = AudioManager.getInstance().stopToGetName();
        Log.e(TAG, name);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_HOME)) {
            // Key code constant: Home key. This key is handled by the framework
            // and is never delivered to applications.
            Log.e(TAG, "Home");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Log.e(TAG, "Back");
        return;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

}
