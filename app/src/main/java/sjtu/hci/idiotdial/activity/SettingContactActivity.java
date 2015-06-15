package sjtu.hci.idiotdial.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
    private String contactImagePath;
    private Boolean isFavorite;
    private Uri outputFileUri;
    private ImageView imageView;
    private ImageView favoriteImgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_contact);
        favoriteImgView = (ImageView) findViewById(R.id.addingImage);
        Intent intent = getIntent();
        name = intent.getStringExtra(SettingChooseContactActivity.CONTACT_NAME);
        phone = intent.getStringExtra(SettingChooseContactActivity.CONTACT_PHONE);
        // Add Contact can be re-enterred
        ContactManger.getInstance().addContact(new ContactItem(this.name, this.phone), this);

        isFavorite = intent.getBooleanExtra(SettingChooseContactActivity.CONTACT_FAVORITE, false);
        contactImagePath = intent.getStringExtra(SettingChooseContactActivity.CONTACT_IMG);

        TextView nameText = (TextView) findViewById(R.id.settingContactName);
        TextView phoneText = (TextView) findViewById(R.id.settingContactPhone);
        imageView = (ImageView) findViewById(R.id.settingContactImage);
        nameText.setText(name);
        phoneText.setText(phone);

        ImageButton recordButton = (ImageButton) findViewById(R.id.settingStartRecording);
        recordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                        stopRecord();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        stopRecord();
                        break;
                }
                return true;
            }
        });

        if (isFavorite){
            favoriteImgView.setImageResource(R.drawable.heart1);
        } else{
            favoriteImgView.setImageResource(R.drawable.heart2);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (contactImagePath != null && !contactImagePath.isEmpty()){
            ContactManger.getInstance().setImageView(imageView, contactImagePath);
        }
    }

    public void startRecord(){
        Log.e(TAG, "start..");
        AudioManager.getInstance().startRecord(this);
    }

    public void stopRecord(){
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
                        isCamera = action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }
                Uri selectedImageUri;
                if (!isCamera) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        try {
                            OutputStream out = new FileOutputStream(outputFileUri.getPath());
                            byte[] buf = new byte[1024];
                            int len;
                            while((len=inputStream.read(buf))>0){
                                out.write(buf,0,len);
                            }
                            out.close();
                            inputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }catch (FileNotFoundException e){
                        Log.e(TAG, "Not FOUND!!");
                    }
                }
                selectedImageUri = outputFileUri;
                setPic(selectedImageUri.getPath());
            }
        }
    }

    private void setPic(String photoPath) {
        ContactManger.getInstance().updateImage(name, phone, photoPath, this);
        ContactManger.getInstance().setImageView(imageView, photoPath);
    }


    public void toggleFavorite(View view){
        ContactManger.getInstance().toggleFavorite(name, this);
        isFavorite = !isFavorite;
        if (isFavorite){
            favoriteImgView.setImageResource(R.drawable.heart1);
        } else{
            favoriteImgView.setImageResource(R.drawable.heart2);
        }
    }

}
