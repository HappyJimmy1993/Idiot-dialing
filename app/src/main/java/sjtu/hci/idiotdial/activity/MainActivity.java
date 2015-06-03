package sjtu.hci.idiotdial.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import sjtu.hci.idiotdial.R;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");

    }

}
