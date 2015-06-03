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
import sjtu.hci.idiotdial.service.LockScreenService;

/**
 * Created by Tian on 2015/5/30.
 */
public class LockScreenActivity extends Activity {

    private static final String TAG = "LockScreenActivity";

    /** ÅÐ¶ÏËøÆÁÒ³ÃæÊÇ·ñ´ò¿ª×´Ì¬ */
    public static boolean isStarted = false;//

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.e(TAG, "On Created!");




        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lockscreen);
        isStarted = true;

//        TextView dateText = (TextView) findViewById(R.id.currentDate);
//        String currentDateString = DateFormat.getDateInstance().format(new Date());
//        dateText.setText(currentDateString);

        startService(new Intent(this, LockScreenService.class));
    }

    public void unlock(View view){
        isStarted = false;
        vibrate();
        finish();
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
            Log.e(TAG, "ÏìÓ¦Home¼ü");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Log.e(TAG, "ÏìÓ¦Back¼ü");
        return;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Õð¶¯
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

}
