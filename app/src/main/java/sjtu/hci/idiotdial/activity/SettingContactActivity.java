package sjtu.hci.idiotdial.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import sjtu.hci.idiotdial.R;
import sjtu.hci.idiotdial.manager.AudioManager;
import sjtu.hci.idiotdial.manager.ContactManger;

import static sjtu.hci.idiotdial.adapter.ContactArrayAdapter.*;

/**
 * Created by Edward on 2015/6/10.
 */
public class SettingContactActivity extends Activity {
    private static final String TAG = "SettingContactActivity";
    private static final int SELECT_PICTURE_REQUEST_CODE = 0;

    private String name;
    private String phone;
    private Uri outputFileUri;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_contact);
        Intent intent = getIntent();
        name = intent.getStringExtra(SettingChooseContactActivity.CONTACT_NAME);
        phone = intent.getStringExtra(SettingChooseContactActivity.CONTACT_PHONE);
        TextView nameText = (TextView) findViewById(R.id.settingContactName);
        TextView phoneText = (TextView) findViewById(R.id.settingContactPhone);
        imageView = (ImageView) findViewById(R.id.settingContactImg);
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

    public void choosePhoto(View view){
        // Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        final String filename = name+phone;
        final File sdImageMainDirectory = new File(root, filename);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, SELECT_PICTURE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE_REQUEST_CODE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                Uri selectedImageUri;
                if (isCamera) {
                    selectedImageUri = outputFileUri;
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                }
                setPic(selectedImageUri.getPath());
            }
        }
    }

    private void setPic(String photoPath) {
        ContactManger.getInstance().updateImage(name, phone, photoPath, this);
        ContactManger.getInstance().setImageView(imageView, photoPath);
    }


    public void markAsFavorite(View view){
        ContactManger.getInstance().addContact(new ContactItem(this.name, this.phone), this);
    }

}
