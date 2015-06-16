package sjtu.hci.idiotdial.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import java.util.ArrayList;
import java.util.Date;

import sjtu.hci.idiotdial.R;
import sjtu.hci.idiotdial.adapter.ContactArrayAdapter;
import sjtu.hci.idiotdial.manager.AudioManager;
import sjtu.hci.idiotdial.manager.ContactManger;
import sjtu.hci.idiotdial.service.LockScreenService;

/**
 * Created by Tian on 2015/5/30.
 */
public class LockScreenActivity extends Activity {

    private static final String TAG = "LockScreenActivity";
    private ArrayList<ContactArrayAdapter.ContactItem> contacts;
    private int currentIndex;
    private ImageView imgView;
    private TextView nameTextView;


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
        imgView = (ImageView) findViewById(R.id.contactImg);
        final TextView hintText = (TextView) findViewById(R.id.lockScreenSettingText);
        final ImageView pressToSay = (ImageView) findViewById(R.id.pressToSay);
        nameTextView = (TextView) findViewById(R.id.contactName);
        pressToSay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startSayName();
                        pressToSay.setImageResource(R.drawable.newmicbutton2);
                        hintText.setText(R.string.pressing_button);
                        break;
                    case MotionEvent.ACTION_UP:
                        pressToSay.setImageResource(R.drawable.newmicbutton1);
                        hintText.setText(R.string.default_button);
                        stopSayName();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        stopSayName();
                        break;
                }
                return true;
            }
        });
        contacts = ContactManger.getInstance().getFavoriteList(this);
        currentIndex = 0;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        updateCurrentContact();
    }

    public void unlock(View view){
        vibrate();
        finish();
    }

    public void turnLeft(View view){
        currentIndex++;
        if (currentIndex >= this.contacts.size()){
            currentIndex = 0;
        }
        updateCurrentContact();
    }

    public void turnRight(View view){
        currentIndex--;
        if (currentIndex< 0){
            currentIndex = this.contacts.size() - 1;
        }
        updateCurrentContact();
    }

    public void callCurrentFavorite(View view){
        ContactArrayAdapter.ContactItem item = this.contacts.get(currentIndex);

        startDial(item.phone);
    }

    public void startSayName(){
        Log.e(TAG, "Start Say Name");
        AudioManager.getInstance().startRecord(getApplicationContext());
    }

    public void stopSayName(){
        Log.e(TAG, "Stop Say Name");
        String name = AudioManager.getInstance().stopToGetName();
        Log.e(TAG, name);
        String phone = ContactManger.getInstance().getPhone(name, this);
        if (phone != null){
            startDial(phone);
        }
    }

    private void startDial(String phone) {
        String uri = "tel:" + phone.trim() ;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    private void updateCurrentContact(){
        ContactArrayAdapter.ContactItem currentItem = this.contacts.get(currentIndex);
        nameTextView.setText(currentItem.name);
        if (currentItem.imagePath != null && !currentItem.imagePath.isEmpty())
        {
            ContactManger.getInstance().setImageView(imgView, currentItem.imagePath);
        }else{
            imgView.setImageResource(R.drawable.maleimage);
        }

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
