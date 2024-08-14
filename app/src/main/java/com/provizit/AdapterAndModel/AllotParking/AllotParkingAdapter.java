package com.provizit.AdapterAndModel.AllotParking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.provizit.Activities.SetupMeetingActivity;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.CustomItemListener;
import com.provizit.FragmentDailouge.AllotParking.AllotParkingFragment;
import com.provizit.FragmentDailouge.AllotParking.CategoryAllotsListDialouges;
import com.provizit.FragmentDailouge.AllotParking.SlotsAllotsListDialouges;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Services.Model1;
import com.provizit.Utilities.Invited;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllotParkingAdapter extends RecyclerView.Adapter<AllotParkingAdapter.ContactViewHolder>{

    private CustomItemListener customItemClick;
    Context context;
    List<Invited> invitedArrayList;

    String categoryId="";
    String slotId="";
    boolean autoallot = false;

    private boolean isEnabled;

    public AllotParkingAdapter(Context context,  List<Invited> invitedArrayList, CustomItemListener customItemClick) {
        this.context = context;
        this.invitedArrayList = invitedArrayList;
        this.customItemClick = customItemClick;
        this.isEnabled = true;
    }

    public void setEnableds(boolean enabled) {
        this.isEnabled = enabled;
        notifyDataSetChanged(); // Refresh the adapter to reflect the change
    }


    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.allot_list_items, parent, false);
        return new ContactViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AllotParkingAdapter.ContactViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Invited contacinvited = invitedArrayList.get(position);

        // Disable / enable item click based on isEnabled flag
        holder.itemView.setEnabled(isEnabled);

        holder.txt_name.setText(contacinvited.getName());

        holder.txt_category.setOnClickListener(isEnabled ? (View.OnClickListener) v ->  {
            AnimationSet see = Conversions.animation();
            v.startAnimation(see);

            categoryId = "";

            FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
            CategoryAllotsListDialouges sFragment = new CategoryAllotsListDialouges();
            // Show DialogFragment
            sFragment.onItemsSelectedListner((id, name) -> {
                categoryId = id;
                holder.txt_category.setText(name);

                //clear
                holder.txt_slot.setText("");
            });
            sFragment.show(fm, "Dialog Fragment");
        }: null);

        holder.txt_slot.setOnClickListener(isEnabled ? (View.OnClickListener) v ->  {
            AnimationSet set = Conversions.animation();
            v.startAnimation(set);

            if (!categoryId.isEmpty()){
                FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("category_id", categoryId);
                SlotsAllotsListDialouges sFragment = new SlotsAllotsListDialouges();
                //Show DialogFragment
                sFragment.onItemsSelectedListner((id, name) -> {
                    holder.txt_slot.setText(name);

                    slotId = id;

                    gettimepslots(contacinvited.getName(), contacinvited.getMobile(), contacinvited.getEmail(), contacinvited.getId(), holder.platNumber.getText().toString(), categoryId, slotId);
                });
                sFragment.setArguments(bundle);
                sFragment.show(fm, "Dialog Fragment");
            }

        }: null);


        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                holder.txt_category.setEnabled(false);
                holder.txt_slot.setEnabled(false);
                holder.platNumber.setEnabled(false);
                autoallot = true;
                gettimepslots(contacinvited.getName(), contacinvited.getMobile(), contacinvited.getEmail(), contacinvited.getId(), "", "", "");
            } else {
                holder.txt_category.setEnabled(true);
                holder.txt_slot.setEnabled(true);
                holder.platNumber.setEnabled(true);
                autoallot = false;
                gettimepslots(contacinvited.getName(), contacinvited.getMobile(), contacinvited.getEmail(), contacinvited.getId(), holder.platNumber.getText().toString(), categoryId, slotId);
            }
        });


        holder.platNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String newText = editable.toString();

                if (categoryId.equalsIgnoreCase("")){
//                    Toast.makeText(context, "Select Category", Toast.LENGTH_SHORT).show();
                    holder.platNumber.getText().clear();
                }else if (slotId.equalsIgnoreCase("")){
//                    Toast.makeText(context, "Select Slot", Toast.LENGTH_SHORT).show();
                    holder.platNumber.getText().clear();
                }else {
                    gettimepslots(contacinvited.getName(), contacinvited.getMobile(), contacinvited.getEmail(), contacinvited.getId(), newText, categoryId, slotId);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return invitedArrayList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linear;
        CheckBox checkbox;
        TextView txt_name;
        TextView txt_category,txt_slot;
        EditText platNumber;


        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);


            linear = itemView.findViewById(R.id.linear);
            checkbox = itemView.findViewById(R.id.checkbox);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_category = itemView.findViewById(R.id.txt_category);
            txt_slot = itemView.findViewById(R.id.txt_slot);
            platNumber = itemView.findViewById(R.id.platNumber);


        }

    }


    public void updateList(List<Invited> list){
        invitedArrayList = list;
        notifyDataSetChanged();
    }

    private void gettimepslots(String name, String mobile, String email, String id, String plateNo, String Cat_id, String lotId) {
        DataManger dataManager = DataManger.getDataManager();
        dataManager.gettimepslots(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                final Model1 model = response.body();
                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {

                    } else if (statuscode.equals(not_verified)) {

                    } else if (statuscode.equals(successcode)) {
                        Model1 model1 = response.body();
                        Invited update_position = null;

                        for (int i = 0; i < SetupMeetingActivity.invitedArrayList.size(); i++) {
                            if (SetupMeetingActivity.invitedArrayList.get(i).getId().equalsIgnoreCase(id)) {
                                update_position = SetupMeetingActivity.invitedArrayList.get(i);

                                update_position.setName(name);
                                update_position.setMobile(mobile);
                                update_position.setEmail(email);
                                update_position.setId(id);
                                update_position.setPlate_no(plateNo);
                                update_position.setCompany("");
                                update_position.setLink("");
                                update_position.setCat_id(Cat_id);
                                update_position.setLot_id(lotId);
                                update_position.setPslot(0);
                                update_position.setSlot(model1.getItems().get(0).get_id().get$oid());
                                update_position.setAuto_allot(autoallot);
                            }
                        }

                    }
                }
            }
            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.e("error",t.getMessage().toString());
            }
        }, context, AllotParkingFragment.comp_id, "",lotId,categoryId,"",SetupMeetingActivity.s_time,SetupMeetingActivity.e_time-1);

    }


}
