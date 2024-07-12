package com.provizit.FragmentDailouge;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.provizit.Activities.SetupMeetingActivity;
import com.provizit.AdapterAndModel.ContactsListAdapter;
import com.provizit.CustomItemListener;
import com.provizit.Config.Preferences;
import com.provizit.R;
import com.provizit.AdapterAndModel.ContactsList;
import com.provizit.databinding.FragmentSetUpMeetingContactListBinding;
import com.provizit.databinding.FragmentSetUpMeetingMailsListBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetUpMeetingContactListFragment extends BottomSheetDialogFragment {
    private static final String TAG = "SetUpMeetingContactList";

    FragmentSetUpMeetingContactListBinding binding;

    private FragmentActivity myContext;

    public static List<ContactsList> contactsLists;

    ContactsList contactsLi;
    public static ContactsListAdapter contactsAdapter;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetSearchDialogTheme);
    }

    SetUpMeetingContactListFragment.ItemSelected listner;

    public void onItemsSelectedListner(SetUpMeetingContactListFragment.ItemSelected listner) {
        this.listner = listner;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetUpMeetingContactListBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();


        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));


        //contact list
        String contacts_list = Preferences.loadStringValue(getActivity(), Preferences.contacts_list, "");
        String contacts_contactsLi = Preferences.loadStringValue(getActivity(), Preferences.contacts_contactsLi, "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ContactsList>>() {}.getType();
        Type obtype = new TypeToken<ContactsList>() {}.getType();
        contactsLists = gson.fromJson(contacts_list, type);
        contactsLi = gson.fromJson(contacts_contactsLi, obtype);
        if (contactsLists == null){
            contactsLists = new ArrayList<>();
        }
        Set<ContactsList> set = new HashSet<>();
        set.addAll(contactsLists);
        contactsLists.clear();
        contactsLists.addAll(set);
        contactsLists.add(contactsLi);


        Collections.sort(contactsLists, new Comparator<ContactsList>(){
            public int compare(ContactsList d1, ContactsList d2){
                return d1.getName().compareTo(d2.getName());
            }
        });


        contactsAdapter = new ContactsListAdapter(getActivity(), contactsLists, (v, position, id) -> {
            if (SetupMeetingActivity.invitedArrayList.size() == 0 || SetupMeetingActivity.invitedArrayList.equals("null")) {
                binding.txtSelectedItems.setVisibility(View.GONE);
            } else {
                binding.txtSelectedItems.setVisibility(View.VISIBLE);
                //defalt selection
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < SetupMeetingActivity.invitedArrayList.size(); i++) {
                    result.append(SetupMeetingActivity.invitedArrayList.get(i).getName());
                    if (i < SetupMeetingActivity.invitedArrayList.size() - 1) {
                        result.append(", ");
                    }
                }
                String list = result.toString();
                binding.txtSelectedItems.setText(list + "");
            }
            //select all
            if (binding.editSearch.getText().toString().equalsIgnoreCase("")) {
            } else {
                binding.editSearch.requestFocus();
                binding.editSearch.selectAll();
            }
        });
        binding.recyclerview.setAdapter(contactsAdapter);
        contactsAdapter.notifyDataSetChanged();


        //search
        binding.editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                filter(text.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        if (SetupMeetingActivity.invitedArrayList.size() == 0 || SetupMeetingActivity.invitedArrayList.equals("null")) {
            binding.txtSelectedItems.setVisibility(View.GONE);
        }else {
            binding.txtSelectedItems.setVisibility(View.VISIBLE);
            //defalt selection
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < SetupMeetingActivity.invitedArrayList.size(); i++) {
                result.append(SetupMeetingActivity.invitedArrayList.get(i).getName());
                if (i < SetupMeetingActivity.invitedArrayList.size() - 1) {
                    result.append(", ");
                }
            }
            String list = result.toString();
            binding.txtSelectedItems.setText(list + "");
        }

        return rootView;
    }

    private void filter(String text) {
        ArrayList<ContactsList> temp = new ArrayList();
        for (ContactsList d : contactsLists) {
            if (d.getName().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        if (temp.size() == 0) {
//            Toast.makeText(getActivity(),"No Data",Toast.LENGTH_LONG).show();
        } else {
            //update recyclerview
            contactsAdapter.updateList(temp);
            contactsAdapter.notifyDataSetChanged();
        }
    }

    public static boolean isPhoneNumberValid(String phone) {
        try {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            phoneNumber = phoneUtil.parse(phone, "IN");
            System.out.println("\nType: " + phoneUtil.getNumberType(phoneNumber));
            Log.e(TAG, "isPhoneNumberValid:s: " + "\nType: " + phoneUtil.getNumberType(phoneNumber));
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return phoneUtil.isValidNumber(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public interface ItemSelected {
        void onSelected(String comId, String DepName, String CompName);
    }

}


