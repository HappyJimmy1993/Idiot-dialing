package sjtu.hci.idiotdial.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import sjtu.hci.idiotdial.activity.SettingContactActivity;
import sjtu.hci.idiotdial.adapter.ContactArrayAdapter;
import sjtu.hci.idiotdial.adapter.ContactArrayAdapter.ContactItem;

/**
 * Created by Tian on 2015/6/13.
 */
public class ContactManger {
    private static final String TAG = "Contact Manager" ;
    private static ContactManger instance = null;
    private static final String PREFERENCE= "IDIOTDIAL.CONTACTREFERENCE";
    private static final String CONTEXT_KEY = "contact_key";
    private SharedPreferences preferences;
    private ArrayList<ContactItem> contactData = new ArrayList<>();
    private boolean loaded = false;

    public void addContact(ContactItem contactItem, Context ctx){
        for (ContactItem item : this.contactData){
            if (item.name.equals(contactItem.name)){
                return;
            }
        }
        contactData.add(contactItem);
        saveToPreference(ctx);
    }

    public void toggleFavorite(String name, Context context) {
        for (ContactItem item : contactData){
            if (item.name.equals(name)){
                item.favorite = !item.favorite;
                Log.e(TAG, "Here!");
                Log.e(TAG, item.toString());
                break;
            }
        }
        saveToPreference(context);
    }

    public void updateImage(String name,String phone, String photoPath, Context ctx) {
        if (!loaded){
            loadFromPreference(ctx);
        }
        boolean alreadyHas = false;
        for (ContactItem item : this.contactData){
            if (item.name.equals(name)){
                item.imagePath = photoPath;
                alreadyHas = true;
                break;
            }
        }
        if (!alreadyHas){
            ContactItem item = new ContactItem(name, phone);
            item.imagePath = photoPath;
        }
        saveToPreference(ctx);
    }

    public void setImageView(ImageView imageView, String photoPath){
        if (photoPath == null || photoPath.isEmpty()){
            return;
        }
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();


        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    public String getPhone(String name, Context ctx){
        if (!loaded){
            loadFromPreference(ctx);
        }
        for (ContactItem item : this.contactData){
            if (item.name.equals(name)){
                return item.phone;
            }
        }
        return null;
    }

    public ArrayList<ContactItem> getFavoriteList(Context ctx) {
        if(!loaded){
            loadFromPreference(ctx);
        }
        ArrayList<ContactItem> favList = new ArrayList<>();
        for (ContactItem item : contactData){
            if (item.favorite){
                favList.add(item);
            }
        }
        return favList;
    }

    private void saveToPreference(Context ctx){
        try {
            preferences = ctx.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
            JSONArray jsonArray = new JSONArray();
            for (ContactItem item : this.contactData) {
                JSONObject obj = new JSONObject();
                obj.put("name", item.name);
                obj.put("phone", item.phone);
                obj.put("imagePath", item.imagePath);
                obj.put("favorite", item.favorite);
                jsonArray.put(obj);
            }
            String str = jsonArray.toString();
            preferences.edit().putString(CONTEXT_KEY, str).apply();
            loaded = true;
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void loadFromPreference(Context ctx){
        preferences = ctx.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        String savedStr = preferences.getString(CONTEXT_KEY, "");
        this.contactData.clear();
        try{
            JSONArray jsonArray = new JSONArray(savedStr);
            for (int i = 0; i < jsonArray.length(); ++i){
                JSONObject obj = jsonArray.getJSONObject(i);
                String name = obj.getString("name");
                String phone = obj.getString("phone");
                String imgPath = obj.getString("imagePath");
                boolean favorite = obj.getBoolean("favorite");
                ContactItem item = new ContactItem(name, phone);
                item.favorite = true;
                item.imagePath = imgPath;
                item.favorite = favorite;
                contactData.add(item);
                loaded = true;
            }
        }catch (JSONException e){

        }

    }

    public List<ContactItem> markFavorite(List<ContactItem> srcList, Context ctx){
        if (!loaded){
            loadFromPreference(ctx);
        }
        for (ContactItem item : srcList){
            for (ContactItem dataItem : this.contactData){
                if (dataItem.name.equals(item.name)){
                    item.favorite = dataItem.favorite;
                    item.imagePath = dataItem.imagePath;
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


    public ContactItem getItemByPhone(String phoneNum, Context applicationContext) {
        if (!loaded){
            loadFromPreference(applicationContext);
        }
        for (ContactItem item: this.contactData){
            if (item.phone.equals(phoneNum)){
                return item;
            }
        }
        return new ContactItem(phoneNum, phoneNum);
    }
}
