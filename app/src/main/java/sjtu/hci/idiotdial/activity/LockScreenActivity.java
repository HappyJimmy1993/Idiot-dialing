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

import sjtu.hci.idiotdial.R;
import sjtu.hci.idiotdial.service.LockScreenService;

/**
 * Created by Tian on 2015/5/30.
 */
public class LockScreenActivity extends Activity {

    private static final String TAG = "LockScreenActivity";
    private int windowWidth;
    private int windowHeight;
    private ImageView keyImageView;
    private ImageView lockerImageView;
    private TextView timeNow;
    private int locker_x;
    private int locker_y;
    private int[] keyPos;
    private RelativeLayout.LayoutParams layoutParams;
    /** 屏蔽4.0+home键, 某些机型可以，不能适用所有版本和机型 */
    // private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    /** 判断锁屏页面是否打开状态 */
    public static boolean isStarted = false;//

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // 屏蔽4.0+home键, 某些机型可以，不能适用所有版本和机型
        // this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED,FLAG_HOMEKEY_DISPATCHED);// 屏蔽4.0+ home键

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lockscreen);

        isStarted = true;
        keyImageView = (ImageView) findViewById(R.id.key);

        ImageView audioImg = (ImageView) findViewById(R.id.audio);

        audioImg.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startRecording();
                        break;
                    case MotionEvent.ACTION_UP:
                        stopRecording();
                        break;
                }
                return true;
            }
        });

        try {
            // initialize receiver
            startService(new Intent(this, LockScreenService.class));

            windowWidth = getWindowManager().getDefaultDisplay().getWidth();
            windowHeight = getWindowManager().getDefaultDisplay().getHeight();

            ViewGroup.MarginLayoutParams marginParams2 = new ViewGroup.MarginLayoutParams(
                    keyImageView.getLayoutParams());
            marginParams2.setMargins((windowWidth / 24) * 10,
                    ((windowHeight / 32) * 8), 0, 0);
            RelativeLayout.LayoutParams layoutdroid = new RelativeLayout.LayoutParams(
                    marginParams2);
            keyImageView.setLayoutParams(layoutdroid);

            LinearLayout homelinear = (LinearLayout) findViewById(R.id.homelinearlayout);
            homelinear.setPadding(0, 0, 0, (windowHeight / 32) * 3);
            lockerImageView = (ImageView) findViewById(R.id.locker);

            ViewGroup.MarginLayoutParams marginParams1 = new ViewGroup.MarginLayoutParams(
                    lockerImageView.getLayoutParams());
            marginParams1.setMargins((windowWidth / 24) * 5, 0,
                    (windowHeight / 32) * 8, 0);
            LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
                    marginParams1);
            lockerImageView.setLayoutParams(layout);

            // keyImageView图片的touch事件
            keyImageView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            int[] lockerPos = new int[2];
                            keyPos = new int[2];
                            lockerImageView.getLocationOnScreen(lockerPos);
                            locker_x = lockerPos[0];
                            locker_y = lockerPos[1];
                            break;
                        case MotionEvent.ACTION_MOVE:
                            int x_cord = (int) event.getRawX();
                            int y_cord = (int) event.getRawY();

                            if (x_cord > windowWidth - (windowWidth / 24)) {
                                x_cord = windowWidth - (windowWidth / 24) * 2;
                            }
                            if (y_cord > windowHeight - (windowHeight / 32)) {
                                y_cord = windowHeight - (windowHeight / 32) * 2;
                            }

                            layoutParams.leftMargin = x_cord;
                            layoutParams.topMargin = y_cord;

                            keyImageView.getLocationOnScreen(keyPos);
                            v.setLayoutParams(layoutParams);

                            // 拖动钥匙图片到小锁的位置
                            if (((x_cord - locker_x) <= (windowWidth / 24) * 5 && (locker_x - x_cord) <= (windowWidth / 24) * 4)
                                    && ((locker_y - y_cord) <= (windowHeight / 32) * 5)) {
                                v.setVisibility(View.GONE);
                                vibrate();// 震动
                                isStarted = false;
                                finish();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            int x_cord1 = (int) event.getRawX();
                            int y_cord2 = (int) event.getRawY();

                            if (((x_cord1 - locker_x) <= (windowWidth / 24) * 5 && (locker_x - x_cord1) <= (windowWidth / 24) * 4)
                                    && ((locker_y - y_cord2) <= (windowHeight / 32) * 5)) {
                            } else {
                                // 如果松开手时未重叠，则钥匙图片返回原位置
                                layoutParams.leftMargin = (windowWidth / 24) * 10;
                                layoutParams.topMargin = (windowHeight / 32) * 8;
                                v.setLayoutParams(layoutParams);
                            }
                    }

                    return true;
                }
            });

        } catch (Exception e) {
            // TODO: handle exception
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
            Log.e(TAG, "响应Home键");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onAttachedToWindow() {
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);// Android4.0以下屏蔽Home键
        super.onAttachedToWindow();
    }

    @Override
    public void onBackPressed() {
        Log.e(TAG, "响应Back键");
        return;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 震动
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    private void startRecording(){

    }

    private void stopRecording(){

    }

}
