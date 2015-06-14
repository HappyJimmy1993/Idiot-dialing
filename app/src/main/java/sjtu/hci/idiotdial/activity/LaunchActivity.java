package sjtu.hci.idiotdial.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import sjtu.hci.idiotdial.manager.RecognizeManager;
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
    }

    public void seeContacts(View view){
//        Intent intent = new Intent();
//        intent.setClass(this, SettingChooseContactActivity.class);
//        startActivity(intent);
        String posted_by = "10086";

        String uri = "tel:" + posted_by.trim() ;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

}