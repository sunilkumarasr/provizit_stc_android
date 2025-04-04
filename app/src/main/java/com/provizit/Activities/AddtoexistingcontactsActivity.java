package com.provizit.Activities;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.provizit.AdapterAndModel.AddtoexistingcontactsAdapter;
import com.provizit.AdapterAndModel.ContactsList;
import com.provizit.Conversions;
import com.provizit.CustomItemListener;
import com.provizit.Config.ConnectionReceiver;
import com.provizit.R;
import com.provizit.databinding.ActivityAddtoexistingcontactsBinding;
import com.provizit.databinding.ActivityInitialBinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AddtoexistingcontactsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddtoexistingcontactsAc";


    ActivityAddtoexistingcontactsBinding binding;
    BroadcastReceiver broadcastReceiver;

    List<ContactsList> contactsLists;
    AddtoexistingcontactsAdapter contactsAdapter;

    String type = "";
    String values_ = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddtoexistingcontactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(AddtoexistingcontactsActivity.this,R.color.white));
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            type =(String) b.get("type");
            values_ =(String) b.get("number");
        }


        if (ContextCompat.checkSelfPermission(AddtoexistingcontactsActivity.this, Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddtoexistingcontactsActivity.this, new String[]{Manifest.permission.WRITE_CONTACTS},
                    200);
        }



        //internet connection
        broadcastReceiver = new ConnectionReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isConnecteds(context)){
                    binding.relativeInternet.getRoot().setVisibility(GONE);
                    binding.relativeUi.setVisibility(View.VISIBLE);
                }else {
                    binding.relativeInternet.getRoot().setVisibility(View.VISIBLE);
                    binding.relativeUi.setVisibility(GONE);
                }
            }
        };
        registoreNetWorkBroadcast();


        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        contactsLists = new ArrayList<>();

        getAllContacts();
        contactsAdapter = new AddtoexistingcontactsAdapter(getApplicationContext(), contactsLists, new CustomItemListener() {
            @Override
            public void onItemClick(View v, String position, String id) {
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);

                if (type.equalsIgnoreCase("email")){
                    ContentResolver resolver = getContentResolver();
                    ContentValues values = new ContentValues();
                    values.put(ContactsContract.Data.RAW_CONTACT_ID,  Integer.parseInt(id));
                    values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
                    values.put(ContactsContract.CommonDataKinds.Email.ADDRESS, values_);
                    resolver.insert(ContactsContract.Data.CONTENT_URI, values);
                    Toast.makeText(getApplicationContext(),"Contact Successfully Added",Toast.LENGTH_SHORT).show();
                    SetupMeetingActivity.name.setText("");
                    finish();
                }else {
                    ContentValues values = new ContentValues();
                    values.put(ContactsContract.Data.RAW_CONTACT_ID, Integer.parseInt(id));
                    values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                    values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, values_);
                    values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                    getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
                    Toast.makeText(getApplicationContext(),"Contact Successfully Added",Toast.LENGTH_SHORT).show();
                    SetupMeetingActivity.name.setText("");
                    finish();
                }


            }
        });
        binding.recyclerview.setAdapter(contactsAdapter);
        contactsAdapter.notifyDataSetChanged();

        binding.editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
//                filter(text.toString());
                contactsAdapter.filter(text.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });


        binding.imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.img_back:
               AnimationSet animation = Conversions.animation();
               v.startAnimation(animation);
               finish();
               break;
       }
    }

    protected void registoreNetWorkBroadcast(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    private void getAllContacts() {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String photo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                String phoneNumValue = "";
                String emailAddress = "";
                int phoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                LinkedList<String> listOfPhoneNumbers = new LinkedList<>();
                if (phoneNumber > 0) {
                    Cursor cursorPhone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            new String[]{id},
                            null
                    );

                    if (cursorPhone.getCount() > 0) {
                        while (cursorPhone.moveToNext()) {
                            phoneNumValue = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            listOfPhoneNumbers.add(phoneNumValue.replace(" ", ""));
                        }
                    }
                    cursorPhone.close();
                }

                LinkedList<String> listOfEmailAddress = new LinkedList<>();
                Cursor cursorEmail = contentResolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);

                if (cursorEmail.getCount() > 0) {
                    while (cursorEmail.moveToNext()) {
                        emailAddress = cursorEmail.getString(cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        listOfEmailAddress.add(emailAddress.replace(" ", ""));
                    }
                }
                cursorEmail.close();

                if (listOfPhoneNumbers.size() != 0) {
                    phoneNumValue = listOfPhoneNumbers.get(0).replace(" ", "");
                }
                if(listOfEmailAddress.size() != 0){
                    emailAddress = listOfEmailAddress.get(0).replace(" ", "");
                }
                ContactsList contactsLi = new ContactsList(false,id,name, phoneNumValue, emailAddress, photo);
                contactsLi.setListOfPhoneNumbers(listOfPhoneNumbers);
                contactsLi.setListOfEmailAddress(listOfEmailAddress);
                contactsLists.add(contactsLi);
            }
        }
        cursor.close();
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


}