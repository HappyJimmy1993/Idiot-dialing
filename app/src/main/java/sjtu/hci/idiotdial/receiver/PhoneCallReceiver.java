package sjtu.hci.idiotdial.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import sjtu.hci.idiotdial.activity.IncomingPhoneCallActivity;


/**
 * Created by Tian on 2015/6/3.
 */
public class PhoneCallReceiver extends BroadcastReceiver {
    private static final String INCOMING_PHONE_CALL_EXTRA = "incomingphone";
    private Context context;
    @Override
    public void onReceive(final Context ctx, Intent intent) {
        this.context = ctx;
//        Bundle extras = intent.getExtras();
//        String phoneNum = getResultData();
//        if (phoneNum == null){
//            phoneNum = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
//        }
//        setResultData(phoneNum);
//        if (extras!= null){
//            String state = extras.getString(TelephonyManager.EXTRA_STATE);
//            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent intentPhoneCall = new Intent(context, IncomingPhoneCallActivity.class);
//                        intentPhoneCall.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intentPhoneCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(intentPhoneCall);
//                    }
//                }, 1000);
//            }
//        }

        try
        {
            TelephonyManager tmgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            MyPhoneStateListener PhoneListener = new MyPhoneStateListener();
            tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
        catch (Exception e)
        {
            Log.e("Phone Receive Error", " " + e);
        }
    }


    private class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(final int state, final String phoneNum){
            Handler handler = new Handler();
            Runnable runMyActivity = new Runnable() {
                @Override
                public void run() {
                    if (state == 1){
                        Intent intentPhoneCall = new Intent(context, IncomingPhoneCallActivity.class);
                        intentPhoneCall.putExtra(INCOMING_PHONE_CALL_EXTRA, phoneNum);
                        intentPhoneCall.putExtra("state", state);
                        intentPhoneCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentPhoneCall);
                    }
                }
            };
            if (state == 1)
            {
                handler.postDelayed(runMyActivity, 100);
            }

            if (state == 0)
            {
                handler.removeCallbacks(runMyActivity);
            }
        }
    }
}
