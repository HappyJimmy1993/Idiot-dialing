package sjtu.hci.idiotdial.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;



/**
 * Created by Tian on 2015/6/3.
 */
public class PhoneCallReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras!= null){
            String state = extras.getString(TelephonyManager.EXTRA_STATE);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intentPhoneCall = new Intent("android.intent.action.ANSWER");
                        intentPhoneCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentPhoneCall);
                    }
                }, 100);
            }
        }
    }
}
