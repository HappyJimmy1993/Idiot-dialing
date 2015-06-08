package sjtu.hci.idiotdial.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import sjtu.hci.idiotdial.R;

/**
 * Created by Tian on 2015/6/3.
 */
public class IncomingPhoneCallActivity extends Activity {
    private static final String TAG = "InomePhoneCallActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.e(TAG, "On Created!");

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_incoming_phonecall);
    }

    public void answerPhoneCall(View view){

    }

    public void denyPhoneCall(View view){

    }
}
