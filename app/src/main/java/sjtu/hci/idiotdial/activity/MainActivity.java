package sjtu.hci.idiotdial.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import sjtu.hci.idiotdial.service.LockScreenService;
import sjtu.hci.idiotdial.R;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private String __packageName;
    private String __name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");

        // LockScreenActivity��ΪLauncher���Ѿ�������service
        Intent serviceIntent = new Intent();
        serviceIntent.setClass(this, LockScreenService.class);
        startService(serviceIntent);

        getLauncherPackageName(this);
        checkLauncher(this);

        // ����ǹر�����ҳ��״̬�°�Home��������ϵͳ��Home�����������Home���������Ч
        if (!LockScreenActivity.isStarted) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(__packageName, __name));
            startActivity(intent);
        }

		/*
		 * Move the task containing this activity to the back of the activity
		 * stack. The activity's order within the task is unchanged. If false
		 * then this only works if the activity is the root of a task; if true
		 * it will work for any ��Activity������android:launchMode="singleInstance"
		 */
        moveTaskToBack(false);// MainActivity��ΪHomeҳ�������õ����ȥ�����򸲸�������ҳ�ϱ�
    }

    private void getLauncherPackageName(Context context){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> res = context.getPackageManager()
                .queryIntentActivities(intent, PackageManager.GET_ACTIVITIES);
        if (res != null) {
            for (int i = 0; i < res.size(); i++) {
                ResolveInfo r = res.get(i);
                if ((r.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                    Log.e(TAG, "ϵͳHome-packageName:" + r.activityInfo.packageName);
                    Log.e(TAG, "ϵͳHome-Name:" + r.activityInfo.name);

                    __packageName = r.activityInfo.packageName;
                    __name = r.activityInfo.name;
                    break;
                }
            }
        }
    }

    private void checkLauncher(Context context){
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(
                intent, 0);
        if (res.activityInfo == null) {
            return;
        }
        if (res.activityInfo.packageName.equals("android")) {
            return;
        } else {
            return;
        }
    }
}
