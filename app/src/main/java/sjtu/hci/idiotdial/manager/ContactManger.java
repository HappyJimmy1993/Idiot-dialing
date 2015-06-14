package sjtu.hci.idiotdial.manager;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import sjtu.hci.idiotdial.adapter.ContactArrayAdapter;
import sjtu.hci.idiotdial.adapter.ContactArrayAdapter.ContactItem;

/**
 * Created by Tian on 2015/6/13.
 */
public class ContactManger {
    private static ContactManger instance = null;
    private static final String CONTACT_KEY = "contact_key";
    private SharedPreferences preferences;
    private ArrayList<ContactItem> contactData;

    public void addContact(ContactItem contactItem){
        contactItem.favorite = true;
        contactData.add(contactItem);
    }

    public ArrayList<ContactItem> getFavoriteList() {
        return this.contactData;
    }

    private void saveToPreference(){
        try {
            JSONArray jsonArray = new JSONArray();
            for (ContactItem item : this.contactData) {
                JSONObject obj = new JSONObject();
                obj.put("name", item.name);
                obj.put("phone", item.phone);
                jsonArray.put(obj);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void loadFromPreference(){

    }

    public List<ContactItem> markFavorite(List<ContactItem> srcList){
        for (ContactItem item : srcList){
            for (ContactItem dataItem : this.contactData){
                if (dataItem.name.equals(item.name)){
                    item.favorite = true;
                    dataItem.phone = item.phone;
                }
            }
        }
        return srcList;
    }

    public static ContactManger getInstance(){
        if(instance == null){
            instance = new ContactManger();
        }
        return instance;
    }

    private ContactManger(){

    }

}
