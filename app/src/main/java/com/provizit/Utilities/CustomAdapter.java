package com.provizit.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.provizit.R;
import com.provizit.Services.DataManger;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
public class CustomAdapter extends ArrayAdapter<CompanyData> {
    Context context;
    int resource, textViewResourceId;
    List<CompanyData> items, tempItems, suggestions;
    int status;
    String comp_id;
    boolean isMeetingroom;
    DatabaseHelper myDb;
    public CustomAdapter(Context context, int resource, int textViewResourceId, List<CompanyData> items, int status, String Comp_id, boolean... isMeetingroom) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.status = status;
        this.resource = resource;
        this.comp_id = Comp_id;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        if(isMeetingroom != null && isMeetingroom.length != 0){
            this.isMeetingroom = isMeetingroom[0];
        }
        tempItems = new ArrayList<>(items); // this makes the difference.
        suggestions = new ArrayList<>();

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row, parent, false);
        }
        CompanyData people = items.get(position);

        if (people != null) {
            RelativeLayout relative_ = (RelativeLayout) view.findViewById(R.id.relative_);
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            CircleImageView emp_img = (CircleImageView) view.findViewById(R.id.emp_img);
            if (lblName != null) {

//                lblName.setText(people.getName());
                if(isMeetingroom){
                    myDb = new DatabaseHelper(context);
                    EmpData empData = new EmpData();
                    empData = myDb.getEmpdata();
                    if(!people.getName().equals(empData.getName() + " Cabin")){
                        String str1 = " ("+people.getCapacity() + " Persons)";
                        if (people.getActive()==true){
                            lblName.setText(people.getName() + str1);
                        }
                    }
                }else {
                    lblName.setText(people.getName());
                }
                if (status == 1) {
                    emp_img.setVisibility(View.VISIBLE);
                    emp_img.setImageResource(R.drawable.ic_user);
                    if (people.getPic() != null && people.getPic().size() != 0) {
                        Glide.with(context).load(DataManger.IMAGE_URL + "/uploads/" + comp_id + "/" + people.getPic().get(0))
                                .into(emp_img);
                    }
                    lblName.setText(people.getName() + "(" + people.getEmail() + ")");
                }

            }

        }

        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((CompanyData) resultValue).getName()  ;
            if (status == 1){
                return "";
            }
            else{
                return str;
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (CompanyData people : tempItems) {
                    if (people.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                suggestions.clear();
                suggestions.addAll(tempItems);
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<CompanyData> filterList = (ArrayList<CompanyData>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (CompanyData people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}