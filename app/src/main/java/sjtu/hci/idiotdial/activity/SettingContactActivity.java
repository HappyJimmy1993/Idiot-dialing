package sjtu.hci.idiotdial.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import sjtu.hci.idiotdial.R;
import sjtu.hci.idiotdial.manager.AudioManager;
import sjtu.hci.idiotdial.manager.ContactManger;

import static sjtu.hci.idiotdial.adapter.ContactArrayAdapter.*;

/**
 * Created by Edward on 2015/6/10.
 */
public class SettingContactActivity extends Activity {
    private static final String TAG = "SettingContactActivity";

    private String name;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_contact);
        Intent intent = getIntent();
        name = intent.getStringExtra(SettingChooseContactActivity.CONTACT_NAME);
        phone = intent.getStringExtra(SettingChooseContactActivity.CONTACT_PHONE);
        TextView nameText = (TextView) findViewById(R.id.settingContactName);
        TextView phoneText = (TextView) findViewById(R.id.settingContactPhone);
        nameText.setText(name);
        phoneText.setText(phone);
    }

    public void startRecord(View view){
        Log.e(TAG, "start..");
        AudioManager.getInstance().startRecord(getApplicationContext());
    }

    public void stopRecord(View view){
        Log.e(TAG, "end...");
        AudioManager.getInstance().stopToTrain(this.name);
    }

    public void markAsFavorite(){
        ContactManger.getInstance().addContact(new ContactItem(this.name, this.phone));
    }

}
