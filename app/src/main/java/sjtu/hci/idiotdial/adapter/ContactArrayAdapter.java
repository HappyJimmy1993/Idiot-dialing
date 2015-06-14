package sjtu.hci.idiotdial.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import sjtu.hci.idiotdial.R;

/**
 * Created by Tian on 2015/6/14.
 */
public class ContactArrayAdapter extends ArrayAdapter<ContactArrayAdapter.ContactItem> {

    private final Context context;

    public static class ContactItem{
        public String name;
        public String phone;
        public boolean favorite;
        public ContactItem(String name, String phone){
            this.name = name;
            this.phone = phone;
            this.favorite = false;
        }
    }
    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView contactHeader;
        TextView contactInfo;
    }


    public ContactArrayAdapter(Context context, List<ContactItem> objects) {
        super(context, R.layout.list_contact, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ContactItem item = getItem(position);
        View rowView = inflater.inflate(R.layout.list_contact, parent, false);
        TextView header = (TextView) rowView.findViewById(R.id.contactDetailHeader);
        TextView info = (TextView) rowView.findViewById(R.id.contactDetailItem);
        ImageView img = (ImageView) rowView.findViewById(R.id.buttonAsFavorite);
        return rowView;
    }
}
