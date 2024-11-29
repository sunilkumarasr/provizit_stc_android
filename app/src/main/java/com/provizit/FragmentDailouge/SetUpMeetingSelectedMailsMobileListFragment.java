package com.provizit.FragmentDailouge;

import static org.apache.poi.sl.draw.binding.STRectAlignment.BR;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.provizit.Activities.SetupMeetingActivity;
import com.provizit.R;
import com.provizit.Utilities.Invited;
import com.provizit.databinding.ActivityInitialBinding;
import com.provizit.databinding.EmailsSelectedListItemsBinding;
import com.provizit.databinding.FragmentSetUpMeetingMailsListBinding;

import java.util.ArrayList;
import java.util.Objects;


public class SetUpMeetingSelectedMailsMobileListFragment extends BottomSheetDialogFragment {
    private static final String TAG = "SetUpMeetingMailsListFr";

    FragmentSetUpMeetingMailsListBinding binding;

    private FragmentActivity myContext;

    ArrayList<Invited> imail_list;
    EmailAdapter emailAdapter;

    int RMS_STATUS =0;

    @Override
    public void onAttach(@NonNull Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style. BottomSheetSearchDialogTheme);
    }

    SetUpMeetingSelectedMailsMobileListFragment.ItemSelected listner;
    public void onItemsSelectedListner(SetUpMeetingSelectedMailsMobileListFragment.ItemSelected listner){
        this.listner = listner;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetUpMeetingMailsListBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        Bundle receivedBundle = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        if (receivedBundle != null) {
            RMS_STATUS = receivedBundle.getInt("RMS_STATUS");
        }


        imail_list = new ArrayList<>();

        if (SetupMeetingActivity.invitedArrayList.size()==0){
            binding.txtNoList.setVisibility(View.VISIBLE);
        }else {
            binding.txtNoList.setVisibility(View.GONE);
            for (int i = 0; i < SetupMeetingActivity.invitedArrayList.size(); i++) {
                imail_list.add(SetupMeetingActivity.invitedArrayList.get(i));
            }

            emailAdapter = new EmailAdapter(getActivity(), imail_list);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            binding.recyclerview.setLayoutManager(manager);
            //set adapter
            binding.recyclerview.setAdapter(emailAdapter);
        }
       


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

        return rootView;
    }


    public class EmailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        ArrayList<Invited> todayMeetings;
        private Context context;

        public EmailAdapter(Context context, ArrayList<Invited> todayMeetings) {
            this.todayMeetings = todayMeetings;
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = null;
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emails_selected_list_items, parent, false);
//            return new EmailAdapter.ViewHolderToday(view);

            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            EmailsSelectedListItemsBinding binding = EmailsSelectedListItemsBinding.inflate(layoutInflater, parent, false);
            return new ViewHolderToday(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position)  {
            EmailAdapter.ViewHolderToday Holder = (EmailAdapter.ViewHolderToday) holder;

            Log.e(TAG, "onBindViewHolder:history:stype "+todayMeetings.get(position).getName());
            Holder.binding.txtName.setText(todayMeetings.get(position).getName());
            Holder.binding.txtEmail.setText(todayMeetings.get(position).getEmail());
            Holder.binding.txtMobile.setText(todayMeetings.get(position).getMobile());

            if (todayMeetings.get(position).getEmail().equalsIgnoreCase("") || todayMeetings.get(position).getEmail()==null){
                Holder.binding.linearEmail.setVisibility(View.GONE);
            }else if (todayMeetings.get(position).getMobile().equalsIgnoreCase("") || todayMeetings.get(position).getMobile()==null){
                Holder.binding.linearMobile.setVisibility(View.GONE);
            }else {

            }


            if (SetupMeetingActivity.sharedPreferences1.getInt("m_action", 0) == 2){
                Holder.binding.imgDelete.setVisibility(View.GONE);
            }

            if (RMS_STATUS == 3){
                Holder.binding.imgDelete.setVisibility(View.GONE);
            }


//            //add guest previews contacts checked
//            if (SetupMeetingActivity.sharedPreferences1.getInt("m_action", 0) == 1){
//                if (SetupMeetingActivity.invitedArrayList != null) {
//                    if (SetupMeetingActivity.invitedArrayList.size() != 0) {
//                        for (int chek = 0; chek < SetupMeetingActivity.invitedArrayList.size(); chek++) {
//                            if (todayMeetings.get(position).getEmail().equalsIgnoreCase(SetupMeetingActivity.invitedArrayList.get(chek).getEmail())){
//                                Holder.img_delete.setVisibility(View.GONE);
//                            }else {
//                                Holder.img_delete.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    }
//                }
//            }


            Holder.binding.imgEdit.setOnClickListener(v -> {
                AlertDialog dialog1;
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(getActivity());
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_mail_popup,null);
                ImageView img_close=(ImageView) view.findViewById(R.id.img_close);
                EditText edit_name=(EditText) view.findViewById(R.id.edit_name);
                EditText edit_email=(EditText) view.findViewById(R.id.edit_email);
                EditText edit_mobile=(EditText) view.findViewById(R.id.edit_mobile);
                CardView card_update=(CardView) view.findViewById(R.id.card_update);


                edit_name.setText(todayMeetings.get(position).getName());
                edit_email.setText(todayMeetings.get(position).getEmail());
                edit_mobile.setText(todayMeetings.get(position).getMobile());


                mbuilder.setView(view);
                dialog1=mbuilder.create();
                dialog1.show();
                Objects.requireNonNull(dialog1.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.setCancelable(false);
                dialog1.setCanceledOnTouchOutside(false);

                img_close.setOnClickListener(view1 -> dialog1.dismiss());

                card_update.setOnClickListener(view12 -> {
                    if (edit_name.getText().toString().equalsIgnoreCase("")){
                        edit_name.setError("Enter Name");
                    }else if (edit_email.getText().toString().equalsIgnoreCase("")){
                        edit_email.setError("Enter Email");
                    }else if (edit_mobile.getText().toString().equalsIgnoreCase("")){
                        edit_mobile.setError("Enter Mobile");
                    }else {
                        todayMeetings.get(position).setName(edit_name.getText().toString());
                        todayMeetings.get(position).setEmail(edit_email.getText().toString());
                        todayMeetings.get(position).setMobile(edit_mobile.getText().toString());
                        SetupMeetingActivity.invitedArrayList.get(position).setName(edit_name.getText().toString());
                        SetupMeetingActivity.invitedArrayList.get(position).setEmail(edit_email.getText().toString());
                        SetupMeetingActivity.invitedArrayList.get(position).setMobile(edit_mobile.getText().toString());
                        emailAdapter.notifyDataSetChanged();
                        SetupMeetingActivity.txt_list_count.setText(SetupMeetingActivity.invitedArrayList.size()+"");
                        dialog1.dismiss();
                    }
                });
            });

            Holder.binding.imgDelete.setOnClickListener(v -> {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Are you sure if you want to Delete ?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                todayMeetings.remove(position);
                                SetupMeetingActivity.invitedArrayList.remove(position);
                                //addguest
                                SetupMeetingActivity.AddGuestinvitedArrayList.remove(position);
                                emailAdapter.notifyDataSetChanged();
                                SetupMeetingActivity.txt_list_count.setText(SetupMeetingActivity.invitedArrayList.size()+"");
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        (dialog, id) -> dialog.cancel());

                AlertDialog alert11 = builder1.create();
                alert11.show();
            });
        }

        @Override
        public int getItemCount() {
            return todayMeetings.size();
        }

        public class ViewHolderToday extends RecyclerView.ViewHolder {

            EmailsSelectedListItemsBinding binding;

            public ViewHolderToday(EmailsSelectedListItemsBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

        }

        public void updateList(ArrayList<Invited> list){
            todayMeetings = list;
            notifyDataSetChanged();
        }

    }


    private void filter(String text) {
        ArrayList<Invited> temp = new ArrayList();
        for(Invited d: imail_list){
            if(d.getName().toLowerCase().contains(text)){
                temp.add(d);
            }
        }
        if (temp.size()==0){
//            Toast.makeText(getActivity(),"No Data",Toast.LENGTH_LONG).show();
        }
        else {
            //update recyclerview
            emailAdapter.updateList(temp);
        }
    }

    public interface ItemSelected {
        void onSelected(String comId,String DepName,String CompName);
    }

}