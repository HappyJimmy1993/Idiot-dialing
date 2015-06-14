package sjtu.hci.idiotdial.activity;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import sjtu.hci.idiotdial.R;
import sjtu.hci.idiotdial.adapter.ContactArrayAdapter;
import sjtu.hci.idiotdial.adapter.ContactArrayAdapter.ContactItem;

/**
 * Created by Tian on 2015/6/8.
 */
public class SettingChooseContactActivity extends ListActivity {
    static private final String TAG = "SettingCCActivity";

    static public final String CONTACT_NAME = "Concact_name";
    static public final String CONTACT_PHONE = "Concact phone";

    ListView listView;
    private List<ContactItem> contactInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_choose_contact);
        listView = getListView();

        // Defined Array values to show in ListView
        contactInfoList = fetchContacts();
        ContactArrayAdapter adapter = new ContactArrayAdapter(this, contactInfoList);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Log.e(TAG, "Here!:"+ position);
        ContactItem info = contactInfoList.get(position);
        Intent intent = new Intent(this, SettingContactActivity.class);
        intent.putExtra(CONTACT_NAME, info.name);
        intent.putExtra(CONTACT_PHONE, info.phone);
        startActivity(intent);
    }

    private List<ContactItem> fetchContacts(){
        List<ContactItem> contactList = new ArrayList<>();
        String phoneNumber = null;
        String email = null;
        ContactItem contactItem;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);

        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                    }
                    phoneCursor.close();
                }
                contactList.add(new ContactItem(name, phoneNumber));
            }
        }
        return contactList;
    }
}
