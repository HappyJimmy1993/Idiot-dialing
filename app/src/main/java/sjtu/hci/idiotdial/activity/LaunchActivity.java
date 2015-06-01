package sjtu.hci.idiotdial.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import sjtu.hci.idiotdial.service.LockScreenService;
import sjtu.hci.idiotdial.R;

/**
 * Created by Tian on 2015/5/30.
 */
public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }

    public void startLockScreen(View v) {
        Intent intent = new Intent();
        intent.setClass(this, LockScreenService.class);
        startService(intent);
        Toast.makeText(this, "锁屏已启动，请关闭屏幕后再点亮测试", Toast.LENGTH_SHORT).show();
    }

}
