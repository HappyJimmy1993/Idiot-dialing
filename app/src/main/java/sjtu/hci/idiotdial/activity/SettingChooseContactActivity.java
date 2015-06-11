package sjtu.hci.idiotdial.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sjtu.hci.idiotdial.R;

/**
 * Created by Tian on 2015/6/8.
 */
public class SettingChooseContactActivity extends Activity {
    static private final String TAG = "SettingCCActivity";

    static public final String CONTACT_NAME = "Concact_name";
    static public final String CONTACT_PHONE = "Concact phone";

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_choose_contact);
        listView = (ListView) findViewById(R.id.contactList);

        // Defined Array values to show in ListView
        final List<ContactInfo> concactInfoList = fetchContacts();
        List<String> values = new ArrayList<>();
        for (ContactInfo info : concactInfoList){
            values.add(info.toString());
        }

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        listView.setAdapter(adapter);
        // ListView Item Click Listener
        final ContextWrapper ctx = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // ListView Clicked item value
                ContactInfo info = concactInfoList.get(position);
                Intent intent = new Intent(ctx, SettingContactActivity.class);
                intent.putExtra(CONTACT_NAME, info.name);
                intent.putExtra(CONTACT_PHONE, info.phoneNumber);
                startActivity(intent);
            }
        });
    }

    private static class ContactInfo{
        public String phoneNumber;
        public String email;
        public String name;
        public ContactInfo(String name, String phoneNumber, String email){
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.email = email;
        }

        @Override
        public String toString(){
            return "Name:" + name + "\nphone:" + phoneNumber + "\n";
        }
    }

    private List<ContactInfo> fetchContacts(){
        List<ContactInfo> contactList = new ArrayList<>();
        String phoneNumber = null;
        String email = null;
        ContactInfo contactInfo;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        String output = "";

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
                    // Query and loop for every email of the contact
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);
                    while (emailCursor.moveToNext()) {
                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                    }
                    emailCursor.close();
                }
                contactList.add(new ContactInfo(name, phoneNumber, email));
            }
        }
        return contactList;
    }
}
