package sjtu.hci.idiotdial.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Tian on 2015/5/30.
 */
public class LockScreenReceiver extends BroadcastReceiver {

    private static final String TAG = "LockScreenReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.e(TAG, "ACTION_SCREEN_OFF");

            Intent intent1 = new Intent("android.intent.lockdemo");
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.e(TAG, "ACTION_SCREEN_ON");
        } else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.e(TAG, "ACTION_BOOT_COMPLETED");
        }

    }


}