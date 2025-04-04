package com.provizit.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.provizit.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends ArrayAdapter<ContactModel> {

    Context context;
    int resource, textViewResourceId;
    List<ContactModel> items, tempItems, suggestions;

    public ContactsAdapter(Context context, int resource, int textViewResourceId, List<ContactModel> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<ContactModel>(items); // this makes the difference.
        suggestions = new ArrayList<ContactModel>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custcontview, parent, false);
        }
        ContactModel people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.ccontName);
            TextView lblNumber = (TextView) view.findViewById(R.id.ccontNo);
            if (lblName != null)
                lblName.setText(people.getName());
            lblNumber.setText(people.getMobileNumber());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((ContactModel) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (ContactModel people : tempItems) {
                    if (people.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<ContactModel> filterList = (ArrayList<ContactModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (ContactModel people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}