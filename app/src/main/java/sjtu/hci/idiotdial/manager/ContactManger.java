package sjtu.hci.idiotdial.manager;

import java.util.ArrayList;

/**
 * Created by Tian on 2015/6/13.
 */
public class ContactManger {
    private static ContactManger instance = null;
    private static final String CONTACT_KEY = "contact_key";
    private ArrayList<String> contactData;

    public void addContact(String name){

    }

    ArrayList<String> getContactData(){
        return this.contactData;
    }

    private void saveToPreference(){

    }

    private void loadFromPreference(){

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
