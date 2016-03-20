package com.sdsmdg.harshit.draw.TeacherNotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sdsmdg.harshit.draw.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul Yadav on 3/17/2016.
 */
public class ContactAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public ContactAdapter(Context context, int resource) {
        super(context, resource);
    }


    public void add(Contacts object) {

        super.add(object);
        list.add(object);
    }
    public void clear()
    {
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row;
        row = convertView;
        ContactHolder contactHolder;

        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_layout, parent, false);
            contactHolder = new ContactHolder();
            contactHolder.nameDisp = (TextView) row.findViewById(R.id.tx_name);
            contactHolder.topicDisp = (TextView) row.findViewById(R.id.tx_topic);
            contactHolder.contentDisp = (TextView) row.findViewById(R.id.tx_content);
            row.setTag(contactHolder);

        } else {
            contactHolder = (ContactHolder) row.getTag();
        }

        Contacts contacts = (Contacts) this.getItem(position);
        contactHolder.nameDisp.setText(contacts.getName());
        contactHolder.topicDisp.setText(contacts.getTopic());
        contactHolder.contentDisp.setText(contacts.getContent());

        return row;
    }

    static class ContactHolder {
        TextView nameDisp, topicDisp, contentDisp;

    }
}
