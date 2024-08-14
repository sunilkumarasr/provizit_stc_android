package com.provizit.AdapterAndModel;

import static com.provizit.FragmentDailouge.SetUpMeetingContactListFragment.isPhoneNumberValid;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.provizit.Activities.SetupMeetingActivity;
import com.provizit.CustomItemListener;
import com.provizit.R;
import com.provizit.Utilities.Invited;
import com.provizit.interfaces.MultipleContactsSelectionCallBack;
import com.provizit.interfaces.MultipleEmailAddressSelectionCallBack;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ContactViewHolder>{
    private static final String TAG = "ContactsListAdapter";

    private CustomItemListener customItemClick;
    Context context;
    List<ContactsList> contactsLists;
    ContactsList deliveryEarningListModel;

    private AlertDialog alertDialog;

    private String selectedPhoneNumber = "";

    private String checked_status = "";

    private String selectedEmailAddress = "";

    public ContactsListAdapter(Context context, List<ContactsList> contactsLists, CustomItemListener customItemClick) {
        this.context = context;
        this.contactsLists = contactsLists;
        this.customItemClick = customItemClick;
    }

    @NonNull
    @Override
    public ContactsListAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contacts_list_items, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsListAdapter.ContactViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ContactsList contactsL = contactsLists.get(position);
        deliveryEarningListModel = contactsLists.get(position);
        holder.txt_name.setText("Name: " + contactsL.getName());
        holder.txt_phone.setText("Mobile: " + contactsL.getPhone());
        holder.txt_email.setText("Email: " + contactsL.getEmail());



        if (deliveryEarningListModel.getPhone().equalsIgnoreCase("") && deliveryEarningListModel.getEmail().equalsIgnoreCase("")){
            holder.itemView.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            params.height = 0;
            params.width = 0;
            holder.itemView.setLayoutParams(params);
        }else if (deliveryEarningListModel.getPhone().equalsIgnoreCase("")){
            holder.txt_phone.setVisibility(View.GONE);
        }else if (deliveryEarningListModel.getEmail().equalsIgnoreCase("")){
            holder.txt_email.setVisibility(View.GONE);
        }else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.txt_phone.setVisibility(View.VISIBLE);
            holder.txt_email.setVisibility(View.VISIBLE);
        }


        ArrayList<Invited> invitedArrayList = SetupMeetingActivity.invitedArrayList;
        boolean isChecked = false;
        for (int index = 0; index < invitedArrayList.size(); index++) {
            Invited invited = invitedArrayList.get(index);
            String id = invited.getId();
            String email = invited.getEmail();
            String Mobile = invited.getMobile();
            if (id.equalsIgnoreCase(contactsL.getId())) {
                isChecked = true;
            }
        }
        if (isChecked) {
            holder.checkbox.setChecked(true);
        } else {
            holder.checkbox.setChecked(false);
        }


        if (contactsL.getPhote() != null) {
            Glide.with(context).load(contactsL.getPhote()).into(holder.img_profile);
        } else {
            holder.img_profile.setImageResource(R.drawable.user);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.checkbox.isChecked()) {
                    holder.checkbox.setChecked(false);
                } else {
                    holder.checkbox.setChecked(true);
                }

                ContactsList selectedContactList = contactsLists.get(position);

                if (holder.checkbox.isChecked()){
                    LinkedList<String> listOfPhoneNumbers = selectedContactList.getListOfPhoneNumbers();
                    if (listOfPhoneNumbers != null) {

                        if (selectedContactList.getPhone().length() >= 10){

                            if (listOfPhoneNumbers.size() != 0) {
                                Log.e(TAG, "onCheckedChanged:da" + selectedContactList.getEmail());
                                Log.e(TAG, "onCheckedChanged:daa" + selectedContactList.getPhone());

                                //numbers
                                HashSet<String> setnumbers = new HashSet<>();
                                for (String ss : listOfPhoneNumbers) {
                                    setnumbers.add(ss);
                                }
                                listOfPhoneNumbers.clear();
                                listOfPhoneNumbers.addAll(setnumbers);
                                //remove duplicate numbers items in list
                                for (int j = 0; j<= listOfPhoneNumbers.size(); j++){
                                    HashSet<String> uniqueNumbers = new HashSet<>(listOfPhoneNumbers);
                                    listOfPhoneNumbers.clear();
                                    listOfPhoneNumbers.addAll(uniqueNumbers);
                                }

                                //emails
                                LinkedList<String> EmailAddresslist = selectedContactList.getListOfEmailAddress();
                                HashSet<String> setemails = new HashSet<>();
                                for (String emails : EmailAddresslist) {
                                    setemails.add(emails);
                                }
                                EmailAddresslist.clear();
                                EmailAddresslist.addAll(setemails);
                                //remove duplicate Emails items in list
                                for (int j = 0; j<= EmailAddresslist.size(); j++){
                                    HashSet<String> uniqueemail = new HashSet<>(EmailAddresslist);
                                    EmailAddresslist.clear();
                                    EmailAddresslist.addAll(uniqueemail);
                                }


                                if (listOfPhoneNumbers.size() == 1) {

                                    String self_result = SetupMeetingActivity.empData.getMobile().replaceAll("[-+.^:,]","");
                                    String c_result = selectedContactList.getPhone().replaceAll("[-+.^:,]","");

                                    //self meeting check
                                    if (SetupMeetingActivity.empData.getEmail().equalsIgnoreCase(selectedContactList.getEmail()) || self_result.equalsIgnoreCase(c_result)){
                                        holder.checkbox.setChecked(false);
                                        holder.checkbox.setSelected(false);
                                        Toast.makeText(context, "Self Account Not Acceptable", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Invited invited = new Invited();
                                        invited.setId(selectedContactList.getId());
                                        invited.setName(selectedContactList.getName());
                                        invited.setEmail(selectedContactList.getEmail());
                                        invited.setId(selectedContactList.getId());

                                        if (selectedContactList.getPhone().equalsIgnoreCase("")) {

                                        }else {
                                            String[] phonenumbers = {selectedContactList.getPhone()};
                                            for (String phone : phonenumbers) {

                                                //old_code
                                                if (isPhoneNumberValid(phone)) {
                                                    String l_mobile = selectedContactList.getPhone().replace(" ", "");
                                                    if (l_mobile.length() == 10) {
                                                        invited.setMobile("+91" + selectedContactList.getPhone());
                                                    } else if (l_mobile.length() == 14) {
                                                        String s = "";
                                                        if (l_mobile.length() > 2) {
                                                            s = l_mobile.substring(0, 2);
                                                        } else {
                                                            s = l_mobile;
                                                        }
                                                        if (s.equalsIgnoreCase("00")) {
                                                            invited.setMobile(l_mobile.replaceFirst("00", "+"));
                                                        } else {
                                                            invited.setMobile(selectedContactList.getPhone());
                                                        }
                                                    } else {
                                                        invited.setMobile(selectedContactList.getPhone());
                                                    }
                                                } else {
                                                    String l_mobile = selectedContactList.getPhone().replace(" ", "");
                                                    if (l_mobile.length() == 15) {
                                                        String s = "";
                                                        if (l_mobile.length() > 1) {
                                                            s = l_mobile.substring(0, 1);
                                                        } else {
                                                            s = l_mobile;
                                                        }
                                                        if (s.equalsIgnoreCase("+")) {
                                                            String[] arrOfStr = selectedContactList.getPhone().split("[+]");
                                                            for (String a : arrOfStr) {
                                                                invited.setMobile(a.replaceFirst("00", "+"));
                                                            }
                                                        } else {
                                                            Toast.makeText(context, "Mobile Number not valid", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else if (l_mobile.length() == 12) {
                                                        String s = "";
                                                        if (l_mobile.length() > 2) {
                                                            s = l_mobile.substring(0, 2);
                                                        } else {
                                                            s = l_mobile;
                                                        }
                                                        if (s.equalsIgnoreCase("00")) {
                                                            invited.setMobile(l_mobile.replaceFirst("00", "+91"));
                                                        } else {
                                                            invited.setMobile(selectedContactList.getPhone());
                                                        }
                                                    } else {
                                                        Toast.makeText(context, "Mobile Number not valid", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                            }
                                        }

                                        invited.setCompany("");
                                        invited.setLink("");
                                        SetupMeetingActivity.invitedArrayList.add(invited);
                                        //addguest
                                        SetupMeetingActivity.AddGuestinvitedArrayList.add(invited);

                                        SetupMeetingActivity.iEmail.add(selectedContactList.getPhone());
                                        SetupMeetingActivity.img_gmails_add.setVisibility(View.GONE);
                                        SetupMeetingActivity.relative_emails_list_open_eye.setVisibility(View.VISIBLE);
                                        SetupMeetingActivity.txt_list_count.setText(SetupMeetingActivity.invitedArrayList.size() + "");
                                        customItemClick.onItemClick(v, contactsLists.get(position) + "", selectedContactList.getId());
                                    }

                                } else {
                                   // showMultipleContactsDialog(selectedContactList,listOfPhoneNumbers,EmailAddresslist, v, contactsLists.get(position), holder.checkbox);
                                }


                            } else {
                                if (selectedContactList.getEmail().equalsIgnoreCase("")) {
                                    holder.checkbox.setChecked(false);
                                    holder.checkbox.setSelected(false);
                                    Toast.makeText(context, "No Address Found", Toast.LENGTH_SHORT).show();
                                } else {

                                    //self meeting check
                                    if (SetupMeetingActivity.empData.getEmail().equalsIgnoreCase(selectedContactList.getEmail())){
                                        holder.checkbox.setChecked(false);
                                        holder.checkbox.setSelected(false);
                                        Toast.makeText(context, "Self Account Not Acceptable", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Invited invited = new Invited();
                                        invited.setId(selectedContactList.getId());
                                        invited.setName(selectedContactList.getName());
                                        invited.setEmail(selectedContactList.getEmail());
                                        invited.setCompany("");
                                        invited.setLink("");
                                        SetupMeetingActivity.invitedArrayList.add(invited);
                                        //addguest
                                        SetupMeetingActivity.AddGuestinvitedArrayList.add(invited);
                                        SetupMeetingActivity.iEmail.add(selectedContactList.getPhone());
                                        SetupMeetingActivity.img_gmails_add.setVisibility(View.GONE);
                                        SetupMeetingActivity.relative_emails_list_open_eye.setVisibility(View.VISIBLE);
                                        SetupMeetingActivity.txt_list_count.setText(SetupMeetingActivity.invitedArrayList.size() + "");
                                        customItemClick.onItemClick(v, contactsLists.get(position) + "", selectedContactList.getId());
                                    }

                                }
                            }

                        }else if (selectedContactList.getEmail() != null && !selectedContactList.getEmail().equals("")){
                            //self meeting check
                            if (SetupMeetingActivity.empData.getEmail().equalsIgnoreCase(selectedContactList.getEmail())){
                                holder.checkbox.setChecked(false);
                                holder.checkbox.setSelected(false);
                                Toast.makeText(context, "Self Account Not Acceptable", Toast.LENGTH_SHORT).show();
                            }else {
                                Invited invited = new Invited();
                                invited.setId(selectedContactList.getId());
                                invited.setName(selectedContactList.getName());
                                invited.setChecked(true);
                                invited.setEmail(selectedContactList.getEmail());
                                invited.setMobile("");
                                invited.setCompany("");
                                invited.setLink("");
                                SetupMeetingActivity.invitedArrayList.add(invited);
                                //addguest
                                SetupMeetingActivity.AddGuestinvitedArrayList.add(invited);
                                SetupMeetingActivity.iEmail.add(selectedContactList.getPhone());
                                SetupMeetingActivity.img_gmails_add.setVisibility(View.GONE);
                                SetupMeetingActivity.relative_emails_list_open_eye.setVisibility(View.VISIBLE);
                                SetupMeetingActivity.txt_list_count.setText(SetupMeetingActivity.invitedArrayList.size() + "");
                                customItemClick.onItemClick(v, contactsLists.get(position)  + "", selectedContactList.getId());
                            }

                        }else {
                            holder.checkbox.setChecked(false);
                            holder.checkbox.setSelected(false);
                        }

                    } else {
                        Log.e(TAG, "onCheckedChanged:daerror2" + selectedContactList.getEmail());
                    }
                }else {

                    if (SetupMeetingActivity.invitedArrayList != null) {
                        if (SetupMeetingActivity.invitedArrayList.size() != 0) {

                                for (int index = 0; index < SetupMeetingActivity.invitedArrayList.size(); index++) {

                                if (selectedContactList.getId().equalsIgnoreCase(SetupMeetingActivity.invitedArrayList.get(index).getId())){
                                    SetupMeetingActivity.invitedArrayList.remove(index);
                                    for (int ar = 0; ar < SetupMeetingActivity.AddGuestinvitedArrayList.size(); ar++) {
                                        SetupMeetingActivity.AddGuestinvitedArrayList.remove(ar);
                                    }
                                    SetupMeetingActivity.iEmail.remove(index);
                                    SetupMeetingActivity.txt_list_count.setText(SetupMeetingActivity.invitedArrayList.size() + "");
                                    customItemClick.onItemClick(v, index + "", selectedContactList.getId());
                                }

                            }
                        }
                    }
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return contactsLists.size();
    }


    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout card_v;
        CircleImageView img_profile;
        TextView txt_name;
        TextView txt_phone;
        TextView txt_email;
        CheckBox checkbox;


        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            card_v = itemView.findViewById(R.id.card_v);
            img_profile = itemView.findViewById(R.id.img_profile);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_phone = itemView.findViewById(R.id.txt_phone);
            txt_email = itemView.findViewById(R.id.txt_email);
            checkbox = itemView.findViewById(R.id.checkbox);

        }
    }

    public void updateList(ArrayList<ContactsList> list) {
        contactsLists = list;
        notifyDataSetChanged();
    }


}
