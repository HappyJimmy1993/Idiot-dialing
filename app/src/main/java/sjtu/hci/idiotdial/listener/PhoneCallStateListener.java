package sjtu.hci.idiotdial.listener;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;


/**
 * Created by Tian on 2015/6/3.
 */
public class PhoneCallStateListener extends PhoneStateListener {
    private Context context;

    public PhoneCallStateListener(Context context) {
        this.context = context;
    }


    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                // CALL_STATE_IDLE;
                Toast.makeText(context, "CALL_STATE_IDLE",
                        Toast.LENGTH_LONG).show();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                // CALL_STATE_OFFHOOK;
                Toast.makeText(context, "CALL_STATE_OFFHOOK",
                        Toast.LENGTH_LONG).show();
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                // CALL_STATE_RINGING
                Toast.makeText(context, incomingNumber,
                        Toast.LENGTH_LONG).show();
                Toast.makeText(context, "CALL_STATE_RINGING",
                        Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
}