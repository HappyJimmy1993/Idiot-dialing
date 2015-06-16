package sjtu.hci.idiotdial.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import sjtu.hci.idiotdial.R;
import sjtu.hci.idiotdial.adapter.ContactArrayAdapter;
import sjtu.hci.idiotdial.manager.ContactManger;
import sjtu.hci.idiotdial.receiver.PhoneCallReceiver;

/**
 * Created by Tian on 2015/6/3.
 */
public class IncomingPhoneCallActivity extends Activity implements SensorEventListener {
    private static final String TAG = "InomePhoneCallActivity";
    SensorManager sensorManager;
    Sensor sensor;
    private ContactArrayAdapter.ContactItem item;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.e(TAG, "On Created!");

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_incoming_phonecall);

        String phoneNum = getIntent().getStringExtra(PhoneCallReceiver.INCOMING_PHONE_CALL_EXTRA);
        item = ContactManger.getInstance().getItemByPhone(phoneNum, this.getApplicationContext());

        TextView commingName = (TextView) findViewById(R.id.incomingName);
        ImageView imgView = (ImageView) findViewById(R.id.incomingImg);
        commingName.setText(item.name);
        if (item.imagePath!= null && !item.imagePath.isEmpty()){
            ContactManger.getInstance().setImageView(imgView, item.imagePath);
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (sensor == null){

        }
        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_PROXIMITY){
                    Log.e(TAG, String.valueOf(event.values[0]));
                    if (event.values[0] == 0){
                        Log.e(TAG, "Answer by sensor");
                        answerPhoneCall(null);
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, sensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    public void answerPhoneCall(View view){
        final Context ctx = this;
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Runtime.getRuntime().exec("input keyevent " +
                            Integer.toString(KeyEvent.KEYCODE_HEADSETHOOK));
                } catch (IOException e) {
                    // Runtime.exec(String) had an I/O problem, try to fall back
                    String enforcedPerm = "android.permission.CALL_PRIVILEGED";
                    Intent btnDown = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                            Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN,
                                    KeyEvent.KEYCODE_HEADSETHOOK));
                    Intent btnUp = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                            Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP,
                                    KeyEvent.KEYCODE_HEADSETHOOK));

                    ctx.sendOrderedBroadcast(btnDown, enforcedPerm);
                    ctx.sendOrderedBroadcast(btnUp, enforcedPerm);
                }
            }

        }).start();
        this.finish();
    }

    public void denyPhoneCall(View view){
//        Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);
//        buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
//        this.getApplicationContext().sendOrderedBroadcast(buttonDown, "android.permission.CALL_PRIVILEGED");
//        Log.e(TAG, "Deny !");
//        this.finish();

        Context context = this.getApplicationContext();

        try {
            Object telephonyObject = getTelephonyObject(context);
            if (null != telephonyObject) {
                Class telephonyClass = telephonyObject.getClass();

                Method endCallMethod = telephonyClass.getMethod("endCall");
                endCallMethod.setAccessible(true);

                endCallMethod.invoke(telephonyObject);
                Log.e(TAG, "Deny Success!!!");
                finish();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    private static Object getTelephonyObject(Context context) {
        Object telephonyObject = null;
        try {
            // 初始化iTelephony
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            // Will be used to invoke hidden methods with reflection
            // Get the current object implementing ITelephony interface
            Class telManager = telephonyManager.getClass();
            Method getITelephony = telManager.getDeclaredMethod("getITelephony");
            getITelephony.setAccessible(true);
            telephonyObject = getITelephony.invoke(telephonyManager);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return telephonyObject;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] == 0){

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
